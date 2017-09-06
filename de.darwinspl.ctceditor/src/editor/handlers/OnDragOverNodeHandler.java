package editor.handlers;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorMovableBlock;
import editor.model.control.ControlBlockModel;
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
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseDragEvent;

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

	@Override
	public void mouseDragEnteredTarget(MouseDragEvent event, Dimension delta) {
		// ObservableList<Object> list = getHost().getViewer().getContents();

		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(0.5);

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

			ControlBlockPart controlBlockPart = (ControlBlockPart) getHost();
			Object o = getHost().getContent();

			if (o instanceof ControlIfBlockModel) {

				ControlIfBlockModel controlBlock = (ControlIfBlockModel) o;
				ObservableList<IContentPart<? extends Node>> list2 = getContentViewer().getAdapter(SelectionModel.class)
						.getSelectionUnmodifiable();

				for (IContentPart<? extends Node> n : list2) {

					if (n instanceof ControlBlockPart) {

						if (!((ControlBlockPart) n).getAnchoragesUnmodifiable().containsEntry(controlBlockPart,
								AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE) && !(controlBlockPart.getAnchoragesUnmodifiable().containsEntry(n, AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE))) {
							
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
							((ControlBlockPart) n).doAttachToAnchorageVisual(controlBlockPart, "relink");
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
