package editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.Line;

import editor.model.AbstractBlockElement;
import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.control.ControlBlockModel.ControlValidateOperatorBlockType;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorFixedBlockModel.OperatorFixedBlockType;
import editor.model.vf.VFControlBlockModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureModel;
import javafx.scene.paint.Color;

public class ControlShapeService {

	public static CurvedPolygon createControlBlockShape() {
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 0, 0, 90));
		segments.add(new Line(0, 90, 200, 90));
		segments.add(new Line(200, 90, 200, 60));
		segments.add(new Line(200, 60, 25, 60));
		segments.add(new Line(25, 60, 25, 30));
		segments.add(new Line(25, 30, 200, 30));
		segments.add(new Line(200, 30, 200, 0));
		segments.add(new Line(200, 0, 0, 0));
		return new CurvedPolygon(segments);
	}
	public static List<AbstractBlockElement> createImpliesNot(){
		List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();
		final ControlIfBlockModel model = new ControlIfBlockModel(createImpliesOrImplies(), new AffineTransform(1,0,0,1,1,1),Color.YELLOW, null, ControlBlockType.NOT_IMPLIES);
	visualParts.add(model);
	return visualParts;
	
	}
	
	public static CurvedPolygon createImpliesOrImplies() {
		List<BezierCurve> segments = new ArrayList<>();
		int w = 70;
		int l = 160;
		int m = 30;
		segments.add(new Line(0, 0, 60, 0));
		segments.add(new Line(60, 0, 60 ,30));
		segments.add(new Line(60, 30, 30, 30));
		segments.add(new Line(30, 30, 30, 120));
		segments.add(new Line(30, 120, 60, 120));
		segments.add(new Line(60, 120, 60, 150));
		segments.add(new Line(60, 150, 30, 150));
		segments.add(new Line(30, 150, 30, 240));
		segments.add(new Line(30, 240, 60, 240));
		segments.add(new Line(60, 240, 60, 270));
		segments.add(new Line(60, 270, 0, 270));
		segments.add(new Line(0, 270, 0, 0));
        return new CurvedPolygon(segments);
	}

	public static CurvedPolygon sizeControlBlockShape(int numberOfChildBlocks) {
		List<BezierCurve> segments = new ArrayList<>();
		int bla = (numberOfChildBlocks - 1) * 30;
		segments.add(new Line(0, 0, 0, (90 + bla)));
		segments.add(new Line(0, (90 + bla), 200, 90 + bla));
		segments.add(new Line(200, 90 + bla, 200, 60 + bla));
		segments.add(new Line(200, 60 + bla, 25, 60 + bla));
		segments.add(new Line(25, 60 + bla, 25, 30));
		segments.add(new Line(25, 30, 200, 30));
		segments.add(new Line(200, 30, 200, 0));
		segments.add(new Line(200, 0, 0, 0));

		return new CurvedPolygon(segments);
	}

	public static CurvedPolygon createControlIfBlockShape(int numberOfChildBlocks, int width) {
		int height = 90 + ((numberOfChildBlocks - 1) * 30);
		return createCustomControlBlockShape(height, width);
	}

	public static CurvedPolygon createDefaultControlBlockShape() {
		return createCustomControlBlockShape(90, 200);

	}

	public static CurvedPolygon createCustomControlValidateOperatorShape(int height, int width) {
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 0, 0, height));
		segments.add(new Line(0, height, width, height));
		segments.add(new Line(width, height, width, 0));
		segments.add(new Line(width, 0, 0, 0));
		return new CurvedPolygon(segments);

	}

	public static CurvedPolygon createCustomControlBlockShape(int height, int width) {

		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 0, 0, height));
		segments.add(new Line(0, height, width, height));
		segments.add(new Line(width, height, width, height - 30));
		segments.add(new Line(width, height - 30, 25, height - 30));
		segments.add(new Line(25, height - 30, 25, 30));
		segments.add(new Line(25, 30, width, 30));
		segments.add(new Line(width, 30, width, 0));
		segments.add(new Line(width, 0, 0, 0));

		return new CurvedPolygon(segments);

	}

	public static List<Object> createControlValidateOperatorBlockGroup(ControlValidateOperatorBlockType type) {

		List<Object> visualParts = new ArrayList<>();

		final ControlValidateOperatorBlockModel controlBlockModel = new ControlValidateOperatorBlockModel(
				createCustomControlValidateOperatorShape(30, 200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.YELLOW,
				null, ControlValidateOperatorBlockType.VALIDATE);

		final OperatorFixedBlockModel fixedOperatorModel = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(160),
				OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null,
				OperatorFixedBlockType.VALIDATE_OPERATOR);

		fixedOperatorModel.setParentBlock(controlBlockModel);

		visualParts.add(controlBlockModel);

		visualParts.add(fixedOperatorModel);

		if (type.equals(ControlValidateOperatorBlockType.VALIDATE_NOT)) {
			final TextModel textModel = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.IF);
			textModel.addAnchorage(controlBlockModel);
			visualParts.add(textModel);

		}
		return visualParts;

	}

	
	public static List<AbstractBlockElement> createVadilityFormulaBlockShape(HyFeatureModel featureModel, HyContextModel contextModel){
		
			List<AbstractBlockElement> blockElements = new ArrayList<>();

			// Create Model for block elements
			final VFControlBlockModel controlBlock = new VFControlBlockModel(ControlShapeService.createCustomControlBlockShape(90, 260),
					new AffineTransform(1, 0, 0, 1, 23, 5), Color.BLUE, null, ControlBlockType.IMPLIES);

			final ChoiceBoxFeatureModel choiceBox = new ChoiceBoxFeatureModel(new AffineTransform(1,0,0,1,1,1 ), contextModel, featureModel,ChoiceBoxType.FEATURE );

			final TextModel textModel = new TextModel("selectable if", new AffineTransform(1,0,0,1,1,1), TextType.SELECTABLE);
			textModel.setFill(Color.WHITE);
			choiceBox.setParentBlock(controlBlock);
			textModel.setParentBlock(controlBlock);

			blockElements.add(controlBlock);
			blockElements.add(choiceBox);
			blockElements.add(textModel);
			// blockElements.add(textModel);
			return blockElements;
		
		
		
	}
	
	public static List<AbstractBlockElement> createVadilityFormulaBlockShape2(HyFeatureModel featureModel, HyContextModel contextModel){
		
		List<AbstractBlockElement> blockElements = new ArrayList<>();

		// Create Model for block elements
		final VFControlBlockModel controlBlock = new VFControlBlockModel(ControlShapeService.createCustomControlBlockShape(90, 380),
				new AffineTransform(1, 0, 0, 1, 23, 5), Color.BLUE, null, ControlBlockType.IMPLIES);

		final ChoiceBoxModel<HyFeature> choiceBox = new ChoiceBoxFeatureModel(new AffineTransform(1,0,0,1,1,1 ), contextModel, featureModel ,ChoiceBoxType.FEATURE );

		final TextModel textModel = new TextModel("selectable if", new AffineTransform(1,0,0,1,1,1), TextType.SELECTABLE);
		textModel.setFill(Color.WHITE);
		choiceBox.setParentBlock(controlBlock);
		textModel.setParentBlock(controlBlock);

		blockElements.add(controlBlock);
		blockElements.add(choiceBox);
		blockElements.add(textModel);
		// blockElements.add(textModel);
		return blockElements;
	
	
	
}
}
