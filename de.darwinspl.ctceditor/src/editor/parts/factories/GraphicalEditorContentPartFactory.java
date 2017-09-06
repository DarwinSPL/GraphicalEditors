/*******************************************************************************
 * Copyright (c) 2014, 2015 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package editor.parts.factories;

import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IContentPartFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

import editor.model.CreateNewContextButton;
import editor.model.GeometricShape;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorMovableBlock;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.control.ControlBlockModel;
import editor.model.evolutionslider.EvolutionSliderModel;
import editor.model.nodes.TextModel;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorFeatureIsSelectedModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorMovableBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.TextFieldPart;
import editor.parts.arithmetical.ArithmeticalOperationOperatorPart;
import editor.parts.arithmetical.ArithmeticalOperatorFixedBlockPart;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.arithmetical.MinMaxContextOperatorPart;
import editor.parts.choiceboxes.ChoiceBoxFeatureAttributePart;
import editor.parts.choiceboxes.ChoiceBoxFeaturePart;
import editor.parts.choiceboxes.ChoiceBoxTemporalElementPart;
import editor.parts.choiceboxes.ChoiceBoxValuePart;
import editor.parts.context.CreateNewContext;
import editor.parts.control.ControlBlockPart;
import editor.parts.evolution.slider.EvolutionSliderPart;
import editor.parts.nodes.TextPart;
import editor.parts.operator.OperatorAndBlockPart;
import editor.parts.operator.OperatorFeatureIsSelectedBlockPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import editor.parts.operator.OperatorNumericalComparisonBlockPart;
import javafx.scene.Node;

public class GraphicalEditorContentPartFactory implements IContentPartFactory {

	@Inject
	private Injector injector;

	@Override
	public IContentPart<? extends Node> createContentPart(Object content, Map<Object, Object> contextMap) {
		if (content instanceof CreateNewContextButton) {
			return injector.getInstance(CreateNewContext.class);
		}
		if(content  instanceof OperatorNumercialComparisonBlockModel){
			return injector.getInstance(OperatorNumericalComparisonBlockPart.class);
		}
		if(content instanceof ChoiceBoxFeatureModel){
			return injector.getInstance(ChoiceBoxFeaturePart.class);
		}
		if (content instanceof ChoiceBoxFeatureAttributeModel) {
			return injector.getInstance(ChoiceBoxFeatureAttributePart.class);
		}
		if (content instanceof MinMaxObjectReferenceBlockModel) {
			return injector.getInstance(MinMaxContextOperatorPart.class);
		}
		if (content instanceof ArithmeticalOperatorFixedBlock) {
			return injector.getInstance(ArithmeticalOperatorFixedBlockPart.class);
		}
		if (content instanceof ArithmeticalOperationOperatorBlockModel) {
			return injector.getInstance(ArithmeticalOperationOperatorPart.class);
		}
		if (content instanceof ArithmeticalOperatorMovableBlock) {
			return injector.getInstance(ArithmeticalOperatorMovableBlockPart.class);
		}

		if (content instanceof TextFieldModel) {
			return injector.getInstance(TextFieldPart.class);
		}
		if (content instanceof EvolutionSliderModel) {
			return injector.getInstance(EvolutionSliderPart.class);
		}
		if (content instanceof ChoiceBoxContextModel) {
			return injector.getInstance(ChoiceBoxTemporalElementPart.class);
		}
		if (content instanceof ChoiceBoxValueModel) {
			return injector.getInstance(ChoiceBoxValuePart.class);
		}
		if (content instanceof ControlBlockModel) {
			return injector.getInstance(ControlBlockPart.class);

		}

		if (content instanceof OperatorFeatureIsSelectedModel) {
			return injector.getInstance(OperatorFeatureIsSelectedBlockPart.class);
		}
		if (content instanceof OperatorFixedBlockModel) {
			return injector.getInstance(OperatorFixedBlockPart.class);
		}
		if (content instanceof OperatorAndBlockModel) {
			return injector.getInstance(OperatorAndBlockPart.class);
		}

		if (content instanceof OperatorMovableBlockModel) {
			return injector.getInstance(OperatorMovableBlockPart.class);
		}
		if (content instanceof ChoiceBoxFeatureAttributeModel) {
			return injector.getInstance(ChoiceBoxFeatureAttributePart.class);
		}
		
		if (content instanceof TextModel) {
			return injector.getInstance(TextPart.class);
		}
		

		if (content instanceof GeometricShape) {
			return injector.getInstance(GeometricShapePart.class);
		}
	

		else {
			throw new IllegalArgumentException(content.getClass().toString());
		}
	};

}
