package editor.parts.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import com.google.common.collect.Multiset.Entry;

import editor.ControlShapeService; 
import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlBlockModel;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.control.ControlBlockModel.ControlBlockOperandType;
import editor.model.nodes.TextModel.TextType;
import editor.parts.AbstractGeometricElementPart;
import editor.parts.GeometricShapePart;
import editor.parts.nodes.AbstractNodePart;
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
		if(((ControlIfBlockModel) getContent()).getControlBlockType().equals(ControlBlockType.EQUIVALENCE) || ((ControlIfBlockModel) getContent()).getControlBlockType().equals(ControlBlockType.NOT_EQUIVALENCE) ){
			
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

			ControlBlockPart parentPart = (ControlBlockPart) anchorage;

			Bounds parentBounds = anchorage.getVisual().getBoundsInParent();
			Bounds bounds = getVisual().getBoundsInParent();

			if(parentPart.getContent() instanceof ControlAndOrBlock){
				
				if(role.equals("relink")){
					attachVisualToControlAndOrBlock(parentPart, bounds, parentBounds, true);
				}else{
				attachVisualToControlAndOrBlock(parentPart, bounds, parentBounds, false);
				}
				
			}
			
			else if (getContent() instanceof ControlBlockModel) {

				parentPart.resizeVisual(true, getContent().getGeometry().getBounds().getHeight(), 0);
				
				double childHightsAdded = 0;
				
				childHightsAdded = getNumberOfControlBlockChildren(parentPart, childHightsAdded);
				
				parentBounds = parentPart.getVisual().getBoundsInParent();

				double hightParentVisual = parentBounds.getHeight();

				double hightBottomBlockPart = 0;
				if (childHightsAdded==0) {
					hightBottomBlockPart = ((hightParentVisual / 3));
				} else {
					hightBottomBlockPart = ((hightParentVisual - childHightsAdded) / 2);
				}

				double hightNewChild = bounds.getHeight();
				double newHight = (hightParentVisual + hightNewChild);

				if (i == 1) {
					newHight -= hightBottomBlockPart;
				}

					
				
		
//				((ControlBlockPart) anchorage).getContent()
//						.setGeometry(ControlShapeService.createCustomControlBlockShape((int) newHight,
//								(int) ((ControlBlockPart) anchorage).getContent().getGeometry().getBounds()
//										.getWidth()));
				
				
				//For Equivalence Control Blocks the corresponding text visual needs to be repositioned
				if(getContent() instanceof ControlIfBlockModel){
					ControlBlockType type = ((ControlIfBlockModel) getContent()).getControlBlockType();
				
				     if(type.equals(ControlBlockType.EQUIVALENCE) || type.equals(ControlBlockType.NOT_EQUIVALENCE)){
				     
				     repositionEquivalenceTextVisual(parentPart, hightParentVisual, newHight);
				     }
				}
				
				
			

				

				double nX = parentBounds.getMinX() + 25;
				double nY = parentBounds.getMinY() + (newHight - (hightBottomBlockPart + hightNewChild)) + 1;

				distanceX = nX - bounds.getMinX();
				distanceY = nY - bounds.getMinY();

				transformVisual(distanceX, distanceY);
				
				
			
				
				if (parentPart.getContent().getParentBlock() != null) {

					ControlBlockPart parentParent = null;
					Set<IVisualPart<? extends Node>> anchorages = parentPart.getAnchoragesUnmodifiable().keySet();
					for (IVisualPart<? extends Node> p : anchorages) {
						if (p instanceof ControlBlockPart) {
							if (((ControlBlockPart) p).getContent().equals(parentPart.getContent().getParentBlock())) {
								parentParent = (ControlBlockPart) p;
							}
						}
					}

					if (parentParent != null) {
//						if(parentPart.getContent().getOperandType()== null || parentPart.getContent().getOperandType().equals(ControlBlockOperandType.OPERAND1)){
//						parentParent.resizeVisual(true, newHight, parentParent.getContent().getGeometry().getBounds().getWidth());
//						}
//						else{
//							parentParent.resizeVisual(false, newHight, parentParent.getContent().getGeometry().getBounds().getWidth());
//						}
						
						parentParent.resizeParentControlBlock((int)(newHight-hightParentVisual));
//						List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
//						linked = getAnchoredsRecursive(linked, parentParent);
//						for (IContentPart<? extends Node> link : lfainked) {
//							if (link instanceof ControlBlockPart) {
//								((ControlBlockPart) link).doAttachToAnchorageVisual(parentParent, role);
//
//							}
//						}
					}

				}

				
				if(parentPart.getContent().getOperandType()!= null){
				
					if(parentPart.getContent().getOperandType().equals(ControlBlockOperandType.OPERAND1)){
						parentPart.resizeParentVisual(true, parentPart.getContent().getGeometry().getBounds().getHeight(), 0);
					}else{
						parentPart.resizeParentVisual(false, parentPart.getContent().getGeometry().getBounds().getHeight(), 0);
					}
				} else{
				parentPart.resizeParentVisual(true, parentPart.getContent().getGeometry().getBounds().getHeight(), 0);
				}
		
			} else {

				double idouble = (double) i;

				double nX = parentBounds.getMinX() + 20;
				double nY = parentBounds.getMinY() + (parentBounds.getHeight() / (2 + idouble));

				distanceX = nX - bounds.getMinX();
				distanceY = nY - bounds.getMinY();

				transformVisual(distanceX, distanceY);
			}

			// relocateAnchorads(distanceX, distanceY);

		}
		}else{
			
			ControlBlockPart anchPart = (ControlBlockPart) anchorage;

			Bounds b11 = anchorage.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();
			
             if(anchPart.getContent() instanceof ControlAndOrBlock){
				
				ControlBlockModel childModel = getContent();
				
				
				
				
				if(childModel.getOperandType().equals(ControlBlockOperandType.OPERAND1)){
					
				//anchPart.resizeBlock(true, childHight, 0);
					
					
					b11 = anchorage.getVisual().getBoundsInParent();
					
					double nX = b11.getMinX() + 19;
					double nY = b11.getMinY() + 13;

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();
					transformVisual(distanceX, distanceY);
			
					//anchPart.resizeParentBlocks(true, anchPart.getContent().getGeometry().getBounds().getHeight(), 0);
					
					
				} else{
				
				 
					b11 = anchorage.getVisual().getBoundsInParent();
				 
					double nX = b11.getMinX() + 16;
					double nY = b11.getMaxY() - 14 - b22.getHeight();

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();
					transformVisual(distanceX, distanceY);
					//anchPart.resizeParentBlocks(true, anchPart.getContent().getGeometry().getBounds().getHeight(), 0);
				}
				
				

				
			}
			
			
             else if (anchorage instanceof ControlBlockPart) {

				

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

					transformVisual(distanceX, distanceY);
		
			
				} else {

					double idouble = (double) i;

					double nX = b11.getMinX() + 25;
					double nY = b11.getMinY() + (b11.getHeight() / (2 + idouble));

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();

					transformVisual(distanceX, distanceY);
				}

				// relocateAnchorads(distanceX, distanceY);

			}
		}



	}

	private void repositionEquivalenceTextVisual(ControlBlockPart parentPart, double hightParentVisual,
			double newHight) {
		ObservableMultiset<IVisualPart<? extends Node>> linked = parentPart.getAnchoredsUnmodifiable();

			for (Entry<IVisualPart<? extends Node>> anch : linked.entrySet()) {
				
				if (anch.getElement() instanceof TextPart) {
					if(((TextPart) anch.getElement()).getContent().getTextType().equals(TextType.EQUIVALENCE)){
						
					((TextPart) anch.getElement()).doAttachToAnchorageVisual(parentPart, "relink");
					
					((TextPart)anch.getElement()).refreshVisual();
					}
				}
			}
	}

	
	/**
	 * 
	 * 
	 * @param parentPart
	 * @param childHightsAdded
	 * @return the number of child blocks of type ControlBlocks already added to the parentPart
	 */
	private double getNumberOfControlBlockChildren(ControlBlockPart parentPart, double childHightsAdded) {
		List<AbstractGeometricElement<? extends IGeometry>> childBlocks = ((ControlIfBlockModel) parentPart
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
		return childHightsAdded;
	}

	private void attachVisualToControlAndOrBlock(ControlBlockPart parentPart,
			Bounds bounds, Bounds parentBounds, boolean isRelink) {
		
		double distanceX;
		double distanceY;
		ControlBlockModel content = getContent();
		
		Double contentHight = content.getGeometry().getBounds().getHeight();
		
		if(content.getOperandType().equals(ControlBlockOperandType.OPERAND1)){
			
			if(!isRelink){
		    parentPart.resizeVisual(true, contentHight, 0);
			
			parentBounds = parentPart.getVisual().getBoundsInParent();
			}
			
			double nX = parentBounds.getMinX() + 19;
			double nY = parentBounds.getMinY() + 13;

			distanceX = nX - bounds.getMinX();
			distanceY = nY - bounds.getMinY();
			transformVisual(distanceX, distanceY);

			if(!isRelink){
			parentPart.resizeParentVisual(true, parentPart.getContent().getGeometry().getBounds().getHeight(), 0);
			}
			
		} else{
		   if(!isRelink){
			parentPart.resizeVisual(false, contentHight, 0);
		 
			parentBounds = parentPart.getVisual().getBoundsInParent();
		   }
			double nX = parentBounds.getMinX() + 16;
			double nY = parentBounds.getMaxY() - 14 - bounds.getHeight();

			distanceX = nX - bounds.getMinX();
			distanceY = nY - bounds.getMinY();
			transformVisual(distanceX, distanceY);
			
			if(!isRelink){
			parentPart.resizeParentVisual(false, parentPart.getContent().getGeometry().getBounds().getHeight(), 0);
			}
		}
			
	}

	private void transformVisual(double distanceX, double distanceY) {
		getContent().getTransform().translate(distanceX, distanceY);
		
		refreshVisual();
		relocateAnchorads(distanceX, distanceY);
		
	
	
	}

	/**
	 * Method for resizing a block 
	 * Needs to be called directly from the part which should be resized
	 * 
	 * @param isOperand1
	 * @param newHight
	 * @param newWidth
	 */
	public void resizeVisual(boolean isOperand1, double newHight, double newWidth){
	
		CurvedPolygon newShape;
		
		if(getContent() instanceof ControlAndOrBlock){

			if(isOperand1){
			newShape = ControlShapeService.adjustControlAndOrBlockShapeOperand1((CurvedPolygon)getContent().getGeometry(), newHight);
			}else{
				newShape = ControlShapeService.adjustControlAndOrBlockShapeOperand2((CurvedPolygon)getContent().getGeometry(), newHight);
			}
            getContent().setGeometry(newShape);
			refreshVisual();
			
		}
		//TODO: needs to be tested
		else if(getContent() instanceof ControlIfBlockModel){
			
			newShape = ControlShapeService.adjustControlIfBlockShapeHight((CurvedPolygon) getContent().getGeometry(), newHight);
//			newShape = ControlShapeService.createCustomControlBlockShape((int) newHight,
//					(int) newWidth);
			getContent().setGeometry(newShape);
			refreshVisual();
		}
		
	}
	
	
	public void resizeParentVisual(boolean isOperand1, double newHight, double newWidth){
		
		//Check if parent block exists which should be resized
		if(getContent().getParentBlock()!= null){

			Set<IVisualPart<? extends Node>> anchorages = getAnchoragesUnmodifiable().keySet();
			
			for (IVisualPart<? extends Node> p : anchorages) {
				if (p instanceof ControlBlockPart) {
					
					if (((AbstractGeometricElementPart<?>) p).getContent().equals(getContent().getParentBlock())) {
						
						
						((ControlBlockPart) p).resizeVisual(isOperand1, newHight, newWidth);
						
						List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
						
						refreshAnchoredsRecursive(linked, (IContentPart<? extends Node>) p);
						
						((ControlBlockPart) p).resizeParentVisual(isOperand1, newHight, newWidth);
						
					}
				}
			}
			
			
		}
		
		
	}
	
	public List<IContentPart<? extends Node>> refreshAnchoredsRecursive(List<IContentPart<? extends Node>> linked,
			IContentPart<? extends Node> parent) {
	
		@SuppressWarnings("unchecked")
		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(parent.getAnchoredsUnmodifiable(),
				IContentPart.class);

		for (IContentPart<? extends Node> node : anch) {
			if (!(linked.contains(node))) {
				linked.add(node);

				if (node instanceof GeometricShapePart) {
					((GeometricShapePart) node).doAttachToAnchorageVisual(parent, "relink");
				}
				if (node instanceof AbstractNodePart) {
					if(node instanceof TextPart){
						if(!((TextPart) node).getContent().getTextType().equals(TextType.AND_OR_CONTROL)){
							((AbstractNodePart<? extends Node>) node).doAttachToAnchorageVisual(parent, "relink");	
						}
						
					}else{
					((AbstractNodePart<? extends Node>) node).doAttachToAnchorageVisual(parent, "relink");
					}
				}
				if (!(node.getAnchoredsUnmodifiable().isEmpty())) {
					linked = getAnchoredsRecursive(linked, node);
				}

			}
		}

		return linked;

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
