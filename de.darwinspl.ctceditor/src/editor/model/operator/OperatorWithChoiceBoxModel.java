package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.choiceboxes.ChoiceBoxModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public abstract class OperatorWithChoiceBoxModel extends OperatorMovableBlockModel {

	public final static String CHOICEBOX_CHILD_SELECT_CONTEXT_PROPERTY = "choiebox_child_select_context_property";

	public final static String CHOICEBOX_CHILD_SELECT_VALUE_PROPERTY = "choiebox_child_select_value_property";

	private final ObjectProperty<ChoiceBoxModel<?>> choiceBoxContextChildProperty = new SimpleObjectProperty<>(this,
			CHOICEBOX_CHILD_SELECT_CONTEXT_PROPERTY);

	private final ObjectProperty<ChoiceBoxModel<?>> choiceBoxValueChildProperty = new SimpleObjectProperty<>(this,
			CHOICEBOX_CHILD_SELECT_VALUE_PROPERTY);

	public OperatorWithChoiceBoxModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);

	}

	public ObjectProperty<ChoiceBoxModel<?>> getChoiceBoxContextChildProperty() {
		return choiceBoxContextChildProperty;
	}

	public ChoiceBoxModel<?> getChoiceBoxContextChild() {
		return choiceBoxContextChildProperty.get();
	}

	public void setChoiceBoxContextChild(ChoiceBoxModel<?> choiceBoxModel) {
		choiceBoxContextChildProperty.set(choiceBoxModel);

	}

	public void removeChoiceBoxContextChild() {

		choiceBoxContextChildProperty.set(null);

	}

	public ObjectProperty<ChoiceBoxModel<?>> getChoiceBoxValueChildProperty() {
		return choiceBoxValueChildProperty;
	}

	public ChoiceBoxModel<?> getChoiceBoxValueChild() {
		return choiceBoxValueChildProperty.get();
	}

	public void setChoiceBoxValueChild(ChoiceBoxModel<?> choiceBoxModel) {
		choiceBoxValueChildProperty.set(choiceBoxModel);
		// choiceBoxModel.setParentBlockProperty(this);

	}

	public void removeChoiceBoxValueChild() {

		choiceBoxValueChildProperty.set(null);

	}

}
