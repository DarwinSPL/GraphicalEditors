/*******************************************************************************
 * Copyright (c) 2015 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package editor.handlers;

import java.util.List;

import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.IShape;

import com.google.inject.Provider;

import editor.handlers.CreationMenuOnClickHandler.ICreationMenuItem;
import editor.model.GeometricShape;
import javafx.scene.Node;

public class CreationMenuItemProvider implements Provider<List<CreationMenuOnClickHandler.ICreationMenuItem>> {

	static class GeometricShapeItem implements CreationMenuOnClickHandler.ICreationMenuItem {
		private final GeometricShape template;

		public GeometricShapeItem(GeometricShape content) {
			template = content;
		}

		@Override
		public Object createContent() {
			return template.getCopy();
		}

		@Override
		public Node createVisual() {
			GeometryNode<IShape> visual = new GeometryNode<>(template.getGeometry());
			visual.setStroke(template.getStroke());
			visual.setStrokeWidth(template.getStrokeWidth());
			visual.setFill(template.getFill());
			visual.setEffect(template.getEffect());
			return visual;
		}

	}

	@Override
	public List<ICreationMenuItem> get() {
		// TODO Auto-generated method stub
		return null;
	}

}
