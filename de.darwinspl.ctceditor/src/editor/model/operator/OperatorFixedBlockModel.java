package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorFixedBlockModel extends GeometricShape {

	public enum OperatorFixedBlockType {
		AND_OPERATOR1, AND_OPERATOR2, IF_OPERATOR, VALIDATE_OPERATOR, VALIDATE_NOT_OPERATOR

	}

	public final static String MOVABLE_CHILD_BLOCK_PROPERTY = "movable_child_block_property";

	public final OperatorFixedBlockType operatorFixedBlockType;

	private final ObjectProperty<OperatorMovableBlockModel> movablechildBlockProperty = new SimpleObjectProperty<>(this,
			MOVABLE_CHILD_BLOCK_PROPERTY);

	public OperatorFixedBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			OperatorFixedBlockType type, AbstractGeometricElement<? extends IGeometry> fixedParentBlock) {
		super(shape, transform, fill, effect);
		this.operatorFixedBlockType = type;

		// TODO: control if this works
		setParentBlock(fixedParentBlock);
	}

	public OperatorFixedBlockType getOperatorFixedBlockType() {
		return operatorFixedBlockType;
	}

	public OperatorFixedBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			OperatorFixedBlockType type) {
		// TODO Auto-generated constructor stub
		super(shape, transform, fill, effect);
		this.operatorFixedBlockType = type;
	}

	@Override
	public OperatorFixedBlockModel getCopy() {
		// TODO Auto-generated method stub
		OperatorFixedBlockModel copy = new OperatorFixedBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getOperatorFixedBlockType());
		return copy;
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {
		parentBlockProperty.set(parentBlock);

		if ((getOperatorFixedBlockType().equals(OperatorFixedBlockType.IF_OPERATOR)
				|| (getOperatorFixedBlockType().equals(OperatorFixedBlockType.VALIDATE_OPERATOR))
				|| (getOperatorFixedBlockType().equals(OperatorFixedBlockType.VALIDATE_NOT_OPERATOR)))) {

			parentBlock.setFixedChildOperatorBlockModel(this);
		}
		if (getOperatorFixedBlockType().equals(OperatorFixedBlockType.AND_OPERATOR1)) {
			((OperatorAndBlockModel) parentBlock).setFixedChildOperator1(this);
		}
		if (getOperatorFixedBlockType().equals(OperatorFixedBlockType.AND_OPERATOR2)) {
			((OperatorAndBlockModel) parentBlock).setFixedChildOperator2(this);
		}

	}

	//
	//
	//
	@Override
	public void removeParentBlock() {
		if (parentBlockProperty.get() != null) {
			// parentBlockProperty.get().setFixedChildOperatorBlockModel(null);

			if ((getOperatorFixedBlockType().equals(OperatorFixedBlockType.IF_OPERATOR)
					|| (getOperatorFixedBlockType().equals(OperatorFixedBlockType.VALIDATE_OPERATOR)))) {

				getParentBlock().setFixedChildOperatorBlockModel(null);
			}
			if (getOperatorFixedBlockType().equals(OperatorFixedBlockType.AND_OPERATOR1)) {
				((OperatorAndBlockModel) getParentBlock()).removeFixedChildOperator1();
			}
			if (getOperatorFixedBlockType().equals(OperatorFixedBlockType.AND_OPERATOR2)) {
				((OperatorAndBlockModel) getParentBlock()).removeFixedChildOperator2();
			}

			parentBlockProperty.set(null);
		}

	}

	public ObjectProperty<OperatorMovableBlockModel> getMovableChildBlockProperty() {
		return movablechildBlockProperty;
	}

	public void setMovableChildBlock(OperatorMovableBlockModel movableChildBlock) {
		movablechildBlockProperty.set(movableChildBlock);

	}

	public OperatorMovableBlockModel getMovableChildBlock() {
		return movablechildBlockProperty.get();
	}

	public void removeMovableChildBlock() {
		movablechildBlockProperty.set(null);

	}

}
