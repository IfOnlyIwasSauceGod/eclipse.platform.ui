/*******************************************************************************
 * Copyright (c) 2007 Brad Reynolds and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Brad Reynolds - initial API and implementation
 ******************************************************************************/

package org.eclipse.jface.conformance.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.StaleEvent;

/**
 * @since 3.3
 */
public class ObservableStaleContractTest extends ObservableDelegateTest {
	private IObservableContractDelegate delegate;
	private IObservable observable;
	
	public ObservableStaleContractTest(IObservableContractDelegate delegate) {
		this(null, delegate);
	}
	
	public ObservableStaleContractTest(String testName, IObservableContractDelegate delegate) {
		super(testName, delegate);
		this.delegate = delegate;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		observable = getObservable();
	}
	
	public void testIsStaleReturnsTrueWhenStale() throws Exception {
		delegate.setStale(observable, true);
		assertTrue("When stale isStale() should return true.", observable.isStale());
	}
	
	public void testIsStaleReturnsFalseWhenNotStale() throws Exception {
		delegate.setStale(observable, false);
		assertFalse("When not stale isStale() should return false.", observable.isStale());
	}

	public void testBecomingStaleFiresStaleEvent() throws Exception {
		StaleListener listener = new StaleListener();

		// precondition
		ensureStale(observable, false);

		observable.addStaleListener(listener);
		delegate.setStale(observable, true);

		assertEquals("When becoming stale listeners should be notified.", 1, listener.count);
	}

	public void testStaleEventObservable() throws Exception {
		StaleListener listener = new StaleListener();

		// precondition
		ensureStale(observable, false);

		observable.addStaleListener(listener);
		delegate.setStale(observable, true);

		StaleEvent event = listener.event;
		assertNotNull("stale event was null", event);
		assertEquals("When notifying listeners of becoming stale the observable should be the source of the event.", observable,
				event.getObservable());
	}

	public void testRemoveStaleListenerRemovesListener() throws Exception {
		StaleListener listener = new StaleListener();

		observable.addStaleListener(listener);
		ensureStale(observable, false);
		delegate.setStale(observable, true);

		// precondition check
		assertEquals("set stale did not notify listeners", 1, listener.count);

		observable.removeStaleListener(listener);
		ensureStale(observable, false);
		delegate.setStale(observable, true);

		assertEquals("Once removed stale listeners should not be notified of becoming stale.", 1,
				listener.count);
	}

	public void testStaleListenersAreNotNotifiedWhenObservableIsNoLongerStale()
			throws Exception {
		ensureStale(observable, true);

		StaleListener listener = new StaleListener();
		observable.addStaleListener(listener);
		delegate.setStale(observable, false);

		assertEquals("Stale listeners should not be notified when the stale state changes from true to false.", 0,
				listener.count);
	}

	public void testObservableRealmIsCurrentOnStale() throws Exception {
		ensureStale(observable, false);

		StaleListener listener = new StaleListener();
		observable.addStaleListener(listener);
		delegate.setStale(observable, true);

		assertTrue("When notifying listeners of becoming stale the observable's realm should be the current realm.",
				listener.isCurrentRealm);
	}
	
	/**
	 * Ensures that stale is set to the provided state. Will throw an
	 * AssertionFailedError if setting of the state is unsuccessful.
	 * 
	 * @param observable
	 * @param stale
	 */
	private void ensureStale(IObservable observable, boolean stale) {
		if (observable.isStale() != stale) {
			delegate.setStale(observable, stale);
		}

		assertEquals(stale, observable.isStale());
	}
	
	/* package */static class StaleListener implements IStaleListener {
		int count;

		StaleEvent event;

		boolean isCurrentRealm;

		public void handleStale(StaleEvent staleEvent) {
			count++;
			this.event = staleEvent;
			this.isCurrentRealm = staleEvent.getObservable().getRealm()
					.isCurrent();
		}
	}

}
