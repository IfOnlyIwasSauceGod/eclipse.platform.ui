/*******************************************************************************
 * Copyright (c) 2008 Matthew Hall and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 247997)
 ******************************************************************************/

package org.eclipse.core.databinding.property.set;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IPropertyChangeListener;
import org.eclipse.core.databinding.property.Property;

/**
 * @since 1.2
 * 
 */
public abstract class DelegatingSetProperty extends SetProperty {
	private final Object elementType;
	private final ISetProperty nullProperty = new NullSetProperty();

	protected DelegatingSetProperty() {
		this(null);
	}

	protected DelegatingSetProperty(Object elementType) {
		this.elementType = elementType;
	}

	protected final ISetProperty getDelegate(Object source) {
		if (source == null)
			return null;
		ISetProperty delegate = doGetDelegate(source);
		if (delegate == null)
			delegate = nullProperty;
		return delegate;
	}

	protected abstract ISetProperty doGetDelegate(Object source);

	public Object getElementType() {
		return elementType;
	}

	public Realm getPreferredRealm(Object source) {
		ISetProperty delegate = getDelegate(source);
		Realm realm = null;
		if (delegate instanceof Property)
			realm = ((Property) delegate).getPreferredRealm(source);
		if (realm == null)
			realm = super.getPreferredRealm(source);
		return realm;
	}

	public IObservableSet observe(Realm realm, Object source) {
		return getDelegate(source).observe(realm, source);
	}

	private class NullSetProperty extends SimpleSetProperty {
		public Object getElementType() {
			return elementType;
		}

		protected Set doGetSet(Object source) {
			return Collections.EMPTY_SET;
		}

		protected void doSetSet(Object source, Set set, SetDiff diff) {
		}

		protected INativePropertyListener adaptListener(
				IPropertyChangeListener listener) {
			return null;
		}

		protected void doAddListener(Object source,
				INativePropertyListener listener) {
		}

		protected void doRemoveListener(Object source,
				INativePropertyListener listener) {
		}
	}
}
