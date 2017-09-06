package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.choiceboxes.ChoiceBoxModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorFeatureAttributeBlockModel extends OperatorWithChoiceBoxModel {

	public final static String CHOICEBOX_CHILD_SELECT_CONTEXT_ATTRIBUTE_PROPERTY = "choiebox_child_select_context_attribute_property";

	private final ObjectProperty<ChoiceBoxModel<?>> choiceBoxContextAttributeChildProperty = new SimpleObjectProperty<>(
			this, CHOICEBOX_CHILD_SELECT_CONTEXT_ATTRIBUTE_PROPERTY);

	public OperatorFeatureAttributeBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);
		// TODO Auto-generated constructor stub
	}

	public ObjectProperty<ChoiceBoxModel<?>> getChoiceBoxContextAttributeChildProperty() {
		return choiceBoxContextAttributeChildProperty;
	}

	public void setChoiceBoxContextAttributeChild(ChoiceBoxModel<?> choiceBoxModel) {
		choiceBoxContextAttributeChildProperty.set(choiceBoxModel);

	}

	public void removeChoiceBoxContextAttributeChild() {
		choiceBoxContextAttributeChildProperty.set(null);

	}

	@Override
	public OperatorFeatureAttributeBlockModel getCopy() {
		// TODO Auto-generated method stub
		OperatorFeatureAttributeBlockModel copy = new OperatorFeatureAttributeBlockModel(
				(IShape) getGeometry().getCopy(), (AffineTransform) getTransform().getCopy(), getFill(), getEffect());
		return copy;
	}

}
