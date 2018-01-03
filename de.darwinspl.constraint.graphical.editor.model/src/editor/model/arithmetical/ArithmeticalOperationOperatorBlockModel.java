package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ArithmeticalOperationOperatorBlockModel extends ArithmeticalOperatorMovableBlock {

	public enum ArithmeticalOperatorType {
		ADDITION, SUBTRACTION, MULTIPLICATION, MODULO, DIVISION
	}

	private ArithmeticalOperatorType arithmeticalOperatorType;

	public final static String FIXED_CHILD_OPERATOR1 = "fixed_child_operator1";

	private final ObjectProperty<ArithmeticalOperatorFixedBlock> fixedChildoperator1Property = new SimpleObjectProperty<>(
			this, FIXED_CHILD_OPERATOR1);

	public final static String FIXED_CHILD_OPERATOR2 = "fixed_child_operator2";

	private final ObjectProperty<ArithmeticalOperatorFixedBlock> fixedChildoperator2Property = new SimpleObjectProperty<>(
			this, FIXED_CHILD_OPERATOR2);

	public ArithmeticalOperationOperatorBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			ArithmeticalOperatorType type) {
		super(shape, transform, fill, effect);
		setArithmeticalOperatorType(type);

	}

	@Override
	public ArithmeticalOperationOperatorBlockModel getCopy() {

		ArithmeticalOperationOperatorBlockModel copy = new ArithmeticalOperationOperatorBlockModel(
				(IShape) getGeometry().getCopy(), (AffineTransform) getTransform().getCopy(), getFill(), getEffect(),
				getArithmeticalOperatorType());
		return copy;
	}

	public ObjectProperty<ArithmeticalOperatorFixedBlock> getFixedChildoperator1Property() {
		return fixedChildoperator1Property;
	}

	public void setFixedChildOperator1(ArithmeticalOperatorFixedBlock child) {
		fixedChildoperator1Property.set(child);
	}

	public ArithmeticalOperatorFixedBlock getFixedChildOperator1() {
		return fixedChildoperator1Property.get();
	}

	public void removeFixedChildOperator1() {
		fixedChildoperator1Property.set(null);
	}

	public ObjectProperty<ArithmeticalOperatorFixedBlock> getFixedChildoperator2Property() {
		return fixedChildoperator2Property;
	}

	public void setFixedChildOperator2(ArithmeticalOperatorFixedBlock child) {
		fixedChildoperator2Property.set(child);
	}

	public ArithmeticalOperatorFixedBlock getFixedChildOperator2() {
		return fixedChildoperator2Property.get();
	}

	public void removeFixedChildOperator2() {
		fixedChildoperator2Property.set(null);
	}

	public ArithmeticalOperatorType getArithmeticalOperatorType() {
		return arithmeticalOperatorType;
	}

	public void setArithmeticalOperatorType(ArithmeticalOperatorType operatorType) {
		this.arithmeticalOperatorType = operatorType;
	}
}
