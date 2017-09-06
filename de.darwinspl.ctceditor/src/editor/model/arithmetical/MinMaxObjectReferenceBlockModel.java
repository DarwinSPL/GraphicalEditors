package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class MinMaxObjectReferenceBlockModel extends NumericalObjectReferenceOperator {

	public enum MinMaxType {
		MIN, MAX;
	}

	private MinMaxType type;

	public MinMaxObjectReferenceBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			MinMaxType type) {
		super(shape, transform, fill, effect);
		this.type = type;
	}

	@Override
	public MinMaxObjectReferenceBlockModel getCopy() {

		MinMaxObjectReferenceBlockModel copy = new MinMaxObjectReferenceBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getMinMaxType());
		return copy;
	}

	public MinMaxType getMinMaxType() {
		return type;
	}

	public void setMinMaxType(MinMaxType type) {
		this.type = type;
	}

}
