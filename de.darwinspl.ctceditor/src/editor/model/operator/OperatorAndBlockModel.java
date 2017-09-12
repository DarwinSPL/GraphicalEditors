package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorAndBlockModel extends OperatorMovableBlockModel {

	public enum OperatorAndORType {
		AND, OR
	}

	private OperatorAndORType operatorAndORType;

	public final static String FIXED_CHILD_OPERATOR1 = "fixed_child_operator1";

	private final ObjectProperty<OperatorFixedBlockModel> fixedChildoperator1Property = new SimpleObjectProperty<>(this,
			FIXED_CHILD_OPERATOR1);

	public final static String FIXED_CHILD_OPERATOR2 = "fixed_child_operator2";

	private final ObjectProperty<OperatorFixedBlockModel> fixedChildoperator2Property = new SimpleObjectProperty<>(this,
			FIXED_CHILD_OPERATOR2);

	public OperatorAndBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			OperatorAndORType type) {
		super(shape, transform, fill, effect);
		this.operatorAndORType = type;
		
	}

	@Override
	public OperatorAndBlockModel getCopy() {
		
		OperatorAndBlockModel copy = new OperatorAndBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getOperatorAndORType());
		return copy;
	}

	public ObjectProperty<OperatorFixedBlockModel> getFixedChildoperator1Property() {
		return fixedChildoperator1Property;
	}

	public void setFixedChildOperator1(OperatorFixedBlockModel child) {
		fixedChildoperator1Property.set(child);
	}

	public OperatorFixedBlockModel getFixedChildOperator1() {
		return fixedChildoperator1Property.get();
	}

	public void removeFixedChildOperator1() {
		fixedChildoperator1Property.set(null);
	}

	public ObjectProperty<OperatorFixedBlockModel> getFixedChildoperator2Property() {
		return fixedChildoperator2Property;
	}

	public void setFixedChildOperator2(OperatorFixedBlockModel child) {
		fixedChildoperator2Property.set(child);
	}

	public OperatorFixedBlockModel getFixedChildOperator2() {
		return fixedChildoperator2Property.get();
	}

	public void removeFixedChildOperator2() {
		fixedChildoperator2Property.set(null);
	}

	public OperatorAndORType getOperatorAndORType() {
		return operatorAndORType;
	}

	public void setOperatorAndORType(OperatorAndORType operatorAndORType) {
		this.operatorAndORType = operatorAndORType;
	}

}
