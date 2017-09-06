package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorFeatureIsSelectedModel extends OperatorWithChoiceBoxModel {

//	public enum FeatureSelectionType {
//		IS_SELECTED, IS_NOT_SELECTED
//	}
	public OperatorFeatureIsSelectedModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);


	}

	@Override
	public OperatorFeatureIsSelectedModel getCopy() {
		// TODO Auto-generated method stub
		OperatorFeatureIsSelectedModel copy = new OperatorFeatureIsSelectedModel((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect());

		return copy;
	}


}
