package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorComparisonBlockModel extends OperatorWithChoiceBoxModel {

	private OperatorComparisonType operatorComparisonType;

	public enum OperatorComparisonType {
		EQUALS, SMALLER, BIGGER, NOT_EQUALS, EQUALS_OR_SMALLER, EQUALS_OR_BIGGER
	}

	public enum OperatorComparisonValueType {
		BOOLEAN, NUMBER, ENUM
	}

	private final OperatorComparisonValueType comparisonValueType;

	public OperatorComparisonBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect,
			OperatorComparisonType type, OperatorComparisonValueType valueType) {
		super(shape, transform, fill, effect);

		this.operatorComparisonType = type;
		this.comparisonValueType = valueType;

	}

	public void setComparisonType(OperatorComparisonType comparisonType) {
		this.operatorComparisonType = comparisonType;

	}

	public OperatorComparisonType getComparisonType() {
		return operatorComparisonType;
	}

	public OperatorComparisonValueType getComparisonValueType() {
		return comparisonValueType;
	}

	@Override
	public OperatorComparisonBlockModel getCopy() {
		// TODO Auto-generated method stub
		OperatorComparisonBlockModel copy = new OperatorComparisonBlockModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getComparisonType(),
				getComparisonValueType());
		return copy;

	}
}
