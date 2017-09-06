package editor.parts.arithmetical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock.ArithmeticalOperatorFixedType;
import editor.parts.nodes.TextPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.geometry.Bounds;
import javafx.scene.Node;

public class ArithmeticalOperationOperatorPart extends ArithmeticalOperatorMovableBlockPart {

	@Override
	public ArithmeticalOperationOperatorBlockModel getContent() {

		return (ArithmeticalOperationOperatorBlockModel) super.getContent();
	}

	public void doRefreshFixedAnchoradsVisual() {

		List<IContentPart<? extends Node>> linked = getAnchoreds(this, AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);

		for (IContentPart<? extends Node> link : linked) {
			if (link instanceof ArithmeticalOperatorFixedBlockPart) {

				ArithmeticalOperatorFixedBlockPart operatorFixedBlockPart = (ArithmeticalOperatorFixedBlockPart) link;

				Bounds b11 = getVisual().getBoundsInParent();
				Bounds b22 = operatorFixedBlockPart.getVisual().getBoundsInParent();

				if (operatorFixedBlockPart.getContent().getArithmeticalOperatorFixedType()
						.equals(ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND2)) {

					operatorFixedBlockPart.getContent().getTransform().translate(b11.getMaxX() - b22.getMaxX(),
							b11.getMinY() - b22.getMinY());

					operatorFixedBlockPart.getVisual().toFront();
					operatorFixedBlockPart.refreshVisual();
					List<IContentPart<? extends Node>> children = new ArrayList<>();
					children = getAnchoreds(operatorFixedBlockPart, AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE);
					for (IContentPart<? extends Node> child : children) {
						if (child instanceof ArithmeticalOperatorMovableBlockPart) {
							((ArithmeticalOperatorMovableBlockPart) child).doAttachToAnchorageVisual(operatorFixedBlockPart,
									"relink");
							((ArithmeticalOperatorMovableBlockPart) child).refreshVisual();
							((ArithmeticalOperatorMovableBlockPart) child).doRefreshAllChildBlockParts();
						}
					}

				}
			}
			if (link instanceof TextPart) {

				TextPart textPart = (TextPart) link;

				Bounds b11 = getVisual().getBoundsInParent();
				Bounds b22 = textPart.getVisual().getBoundsInParent();

				//double widthParent = getVisual().getBoundsInParent().getWidth();

				double widthOperator1 = getContent().getFixedChildOperator1().getGeometry().getBounds().getWidth();
			

				double nX = b11.getMinX() + widthOperator1 + 10;
				double nY = b11.getMinY() + 2.0;

				
				textPart.getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

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
	@SuppressWarnings("unchecked")
	protected List<IContentPart<? extends Node>> getAnchoreds(IContentPart<? extends Node> parent, String role) {
	
		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(
				PartUtils.getAnchoreds(parent, AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE), IContentPart.class);

		anch.addAll((Collection<? extends IContentPart<? extends Node>>) new ArrayList<>(PartUtils.filterParts(
				PartUtils.getAnchoreds(parent, AbstractBlockElement.PARENT_BLOCK_PROPERTY), IContentPart.class)));

		return anch;

	}
}
