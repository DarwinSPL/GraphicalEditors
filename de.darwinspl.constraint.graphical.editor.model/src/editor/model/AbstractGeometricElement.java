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
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.model.nodes.TextModel;
import editor.model.operator.OperatorFixedBlockModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

/**
 * Abstract class for geometric block elements
 *
 * @param <G>
 */
abstract public class AbstractGeometricElement<G extends IGeometry> extends AbstractBlockElement {

	public static final String GEOMETRY_PROPERTY = "geometry";

	private final ObjectProperty<G> geometryProperty = new SimpleObjectProperty<>(this, GEOMETRY_PROPERTY);

	public final static String FIXED_CHILD_TEXT_PROPERTY = "fixed_child_text_property";

	private final ObjectProperty<TextModel> fixedChildTextProperty = new SimpleObjectProperty<>(this,
			FIXED_CHILD_TEXT_PROPERTY);

	public final static String FIXED_CHILD_OPERATOR_PROPERTY = "fixed_child_operator_property";

	private final ObjectProperty<OperatorFixedBlockModel> fixedChildOperatorProperty = new SimpleObjectProperty<>(this,
			FIXED_CHILD_OPERATOR_PROPERTY);

	public AbstractGeometricElement(G geometry) {
		super();
		setGeometry(geometry);

	}

	public AbstractGeometricElement(G geometry, AffineTransform transform, Paint stroke, double strokeWidth,
			Effect effect) {
		super(transform, stroke, strokeWidth, effect);
		setGeometry(geometry);

	}

	public G getGeometry() {
		return geometryProperty.get();
	}

	public void setGeometry(G geometry) {
		geometryProperty.set(geometry);
	}

	public ObjectProperty<OperatorFixedBlockModel> getFixedChildOperatorProperty() {
		return fixedChildOperatorProperty;
	}

	public OperatorFixedBlockModel getFixedChildOperatorBlockModel() {
		return fixedChildOperatorProperty.get();
	}

	public void setFixedChildOperatorBlockModel(OperatorFixedBlockModel child) {
		fixedChildOperatorProperty.set(child);

	}

	public ObjectProperty<TextModel> getFixedChildTextProperty() {
		return fixedChildTextProperty;
	}

	public TextModel getFixedChildTextModel() {
		return fixedChildTextProperty.get();
	}

	public void setFixedChildTextModel(TextModel child) {
		fixedChildTextProperty.set(child);

	}

}
