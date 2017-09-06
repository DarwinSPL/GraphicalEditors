package editor.parts.operator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import editor.model.AbstractBlockElement;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorFixedBlockModel.OperatorFixedBlockType;
import editor.parts.nodes.TextPart;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class OperatorAndBlockPart extends OperatorMovableBlockPart {

	@Override
	public OperatorAndBlockModel getContent() {
		
		return (OperatorAndBlockModel) super.getContent();
	}

	public void doRefreshFixedAnchoradsVisual(int widthDifference) {

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		linked = getAnchoreds(this, AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
		for (IContentPart<? extends Node> link : linked) {
			if (link instanceof OperatorFixedBlockPart) {

				OperatorFixedBlockPart operatorFixedBlockPart = (OperatorFixedBlockPart) link;

				Bounds b11 = getVisual().getBoundsInParent();
				Bounds b22 = operatorFixedBlockPart.getVisual().getBoundsInParent();

				if (operatorFixedBlockPart.getContent().getOperatorFixedBlockType()
						.equals(OperatorFixedBlockType.AND_OPERATOR2)) {

					operatorFixedBlockPart.getContent().getTransform().translate(b11.getMaxX() - b22.getMaxX(),
							b11.getMinY() - b22.getMinY());

					operatorFixedBlockPart.getVisual().toFront();
					operatorFixedBlockPart.refreshVisual();
					// operatorFixedBlockPart.doRefreshAllChildBlockParts();
					List<IContentPart<? extends Node>> children = new ArrayList<>();
					children = getAnchoreds(operatorFixedBlockPart, AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE);
					for (IContentPart<? extends Node> child : children) {
						if (child instanceof OperatorMovableBlockPart) {
							((OperatorMovableBlockPart) child).doAttachToAnchorageVisual(operatorFixedBlockPart,
									"relink");
							((OperatorMovableBlockPart) child).refreshVisual();
							((OperatorMovableBlockPart) child).doRefreshAllChildBlockParts();
						}
					}

				}

				// operatorFixedBlockPart.resizedIncludingParent(widthDifference);
			}
			if (link instanceof TextPart) {

				TextPart textPart = (TextPart) link;

				Bounds b11 = getVisual().getBoundsInParent();
				Bounds b22 = textPart.getVisual().getBoundsInParent();

				double widthParent = getVisual().getBoundsInParent().getWidth();

				double widthOperator1 = getContent().getFixedChildOperator1().getGeometry().getBounds().getWidth();
				// double widthThis =
				// textPart.getVisual().getBoundsInParent().getWidth();

				double nX = b11.getMinX() + widthOperator1 + 10;
				double nY = b11.getMinY() + 2.0;

				// distanceX = nX - b22.getMinX();
				// distanceY = nY - b22.getMinY();

				textPart.getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				textPart.getVisual().toFront();
				textPart.refreshVisual();

			}
		}

	}

	/**
	 * 
	 * 
	 * @param linked
	 * @param parent
	 * @return
	 */
	protected List<IContentPart<? extends Node>> getAnchoreds(IContentPart<? extends Node> parent, String role) {
		@SuppressWarnings("unchecked")
		// List<IContentPart<? extends Node>> anch =
		// PartUtils.filterParts(PartUtils.getAnchoreds(parent, "link"),
		// IContentPart.class);
		//
		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(PartUtils.getAnchoreds(parent, role),
				IContentPart.class);

		// anch.addAll((Collection<? extends IContentPart<? extends Node>>) new
		// ArrayList<>(PartUtils.filterParts(PartUtils.getAnchoreds(parent,
		// AbstractBlockElement.PARENT_BLOCK_PROPERTY), IContentPart.class)));

		return anch;

	}
}
