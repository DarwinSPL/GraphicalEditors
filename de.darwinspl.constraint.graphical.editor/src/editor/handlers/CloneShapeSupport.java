/*******************************************************************************
 * Copyright (c) 2015, 2016 itemis AG and others.
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

import org.eclipse.gef.geometry.planar.IShape;

import editor.model.GeometricShape;
import editor.model.control.ControlIfBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.control.ControlBlockPart;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

// only applicable for GeometricShapePart
public class CloneShapeSupport extends AbstractCloneContentSupport {

	@Override
	public Object cloneContent() {

		GeometricShape originalShape = getAdaptable().getContent();

		if (originalShape instanceof ControlIfBlockModel) {
			ControlIfBlockModel shape = new ControlIfBlockModel((IShape) originalShape.getGeometry().getCopy(),
					originalShape.getTransform().getCopy(), copyPaint(originalShape.getFill()),
					copyEffect(originalShape.getEffect()), ((ControlIfBlockModel) originalShape).getControlBlockType());
			shape.setStroke(copyPaint(originalShape.getStroke()));
			shape.setStrokeWidth(originalShape.getStrokeWidth());
			return shape;
		}
		 else {
			GeometricShape shape = new GeometricShape((IShape) originalShape.getGeometry().getCopy(),
					originalShape.getTransform().getCopy(), copyPaint(originalShape.getFill()),
					copyEffect(originalShape.getEffect()));
			shape.setStroke(copyPaint(originalShape.getStroke()));
			shape.setStrokeWidth(originalShape.getStrokeWidth());
			return shape;
		}

	}

	private Effect copyEffect(Effect effect) {
		
		return effect.impl_copy();
	}

	private Paint copyPaint(Paint paint) {
		
		return Paint.valueOf(paint.toString());
	}
	


	@Override
	public GeometricShapePart getAdaptable() {
		GeometricShapePart part = (GeometricShapePart) super.getAdaptable();
		if (part instanceof ControlBlockPart) {
			return (ControlBlockPart) part;
		}
		 else {
			return part;
		}
	}
}
