package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorNotBlockModel extends OperatorMovableBlockModel {

	public OperatorNotBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);
	}

	@Override
	public OperatorNotBlockModel getCopy() {
		// TODO Auto-generated method stub
		OperatorNotBlockModel copy = new OperatorNotBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

}
