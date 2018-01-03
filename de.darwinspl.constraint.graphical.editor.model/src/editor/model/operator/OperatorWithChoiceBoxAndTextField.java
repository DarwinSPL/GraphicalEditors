package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.context.TextFieldModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorWithChoiceBoxAndTextField extends OperatorComparisonBlockModel {

	public OperatorWithChoiceBoxAndTextField(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			OperatorComparisonType type, OperatorComparisonValueType valueType) {
		super(shape, transform, fill, effect, type, valueType);
		// TODO Auto-generated constructor stub
	}

	public final static String TEXTFIELD_CHILD_PROPERTY = "textfield_child_property";

	private final ObjectProperty<TextFieldModel> textFieldChildProperty = new SimpleObjectProperty<TextFieldModel>(this,
			TEXTFIELD_CHILD_PROPERTY);

	public ObjectProperty<TextFieldModel> getTextFieldChildProperty() {
		return textFieldChildProperty;
	}

	public TextFieldModel getTextFieldModel() {
		return textFieldChildProperty.get();
	}

	public void setTextFieldModel(TextFieldModel textFieldModel) {
		textFieldChildProperty.set(textFieldModel);
	}

	public void removeTextFieldModel() {
		textFieldChildProperty.set(null);
	}

	@Override
	public OperatorWithChoiceBoxAndTextField getCopy() {
		OperatorWithChoiceBoxAndTextField copy = new OperatorWithChoiceBoxAndTextField((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getComparisonType(),
				getComparisonValueType());
		return copy;
	}

}
