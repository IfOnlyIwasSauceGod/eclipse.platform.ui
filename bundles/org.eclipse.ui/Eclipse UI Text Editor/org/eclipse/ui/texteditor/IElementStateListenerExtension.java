package org.eclipse.ui.texteditor;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
/**
 * Extends <code>IElementStateListener</code> with additional
 * functionality.
 */ 
public interface IElementStateListenerExtension {
	
	/**
	 * Notifies that the state validation of the given element
	 * has changed.
	 *
	 * @param element the element
	 * @param isStateValidated the flag indicating whether state validation is done
	 */
	void elementStateValidationChanged(Object element, boolean isStateValidated);
}
