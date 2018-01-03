package editor.model.arithmetical;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.choiceboxes.ChoiceBoxModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class NumericalObjectReferenceOperator extends ArithmeticalOperatorMovableBlock {

	public final static String CONTEXT_CHOICEBOX_PROPERTY = "context_choicebox_property";

	private final ObjectProperty<ChoiceBoxModel<?>> contextChoiceBoxProperty = new SimpleObjectProperty<>(
			this, CONTEXT_CHOICEBOX_PROPERTY);

	public NumericalObjectReferenceOperator(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);
	}

	public ObjectProperty<ChoiceBoxModel<?>> getContextChoiceBoxProperty() {
		return contextChoiceBoxProperty;
	}

	public void setContextChoiceBox(ChoiceBoxModel<?> choiceBoxContextModel) {
		contextChoiceBoxProperty.set(choiceBoxContextModel);
	}

	public ChoiceBoxModel<?> getContextChoiceBox() {
		return contextChoiceBoxProperty.get();
	}

	public void removeContextChoiceBox() {
		contextChoiceBoxProperty.set(null);
	}

	@Override
	public NumericalObjectReferenceOperator getCopy() {

		NumericalObjectReferenceOperator copy = new NumericalObjectReferenceOperator((IShape) getGeometry().getCopy(),
				(AffineTransform) getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

}
