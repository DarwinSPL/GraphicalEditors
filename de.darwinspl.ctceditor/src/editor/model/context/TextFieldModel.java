package editor.model.context;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.ArithmeticalTextFielOperatorBlockModel;
import editor.model.operator.OperatorWithChoiceBoxAndTextField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TextFieldModel extends AbstractBlockElement {

	public static final String ENTERED_VALUE_PROPERTY = "entered_value_property";

	private final ObjectProperty<String> enteredValueProperty = new SimpleObjectProperty<>(this,
			ENTERED_VALUE_PROPERTY);

	public TextFieldModel(AffineTransform transform) {
		super(transform);
	}
	// @Override
	// public OperatorWithChoiceBoxAndTextField getParentBlock() {
	// // TODO Auto-generated method stub
	// if(getParentBlock() instanceof OperatorWithChoiceBoxAndTextField)
	// return (OperatorWithChoiceBoxAndTextField) parentBlockProperty.get();
	// //return (OperatorWithChoiceBoxModel) super.getParentBlock();
	// }

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {
		// TODO Auto-generated method stub
		super.setParentBlock(parentBlock);
		if (parentBlock instanceof OperatorWithChoiceBoxAndTextField) {
			((OperatorWithChoiceBoxAndTextField) parentBlock).setTextFieldModel(this);
		}
		if (parentBlock instanceof ArithmeticalTextFielOperatorBlockModel) {
			((ArithmeticalTextFielOperatorBlockModel) parentBlock).setTextFieldModel(this);
		}

	}

	@Override
	public void removeParentBlock() {
		// TODO Auto-generated method stub
		if (getParentBlock() != null) {
			if (getParentBlock() instanceof OperatorWithChoiceBoxAndTextField) {
				((OperatorWithChoiceBoxAndTextField) getParentBlock()).removeTextFieldModel();
			}
			if (getParentBlock() instanceof ArithmeticalTextFielOperatorBlockModel) {
				((ArithmeticalTextFielOperatorBlockModel) getParentBlock()).removeTextFieldModel();
			}

		}

		super.removeParentBlock();

	}

	public ObjectProperty<String> getEnteredValueProperty() {
		return enteredValueProperty;
	}

	public void setEnteredValue(String enteredValue) {
		enteredValueProperty.set(enteredValue);
	}

	public String getEnteredValue() {
		return enteredValueProperty.get();
	}

	public TextFieldModel getCopy() {
		TextFieldModel copy = new TextFieldModel(getTransform().getCopy());
		if (getEnteredValue() != null) {
			copy.setEnteredValue(getEnteredValue());
		}
		return copy;
	}

}
