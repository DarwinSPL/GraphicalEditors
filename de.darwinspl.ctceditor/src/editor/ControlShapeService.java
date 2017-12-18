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
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.control.ControlValidateOperatorBlockModel.ControlValidateOperatorBlockType;
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
	
	public static CurvedPolygon createCustomControlAndOrBlockShape(){
		return createControlAndOrBlockShape(150, 60, 10, 45, 50, 30);
		
	}

	
	/**
	 * Method for adjusting the shape of a ControlAndOrBlock according to an added first operand
	 * 
	 * 
	 * @param currentShape current shape of he controlAndOrBlock
	 * @param newHeight Hight of the added operand
	 * @return the new shape
	 */
	public static CurvedPolygon adjustControlAndOrBlockShapeOperand1(CurvedPolygon currentShape, double newHeight ){
		BezierCurve[] bezier = currentShape.getOutlineSegments();
		newHeight = newHeight +2;
		double currentHight = bezier[8].getY1() - bezier[8].getY2();
		double difference = newHeight - currentHight;
		bezier[0] = new Line(0, bezier[0].getY1()-difference, bezier[0].getX2(), bezier[0].getY2());
		double startY = bezier[7].getY2();
		double startX = bezier[7].getX2();
		bezier[8] = new Line(startX, startY, startX, startY - newHeight);
		bezier[9] = new Line(startX, startY - newHeight, bezier[10].getX2(), startY-newHeight);
		bezier[10] = new Line(bezier[10].getX2(), startY-newHeight, bezier[10].getX2(), bezier[10].getY2()- difference);
		bezier[11] = new Line(bezier[10].getX2(), bezier[10].getY2(), 0, bezier[10].getY2());
		return new CurvedPolygon(bezier);
	
	}
	
	
	/**
	 * 
	 * Method for adjusting the height of an given Control Block Shape according to a given child Block height
	 * 
	 * @param currentShape
	 * @param newHeight
	 * @return
	 */
	public static CurvedPolygon adjustControlIfBlockShapeHight(CurvedPolygon currentShape, double newHeight){
		BezierCurve[] bezier = currentShape.getOutlineSegments();
		
		newHeight = newHeight+2;
		double currentHight = bezier[4].getY1() - bezier[4].getY2();
		double  difference = newHeight-currentHight;
		
		bezier[0] = new Line(bezier[0].getX1(), bezier[0].getY1(), bezier[0].getX2(), bezier[0].getY2()+difference);
		bezier[1] = new Line(bezier[0].getX2(), bezier[0].getY2(), bezier[1].getX2(), bezier[0].getY2());
		bezier[2] = new Line(bezier[1].getX2(), bezier[1].getY2(), bezier[2].getX2(), bezier[2].getY2()+ difference);
		bezier[3] = new Line(bezier[2].getX2(), bezier[2].getY2(), bezier[3].getX2(), bezier[2].getY2());
		bezier[4] = new Line(bezier[3].getX2(), bezier[3].getY2(), bezier[3].getX2(), bezier[4].getY2());
		return new CurvedPolygon(bezier);
		
	}
	
	/**
	 * Method for adjusting the shape of a ControlAndOrBlock according to an added first operand
	 * 
	 * 
	 * @param currentShape current shape of he controlAndOrBlock
	 * @param newHight Hight of the added operand
	 * @return the new shape
	 */
	public static CurvedPolygon adjustControlAndOrBlockShapeOperand2(CurvedPolygon currentShape, double newHight ){
		BezierCurve[] bezier = currentShape.getOutlineSegments();
		newHight = newHight +2;
		double currentHight = bezier[4].getY1() - bezier[4].getY2();
		double difference = newHight - currentHight;
		
		bezier[0] = new Line(bezier[0].getX1(), bezier[0].getY1(), bezier[0].getX2(), bezier[0].getY2()+ difference);
		bezier[1] = new Line(bezier[1].getX1(), bezier[0].getY2(), bezier[1].getX2(), bezier[0].getY2());
		bezier[2] = new Line(bezier[1].getX2(), bezier[1].getY2(), bezier[2].getX2(), bezier[2].getY2() + difference);
		bezier[3] = new Line(bezier[2].getX2(), bezier[2].getY2(), bezier[3].getX2(), bezier[2].getY2());
		bezier[4] = new Line(bezier[3].getX2(), bezier[3].getY2(), bezier[3].getX2(), bezier[4].getY2());
		return new CurvedPolygon(bezier);
	
	}
	
	
	/**	
	 *   _____
	 *  |  ___|
	 *  | | 
	 *  | |___
	 * a|  ___|f
	 *  | |e  
	 *  | |__d
	 *  |____| c
	 *    b
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @return
	 */
	public static CurvedPolygon createControlAndOrBlockShape(int a, int b, int c, int d, int e, int f){
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 0, 0, a));
		segments.add(new Line(0, a, b, a));
		segments.add(new Line(b, a, b, a-c));
		segments.add(new Line(b, a-c, b-d, a-c));
		segments.add(new Line(b-d, a-c, b-d, a-c-e));
		segments.add(new Line(b-d, a-c-e, b, a-c-e));
		segments.add(new Line(b, a-c-e, b, a-c-e-f));
		segments.add(new Line(b, a-c-e-f, b-d, a-c-e-f));
		segments.add(new Line(b-d, a-c-e-f, b-d, a-c-e-f-e));
		segments.add(new Line(b-d, a-c-e-f-e, b, a-c-e-f-e));
		segments.add(new Line(b, a-c-e-f-e, b, 0));
		segments.add(new Line(b, 0, 0, 0));
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

//	public static CurvedPolygon sizeControlBlockShape(int numberOfChildBlocks) {
//		List<BezierCurve> segments = new ArrayList<>();
//		int bla = (numberOfChildBlocks - 1) * 30;
//		segments.add(new Line(0, 0, 0, (90 + bla)));
//		segments.add(new Line(0, (90 + bla), 200, 90 + bla));
//		segments.add(new Line(200, 90 + bla, 200, 60 + bla));
//		segments.add(new Line(200, 60 + bla, 25, 60 + bla));
//		segments.add(new Line(25, 60 + bla, 25, 30));
//		segments.add(new Line(25, 30, 200, 30));
//		segments.add(new Line(200, 30, 200, 0));
//		segments.add(new Line(200, 0, 0, 0));
//
//		return new CurvedPolygon(segments);
//	}

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
