/*******************************************************************************
 * Copyright (c) 2014, 2016 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package editor.model;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GeometricShape extends AbstractGeometricElement<IShape> {

	public GeometricShape(IShape shape, AffineTransform transform, Color stroke, double strokeWidth, Paint fill,
			Effect effect) {
		super(shape, transform, stroke, strokeWidth, effect);
		setFill(fill);
	}

	public GeometricShape(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		this(shape, transform, new Color(0, 0, 0, 1), 1.0, fill, effect);
	}

	public GeometricShape getCopy() {
		GeometricShape copy = new GeometricShape((IShape) getGeometry().getCopy(), getTransform().getCopy(),
				(Color) getStroke(), getStrokeWidth(), getFill(), getEffect());
		return copy;
	}

}
