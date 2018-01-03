package editor.parts.arithmetical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.ArithmeticalOperatorShapeService;
import editor.OperatorShapeService;
import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperatorMovableBlock;
import editor.parts.GeometricShapePart;
import editor.parts.nodes.AbstractNodePart;
import editor.parts.operator.OperatorFixedBlockPart;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class ArithmeticalOperatorMovableBlockPart extends GeometricShapePart {

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

	Tooltip t = new Tooltip("Operator Block needs to be attached to a fixed Operator");

	@Override
	protected void doRefreshVisual(GeometryNode<IShape> visual) {
		ArithmeticalOperatorMovableBlock content = getContent();

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

	protected IViewer getContentViewer() {
		return getRoot().getViewer().getDomain().getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
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

		if (anchorage instanceof ArithmeticalOperatorFixedBlockPart) {

			ArithmeticalOperatorFixedBlockPart p = (ArithmeticalOperatorFixedBlockPart) anchorage;

			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			distanceX = b11.getMinX() - b22.getMinX();
			distanceY = b11.getMinY() - b22.getMinY();
			
			if(!role.equals("relink")){
			double widthDifference = (getContent().getBoundsInParent().getWidth())
					- (p.getContent().getBoundsInParent().getWidth());

			p.getContent().setGeometry((IShape) getContent().getGeometry().getCopy());

			
			p.refreshVisual();

			p.resizedIncludingParent((int) widthDifference);

			p.refreshVisual();
			}

			getContent().getTransform().translate(b11.getMinX() - b22.getMinX(), b11.getMinY() - b22.getMinY());
			
			getVisual().toFront();
			refreshVisual();
			for (IVisualPart<? extends Node> anch : p.getAnchoredsUnmodifiable()) {
				anch.getVisual().toFront();
				anch.refreshVisual();
			}

			for (IVisualPart<? extends Node> anchored : getAnchoredsUnmodifiable()) {
				anchored.getVisual().toFront();
				anchored.refreshVisual();
			}
			

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

			}
		}

	}

	@Override
	public ArithmeticalOperatorMovableBlock getContent() {
		return (ArithmeticalOperatorMovableBlock) super.getContent();
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

		for (Entry<IVisualPart<? extends Node>, String> part : getAnchoragesUnmodifiable().entries()) {

			if (part.getValue().equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE) || part.getValue().equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
				if (part.getKey() instanceof GeometricShapePart) {
					((GeometricShapePart) part.getKey()).resizeIncludingParent(widthDifference);

				}
				if (part.getKey() instanceof ArithmeticalOperatorFixedBlockPart) {
					((ArithmeticalOperatorFixedBlockPart) part.getKey()).getContent().setGeometry((IShape)getContent().getGeometry().getCopy());
					((ArithmeticalOperatorFixedBlockPart) part.getKey()).resizedIncludingParent(widthDifference);
				}
			}
			part.getKey().refreshVisual();
		}
		int oldW = (int) getContent().getGeometry().getBounds().getWidth();
		getContent().setGeometry((IShape) ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(oldW + widthDifference, (int) getContent().getGeometry().getBounds().getHeight()));
		refreshVisual();
		if (getContent() instanceof ArithmeticalOperationOperatorBlockModel) {
			((ArithmeticalOperationOperatorPart) this).doRefreshFixedAnchoradsVisual();
		}

	}
}
