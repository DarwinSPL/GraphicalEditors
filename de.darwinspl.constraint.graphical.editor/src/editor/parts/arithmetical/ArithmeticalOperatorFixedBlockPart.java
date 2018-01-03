package editor.parts.arithmetical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IScalable;
import org.eclipse.gef.geometry.planar.IShape;
import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock.ArithmeticalOperatorFixedType;
import editor.parts.GeometricShapePart;
import editor.parts.nodes.AbstractNodePart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class ArithmeticalOperatorFixedBlockPart extends AbstractNodePart<GeometryNode<IShape>> {

	@Override
	public ArithmeticalOperatorFixedBlock getContent() {
		
		return (ArithmeticalOperatorFixedBlock) super.getContent();
	}

	@Override
	protected GeometryNode<IShape> doCreateVisual() {

		GeometryNode<IShape> geometryNode = new GeometryNode<>();

		geometryNode.setOnDragDetected(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				geometryNode.startFullDrag();

			}
		});

		return geometryNode;

	}

	@Override
	public void resizedIncludingParent(int widthDifference) {

		for (Entry<IVisualPart<? extends Node>, String> part : getAnchoragesUnmodifiable().entries()) {

			if (part.getValue().equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)
					|| part.getValue().equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
				((GeometricShapePart) part.getKey()).resizeIncludingParent(widthDifference);
			}

			part.getKey().refreshVisual();
		}

	}

	Tooltip toolTipMissingChild = new Tooltip("Child Block must be attached.");
	@Override
	protected void doRefreshVisual(GeometryNode<IShape> visual) {

		ArithmeticalOperatorFixedBlock content = getContent();
		
		
		if (getContentViewer().equals(getViewer())) {
			if(!(content.getStroke().equals(Color.RED))){
			if(content.getMovableChildBlock()==null){
				getContent().setStroke(Color.RED);
				getContent().setStrokeWidth(2);
				Tooltip.install(getVisual(), toolTipMissingChild);
			}
			}
			if(content.getStroke().equals(Color.RED)){
				if(content.getMovableChildBlock()!=null){
					getContent().setStroke(Color.BLACK);
					Tooltip.uninstall(getVisual(), toolTipMissingChild);
				}
			}
		}

		if (visual.getGeometry() != content.getGeometry()) {
			visual.setGeometry(content.getGeometry());
		}

		AffineTransform transform = content.getTransform();
		if (transform != null) {
			setVisualTransform(Geometry2FX.toFXAffine(transform));
		}

		// apply stroke paint
		if (visual.getStroke() != content.getStroke()) {
			visual.setStroke(content.getStroke());
		}

		// stroke width
		if (visual.getStrokeWidth() != content.getStrokeWidth()) {
			visual.setStrokeWidth(content.getStrokeWidth());
		}

		if (visual.getFill() != content.getFill()) {
			visual.setFill(content.getFill());
		}

		if (visual.getEffect() != content.getEffect()) {
			visual.setEffect(content.getEffect());
		}

		super.doRefreshVisual(visual);
	}

	public void doRefreshAllChildBlockParts() {
		List<IContentPart<? extends Node>> list = new ArrayList<>();

		for (IContentPart<? extends Node> anch : getAnchoredsRecursive(list, this)) {

			if (anch instanceof GeometricShapePart) {
				((GeometricShapePart) anch).doAttachToAnchorageVisual(this, "default");
			}
			if (anch instanceof AbstractNodePart) {
				((AbstractNodePart<? extends Node>) anch).doAttachToAnchorageVisual(this, "default");
			}
		}
	}

	public void relocateAnchorads(double distanceX, double distanceY) {

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		linked = getAnchoredsRecursive(linked, this);

		for (IContentPart<? extends Node> anch : linked) {
			if (anch instanceof GeometricShapePart) {
				((GeometricShapePart) anch).getContent().getTransform().translate(distanceX, distanceY);
				anch.refreshVisual();
			}
			if (anch instanceof AbstractNodePart) {
				((AbstractNodePart<? extends Node>) anch).getContent().getTransform().translate(distanceX, distanceY);
				anch.refreshVisual();
			}
		}

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
		// int i = 0;
		// for(IVisualPart<? extends Node> node: allAn){
		// if(node instanceof FeatureBlockPart){
		// i++;
		// }
		// }

		if (anchorage.getParent() != null) {

			if (!getVisual().getParent().equals(anchorage.getVisual().getParent())) {

				Group parenttthis = (Group) getVisual().getParent();
				Group parentparent = (Group) anchorage.getVisual().getParent();

				parenttthis.getChildren().remove(getVisual());
				parentparent.getChildren().add(getVisual());

			}
		}

		if (anchorage instanceof ArithmeticalOperatorMovableBlockPart) {

			ArithmeticalOperatorMovableBlockPart p = (ArithmeticalOperatorMovableBlockPart) anchorage;

			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			if ((getContent().getArithmeticalOperatorFixedType()
					.equals(ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND2))
					|| (getContent().getArithmeticalOperatorFixedType()
							.equals(ArithmeticalOperatorFixedType.NEG_OPERAND))) {

				double nX = b11.getMaxX() - 2;
				double nY = b11.getMinY() + 2;

				// getContent().getTransform().translate(b11.getMaxX()-b22.getMaxX(),
				// b11.getMinY()-b22.getMinY() );

				getContent().getTransform().translate(nX - b22.getMaxX(), nY - b22.getMinY());

			} else {

				double nX = b11.getMinX() + 2;
				double nY = b11.getMinY() + 2;

				// getContent().getTransform().translate(b11.getMinX()-b22.getMinX(),
				// b11.getMinY()-b22.getMinY());

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			}

			// getContent().getTransform().translate(b11.getMinX()-b22.getMinX(),
			// b11.getMinY()-b22.getMinY());

			getVisual().toFront();
			refreshVisual();
			for (IVisualPart<? extends Node> anch : getAnchoredsUnmodifiable()) {
				anch.getVisual().toFront();
				anch.refreshVisual();
			}

		}

		if (anchorage instanceof OperatorMovableBlockPart) {

			OperatorMovableBlockPart p = (OperatorMovableBlockPart) anchorage;

			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			if ((getContent().getArithmeticalOperatorFixedType()
					.equals(ArithmeticalOperatorFixedType.COMPARISON_OPERAND2))) {

				double nX = b11.getMaxX() - 12;
				double nY = b11.getMinY() + 2;

				// getContent().getTransform().translate(b11.getMaxX()-b22.getMaxX(),
				// b11.getMinY()-b22.getMinY() );

				getContent().getTransform().translate(nX - b22.getMaxX(), nY - b22.getMinY());

			} else {

				double nX = b11.getMinX() + 12;
				double nY = b11.getMinY() + 2;

				// getContent().getTransform().translate(b11.getMinX()-b22.getMinX(),
				// b11.getMinY()-b22.getMinY());

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			}

			// getContent().getTransform().translate(b11.getMinX()-b22.getMinX(),
			// b11.getMinY()-b22.getMinY());

			getVisual().toFront();
			refreshVisual();
			for (IVisualPart<? extends Node> anch : getAnchoredsUnmodifiable()) {
				anch.getVisual().toFront();
				anch.refreshVisual();
			}

		}

		// if(anchorage instanceof ControlBlockPart){
		//
		// ControlBlockPart p = (ControlBlockPart) anchorage;
		//
		// Bounds b11= p.getVisual().getBoundsInParent();
		// Bounds b22 = getVisual().getBoundsInParent();
		//
		// double newx = b11.getMaxX()-5;
		// //double newy = b11.getMinY()+3;
		//
		//
		// getContent().getTransform().translate(newx-b22.getMaxX(),
		// b11.getMinY()-b22.getMinY());
		//
		// getVisual().toFront();
		// refreshVisual();
		//
		//
		//
		//
		// getVisual().toFront();
		//
		//
		// }

	}

	@Override
	protected SetMultimap<? extends Object, String> doGetContentAnchorages() {
		SetMultimap<Object, String> anchorages = HashMultimap.create();
		for (AbstractGeometricElement<? extends IGeometry> anchorage : getContent().getAnchorages()) {
			anchorages.put(anchorage, "link");
		}
		if (getContent().getParentBlock() != null) {
			anchorages.put(getContent().getParentBlock(), AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
		}
		return anchorages;
	}

	@Override
	protected void doAttachToContentAnchorage(Object contentAnchorage, String role) {
		if (!(contentAnchorage instanceof AbstractGeometricElement)) {
			throw new IllegalArgumentException("Cannot attach to content anchorage: wrong type!");
		}
		if (role.equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
			getContent().setParentBlock((AbstractGeometricElement<? extends IGeometry>) contentAnchorage);
			return;
		}

		getContent().getAnchorages().add((AbstractGeometricElement<?>) contentAnchorage);
		// }
	}

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {

		if (role.equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
			getContent().removeParentBlock();

		}
		getContent().getAnchorages().remove(contentAnchorage);
	}

	@Override
	public Dimension getContentSize() {
		return getContent().getGeometry().getBounds().getSize();
	}

	@Override
	public void setContentSize(Dimension size) {
		IShape geometry = getContent().getGeometry();
		Rectangle geometricBounds = geometry.getBounds();
		// XXX: The given <i>size</i> contains the stroke of the underlying
		// geometry, therefore, we need to subtract the stroke width from both
		// width and height (actually this depends on the stroke type (which is
		// centered, per default).
		double sx = (size.width - getContent().getStrokeWidth()) / geometricBounds.getWidth();
		double sy = (size.height - getContent().getStrokeWidth()) / geometricBounds.getHeight();
		((IScalable<?>) geometry).scale(sx, sy, geometricBounds.getX(), geometricBounds.getY());
	}
}
