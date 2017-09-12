package editor.handlers;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.ICurve;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorMovableBlock;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlBlockModel;
import editor.model.control.ControlBlockModel.ControlBlockOperandType;
import editor.model.control.ControlIfBlockModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorMovableBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.arithmetical.ArithmeticalOperatorFixedBlockPart;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;

/**
 * Handler for the drag over gestures
 * 
 * provides the functionality to combine blocks using drag and drop
 * 
 * @author Anna-Liisa
 *
 */
public class OnDragOverNodeHandler extends AbstractHandler implements IOnDragOverHandler {

	@Override
	public void mouseDragOver(MouseDragEvent event, Dimension delta) {

	}
	
	
	private boolean isBlockAddedAsOperand1(ControlBlockPart childBlock, ControlBlockPart part){
		
		double childMinY = childBlock.getVisual().getBoundsInParent().getMinY();
		
		Bounds parentBound = part.getVisual().getBoundsInParent();
		
		double parentHight = parentBound.getHeight();
		System.out.println(parentHight);
		
		double parentMinY = parentBound.getMinY();
		System.out.println(parentMinY);
		double half = parentMinY + (parentHight/2);
		System.out.println(childMinY);
		
		if(half < childMinY){
		    return false;
		}else{
			return true;
		}
	}

	@Override
	public void mouseDragEnteredTarget(MouseDragEvent event, Dimension delta) {
		// ObservableList<Object> list = getHost().getViewer().getContents();

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.5);
		
		System.out.println("IN " +  getHost().getContent());
		if(getHost() instanceof ControlBlockPart){
			
			ControlBlockPart part = (ControlBlockPart) getHost();

			ControlAndOrBlock controlBlock;
			
			Object o = getHost().getContent();
			

			if (o instanceof ControlAndOrBlock) {
				controlBlock = (ControlAndOrBlock) o;
		

				ObservableList<IContentPart<? extends Node>> list2 = getContentViewer().getAdapter(SelectionModel.class)
						.getSelectionUnmodifiable();

				for (IContentPart<? extends Node> n : list2) {

					if (n instanceof ControlBlockPart) {

						ControlBlockPart childBlock = (ControlBlockPart) n;
					
						DropShadow dropShadow = new DropShadow(50, Color.GREY);
						DropShadow ds2 = new DropShadow(50, Color.LIGHTBLUE);
						 //Setting the height of the shadow
					      dropShadow.setHeight(5); 
					      
					      //Setting the width of the shadow 
					      dropShadow.setWidth(5); 
					      
					      //Setting the radius of the shadow 
					      dropShadow.setRadius(5); 
					      
					      //setting the offset of the shadow 
					      dropShadow.setOffsetX(3); 
					      dropShadow.setOffsetY(2); 
					      
					      //Setting the spread of the shadow 
					      dropShadow.setSpread(12); 
						
//					      node.setStyle("-fx-effect: dropshadow(three-pass-box, purple, 0.0, 25.0, 0.0, -5.0);"); //north
						if(isBlockAddedAsOperand1(childBlock, part)){
							controlBlock.setEffect(dropShadow);
							part.refreshVisual();
							
						}else{
							DropShadow dstest = new DropShadow(null, Color.RED, 0.0, 25.0, 10.0, 5.0);
							controlBlock.setEffect(dstest);
							IShape shapebefore = part.getVisual().geometryProperty().getValue();
							ICurve curveBefore = shapebefore.getOutline();
						
							double h1 = part.getVisual().getBoundsInParent().getHeight();
							part.refreshVisual();
						
							IShape shape = part.getVisual().geometryProperty().getValue();
							ICurve curveAfter = shape.getOutline();
					
						    double h2 = part.getVisual().getBoundsInParent().getHeight();
						    
						    part.refreshVisual();
						    IShape shapetest = part.getVisual().getGeometry();
						    
						
							IShape hhh = part.getVisual().geometryProperty().get();
							
							
							
							
							if(curveAfter.equals(curveBefore)){
							System.out.println("ja");
							}
						
						}
						

					}

				}
			
			}
		}

		if ((getHost()) instanceof ControlBlockPart) {
			ControlBlockPart part = (ControlBlockPart) getHost();

			ControlIfBlockModel controlBlock;
			
			Object o = getHost().getContent();

			if (o instanceof ControlIfBlockModel) {
				controlBlock = (ControlIfBlockModel) o;
				if(controlBlock.getChildBlocks().isEmpty()){

				ObservableList<IContentPart<? extends Node>> list2 = getContentViewer().getAdapter(SelectionModel.class)
						.getSelectionUnmodifiable();

				for (IContentPart<? extends Node> n : list2) {

					if (n instanceof ControlBlockPart) {
						controlBlock.setEffect(colorAdjust);
						part.refreshVisual();

					}

				}
			}
			}
		}

		if ((getHost()) instanceof OperatorFixedBlockPart) {

			OperatorFixedBlockModel opModel = (OperatorFixedBlockModel) getHost().getContent();

			ObservableList<IContentPart<? extends Node>> list3 = getContentViewer().getAdapter(SelectionModel.class)
					.getSelectionUnmodifiable();

			for (IContentPart<? extends Node> n : list3) {

				if (n instanceof OperatorMovableBlockPart) {

					if (n.getContent() instanceof OperatorMovableBlockModel) {
						opModel.setEffect(colorAdjust);
						getHost().refreshVisual();

					}

				}

			}
		}

		if ((getHost()) instanceof ArithmeticalOperatorFixedBlockPart) {

			ArithmeticalOperatorFixedBlock opModel = (ArithmeticalOperatorFixedBlock) getHost().getContent();

			ObservableList<IContentPart<? extends Node>> list3 = getContentViewer().getAdapter(SelectionModel.class)
					.getSelectionUnmodifiable();

			for (IContentPart<? extends Node> n : list3) {

				if (n instanceof ArithmeticalOperatorMovableBlockPart) {

					if (n.getContent() instanceof ArithmeticalOperatorMovableBlock) {
						opModel.setEffect(colorAdjust);
						getHost().refreshVisual();

					}

				}

			}
		}

	}

	protected IViewer getContentViewer() {
		return getHost().getRoot().getViewer().getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
	}

	@Override
	public IContentPart<? extends Node> getHost() {
		if (super.getHost() instanceof OperatorFixedBlockPart) {
			return (OperatorFixedBlockPart) super.getHost();
		} else if (super.getHost() instanceof ArithmeticalOperatorFixedBlockPart) {
			return (ArithmeticalOperatorFixedBlockPart) super.getHost();
		} else {
			return (GeometricShapePart) super.getHost();
		}
	}

	@Override
	public void mouseDragExitedTarget(MouseDragEvent event, Dimension delta) {
	

		if ((getHost()) instanceof ControlBlockPart) {
			ControlBlockPart part = (ControlBlockPart) getHost();
			
		

			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);

			ControlIfBlockModel controlBlock;
			Object o = getHost().getContent();

			if (o instanceof ControlIfBlockModel) {
				controlBlock = (ControlIfBlockModel) o;
				controlBlock.setEffect(colorAdjust);

				part.refreshVisual();
				
			}else if(o instanceof ControlAndOrBlock){
				ControlAndOrBlock child = (ControlAndOrBlock) o;
				child.setEffect(null);
				part.refreshVisual();
			}
		}
		
		if ((getHost()) instanceof OperatorFixedBlockPart) {
			OperatorFixedBlockPart part = (OperatorFixedBlockPart) getHost();

			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);

			OperatorFixedBlockModel opModel = (OperatorFixedBlockModel) part.getContent();

			opModel.setEffect(colorAdjust);

			part.refreshVisual();

		}
		if((getHost() instanceof ArithmeticalOperatorFixedBlockPart)){
			ArithmeticalOperatorFixedBlockPart part = (ArithmeticalOperatorFixedBlockPart) getHost();
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(0);
		ArithmeticalOperatorFixedBlock model  = part.getContent();
		model.setEffect(colorAdjust);
		part.refreshVisual();
			
		}
	}

	@Override
	public void mouseDragReleased(MouseDragEvent event, Dimension delta) {

		if ((getHost()) instanceof ControlBlockPart) {

			ControlBlockPart parentPart = (ControlBlockPart) getHost();
			Object o = getHost().getContent();
			
			
			if(o instanceof ControlAndOrBlock){
				
				ControlAndOrBlock parentModel = (ControlAndOrBlock) o;
				ObservableList<IContentPart<? extends Node>> selectedParts = getContentViewer().getAdapter(SelectionModel.class)
						.getSelectionUnmodifiable();

				for (IContentPart<? extends Node> n : selectedParts) {

					if (n instanceof ControlBlockPart) {
						ControlBlockPart childPart = (ControlBlockPart) n;
						if (!(childPart.getAnchoragesUnmodifiable().containsEntry(parentPart,
								AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE) && !(parentPart.getAnchoragesUnmodifiable().containsEntry(n, AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)))) {
							
							
							ControlBlockModel childModel = childPart.getContent();
							
				
								if(childModel.getParentBlock()== null){
									if(isBlockAddedAsOperand1(childPart, parentPart)){ 
									childModel.setOperandType(ControlBlockOperandType.OPERAND1);
									} else{
										childModel.setOperandType(ControlBlockOperandType.OPERAND2);
									}
									childModel.setParentBlock(parentModel);
									childPart.refreshContentAnchorages();
									childPart.refreshVisual();
								}
									
							}
							 
						
						else {
							childPart.doAttachToAnchorageVisual(parentPart, "relink");
						}
					}

		
				}
				
			}

			if (o instanceof ControlIfBlockModel) {

				ControlIfBlockModel controlBlock = (ControlIfBlockModel) o;
				ObservableList<IContentPart<? extends Node>> list2 = getContentViewer().getAdapter(SelectionModel.class)
						.getSelectionUnmodifiable();

				for (IContentPart<? extends Node> n : list2) {

					if (n instanceof ControlBlockPart) {

						if (!((ControlBlockPart) n).getAnchoragesUnmodifiable().containsEntry(parentPart,
								AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE) && !(parentPart.getAnchoragesUnmodifiable().containsEntry(n, AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE))) {
							
							ControlBlockModel childBlockModel = ((ControlBlockPart) n).getContent();
							if(controlBlock.getChildBlocks().isEmpty()){
							if (childBlockModel.getParentBlock() == null) {
								childBlockModel.setParentBlock(controlBlock);
								((ControlBlockPart) n).refreshContentAnchorages();
								((ControlBlockPart) n).refreshVisual();
							}
							}
						} 
						else {
							((ControlBlockPart) n).doAttachToAnchorageVisual(parentPart, "relink");
						}
					}

		

				}

			}

		}

		if ((getHost()) instanceof OperatorFixedBlockPart) {

			OperatorFixedBlockPart fixedOperatorPart = (OperatorFixedBlockPart) getHost();

			OperatorFixedBlockModel fixedOperatorModel = (OperatorFixedBlockModel) fixedOperatorPart.getContent();
			ObservableList<IContentPart<? extends Node>> selection = getContentViewer().getAdapter(SelectionModel.class)
					.getSelectionUnmodifiable();

			for (IContentPart<? extends Node> n : selection) {

				if (n instanceof OperatorMovableBlockPart) {
					OperatorMovableBlockPart movableOperatorPart = (OperatorMovableBlockPart) n;

					OperatorMovableBlockModel movableOperatorModel = (OperatorMovableBlockModel) n.getContent();
					if (!(movableOperatorPart.getAnchoragesUnmodifiable().containsEntry(fixedOperatorPart,
							AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE))) {

						movableOperatorModel.setParentBlock(fixedOperatorModel);
						movableOperatorPart.refreshContentAnchorages();
					    movableOperatorPart.refreshVisual();

					} else {
						movableOperatorPart.doAttachToAnchorageVisual(fixedOperatorPart, "relink");
					}

				}
			}

		}
		
		
		
//		fixedOperatorPart.refreshContentAnchorages();
//		movableOperatorPart.refreshContentAnchorages();
//		fixedOperatorPart.refreshVisual();
//		movableOperatorPart.refreshVisual();
		if ((getHost()) instanceof ArithmeticalOperatorFixedBlockPart) {

			ArithmeticalOperatorFixedBlockPart operationBlockPart = (ArithmeticalOperatorFixedBlockPart) getHost();

			ArithmeticalOperatorFixedBlock operatorBlockModel = (ArithmeticalOperatorFixedBlock) operationBlockPart
					.getContent();
			ObservableList<IContentPart<? extends Node>> list2 = getContentViewer().getAdapter(SelectionModel.class)
					.getSelectionUnmodifiable();

			for (IContentPart<? extends Node> n : list2) {

				if (n instanceof ArithmeticalOperatorMovableBlockPart) {
					ArithmeticalOperatorMovableBlockPart operatorBlockPart2 = (ArithmeticalOperatorMovableBlockPart) n;

					ArithmeticalOperatorMovableBlock operatorBlock = (ArithmeticalOperatorMovableBlock) n.getContent();
					if (!(operatorBlockPart2.getAnchoragesUnmodifiable().containsEntry(operationBlockPart,
							AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE))) {

					
						if(operatorBlock.getParentBlock()==null){
						operatorBlock.setParentBlock(operatorBlockModel);
						operatorBlockPart2.refreshContentAnchorages();
					
						operatorBlockPart2.refreshVisual();
						
						operationBlockPart.refreshContentAnchorages();
						operatorBlockPart2.refreshContentAnchorages();
						operationBlockPart.refreshVisual();
						operatorBlockPart2.refreshVisual();
						}
					} else {
						operatorBlockPart2.doAttachToAnchorageVisual(operationBlockPart,
								"relink");
					}

				}
			}
		}

	}

	@Override
	public void abortDrag() {

	}

	@Override
	public void mouseDragReleaseOnScene(MouseDragEvent event, Dimension delta) {

		
	}

}
