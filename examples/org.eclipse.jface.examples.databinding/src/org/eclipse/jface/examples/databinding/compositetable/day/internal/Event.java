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

package org.eclipse.jface.examples.databinding.compositetable.day.internal;

import java.util.Date;

import org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

/**
 * Represents an event inside a multi-day event editor.
 * 
 * @since 3.2
 */
public class Event extends Canvas implements IEvent {

	private Label label = null;

   /**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in
	 * class <code>SWT</code> which is applicable to instances of this
	 * class, or must be built by <em>bitwise OR</em>'ing together 
	 * (that is, using the <code>int</code> "|" operator) two or more
	 * of those <code>SWT</code> style constants. The class description
	 * lists the style constants that are applicable to the class.
	 * Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of control to construct
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Event(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * Create the event control's layout
	 */
	private void initialize() {
        label = new Label(this, SWT.NONE);
        label.setText("Label");
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 5;
        fillLayout.marginWidth = 5;
        this.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        this.setLayout(fillLayout);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#reset()
	 */
	public void reset() {
		label.setText("");
		label.setImage(null);
		startTime = null;
		endTime = null;
	}

	private Date startTime = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#getStartTime()
	 */
	public Date getStartTime() {
		return startTime;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#setStartTime(java.util.Date)
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	private Date endTime = null;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#getEndTime()
	 */
	public Date getEndTime() {
		return endTime;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#setEndTime(java.util.Date)
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#getImage()
	 */
	public Image getImage() {
		return label.getImage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#setImage(org.eclipse.swt.graphics.Image)
	 */
	public void setImage(Image image) {
		label.setImage(image);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#getText()
	 */
	public String getText() {
		return label.getText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.examples.databinding.compositetable.timeeditor.IEvent#setText(java.lang.String)
	 */
	public void setText(String string) {
		label.setText(string);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
