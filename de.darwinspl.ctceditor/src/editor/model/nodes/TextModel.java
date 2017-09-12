package editor.model.nodes;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;

public class TextModel extends AbstractBlockElement {

	public enum TextType {

		SELECTABLE,
		COMPARISON_OPERATOR, ARITHMETICAL_OPERATOR, MIN_MAX_OPERATOR, EQUALS, NOT_EQUALS, SMALLER_THEN, BIGGER_THEN, SMALLER_OR_EQUALS, BIGGER_OR_EQUALS, IS_SELECTED, IS_NOT_SELECTED, VALIDATE, FROM_DATE, TO_DATE, EVOLUTION, IF, SELECT, AND_OPERATOR, VALIDATE_NOT, VALIDATE_NOT_OPERATOR, EQUIVALENCE, ARITHMETIC, NEGATION, AND_OR_CONTROL
	}

	public static final String TEXT_PROPERTY = "text_property";

	private TextType textType;

	private final ObjectProperty<String> textProperty = new SimpleObjectProperty<>(this, TEXT_PROPERTY);

	public TextModel(String text, AffineTransform transform, TextType type) {
		setText(text);
		setTransform(transform);
		this.textType = type;

	}

	public TextModel(String text, AffineTransform transform, TextType type, Paint fill) {
		this(text, transform, type);
		setFill(fill);

	}

	


	public String getText() {
		return textProperty.get();
	}

	public void setText(String text) {
		textProperty.set(text);
	}

	public TextModel getCopy() {
		TextModel copy = new TextModel(getText(), getTransform().getCopy(), getTextType(), getFill());
		return copy;
	}

	public TextType getTextType() {
		return textType;
	}

	public void setTextType(TextType type) {
		this.textType = type;
		// setTextAccordingToType();
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parent) {
		parentBlockProperty.set(parent);
		parent.setFixedChildTextModel(this);
	}

	@Override
	public void removeParentBlock() {
		if (parentBlockProperty.get() != null) {
			getParentBlock().setFixedChildTextModel(null);
			// parentBlockProperty.get().setFixedChildTextModel(null);
		}
		parentBlockProperty.set(null);
	}
}
