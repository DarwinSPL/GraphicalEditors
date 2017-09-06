package editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.geometry.planar.RoundedRectangle;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel.ArithmeticalOperatorType;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock.ArithmeticalOperatorFixedType;
import editor.model.arithmetical.ArithmeticalTextFielOperatorBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel.MinMaxType;
import editor.model.arithmetical.NegationOperatorBlockModel;
import editor.model.arithmetical.NumericalObjectReferenceOperator;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeatureModel;
import javafx.scene.paint.Color;

public class ArithmeticalOperatorShapeService {

	public static RoundedRectangle createCustomArithmeticalBlockShape(int width, int hight) {

		Rectangle rec = new Rectangle(1, 1, width, hight);
		RoundedRectangle rr = new RoundedRectangle(rec, 30, 30);
		return rr;

	}

	public static List<AbstractBlockElement> createArithmeticalOperationOperatorShape() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final ArithmeticalOperationOperatorBlockModel operator = new ArithmeticalOperationOperatorBlockModel(
				createCustomArithmeticalBlockShape(130, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null, ArithmeticalOperatorType.ADDITION);

		final ArithmeticalOperatorFixedBlock operand1 = new ArithmeticalOperatorFixedBlock(
				createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND1);
		operand1.setParentBlock(operator);

		final ArithmeticalOperatorFixedBlock operand2 = new ArithmeticalOperatorFixedBlock(
				createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND2);
		operand2.setParentBlock(operator);

		final TextModel text = new TextModel("+", new AffineTransform(1, 0, 0, 1, 1, 1),
				TextType.ARITHMETICAL_OPERATOR);
		text.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(operand1);
		visualParts.add(operand2);
		visualParts.add(text);

		return visualParts;

	}

	public static List<AbstractBlockElement> createNumercialContextOperatorShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final NumericalObjectReferenceOperator operator = new NumericalObjectReferenceOperator(
				createCustomArithmeticalBlockShape(180, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.CONTEXT_NUMBER);
		choiceBoxContextModel.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(choiceBoxContextModel);

		return visualParts;

	}

	public static List<AbstractBlockElement> createNumercialAttributeOperatorShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final NumericalObjectReferenceOperator operator = new NumericalObjectReferenceOperator(
				createCustomArithmeticalBlockShape(180, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null);

		final ChoiceBoxFeatureAttributeModel choiceBoxContextModel = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), featureModel, contextModel, ChoiceBoxType.ATTRIBUTE_NUMBER);

		choiceBoxContextModel.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(choiceBoxContextModel);

		return visualParts;

	}
	
	public static List<AbstractBlockElement> createValueOperatorShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final NumericalObjectReferenceOperator operator = new NumericalObjectReferenceOperator(
				createCustomArithmeticalBlockShape(180, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null);

		final ChoiceBoxValueModel choiceBoxContextModel = new ChoiceBoxValueModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.VALUE_NUMBER);

		choiceBoxContextModel.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(choiceBoxContextModel);

		return visualParts;

	}


	public static List<AbstractBlockElement> createMinMaxContextShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final MinMaxObjectReferenceBlockModel operator = new MinMaxObjectReferenceBlockModel(
				createCustomArithmeticalBlockShape(220, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null, MinMaxType.MIN);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.CONTEXT_NUMBER);
		choiceBoxContextModel.setParentBlock(operator);

		final TextModel text = new TextModel("min", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.MIN_MAX_OPERATOR);
		text.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(text);

		return visualParts;

	}

	public static List<AbstractBlockElement> createMinMaxAttributeShape(HyFeatureModel featureModel,
			HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final MinMaxObjectReferenceBlockModel operator = new MinMaxObjectReferenceBlockModel(
				createCustomArithmeticalBlockShape(220, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null, MinMaxType.MIN);

		final ChoiceBoxFeatureAttributeModel choiceBoxContextModel = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), featureModel, contextModel, ChoiceBoxType.ATTRIBUTE_NUMBER);

		choiceBoxContextModel.setParentBlock(operator);

		final TextModel text = new TextModel("min", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.MIN_MAX_OPERATOR);
		text.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(text);

		return visualParts;

	}

	public static List<AbstractBlockElement> createOperatorWithTextField() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final ArithmeticalTextFielOperatorBlockModel operator = new ArithmeticalTextFielOperatorBlockModel(
				createCustomArithmeticalBlockShape(120, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null);

		final TextFieldModel textField = new TextFieldModel(new AffineTransform(1, 0, 0, 1, 1, 1));
		textField.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(textField);
		return visualParts;
	}

	public static List<AbstractBlockElement> createNegationOperator() {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		final NegationOperatorBlockModel operator = new NegationOperatorBlockModel(
				createCustomArithmeticalBlockShape(100, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(230, 204, 179), null);

		final ArithmeticalOperatorFixedBlock operand = new ArithmeticalOperatorFixedBlock(
				createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.NEG_OPERAND);
		operand.setParentBlock(operator);

		final TextModel text = new TextModel("¬", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.NEGATION);
		text.setParentBlock(operator);

		visualParts.add(operator);
		visualParts.add(operand);
		visualParts.add(text);
		return visualParts;

	}

	public static List<AbstractBlockElement> createPaletteArithmeticViewContents(HyFeatureModel featureModel, HyContextModel contextModel) {
		List<AbstractBlockElement> visualParts = new ArrayList<>();

		visualParts.addAll(createArithmeticalOperationOperatorShape());
		visualParts.addAll(createNegationOperator());
		visualParts.addAll(createOperatorWithTextField());
		if(featureModel != null && contextModel != null){
		visualParts.addAll(createValueOperatorShape(featureModel, contextModel));
		}

		return visualParts;

	}

}
