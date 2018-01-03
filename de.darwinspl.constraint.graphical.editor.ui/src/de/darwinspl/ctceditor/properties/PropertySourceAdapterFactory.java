/*******************************************************************************
 * Copyright (c) 2015, 2016 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *******************************************************************************/
package de.darwinspl.ctceditor.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import editor.model.GeometricShape;
import editor.model.choiceboxes.ChoiceBoxModel;

public class PropertySourceAdapterFactory implements IAdapterFactory {

	@Override
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IPropertySource.class.equals(adapterType)) {
			
			if (adaptableObject instanceof GeometricShape) {
				return new GeometricShapePropertySource(
						(GeometricShape) adaptableObject);
			}
			if(adaptableObject instanceof ChoiceBoxModel){
				return new ChoiceBoxModelPropertySource((ChoiceBoxModel) adaptableObject);
			}
			
		}
		return null;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}
