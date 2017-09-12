package editor.parts.control;

import java.util.List;
import java.util.Set;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Multiset.Entry;

import editor.ControlShapeService;
import editor.ShapeService;
import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlBlockModel;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.control.ControlBlockModel.ControlBlockOperandType;
import editor.model.nodes.TextModel.TextType;
import editor.parts.GeometricShapePart;
import editor.parts.nodes.TextPart;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class ControlBlockPart extends GeometricShapePart {

	@Override
	protected GeometryNode<IShape> doCreateVisual() {

		GeometryNode<IShape> geometryNode = super.doCreateVisual();

		return geometryNode;

	}

	@Override
	public ControlBlockModel getContent() {
		return (ControlBlockModel) super.getContent();
	}

	@Override
	public void resizeIncludingParent(int widthDifference) {

		int oldH = (int) getContent().getGeometry().getBounds().getHeight();
		
		int oldW = (int) getContent().getGeometry().getBounds().getWidth();

		if (getContent() instanceof ControlIfBlockModel) {

		getContent().setGeometry((IShape) ControlShapeService.createCustomControlBlockShape(oldH, widthDifference + oldW));
		refreshVisual();
		if(((ControlIfBlockModel) getContent()).getControlBlockType().equals(ControlBlockType.EQUIVALENCE)){
			
			ObservableMultiset<IVisualPart<? extends Node>> linked = getAnchoredsUnmodifiable();

			
			for (Entry<IVisualPart<? extends Node>> anch : linked.entrySet()) {
				
				if (anch.getElement() instanceof TextPart) {
					((TextPart) anch.getElement()).doAttachToAnchorageVisual(this, "relink");
					((TextPart)anch.getElement()).refreshVisual();
				}
			}
		}
		
		}
		if (getContent() instanceof ControlValidateOperatorBlockModel) {

			
			getContent().setGeometry((IShape) ControlShapeService.createCustomControlValidateOperatorShape(oldH,
					widthDifference + oldW));

		}

		
	}

	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {

		ObservableMultiset<IVisualPart<? extends Node>> allAn = anchorage.getAnchoredsUnmodifiable();
		int i = 0;
		for (IVisualPart<? extends Node> node : allAn) {
			
			if (node instanceof ControlBlockPart) {
				i++;
			}

		}

		double distanceX = 0;
		double distanceY = 0;

		if(!role.equals("relink")){
		if (anchorage instanceof ControlBlockPart) {

			ControlBlockPart anchPart = (ControlBlockPart) anchorage;

			Bounds b11 = anchorage.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();
			
			
			
			if(anchPart.getContent() instanceof ControlAndOrBlock){
				
				ControlBlockModel childModel = getContent();
				
				Double childHight = childModel.getGeometry().getBounds().getHeight();
				
				CurvedPolygon newShape;
				if(childModel.getOperandType().equals(ControlBlockOperandType.OPERAND1)){
					
				
					
					newShape = ControlShapeService.adjustControlAndOrBlockShapeOperand1((CurvedPolygon)anchPart.getContent().getGeometry(), childHight);
					
					
					
					
				} else{
				 newShape = ControlShapeService.adjustControlAndOrBlockShapeOperand2((CurvedPolygon)anchPart.getContent().getGeometry(), childHight);	
				}
				
				anchPart.getContent().setGeometry(newShape);
				anchPart.refreshVisual();
				System.out.println("in the refresh");
				
			}
			
			else if (getContent() instanceof ControlBlockModel) {

				double childHightsAdded = 0;
				
				List<AbstractGeometricElement<? extends IGeometry>> childBlocks = ((ControlIfBlockModel) ((ControlBlockPart) anchorage)
						.getContent()).getChildBlocks();
				
				for (AbstractGeometricElement<? extends IGeometry> child : childBlocks) {
					if (!(child.equals(getContent()))) {
						if(child.getBoundsInParent()!=null){
						childHightsAdded += child.getBoundsInParent().getHeight();
						}else{
							childHightsAdded += child.getGeometry().getBounds().getHeight();
						}
					}
				}

				double hightParentBlock = b11.getHeight();

				double hightBottomBlockPart = 0;
				if (childHightsAdded==0) {
					hightBottomBlockPart = ((hightParentBlock / 3));
				} else {
					hightBottomBlockPart = ((hightParentBlock - childHightsAdded) / 2);
				}

				double hightNewChild = b22.getHeight();
				double newHight = (hightParentBlock + hightNewChild);

				if (i == 1) {
					newHight -= hightBottomBlockPart;
				}

				if(!role.equals("relink")){
				((ControlBlockPart) anchorage).getContent()
						.setGeometry(ControlShapeService.createCustomControlBlockShape((int) newHight,
								(int) ((ControlBlockPart) anchorage).getContent().getGeometry().getBounds()
										.getWidth()));
				
				
				ObservableMultiset<IVisualPart<? extends Node>> linked = anchPart.getAnchoredsUnmodifiable();

				for (Entry<IVisualPart<? extends Node>> anch : linked.entrySet()) {
					
					if (anch.getElement() instanceof TextPart) {
						if(((TextPart) anch.getElement()).getContent().getTextType().equals(TextType.EQUIVALENCE)){
						((TextPart) anch.getElement()).getContent().getTransform().translate(0, newHight-hightParentBlock);
						((TextPart)anch.getElement()).refreshVisual();
						}
					}
				}
				}

				

				double nX = b11.getMinX() + 25;
				double nY = b11.getMinY() + (newHight - (hightBottomBlockPart + hightNewChild)) + 1;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(distanceX, distanceY);
				refreshVisual();
				relocateAnchorads(distanceX, distanceY);
				
				if (anchPart.getContent().getParentBlock() != null) {

					ControlBlockPart parentParent = null;
					Set<IVisualPart<? extends Node>> anchorages = anchPart.getAnchoragesUnmodifiable().keySet();
					for (IVisualPart<? extends Node> p : anchorages) {
						if (p instanceof ControlBlockPart) {
							if (((ControlBlockPart) p).getContent().equals(anchPart.getContent().getParentBlock())) {
								parentParent = (ControlBlockPart) p;
							}
						}
					}

					if (parentParent != null) {
						parentParent.resizeParentControlBlock((int)(newHight-hightParentBlock));
//						List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
//						linked = getAnchoredsRecursive(linked, parentParent);
//						for (IContentPart<? extends Node> link : linked) {
//							if (link instanceof ControlBlockPart) {
//								((ControlBlockPart) link).doAttachToAnchorageVisual(parentParent, role);
//
//							}
//						}
					}

				}

				
				
		
			} else {

				double idouble = (double) i;

				double nX = b11.getMinX() + 20;
				double nY = b11.getMinY() + (b11.getHeight() / (2 + idouble));

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(distanceX, distanceY);

				refreshVisual();
				relocateAnchorads(distanceX, distanceY);
			}

			// relocateAnchorads(distanceX, distanceY);

		}
		}else{
			if (anchorage instanceof ControlBlockPart) {

				ControlBlockPart anchPart = (ControlBlockPart) anchorage;

				Bounds b11 = anchorage.getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				if (getContent() instanceof ControlBlockModel) {

					double childHightsAdded = 0;
					double childHightsAddedBefore = 0;
					List<AbstractGeometricElement<? extends IGeometry>> childBlocks = ((ControlIfBlockModel) ((ControlBlockPart) anchorage)
							.getContent()).getChildBlocks();
					int index = childBlocks.indexOf(this.getContent());
					
					
					for(int y=0; y<index; y++){
						if(childBlocks.get(y).getBoundsInParent()!=null){
							childHightsAddedBefore += childBlocks.get(y).getBoundsInParent().getHeight();
						}else{
							childHightsAddedBefore += childBlocks.get(y).getGeometry().getBounds().getHeight();
						}
					}
				
					for (AbstractGeometricElement<? extends IGeometry> child : childBlocks) {
						
							if(child.getBoundsInParent()!=null){
							childHightsAdded += child.getBoundsInParent().getHeight();
							}else{
								childHightsAdded += child.getGeometry().getBounds().getHeight();
							}
						
					}
					

					double hightParentBlock = b11.getHeight();

					double hightBottomBlockPart = 0;
					
						hightBottomBlockPart = ((hightParentBlock - childHightsAdded) / 2);
					



					double nX = b11.getMinX() + 25;
					double nY = b11.getMinY() + (hightBottomBlockPart + childHightsAddedBefore) + 1;

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();

					getContent().getTransform().translate(distanceX, distanceY);
					refreshVisual();
					relocateAnchorads(distanceX, distanceY);
					
//					if (anchPart.getContent().getParentBlock() != null) {
//
//						ControlBlockPart parentParent = null;
//						Set<IVisualPart<? extends Node>> anchorages = anchPart.getAnchoragesUnmodifiable().keySet();
//						for (IVisualPart<? extends Node> p : anchorages) {
//							if (p instanceof ControlBlockPart) {
//								if (((ControlBlockPart) p).getContent().equals(anchPart.getContent().getParentBlock())) {
//									parentParent = (ControlBlockPart) p;
//								}
//							}
//						}
//
//						if (parentParent != null) {
//							parentParent.resizeParentControlBlock((int)(newHight-hightParentBlock));
////							List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
////							linked = getAnchoredsRecursive(linked, parentParent);
////							for (IContentPart<? extends Node> link : linked) {
////								if (link instanceof ControlBlockPart) {
////									((ControlBlockPart) link).doAttachToAnchorageVisual(parentParent, role);
//	//
////								}
////							}
//						}
//
//					}

					
					
			
				} else {

					double idouble = (double) i;

					double nX = b11.getMinX() + 25;
					double nY = b11.getMinY() + (b11.getHeight() / (2 + idouble));

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();

					getContent().getTransform().translate(distanceX, distanceY);

					refreshVisual();
					relocateAnchorads(distanceX, distanceY);
				}

				// relocateAnchorads(distanceX, distanceY);

			}
		}



	}

	public void resizeParentControlBlock(int heightdifference){
        int oldH = (int) getContent().getGeometry().getBounds().getHeight();
		
		int oldW = (int) getContent().getGeometry().getBounds().getWidth();

		if (getContent() instanceof ControlIfBlockModel) {

		getContent().setGeometry((IShape) ControlShapeService.createCustomControlBlockShape(oldH + heightdifference, oldW));
		refreshVisual();
		
		
//		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(getAnchoredsUnmodifiable(),
//				IContentPart.class);
//		for(IContentPart<? extends Node> a: anch){
//			if(a instanceof ControlBlockPart){
//				((ControlBlockPart) a).doAttachToAnchorageVisual(this, "relink");
//			}
//		}
//		List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
//		linked = getRelocateChildControlBlocks(linked, this);
//		for (IContentPart<? extends Node> p : linked) {
//			if (p instanceof ControlBlockPart) {
//				if (((ControlBlockPart) p).getContent().equals(anchPart.getContent().getParentBlock())) {
//					parentParent = (ControlBlockPart) p;
//				}
//			}
//		}
//		
		if (getContent().getParentBlock() != null) {

			ControlBlockPart parentParent = null;
			Set<IVisualPart<? extends Node>> anchorages = getAnchoragesUnmodifiable().keySet();
			for (IVisualPart<? extends Node> p : anchorages) {
				if (p instanceof ControlBlockPart) {
					if (((ControlBlockPart) p).getContent().equals(getContent().getParentBlock())) {
						parentParent = (ControlBlockPart) p;
					}
				}
			}

			if (parentParent != null) {
				parentParent.resizeParentControlBlock((int)(heightdifference));
//				List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
//				linked = getAnchoredsRecursive(linked, parentParent);
//				for (IContentPart<? extends Node> link : linked) {
//					if (link instanceof ControlBlockPart) {
//						((ControlBlockPart) link).doAttachToAnchorageVisual(parentParent, role);
//
//					}
//				}
			}

		}
	}}

	Tooltip toolTipMissingOperand2 = new Tooltip("Child Control Block needs to be attached.");
	

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {
		if (role.equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
			getContent().removeParentBlock();
			return;
		}
		getContent().getAnchorages().remove(contentAnchorage);
	}
	
	//Tooltip tip = new Tooltip("Needs to be attached to root Block");
	
	@Override
	protected void doRefreshVisual(GeometryNode<IShape> visual) {

		
		if (getContentViewer().equals(getViewer())) {
			
			//Needed to VF Blocks
//			if(!(getContent() instanceof VFControlBlockModel)){
//				if(getContent().getParentBlock()==null){
//			
//			getContent().setStroke(Color.RED);
//			getContent().setStrokeWidth(2);
//			
//			Tooltip.install(getVisual(), tip);
//				} else{
//					getContent().setStroke(Color.BLACK);
//				}
//			}
			
			
		if(getContent() instanceof ControlIfBlockModel){
			if(!(getContent().getStroke().equals(Color.RED))){
			if(((ControlIfBlockModel) getContent()).getChildBlocks().isEmpty()){
				getContent().setStroke(Color.RED);
				getContent().setStrokeWidth(2);
				Tooltip.install(getVisual(), toolTipMissingOperand2);
			}
			}
			if(getContent().getStroke().equals(Color.RED)){
				if(!((ControlIfBlockModel) getContent()).getChildBlocks().isEmpty()){
					getContent().setStroke(Color.BLACK);
					Tooltip.uninstall(getVisual(), toolTipMissingOperand2);
				}
			}
		}
		
		}
		

		
		
		super.doRefreshVisual(visual);
	}

}
