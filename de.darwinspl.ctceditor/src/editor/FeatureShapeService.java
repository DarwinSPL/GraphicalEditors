package editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;

import editor.model.AbstractBlockElement;
import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonType;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonValueType;
import editor.model.operator.OperatorWithChoiceBoxAndTextField;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureModel;
import javafx.scene.paint.Color;

public class FeatureShapeService {

	public static List<Object> createFeaturePaletteViewerContents(List<HyFeature> features, HyFeatureModel featureModel,
			HyContextModel contextModel) {

		final List<Object> paletteContents = new ArrayList<>();

		paletteContents.add(OperatorShapeService.createGroupingLabelForPaletteViewer("Features:"));
		
		if (contextModel != null && featureModel != null) {
			paletteContents.addAll(ShapeService.createOperatorIfFeatureIsSelectedGroup2(featureModel, contextModel));
			paletteContents.add(OperatorShapeService.createGroupingLabelForPaletteViewer("Boolean Attributes:"));
			paletteContents.addAll(createFeatureAttributeOperator(featureModel, contextModel,
					OperatorComparisonValueType.BOOLEAN, ChoiceBoxType.ATTRIBUTE_BOOLEAN, ChoiceBoxType.VALUE_BOOLEAN));


			paletteContents.add(OperatorShapeService.createGroupingLabelForPaletteViewer("Enumeration Attribute:"));
			paletteContents.addAll(createFeatureAttributeOperator(featureModel, contextModel,
					OperatorComparisonValueType.ENUM, ChoiceBoxType.ATTRIBUTE_ENUM, ChoiceBoxType.VALUE_ENUM));

			paletteContents.add(OperatorShapeService.createGroupingLabelForPaletteViewer("Numerical Attribute:"));
			paletteContents.addAll(createFeatureAttributeOperator(featureModel, contextModel,
					OperatorComparisonValueType.NUMBER, ChoiceBoxType.ATTRIBUTE_NUMBER, ChoiceBoxType.VALUE_NUMBER));
			paletteContents.addAll(createOperatorContextWithTextFielShape(featureModel, contextModel));
			paletteContents.addAll(
					ArithmeticalOperatorShapeService.createNumercialAttributeOperatorShape(featureModel, contextModel));
			paletteContents
					.addAll(ArithmeticalOperatorShapeService.createMinMaxAttributeShape(featureModel, contextModel));

		}

		return paletteContents;

	}
	

	public static List<AbstractBlockElement> createFeatureAttributeOperator(HyFeatureModel featureModel,
			HyContextModel contextModel, OperatorComparisonValueType valueType, ChoiceBoxType attributeType,
			ChoiceBoxType choiceBoxValueType) {

		final List<AbstractBlockElement> visualParts = new ArrayList<>();

		final OperatorComparisonBlockModel operator = new OperatorComparisonBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.LIGHTGREEN, null, OperatorComparisonType.EQUALS, valueType);

		final ChoiceBoxFeatureAttributeModel attributeChoiceBox = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), featureModel, contextModel, attributeType);
		attributeChoiceBox.setParentBlock(operator);
		//
		final ChoiceBoxValueModel valueChoiceBox = new ChoiceBoxValueModel(new AffineTransform(1, 0, 0, 1, 1, 1),
				contextModel, featureModel, choiceBoxValueType);
		valueChoiceBox.setParentBlock(operator);

		final TextModel model = new TextModel("=", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		model.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(attributeChoiceBox);
		visualParts.add(valueChoiceBox);
		visualParts.add(model);
		return visualParts;

	}
	
	public static List<Object> createOperatorContextWithTextFielShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<Object> visualParts = new ArrayList<>();

		final OperatorWithChoiceBoxAndTextField operatorModel = new OperatorWithChoiceBoxAndTextField(
				OperatorShapeService.createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null,
				OperatorComparisonType.EQUALS, OperatorComparisonValueType.NUMBER);

		final ChoiceBoxFeatureAttributeModel choiceBoxContextModel = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), featureModel, contextModel, ChoiceBoxType.ATTRIBUTE_NUMBER);
		choiceBoxContextModel.setParentBlock(operatorModel);

		final TextFieldModel textFieldModel = new TextFieldModel(new AffineTransform(1, 0, 0, 1, 1, 1));
		textFieldModel.setParentBlock(operatorModel);

		final TextModel textModel = new TextModel("=", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		textModel.setParentBlock(operatorModel);

		visualParts.add(operatorModel);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(textFieldModel);
		visualParts.add(textModel);
		return visualParts;
	}

}
