package editor.parts.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.OperatorShapeService;
import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorMovableBlockModel;
import editor.model.operator.OperatorNotBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.arithmetical.ArithmeticalOperatorFixedBlockPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.nodes.AbstractNodePart;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class OperatorMovableBlockPart extends GeometricShapePart {

	@Override
	protected GeometryNode<IShape> doCreateVisual() {

		GeometryNode<IShape> geometryNode = super.doCreateVisual();

		geometryNode.setOnDragDetected(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				geometryNode.startFullDrag();

			}
		});

		return geometryNode;

	}

	Tooltip t = new Tooltip("Operator Block needs to be attached to parent Block");

	@Override
	protected void doRefreshVisual(GeometryNode<IShape> visual) {
		OperatorMovableBlockModel content = getContent();

		
		if (getContentViewer().equals(getViewer())) {
			
			if (content.getParentBlock() == null && !(visual.getStroke().equals(Color.RED))) {
				content.setStroke(Color.RED);
				content.setStrokeWidth(2.0);

				Tooltip.install(getVisual(), t);

			}

			if (content.getParentBlock() != null && visual.getStroke().equals(Color.RED)) {
				content.setStroke(Color.BLACK);
				content.setStrokeWidth(1);
				Tooltip.uninstall(getVisual(), t);
			}
			
		}

		super.doRefreshVisual(visual);
	}

	

	/**
	 * Attaches this part's visual to the visual of the given anchorage.
	 *
	 * @param anchorage
	 *            The anchorage {@link IVisualPart}.
	 * @param role
	 *            The anchorage role.
	 */
	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {

		ObservableMultiset<IVisualPart<? extends Node>> allAn = anchorage.getAnchoredsUnmodifiable();

		double distanceX = 0;
		double distanceY = 0;

		if (anchorage.getParent() != null) {

			if (!getVisual().getParent().equals(anchorage.getVisual().getParent())) {

				Group parenttthis = (Group) getVisual().getParent();
				Group parentparent = (Group) anchorage.getVisual().getParent();

				parenttthis.getChildren().remove(getVisual());
				parentparent.getChildren().add(getVisual());

			}
		}

		if (anchorage instanceof OperatorFixedBlockPart) {
			OperatorFixedBlockPart parentBlock = (OperatorFixedBlockPart) anchorage;
			Bounds b11 = parentBlock.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();
			distanceX = b11.getMinX() - b22.getMinX();
			distanceY = b11.getMinY() - b22.getMinY();

			
			if (!role.equals("relink")) {
				
				
				double widthDifference = (getContent().getBoundsInParent().getWidth())
						- (parentBlock.getContent().getBoundsInParent().getWidth());
				parentBlock.getContent().setGeometry((IShape) getContent().getGeometry().getCopy());
				//getContent().getTransform().translate(b11.getMinX() - b22.getMinX(), b11.getMinY() - b22.getMinY());
				parentBlock.refreshVisual();
				parentBlock.resizedIncludingParent((int) widthDifference);
				parentBlock.refreshVisual();
				
				
			}
				getContent().getTransform().translate(b11.getMinX() - b22.getMinX(), b11.getMinY() - b22.getMinY());
			
			
			
			getVisual().toFront();
			refreshVisual();
			//getVisual().toFront();
			for (IVisualPart<? extends Node> anchored : anchorage.getAnchoredsUnmodifiable()) {
				anchored.getVisual().toFront();
				anchored.refreshVisual();
			}

			
			
			for (IVisualPart<? extends Node> anchored : getAnchoredsUnmodifiable()) {
				anchored.getVisual().toFront();
				anchored.refreshVisual();
			}
			
			
		}

		if (anchorage instanceof ControlBlockPart) {

			ControlBlockPart p = (ControlBlockPart) anchorage;

			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			double newx = b11.getMinX() + 15.0;
			double newy = b11.getMinY() + 3;

			getContent().getTransform().translate(newx - b22.getMinX(), newy - b22.getMinY());

			getVisual().toFront();
			refreshVisual();

			
		}

		if (distanceX != 0 && distanceY != 0) {

			List<IContentPart<? extends Node>> linked = new ArrayList<>();
			linked = getAnchoredsRecursive(linked, this);

			for (IContentPart<? extends Node> anch : linked) {
				if (anch instanceof GeometricShapePart) {
					((GeometricShapePart) anch).getContent().getTransform().translate(distanceX, distanceY);

				}
				if (anch instanceof AbstractNodePart) {
					((AbstractNodePart<? extends Node>) anch).getContent().getTransform().translate(distanceX,
							distanceY);
				}
				anch.getVisual().toFront();
				anch.refreshVisual();

				// if(anch instanceof ChoiceBoxPart){
				// ((ChoiceBoxPart)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				// anch.refreshVisual();
				// }
				//
				// if(anch instanceof TextPart){
				// ((TextPart)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				// anch.refreshVisual();
				// }
				//
				// if(anch instanceof ChoiceBoxContextPartOld){
				// ((ChoiceBoxContextPartOld)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				// anch.refreshVisual();
				// }
				//
				// if(anch instanceof ChoiceBoxContextStringPart){
				// ((ChoiceBoxContextStringPart)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				// anch.refreshVisual();
				// }
				//
				// if (anch instanceof OperatorFixedBlockPart) {
				// ((OperatorFixedBlockPart)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				//
				// anch.refreshVisual();
				// }
				// if(anch instanceof AbstractNodePart){
				// ((AbstractNodePart<? extends Node>)
				// anch).getContent().getTransform().translate(distanceX,
				// distanceY);
				// }
			}
		}

	}

	@Override
	public OperatorMovableBlockModel getContent() {
		return (OperatorMovableBlockModel) super.getContent();
	}

	@Override
	protected SetMultimap<? extends Object, String> doGetContentAnchorages() {
		SetMultimap<Object, String> anchorages = HashMultimap.create();
		for (AbstractGeometricElement<? extends IGeometry> anchorage : getContent().getAnchorages()) {
			anchorages.put(anchorage, "link");
		}
		if (getContent().getParentBlock() != null) {
			anchorages.put(getContent().getParentBlock(), AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE);
		}
		return anchorages;
	}

	@Override
	protected void doAttachToContentAnchorage(Object contentAnchorage, String role) {
		if (!(contentAnchorage instanceof AbstractGeometricElement)) {
			throw new IllegalArgumentException("Cannot attach to content anchorage: wrong type!");
		}
		if (role.equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
			getContent().setParentBlock((AbstractGeometricElement<? extends IGeometry>) contentAnchorage);
			return;
		}
		getContent().getAnchorages().add((AbstractGeometricElement<?>) contentAnchorage);
	}

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {

		if (role.equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
			getContent().removeParentBlock();
			return;
		}
		getContent().getAnchorages().remove(contentAnchorage);
	}

	@Override
	public void resizeIncludingParent(int widthDifference) {
	
		int oldW = (int) getContent().getGeometry().getBounds().getWidth();
		getContent().setGeometry((IShape) OperatorShapeService.createOperatorShapeCustomLength(oldW + widthDifference));
		refreshVisual();
		if(getContent() instanceof OperatorNotBlockModel){
		for (IVisualPart<? extends Node> anchored : getAnchoredsUnmodifiable()) {
			anchored.getVisual().toFront();
			anchored.refreshVisual();
		}
		}


		for (Entry<IVisualPart<? extends Node>, String> part : getAnchoragesUnmodifiable().entries()) {

			if (part.getValue().equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)
					|| part.getValue().equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
				if (part.getKey() instanceof GeometricShapePart) {
					((GeometricShapePart) part.getKey()).resizeIncludingParent(widthDifference);
//					for (IVisualPart<? extends Node> anchored : ((GeometricShapePart)part).getAnchoredsUnmodifiable()) {
//						anchored.getVisual().toFront();
//						anchored.refreshVisual();
//					}

				}
				if (part.getKey() instanceof OperatorFixedBlockPart) {
					((OperatorFixedBlockPart) part.getKey()).getContent().setGeometry(getContent().getGeometry());
					((OperatorFixedBlockPart) part.getKey()).resizedIncludingParent(widthDifference);
				

				}
				if (part.getKey() instanceof ArithmeticalOperatorFixedBlockPart) {
					((ArithmeticalOperatorFixedBlockPart) part.getKey()).getContent().setGeometry(getContent().getGeometry());
					((ArithmeticalOperatorFixedBlockPart) part.getKey()).resizedIncludingParent(widthDifference);
				

				}
			}
			part.getKey().refreshVisual();
		}
		// int oldW = (int)getContent().getGeometry().getBounds().getWidth();
		// getContent().setGeometry((IShape)OperatorShapeService.createOperatorShapeCustomLength(oldW+widthDifference));
		// refreshVisual();
		if (getContent() instanceof OperatorAndBlockModel) {
			((OperatorAndBlockPart) this).doRefreshFixedAnchoradsVisual(widthDifference);

			// List<IContentPart<? extends Node>> linked = getAnchoreds(this,
			// AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
			//
			// for(IContentPart<? extends Node> link: linked){
			// if(link instanceof OperatorFixedBlockPart){
			// ((OperatorFixedBlockPart) link).doAttachToAnchorageVisual(this,
			// "relink");
			// }
			// }
		}
		if(getContent() instanceof OperatorNumercialComparisonBlockModel){
			((OperatorNumericalComparisonBlockPart) this).doRefreshFixedAnchoradsVisual(widthDifference);
		}

	}

	/**
	 * 
	 * 
	 * @param linked
	 * @param parent
	 * @return
	 */
	public List<IContentPart<? extends Node>> refreshAnchoredsRecursive(List<IContentPart<? extends Node>> linked,
			IContentPart<? extends Node> parent) {
		@SuppressWarnings("unchecked")
		// List<IContentPart<? extends Node>> anch =
		// PartUtils.filterParts(PartUtils.getAnchoreds(parent, "link"),
		// IContentPart.class);
		//
		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(parent.getAnchoredsUnmodifiable(),
				IContentPart.class);

		for (IContentPart<? extends Node> node : anch) {
			if (!(linked.contains(node))) {
				linked.add(node);

				if (node instanceof GeometricShapePart) {
					((GeometricShapePart) node).doAttachToAnchorageVisual(parent, "relink");
				}
				if (node instanceof AbstractNodePart) {

					((AbstractNodePart<? extends Node>) node).doAttachToAnchorageVisual(parent, "relink");
				}
				if (!(node.getAnchoredsUnmodifiable().isEmpty())) {
					linked = getAnchoredsRecursive(linked, node);
				}

			}
		}

		return linked;

	}
}
