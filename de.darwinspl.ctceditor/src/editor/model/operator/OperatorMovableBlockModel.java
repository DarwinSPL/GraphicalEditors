package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorMovableBlockModel extends GeometricShape {

	public OperatorMovableBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);

	}

	@Override
	public OperatorMovableBlockModel getCopy() {
		OperatorMovableBlockModel copy = new OperatorMovableBlockModel((IShape) getGeometry().getCopy(),
				getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {

		parentBlockProperty.set((OperatorFixedBlockModel) parentBlock);
		((OperatorFixedBlockModel) parentBlock).setMovableChildBlock(this);
	}

	@Override
	public OperatorFixedBlockModel getParentBlock() {
		return (OperatorFixedBlockModel) parentBlockProperty.get();
	}

	@Override
	public void removeParentBlock() {
		if ((parentBlockProperty.get() != null) || (getParentBlock().getFixedChildOperatorBlockModel() != null)) {
			((OperatorFixedBlockModel) getParentBlock()).removeMovableChildBlock();
			parentBlockProperty.set(null);

		}

	}

}
