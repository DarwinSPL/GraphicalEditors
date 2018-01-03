package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class NegationOperatorBlockModel extends ArithmeticalOperatorMovableBlock {

	public final static String FIXED_CHILD_OPERATOR = "fixed_child_operator1";

	private final ObjectProperty<ArithmeticalOperatorFixedBlock> fixedChildoperatorProperty = new SimpleObjectProperty<>(
			this, FIXED_CHILD_OPERATOR);

	public NegationOperatorBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);
		// TODO Auto-generated constructor stub
	}

	public ObjectProperty<ArithmeticalOperatorFixedBlock> getFixedChildoperatorProperty() {
		return fixedChildoperatorProperty;
	}

	public void setFixedChildOperator(ArithmeticalOperatorFixedBlock child) {
		fixedChildoperatorProperty.set(child);
	}

	public ArithmeticalOperatorFixedBlock getFixedChildOperator() {
		return fixedChildoperatorProperty.get();
	}

	public void removeFixedChildOperator() {
		fixedChildoperatorProperty.set(null);
	}

	@Override
	public NegationOperatorBlockModel getCopy() {

		NegationOperatorBlockModel copy = new NegationOperatorBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

}
