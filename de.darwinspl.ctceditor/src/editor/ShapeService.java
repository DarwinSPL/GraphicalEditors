package editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.geometry.planar.Line;
import org.eclipse.gef.geometry.planar.PolyBezier;

import editor.model.CreateNewContextButton;
import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlAndOrBlock.ControlAndORType;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel.ControlValidateOperatorBlockType;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorFeatureIsSelectedModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorFixedBlockModel.OperatorFixedBlockType;
import editor.model.operator.OperatorMovableBlockModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.context.HyContextualInformationBoolean;
import eu.hyvar.context.HyContextualInformationEnum;
import eu.hyvar.context.HyContextualInformationNumber;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureAttribute;
import eu.hyvar.feature.HyFeatureModel;
import eu.hyvar.feature.constraint.HyConstraint;
import eu.hyvar.feature.constraint.HyConstraintModel;
import javafx.scene.paint.Color;

public class ShapeService {

	/**
	 * Methode to create all blocks and their elements for the 'control' palette
	 * view
	 * 
	 * @return List containing all block elements for the palette view 'control'
	 */
	public static List<Object> createControlPaletteViewerContents(HyFeatureModel featureModel, HyContextModel contextModel) {
		final List<Object> paletteContents = new ArrayList<>();

//		if(featureModel!=null ){
//		paletteContents.addAll(ControlShapeService.createVadilityFormulaBlockShape(featureModel, contextModel));
//		paletteContents.addAll(ControlShapeService.createVadilityFormulaBlockShape2(featureModel, contextModel));
//		
//		}
//		paletteContents.addAll(ControlShapeService.createImpliesNot());
		
		paletteContents.addAll(createControlIfBlock());
		paletteContents.addAll(createControlNotIfBlock());
		paletteContents.addAll(createControlEquivalenceBlock());
		paletteContents.addAll(createControlEquivalenceNotBlock());


		paletteContents.addAll(
				ControlShapeService.createControlValidateOperatorBlockGroup(ControlValidateOperatorBlockType.VALIDATE));


		paletteContents.addAll(createControlAndOrBlock());
		return paletteContents;

	}

	/**
	 * Creates ControlIfBlock
	 * 
	 * @return List of block elements required for the ControlIfBlock shape
	 */
	public static List<Object> createControlIfBlock() {
		List<Object> blockElements = new ArrayList<>();

		// Create Model for block elements
		final ControlIfBlockModel controlBlock = new ControlIfBlockModel(ControlShapeService.createControlBlockShape(),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, ControlBlockType.IMPLIES);
		final OperatorFixedBlockModel fixesOperatorBlock = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 53, 8),
				Color.GREEN, null, OperatorFixedBlockType.IF_OPERATOR);
		final TextModel textModel = new TextModel("if", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.IF);

		// Add Parent to child block elements
		fixesOperatorBlock.setParentBlock(controlBlock);
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(fixesOperatorBlock);
		// blockElements.add(textModel);
		return blockElements;
	}
	
	public static List<Object> createControlAndOrBlock() {
		List<Object> blockElements = new ArrayList<>();

		// Create Model for block elements
		final ControlAndOrBlock controlBlock = new ControlAndOrBlock(ControlShapeService.createCustomControlAndOrBlockShape(),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, ControlAndORType.AND);
	
		final TextModel textModel = new TextModel("AND", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.AND_OR_CONTROL);

		// Add Parent to child block elements
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(textModel);
		return blockElements;
	}
	


	/**
	 * Creates ControlIfBlock
	 * 
	 * @return List of block elements required for the ControlIfBlock shape
	 */
	public static List<Object> createControlEquivalenceBlock() {
		List<Object> blockElements = new ArrayList<>();

		// Create Model for block elements
		final ControlIfBlockModel controlBlock = new ControlIfBlockModel(ControlShapeService.createControlBlockShape(),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, ControlBlockType.EQUIVALENCE);
		final OperatorFixedBlockModel fixesOperatorBlock = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 53, 8),
				Color.GREEN, null, OperatorFixedBlockType.IF_OPERATOR);
		final TextModel textModel = new TextModel("↔", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.EQUIVALENCE);

		// Add Parent to child block elements
		fixesOperatorBlock.setParentBlock(controlBlock);
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(fixesOperatorBlock);
		blockElements.add(textModel);
		return blockElements;
	}

	/**
	 * Creates ControlIfBlock
	 * 
	 * @return List of block elements required for the ControlIfBlock shape
	 */
	public static List<Object> createControlEquivalenceNotBlock() {
		List<Object> blockElements = new ArrayList<>();

		// Create Model for block elements
		final ControlIfBlockModel controlBlock = new ControlIfBlockModel(ControlShapeService.createControlBlockShape(),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, ControlBlockType.NOT_EQUIVALENCE);
		final OperatorFixedBlockModel fixesOperatorBlock = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 53, 8),
				Color.GREEN, null, OperatorFixedBlockType.IF_OPERATOR);
		final TextModel textModelNot = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.IF);
		final TextModel textModel = new TextModel("↔", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.EQUIVALENCE);

		// Add Parent to child block elements
		fixesOperatorBlock.setParentBlock(controlBlock);
		textModelNot.setParentBlock(controlBlock);
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(fixesOperatorBlock);
		blockElements.add(textModelNot);
		blockElements.add(textModel);
		return blockElements;
	}

	/**
	 * Creates ControlIfBlock
	 * 
	 * @return List of block elements required for the ControlIfBlock shape
	 */
	public static List<Object> createControlNotIfBlock() {
		List<Object> blockElements = new ArrayList<>();

		// Create Model for block elements
		final ControlIfBlockModel controlBlock = new ControlIfBlockModel(ControlShapeService.createControlBlockShape(),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, ControlBlockType.NOT_IMPLIES);
		final OperatorFixedBlockModel fixesOperatorBlock = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 53, 8),
				Color.GREEN, null, OperatorFixedBlockType.IF_OPERATOR);
		final TextModel textModel = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.IF);

		// Add Parent to child block elements
		fixesOperatorBlock.setParentBlock(controlBlock);
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(fixesOperatorBlock);
		blockElements.add(textModel);
		return blockElements;
	}

	public static List<Object> createOperatorIfFeatureIsSelectedGroupOld(List<HyFeature> features) {
		final List<Object> visualParts = new ArrayList<>();

		final OperatorFeatureIsSelectedModel operatorBlock = new OperatorFeatureIsSelectedModel(
				OperatorShapeService.createOperatorShapeCustomLength(250), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.LIGHTGREEN, null);
		final ChoiceBoxModel<HyFeature> choiceBoxModel = new ChoiceBoxModel<HyFeature>(
				new AffineTransform(1, 0, 0, 1, 1, 1), features, ChoiceBoxType.FEATURE);
		final TextModel textModel = new TextModel("is Selected", new AffineTransform(1, 0, 0, 1, 1, 1),
				TextType.IS_SELECTED);

		// choiceBoxModel.setParentBlockProperty(operatorBlock, "fixed");
		// textModel.setParentBlock(operatorBlock);

		textModel.addAnchorage(operatorBlock);
		// choiceBoxModel.addAnchorage(operatorBlock);
		choiceBoxModel.setParentBlock(operatorBlock);

		visualParts.add(operatorBlock);
		visualParts.add(choiceBoxModel);
		visualParts.add(textModel);
		return visualParts;

	}
	
	public static List<Object> createOperatorIfFeatureIsSelectedGroup2(HyFeatureModel featureModel, HyContextModel contextModel) {
		final List<Object> visualParts = new ArrayList<>();

		final OperatorFeatureIsSelectedModel operatorBlock = new OperatorFeatureIsSelectedModel(
				OperatorShapeService.createOperatorShapeCustomLength(250), new AffineTransform(1, 0, 0, 1, 1, 1),
				Color.LIGHTGREEN, null);
		final ChoiceBoxFeatureModel choiceBoxModel = new ChoiceBoxFeatureModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextModel, featureModel, ChoiceBoxType.FEATURE);
		final TextModel textModel = new TextModel("is Selected", new AffineTransform(1, 0, 0, 1, 1, 1),
				TextType.IS_SELECTED);

	

		textModel.setParentBlock(operatorBlock);
		choiceBoxModel.setParentBlock(operatorBlock);

		visualParts.add(operatorBlock);
		visualParts.add(choiceBoxModel);
		visualParts.add(textModel);
		return visualParts;

	}

	public static List<Object> createOperatorContextualInfoBooleanBlockGroup(
			List<HyContextualInformationBoolean> contextualInformationBooleans) {
		final List<Object> contents = new ArrayList<>();

		final List<String> values = new ArrayList<>();
		values.add("TRUE");
		values.add("FALSE");

		final OperatorMovableBlockModel operatorBlockModel = new OperatorMovableBlockModel(
				OperatorShapeService.createOperatorShape(), new AffineTransform(1, 0, 0, 1, 1, 1), null, null);

		final ChoiceBoxModel<HyContextualInformationBoolean> choiceBoxModelContexts = new ChoiceBoxModel<HyContextualInformationBoolean>(
				new AffineTransform(1, 0, 0, 1, 1, 1), contextualInformationBooleans, ChoiceBoxType.CONTEXT_BOOLEAN);
		final ChoiceBoxModel<String> choiceBoxModelValues = new ChoiceBoxModel<String>(
				new AffineTransform(1, 0, 0, 1, 1, 1), values, ChoiceBoxType.VALUE_BOOLEAN);

		final TextModel textModel = new TextModel("=", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);

		choiceBoxModelContexts.setParentBlock(operatorBlockModel);
		choiceBoxModelValues.setParentBlock(operatorBlockModel);

		textModel.setParentBlock(operatorBlockModel);

		contents.add(operatorBlockModel);
		contents.add(choiceBoxModelContexts);
		contents.add(choiceBoxModelValues);
		contents.add(textModel);

		return contents;

	}

	public static List<Object> createIsFeatureSelectedGroup(List<HyFeature> choices) {
		final List<Object> visualParts = new ArrayList<>();

		final OperatorMovableBlockModel operatorBlockModel = new OperatorMovableBlockModel(
				OperatorShapeService.createOperatorShape(), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN,
				null);

		final ChoiceBoxModel<HyFeature> choiceBoxModel = new ChoiceBoxModel<HyFeature>(
				new AffineTransform(1, 0, 0, 1, 1, 1), choices, ChoiceBoxType.FEATURE);

		final TextModel textModel = new TextModel("is selected", new AffineTransform(1, 0, 0, 1, 1, 1),
				TextType.IS_SELECTED);

		choiceBoxModel.setParentBlock(operatorBlockModel);
		
		textModel.addAnchorage(operatorBlockModel);

		visualParts.add(operatorBlockModel);
		visualParts.add(choiceBoxModel);
		visualParts.add(textModel);

		return visualParts;

	}



	public static List<Object> createContextPaletteContent(HyFeatureModel featureModel, HyContextModel contextModel,
			List<HyFeature> features) {
		List<Object> paletteContents = new ArrayList<>();


		if (contextModel == null) {
			return paletteContents;
		}

		List<HyFeatureAttribute> attributes = new ArrayList<>();

		for (HyFeature feature : features) {
			attributes.addAll(feature.getAttributes());

		}

		List<HyContextualInformation> contextualInformationBooleans = new ArrayList<>();
		List<HyContextualInformationNumber> contextualInformationNumbers = new ArrayList<>();
		List<HyContextualInformationEnum> contextualInformationEnums = new ArrayList<>();

		for (HyContextualInformation contextualInformation : contextModel.getContextualInformations()) {
			if (contextualInformation instanceof HyContextualInformationBoolean) {
				contextualInformationBooleans.add((HyContextualInformationBoolean) contextualInformation);
			}
			if (contextualInformation instanceof HyContextualInformationNumber) {
				contextualInformationNumbers.add((HyContextualInformationNumber) contextualInformation);
			}
			if (contextualInformation instanceof HyContextualInformationEnum) {
				contextualInformationEnums.add((HyContextualInformationEnum) contextualInformation);
			}
		}

		CreateNewContextButton button = new CreateNewContextButton(new AffineTransform(1, 0, 0, 1, 1, 1));
		paletteContents.add(button);



		paletteContents.addAll(OperatorShapeService.createAllOperatorsForContexts(featureModel, contextModel));
		return paletteContents;

	}



	public static List<Object> createContentViewerContents(HyConstraintModel model) {
		List<Object> paletteContents = new ArrayList<>();
	
		TreeIterator<EObject> tree = model.eAllContents();

		EList<HyConstraint> constraints = model.getConstraints();
		for (HyConstraint constraint : constraints) {
			constraint.getRootExpression();
			TreeIterator<EObject> iterator = constraint.eAllContents();
			while (iterator.hasNext()) {
				iterator.hashCode();
			
			}
		}

		final OperatorMovableBlockModel operatorBlockModel = new OperatorMovableBlockModel(
				OperatorShapeService.createOperatorShape(), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN,
				null);
		paletteContents.add(operatorBlockModel);
		return paletteContents;
	}



	static AffineTransform createDefaultAffineTransform() {
		return new AffineTransform(1, 0, 0, 1, 1, 1);
	}

}
