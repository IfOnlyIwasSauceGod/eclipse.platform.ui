/*******************************************************************************
 * Copyright (c) 2006 Brad Reynolds and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Brad Reynolds - initial API and implementation
 *     Brad Reynolds - bug 171616
 ******************************************************************************/

package org.eclipse.core.tests.internal.databinding.internal.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import junit.framework.Test;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.internal.beans.JavaBeanObservableValue;
import org.eclipse.jface.conformance.databinding.AbstractObservableValueContractDelegate;
import org.eclipse.jface.conformance.databinding.ObservableValueContractTest;
import org.eclipse.jface.conformance.databinding.SuiteBuilder;
import org.eclipse.jface.tests.databinding.AbstractDefaultRealmTestCase;

/**
 * @since 3.2
 */
public class JavaBeanObservableValueTest extends AbstractDefaultRealmTestCase {
	private Bean bean;
	private JavaBeanObservableValue observableValue;
	private PropertyDescriptor propertyDescriptor;
	private String propertyName;

	protected void setUp() throws Exception {
		super.setUp();
		
		bean = new Bean();
		propertyName = "value";
		propertyDescriptor = new PropertyDescriptor(propertyName, Bean.class);
		observableValue = new JavaBeanObservableValue(Realm.getDefault(), bean, propertyDescriptor, String.class);
	}
	
    public void testSetsValueInBean() throws Exception {
        String value = "value";
        assertNull(observableValue.getValue());
        observableValue.setValue(value);
        assertEquals("value", value, observableValue.getValue());
    }

	public void testGetObserved() throws Exception {
		assertEquals(bean, observableValue.getObserved());
	}

	public void testGetPropertyDescriptor() throws Exception {
		assertEquals(propertyDescriptor, observableValue
				.getPropertyDescriptor());
	}

	public void testSetValueThrowsExceptionThrownByBean() throws Exception {
		ThrowsSetException temp = new ThrowsSetException();
		JavaBeanObservableValue observable = new JavaBeanObservableValue(Realm
				.getDefault(), temp,
				new PropertyDescriptor("value", ThrowsSetException.class), ThrowsSetException.class);

		try {
			observable.setValue("");
			fail("exception should have been thrown");
		} catch (RuntimeException e) {	
			assertEquals(temp.thrownException, e.getCause());
		}
	}
	
	public void testGetValueThrowsExceptionThrownByBean() throws Exception {
		ThrowsGetException temp = new ThrowsGetException();
		JavaBeanObservableValue observable = new JavaBeanObservableValue(Realm
				.getDefault(), temp,
				new PropertyDescriptor("value", ThrowsGetException.class), ThrowsGetException.class);

		try {
			observable.getValue();
			fail("exception should have been thrown");
		} catch (RuntimeException e) {	
			assertEquals(temp.thrownException, e.getCause());
		}
	}

	public static Test suite() {
		Object[] params = new Object[] {new Delegate()};
		
		return new SuiteBuilder().addTests(JavaBeanObservableValueTest.class)
				.addParameterizedTests(ObservableValueContractTest.class, params).build();
	}

	/* package */ static class Delegate extends AbstractObservableValueContractDelegate {
		private Bean bean;
		
		public void setUp() {
			super.setUp();
			
			bean = new Bean("");
		}
		
		public IObservableValue createObservableValue(Realm realm) {
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor("value", Bean.class);
				return new JavaBeanObservableValue(realm, bean,
						propertyDescriptor, String.class);					
			} catch (IntrospectionException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void change(IObservable observable) {
			IObservableValue observableValue = (IObservableValue) observable;
			observableValue.setValue(createValue(observableValue));
		}
		
		public Object getValueType(IObservableValue observable) {
			return String.class;
		}
		
		public Object createValue(IObservableValue observable) {
			return observable.getValue() + "a";
		}
	}
	
	/**
	 * Throws an exception when the value is set.
	 * 
	 * @since 3.2
	 */
	/* package */class ThrowsSetException {
		private String value;

		/* package */NullPointerException thrownException;

		public void setValue(String value) {
			throw thrownException = new NullPointerException();
		}

		public String getValue() {
			return value;
		}
	}

	/* package */class ThrowsGetException {
		public String value;

		/* package */NullPointerException thrownException;

		public String getValue() {
			throw thrownException = new NullPointerException();
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
