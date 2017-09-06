package editor.model.control;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ControlValidateOperatorBlockModel extends ControlBlockModel {

	public ControlValidateOperatorBlockModel(IShape shape, AffineTransform transform, Color stroke, double strokeWidth,
			Paint fill, Effect effect, ControlValidateOperatorBlockType type) {
		super(shape, transform, fill, effect);
		this.controlValidateOperatorBlockType = type;
	
	}

	private final ControlValidateOperatorBlockType controlValidateOperatorBlockType;

	public ControlValidateOperatorBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			ControlValidateOperatorBlockType type) {
		super(shape, transform, fill, effect);
		this.controlValidateOperatorBlockType = type;

	}

	@Override
	public ControlValidateOperatorBlockModel getCopy() {
		
		ControlValidateOperatorBlockModel copy = new ControlValidateOperatorBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(),
				getControlValidateOperatorBlockType());

		return copy;
	}

	public ControlValidateOperatorBlockType getControlValidateOperatorBlockType() {
		return controlValidateOperatorBlockType;
	}

}
