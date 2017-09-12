package editor.parts.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.parts.arithmetical.ArithmeticalOperationOperatorPart;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.choiceboxes.AbstractChoiceBoxPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TextPart extends AbstractNodePart<Text> {

	@Override
	public TextModel getContent() {
		return (TextModel) super.getContent();
	}

	@Override
	protected Text doCreateVisual() {
		Text text = new Text();

		text.setStyle("-fx-font-size:small;");

		
		if (getContent().getTextType().equals(TextType.EVOLUTION)
				|| getContent().getTextType().equals(TextType.FROM_DATE)
				|| getContent().getTextType().equals(TextType.TO_DATE)) {
			text.setFill(Color.WHITE);
		}

		if (getContent().getTextType().equals(TextType.EQUIVALENCE)
				|| getContent().getTextType().equals(TextType.NEGATION)
				|| getContent().getTextType().equals(TextType.ARITHMETICAL_OPERATOR)
				|| getContent().getTextType().equals(TextType.COMPARISON_OPERATOR)) {
			text.setStyle("-fx-font-size:x-large;");
		} else {
			text.setStyle("-fx-font-size:small;");
		}

		return text;
	}

	@Override
	protected void doRefreshVisual(Text visual) {
		TextModel content = getContent();

		if (visual.getText() != content.getText()) {

			visual.setText(content.getText());
		}

		if ((content.getFill() != null) && (getContent().getFill() != visual.getFill())) {
			visual.setFill(content.getFill());
		}

		AffineTransform transform = content.getTransform();
		if (transform != null) {
			setVisualTransform(Geometry2FX.toFXAffine(transform));
		}

		super.doRefreshVisual(visual);

	}

	@SuppressWarnings("unchecked")
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
	}

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {
		
		if (getContent().getParentBlock() != null) {
			getContent().removeParentBlock();
			return;
		}

		getContent().getAnchorages().remove(contentAnchorage);

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
	public void setContent(Object model) {
		if (model != null && !(model instanceof TextModel)) {
			throw new IllegalArgumentException("Only IShape models are supported.");
		}
		super.setContent(model);
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


		if (anchorage instanceof ControlBlockPart) {

			ControlBlockPart p = (ControlBlockPart) anchorage;

			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			if (getContent().getTextType().equals(TextType.EQUIVALENCE)) {

				double nX = b11.getMaxX() - 20.0;
				double nY = b11.getMaxY() - 2.0;

				distanceX = nX - b22.getMaxX();
				distanceY = nY - b22.getMaxY();

				getContent().getTransform().translate(nX - b22.getMaxX(), nY - b22.getMaxY());

			} else if (getContent().getTextType().equals(TextType.SELECTABLE)) {

			
					double nX = b11.getMinX() + 160.0;
					double nY = b11.getMinY() + 5.0;

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();

					getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			
					getVisual().toFront();
					refreshVisual();

				
			} else if(getContent().getTextType().equals(TextType.AND_OR_CONTROL)){
				
				    
				double nX = b11.getMinX() + 15;
				double nY = b11.getMinY() + 65;
				
				
//				double hight = ((ControlBlockPart) anchorage).getContent().getGeometry().getBounds().getHeight();
//				hight2;
				

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

		
				getVisual().toFront();
				refreshVisual();
				
				
			}
			
			
			else {

				double nX = b11.getMinX() + 5.0;
				double nY = b11.getMinY() + 2.0;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());
			}
			
			

			// getVisual().resize(getVisual().getWidth(), positionY);

			// getVisual().heightProperty().
			refreshVisual();

		}

		if (anchorage instanceof OperatorMovableBlockPart) {

			if (getContent().getTextType().equals(TextType.VALIDATE_NOT_OPERATOR)) {

				Bounds b11 = ((OperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double nX = b11.getMinX() + 10.0;
				double nY = b11.getMinY() + 2.0;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				refreshVisual();
			}

			if (getContent().getTextType().equals(TextType.EQUALS)) {

				Bounds b11 = ((OperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double nX = b11.getMinX() + 145.0;
				double nY = b11.getMinY() + 5.0;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				getVisual().toFront();
				refreshVisual();
			}

			if (getContent().getTextType().equals(TextType.AND_OPERATOR)) {

				Bounds b11 = ((OperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double widthParent = anchorage.getVisual().getBoundsInParent().getWidth();

				double nX = 0;
				double nY = 0;

				// double widthThis =
				// getVisual().getBoundsInParent().getWidth();
				if (((OperatorAndBlockModel) ((OperatorMovableBlockPart) anchorage).getContent())
						.getFixedChildOperator1() != null) {
					double widthOperator1 = ((OperatorAndBlockModel) ((OperatorMovableBlockPart) anchorage)
							.getContent()).getFixedChildOperator1().getBoundsInParent().getWidth();

					nX = b11.getMinX() + widthOperator1 + 10;
					nY = b11.getMinY() + 5.0;
				} else {

					nX = b11.getMinX() + (widthParent / 2);
					nY = b11.getMinY() + 2.0;
				}

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				getVisual().toFront();
				refreshVisual();

			}

			if (getContent().getTextType().equals(TextType.ARITHMETIC)
					|| getContent().getTextType().equals(TextType.COMPARISON_OPERATOR)) {

				Bounds b11 = ((OperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double widthParent = anchorage.getVisual().getBoundsInParent().getWidth();

				double nX = 0;
				double nY = 0;

				// double widthThis =
				// getVisual().getBoundsInParent().getWidth();
				if (((OperatorNumercialComparisonBlockModel) ((OperatorMovableBlockPart) anchorage).getContent())
						.getFixedChildOperator1() != null) {
					double widthOperator1 = ((OperatorNumercialComparisonBlockModel) ((OperatorMovableBlockPart) anchorage)
							.getContent()).getFixedChildOperator1().getBoundsInParent().getWidth();

					nX = b11.getMinX() + widthOperator1 + 10;
					nY = b11.getMinY() + 5.0;
				} else {

					nX = b11.getMinX() + (widthParent / 2);
					nY = b11.getMinY() + 2.0;
				}

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				getVisual().toFront();
				refreshVisual();

			}

			if (getContent().getTextType().equals(TextType.IS_SELECTED)) {

				for (IVisualPart<? extends Node> anchords : anchorage.getAnchoredsUnmodifiable()) {

					// double width = ((ChoiceBoxPart)
					// anchords).getContentSize().getWidth();
					//

					Bounds b11 = ((OperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
					Bounds b22 = getVisual().getBoundsInParent();

					// double nX = b11.getMinX()+10.0 + 60;
					// double nY = b11.getMinY()+2.0;
					double nX = b11.getMinX() + 160.0;
					double nY = b11.getMinY() + 5.0;

					distanceX = nX - b22.getMinX();
					distanceY = nY - b22.getMinY();

					getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

					// getVisual().resize(getVisual().getWidth(),
					// positionY);

					// getVisual().heightProperty().
					getVisual().toFront();
					refreshVisual();

				}
			}

		}

		if (anchorage instanceof ArithmeticalOperationOperatorPart) {
			if (getContent().getTextType().equals(TextType.ARITHMETICAL_OPERATOR)) {

				Bounds b11 = ((ArithmeticalOperationOperatorPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double widthParent = anchorage.getVisual().getBoundsInParent().getWidth();

				// double widthThis =
				// getVisual().getBoundsInParent().getWidth();

				double nX = 0;
				double nY = 0;

				// double widthThis =
				// getVisual().getBoundsInParent().getWidth();
				if (((ArithmeticalOperationOperatorPart) anchorage).getContent().getFixedChildOperator1() != null) {
					double widthOperator1 = ((ArithmeticalOperationOperatorPart) anchorage).getContent()
							.getFixedChildOperator1().getBoundsInParent().getWidth();

					nX = b11.getMinX() + widthOperator1 + 10;
					nY = b11.getMinY() + 5.0;
				} else {

					nX = b11.getMinX() + (widthParent / 2);
					nY = b11.getMinY() + 5.0;
				}

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				getVisual().toFront();
				refreshVisual();

			}

		}

		if (anchorage instanceof ArithmeticalOperatorMovableBlockPart) {
			if (getContent().getTextType().equals(TextType.MIN_MAX_OPERATOR)) {

				Bounds b11 = ((ArithmeticalOperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double nX = 0;
				double nY = 0;

				nX = b11.getMinX() + 20;
				nY = b11.getMinY() + 3.0;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				getVisual().toFront();
				refreshVisual();

			}

			if (getContent().getTextType().equals(TextType.NEGATION)) {

				Bounds b11 = ((ArithmeticalOperatorMovableBlockPart) anchorage).getVisual().getBoundsInParent();
				Bounds b22 = getVisual().getBoundsInParent();

				double nX = b11.getMinX() + 10.0;
				double nY = b11.getMinY() + 5.0;

				distanceX = nX - b22.getMinX();
				distanceY = nY - b22.getMinY();

				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

				// getVisual().resize(getVisual().getWidth(), positionY);

				// getVisual().heightProperty().
				refreshVisual();
			}

		}

		if (distanceX != 0 && distanceY != 0) {

			List<IContentPart<? extends Node>> linked = new ArrayList<>();
			linked = getAnchoredsRecursive(linked, this);

			for (IContentPart<? extends Node> anch : linked) {

				if (anch instanceof AbstractChoiceBoxPart<?>) {
					((AbstractChoiceBoxPart<?>) anch).getContent().getTransform().translate(distanceX, distanceY);
					anch.refreshVisual();
				}

				if (anch instanceof OperatorFixedBlockPart) {
					((OperatorFixedBlockPart) anch).getContent().getTransform().translate(distanceX, distanceY);

					anch.refreshVisual();
				}
			}
		}
	}

}
