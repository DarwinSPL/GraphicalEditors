package editor.parts;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import editor.model.context.TextFieldModel;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.nodes.AbstractNodePart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextFieldPart extends AbstractNodePart<TextField> {

	@Override
	public TextFieldModel getContent() {

		return (TextFieldModel) super.getContent();
	}

	@Override
	protected TextField doCreateVisual() {

		TextField textField = new TextField();
		textField.setMaxWidth(90);
		textField.setMinWidth(90);
		textField.setMinHeight(28);
		textField.setMaxHeight(28);

		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {

					if ((textField.getText().matches("\\d*"))) {
						getContent().setEnteredValue(textField.getText());

						textField.setStyle("-fx-text-fill: black;");

					}

					else {

						String textString = textField.getText();
						// textField.getStylesheets().addAll(styleError);

						refreshVisual();
						textField.setText(textString);
						textField.setStyle("-fx-text-fill: red;");

						textField.setTooltip(new Tooltip("Only numercial values"));

					}

				}

			}
		});

		return textField;

	}

	@Override
	protected void doRefreshVisual(TextField visual) {

		TextFieldModel content = getContent();

		if (visual.getText() != content.getEnteredValue()) {
			visual.setText(content.getEnteredValue());
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

		if (anchorage instanceof OperatorMovableBlockPart) {

			OperatorMovableBlockPart p = (OperatorMovableBlockPart) anchorage;

			if (p.getVisual().getParent() != null) {
				if (!getVisual().getParent().equals(p.getVisual().getParent())) {

					Group parenttthis = (Group) getVisual().getParent();
					Group parentparent = (Group) p.getVisual().getParent();

					parenttthis.getChildren().remove(getVisual());
					parentparent.getChildren().add(getVisual());

				}
			}
			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			double nX = b11.getMinX() + 170;
			double nY = b11.getMinY() + 2;

			getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			getVisual().toFront();
			refreshVisual();

		}
		if (anchorage instanceof ArithmeticalOperatorMovableBlockPart) {

			ArithmeticalOperatorMovableBlockPart p = (ArithmeticalOperatorMovableBlockPart) anchorage;

			if (p.getVisual().getParent() != null) {
				if (!getVisual().getParent().equals(p.getVisual().getParent())) {

					Group parenttthis = (Group) getVisual().getParent();
					Group parentparent = (Group) p.getVisual().getParent();

					parenttthis.getChildren().remove(getVisual());
					parentparent.getChildren().add(getVisual());

				}
			}
			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			double nX = b11.getMinX() + 15;
			double nY = b11.getMinY() + 2;

			getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			getVisual().toFront();
			refreshVisual();

		}
	}
}
