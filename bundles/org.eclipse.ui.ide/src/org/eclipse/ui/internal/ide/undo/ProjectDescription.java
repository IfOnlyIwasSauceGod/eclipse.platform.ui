/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.ide.undo;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * ProjectDescription is a lightweight description that describes a project to
 * be created.
 * 
 * This class is not intended to be instantiated or used by clients.
 * 
 * @since 3.3
 * 
 */
public class ProjectDescription extends ContainerDescription {

	private IProjectDescription projectDescription;

	/**
	 * Create a project description from a specified project.
	 * 
	 * @param project
	 *            The project to be described. The project must exist and be
	 *            open.
	 */
	public ProjectDescription(IProject project) {
		super(project);
		Assert.isLegal(project.exists() && project.isOpen());
		try {
			this.projectDescription = project.getDescription();
		} catch (CoreException e) {
			// Eat this exception because it only occurs when the project
			// does not exist or is not open, and we have already checked this.
			// We do not want to throw exceptions on the simple constructor, as
			// no one has actually tried to do anything yet.
		}
	}

	/**
	 * Create a project description from a specified IProjectDescription. Used
	 * when the project does not yet exist.
	 * 
	 * @param projectDescription
	 *            the project description for the future project
	 */
	public ProjectDescription(IProjectDescription projectDescription) {
		super();
		this.projectDescription = projectDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.ide.undo.ContainerDescription#createResourceHandle()
	 */
	public IResource createResourceHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.ide.undo.ResourceDescription#createExistentResourceFromHandle(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void createExistentResourceFromHandle(IResource resource,
			IProgressMonitor monitor) throws CoreException {
		Assert.isLegal(resource instanceof IProject);
		if (resource.exists()) {
			return;
		}
		IProject projectHandle = (IProject) resource;
		monitor.beginTask(UndoMessages.ProjectDescription_NewProjectProgress,
				200);
		if (projectDescription == null) {
			projectHandle.create(new SubProgressMonitor(monitor, 100));
		} else {
			projectHandle.create(projectDescription, new SubProgressMonitor(
					monitor, 100));
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		projectHandle.open(IResource.BACKGROUND_REFRESH,
				new SubProgressMonitor(monitor, 100));
		monitor.done();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.internal.ide.undo.ContainerDescription#getName()
	 */
	public String getName() {
		return projectDescription.getName();
	}
}