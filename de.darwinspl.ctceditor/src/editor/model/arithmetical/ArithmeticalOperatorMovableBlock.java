package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ArithmeticalOperatorMovableBlock extends GeometricShape {

	public ArithmeticalOperatorMovableBlock(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);

	}

	@Override
	public ArithmeticalOperatorMovableBlock getCopy() {
		ArithmeticalOperatorMovableBlock copy = new ArithmeticalOperatorMovableBlock((IShape) getGeometry().getCopy(),
				getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {

		if (parentBlock instanceof ArithmeticalOperatorFixedBlock) {
			parentBlockProperty.set((ArithmeticalOperatorFixedBlock) parentBlock);
			((ArithmeticalOperatorFixedBlock) parentBlock).setMovableChildBlock(this);
		}

	}

	@Override
	public ArithmeticalOperatorFixedBlock getParentBlock() {
		return (ArithmeticalOperatorFixedBlock) parentBlockProperty.get();
	}

	@Override
	public void removeParentBlock() {
		if ((parentBlockProperty.get() != null) || (getParentBlock().getFixedChildOperatorBlockModel() != null)) {
			((ArithmeticalOperatorFixedBlock) getParentBlock()).removeMovableChildBlock();
			parentBlockProperty.set(null);

		}

	}

}
