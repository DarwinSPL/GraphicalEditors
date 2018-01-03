package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ArithmeticalOperatorFixedBlock extends GeometricShape {

	public enum ArithmeticalOperatorFixedType {
		ARITHMETICAL_OPERAND1, ARITHMETICAL_OPERAND2, COMPARISON_OPERAND1, COMPARISON_OPERAND2, NEG_OPERAND, MIN_MAX_OPERAND
	}

	private final ArithmeticalOperatorFixedType arithmeticalOperatorFixedType;
	public final static String MOVABLE_CHILD_BLOCK_PROPERTY = "movable_child_block_property";

	private final ObjectProperty<ArithmeticalOperatorMovableBlock> movablechildBlockProperty = new SimpleObjectProperty<>(
			this, MOVABLE_CHILD_BLOCK_PROPERTY);

	public ArithmeticalOperatorFixedBlock(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			ArithmeticalOperatorFixedType type) {
		super(shape, transform, fill, effect);
		this.arithmeticalOperatorFixedType = type;
		
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {
		parentBlockProperty.set(parentBlock);

		if (parentBlock instanceof ArithmeticalOperatorMovableBlock || parentBlock instanceof OperatorNumercialComparisonBlockModel) {

			switch (getArithmeticalOperatorFixedType()) {
			case ARITHMETICAL_OPERAND1:

				((ArithmeticalOperationOperatorBlockModel) parentBlock).setFixedChildOperator1(this);

				break;

			case ARITHMETICAL_OPERAND2:
				((ArithmeticalOperationOperatorBlockModel) parentBlock).setFixedChildOperator2(this);
				break;

			case COMPARISON_OPERAND1:
				((OperatorNumercialComparisonBlockModel) parentBlock).setFixedChildOperator1(this);
				break;
			case COMPARISON_OPERAND2:
				((OperatorNumercialComparisonBlockModel) parentBlock).setFixedChildOperator2(this);
				break;

			case NEG_OPERAND:
				((NegationOperatorBlockModel) parentBlock).setFixedChildOperator(this);
				break;

			default:
				break;
			}

		}

	}

	//
	//
	//
	@Override
	public void removeParentBlock() {
		if (parentBlockProperty.get() != null) {

			if (getParentBlock() instanceof ArithmeticalOperatorMovableBlock) {

				switch (getArithmeticalOperatorFixedType()) {
				case ARITHMETICAL_OPERAND1:

					((ArithmeticalOperationOperatorBlockModel) getParentBlock()).removeFixedChildOperator1();

					break;

				case ARITHMETICAL_OPERAND2:
					((ArithmeticalOperationOperatorBlockModel) getParentBlock()).removeFixedChildOperator2();
					break;

				case COMPARISON_OPERAND1:
					((OperatorNumercialComparisonBlockModel) getParentBlock()).removeFixedChildOperator1();
					break;
				case COMPARISON_OPERAND2:
					((OperatorNumercialComparisonBlockModel) getParentBlock()).removeFixedChildOperator2();
					break;
				case MIN_MAX_OPERAND:
			
					break;
				case NEG_OPERAND:
					((NegationOperatorBlockModel) getParentBlock()).removeFixedChildOperator();
					break;

				default:
					break;
				}

			}

			parentBlockProperty.set(null);
		}

	}

	public ObjectProperty<ArithmeticalOperatorMovableBlock> getMovableChildBlockProperty() {
		return movablechildBlockProperty;
	}

	public void setMovableChildBlock(ArithmeticalOperatorMovableBlock movableChildBlock) {
		movablechildBlockProperty.set(movableChildBlock);

	}

	public ArithmeticalOperatorMovableBlock getMovableChildBlock() {
		return movablechildBlockProperty.get();
	}

	public void removeMovableChildBlock() {
		movablechildBlockProperty.set(null);

	}

	public ArithmeticalOperatorFixedType getArithmeticalOperatorFixedType() {
		return arithmeticalOperatorFixedType;
	}

	@Override
	public ArithmeticalOperatorFixedBlock getCopy() {

		ArithmeticalOperatorFixedBlock copy = new ArithmeticalOperatorFixedBlock((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getArithmeticalOperatorFixedType());
		return copy;
	}

}
