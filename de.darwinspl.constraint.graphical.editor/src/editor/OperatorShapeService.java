package editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.Line;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock.ArithmeticalOperatorFixedType;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorAndBlockModel.OperatorAndORType;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonType;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonValueType;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorFixedBlockModel.OperatorFixedBlockType;
import editor.model.operator.OperatorNotBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.model.operator.OperatorWithChoiceBoxAndTextField;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeatureModel;
import javafx.scene.paint.Color;

public class OperatorShapeService {

	public static CurvedPolygon createOperatorShape() {
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 15, 10, 0));
		segments.add(new Line(10, 0, 140, 0));
		segments.add(new Line(140, 0, 150, 15));
		segments.add(new Line(150, 15, 140, 30));
		segments.add(new Line(140, 30, 10, 30));
		segments.add(new Line(10, 30, 0, 15));
		return new CurvedPolygon(segments);
	}

	public static CurvedPolygon createOperatorShapeCustomLength(double l) {
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 15, 10, 0));
		segments.add(new Line(10, 0, l, 0));
		segments.add(new Line(l, 0, l + 10, 15));
		segments.add(new Line(l + 10, 15, l, 30));
		segments.add(new Line(l, 30, 10, 30));
		segments.add(new Line(10, 30, 0, 15));
		return new CurvedPolygon(segments);
	}

	public static List<AbstractBlockElement> createOperatorAndShapeGroup() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final OperatorAndBlockModel operatorAndBlockModel = new OperatorAndBlockModel(
				createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null,
				OperatorAndORType.AND);

		final OperatorFixedBlockModel fixedOperator1 = new OperatorFixedBlockModel(createOperatorShapeCustomLength(40),
				createDefaultAffineTransformForPalette(), Color.GREEN, null, OperatorFixedBlockType.AND_OPERATOR1,
				operatorAndBlockModel);
		fixedOperator1.setParentBlock(operatorAndBlockModel);

		final OperatorFixedBlockModel fixedOperator2 = new OperatorFixedBlockModel(createOperatorShapeCustomLength(40),
				createDefaultAffineTransformForPalette(), Color.GREEN, null, OperatorFixedBlockType.AND_OPERATOR2,
				operatorAndBlockModel);
		fixedOperator2.setParentBlock(operatorAndBlockModel);

		final TextModel textModel = new TextModel("And", createDefaultAffineTransformForPalette(),
				TextType.AND_OPERATOR);
		textModel.addAnchorage(operatorAndBlockModel);

		visualParts.add(operatorAndBlockModel);
		visualParts.add(fixedOperator1);
		visualParts.add(fixedOperator2);
		visualParts.add(textModel);

		return visualParts;
	}

	public static AffineTransform createDefaultAffineTransformForPalette() {
		return new AffineTransform(1, 0, 0, 1, 1, 1);
	}



	public static List<AbstractBlockElement> createOperatorValidateNotShapeGroup() {

		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final OperatorNotBlockModel movableMovableModel = new OperatorNotBlockModel(
				createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null);

		final OperatorFixedBlockModel fixedBlockModel = new OperatorFixedBlockModel(createOperatorShapeCustomLength(80),
				new AffineTransform(1, 0, 0, 1, 1, 1), Color.GREEN, null, OperatorFixedBlockType.VALIDATE_NOT_OPERATOR);
		fixedBlockModel.setParentBlock(movableMovableModel);

		final TextModel textModel = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 1, 1),
				TextType.VALIDATE_NOT_OPERATOR);

		textModel.addAnchorage(movableMovableModel);

		visualParts.add(movableMovableModel);
		visualParts.add(fixedBlockModel);
		visualParts.add(textModel);

		return visualParts;

	}

	public static List<Object> createOperatorEnumContext(HyFeatureModel featureModel, HyContextModel contextModel) {
		List<Object> visualParts = new ArrayList<>();

		final OperatorComparisonBlockModel operatorModel = new OperatorComparisonBlockModel(
				createOperatorShapeCustomLength(200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null,
				OperatorComparisonType.EQUALS, OperatorComparisonValueType.ENUM);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.CONTEXT_ENUM);
		choiceBoxContextModel.setParentBlock(operatorModel);
		//
		// final ChoiceBoxValueModel choiceBoxValueModel = new
		// ChoiceBoxValueModel(new AffineTransform(1, 0, 0, 1,1,1), contextModel
		// , featureModel, ChoiceBoxType.VALUE_ENUM);
		// choiceBoxValueModel.setDependingChoiceBox(choiceBoxContextModel);
		// choiceBoxValueModel.setParentBlock(operatorModel);

		final TextModel textModel = new TextModel("=", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		textModel.setParentBlock(operatorModel);

		visualParts.add(operatorModel);
		visualParts.add(choiceBoxContextModel);
		// visualParts.add(choiceBoxValueModel);
		visualParts.add(textModel);
		return visualParts;

	}

	public static List<Object> createOperatorsContext(HyFeatureModel featureModel, HyContextModel contextModel,
			OperatorComparisonValueType valueType, ChoiceBoxType choiceBoxType, ChoiceBoxType valueChoiceBoxType) {
		List<Object> visualParts = new ArrayList<>();

		final OperatorComparisonBlockModel operatorModel = new OperatorComparisonBlockModel(
				createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(92, 214, 92),
				null, OperatorComparisonType.EQUALS, valueType);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, choiceBoxType);
		choiceBoxContextModel.setParentBlock(operatorModel);

		final ChoiceBoxValueModel choiceBoxValueModel = new ChoiceBoxValueModel(new AffineTransform(1, 0, 0, 1, 1, 1),
				contextModel, featureModel, valueChoiceBoxType);

		choiceBoxValueModel.setParentBlock(operatorModel);

		final TextModel textModel = new TextModel("=", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		textModel.setParentBlock(operatorModel);

		visualParts.add(operatorModel);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(choiceBoxValueModel);
		visualParts.add(textModel);
		return visualParts;

	}

	public static List<Object> createOperatorContextWithTextFielShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<Object> visualParts = new ArrayList<>();

		final OperatorWithChoiceBoxAndTextField operatorModel = new OperatorWithChoiceBoxAndTextField(
				createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null,
				OperatorComparisonType.EQUALS, OperatorComparisonValueType.NUMBER);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.CONTEXT_NUMBER);
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

	public static Object createGroupingLabelForPaletteViewer(String text) {
		TextModel textModel = new TextModel(text, ShapeService.createDefaultAffineTransform(), TextType.NOT_EQUALS);
		return textModel;
	}

	public static List<Object> createAllOperatorsForContexts(HyFeatureModel featureModel, HyContextModel contextModel) {
		List<Object> visualParts = new ArrayList<>();

		visualParts.add(createGroupingLabelForPaletteViewer("Boolean Contexts:"));
		visualParts.addAll(createOperatorsContext(featureModel, contextModel, OperatorComparisonValueType.BOOLEAN,
				ChoiceBoxType.CONTEXT_BOOLEAN, ChoiceBoxType.VALUE_BOOLEAN));

		visualParts.add(createGroupingLabelForPaletteViewer("Enumeration Contexts:"));

		visualParts.addAll(createOperatorsContext(featureModel, contextModel, OperatorComparisonValueType.ENUM,
				ChoiceBoxType.CONTEXT_ENUM, ChoiceBoxType.VALUE_ENUM));

		visualParts.add(createGroupingLabelForPaletteViewer("Numerical Contexts:"));
		visualParts.addAll(createOperatorsContext(featureModel, contextModel, OperatorComparisonValueType.NUMBER,
				ChoiceBoxType.CONTEXT_NUMBER, ChoiceBoxType.VALUE_NUMBER));
		visualParts.addAll(createOperatorContextWithTextFielShape(featureModel, contextModel));

		visualParts.addAll(
				ArithmeticalOperatorShapeService.createNumercialContextOperatorShape(featureModel, contextModel));
		// visualParts.addAll(createNumercialComparisonBlockShape());
		visualParts.addAll(ArithmeticalOperatorShapeService.createMinMaxContextShape(featureModel, contextModel));
		// visualParts.addAll(ArithmeticalOperatorShapeService.createOperatorWithTextField());

		return visualParts;

	}

	public static List<Object> createPaletteContentsForOperators() {
		List<Object> visualParts = new ArrayList<>();

		return visualParts;
	}

	public static List<AbstractBlockElement> createNumercialComparisonBlockShape() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final OperatorNumercialComparisonBlockModel operator = new OperatorNumercialComparisonBlockModel(
				createOperatorShapeCustomLength(200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(92, 214, 92),
				null, OperatorComparisonType.BIGGER);

		final ArithmeticalOperatorFixedBlock operand1 = new ArithmeticalOperatorFixedBlock(
				ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(60, 26),
				new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(115, 77, 38), null,
				ArithmeticalOperatorFixedType.COMPARISON_OPERAND1);
		operand1.setParentBlock(operator);

		final ArithmeticalOperatorFixedBlock operand2 = new ArithmeticalOperatorFixedBlock(
				ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(60, 26),
				new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(115, 77, 38), null,
				ArithmeticalOperatorFixedType.COMPARISON_OPERAND2);
		operand2.setParentBlock(operator);

		final TextModel text = new TextModel("<", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.COMPARISON_OPERATOR);
		text.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(operand1);
		visualParts.add(operand2);
		visualParts.add(text);

		return visualParts;

	}

	public static List<AbstractBlockElement> createPaletteOperatorsViewContents() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();
		visualParts.addAll(createOperatorAndShapeGroup());
		visualParts.addAll(createOperatorValidateNotShapeGroup());
		visualParts.addAll(createNumercialComparisonBlockShape());

		return visualParts;
	}

}
