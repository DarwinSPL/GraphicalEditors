package editor.model.vf;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.choiceboxes.ChoiceBoxModel;
import editor.model.control.ControlIfBlockModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class VFControlBlockModel extends ControlIfBlockModel {

	public final static String CHOICEBOX_FEATURE_PROPERTY = "choiebox_child_feature_property";
	
	
	private final ObjectProperty<ChoiceBoxModel<?>> choiceBoxFeatureProperty = new SimpleObjectProperty<>(this,
			CHOICEBOX_FEATURE_PROPERTY);

	
	public VFControlBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect, ControlBlockType type) {
		super(shape, transform, fill, effect, type);
	}
	
	
	public ObjectProperty<ChoiceBoxModel<?>> getChoiceBoxContextChildProperty() {
		return choiceBoxFeatureProperty;
	}

	public ChoiceBoxModel<?> getChoiceBoxContextChild() {
		return choiceBoxFeatureProperty.get();
	}

	public void setChoiceBoxContextChild(ChoiceBoxModel<?> choiceBoxModel) {
		choiceBoxFeatureProperty.set(choiceBoxModel);

	}

	public void removeChoiceBoxContextChild() {

		choiceBoxFeatureProperty.set(null);

	}
	
	
	@Override
	public VFControlBlockModel getCopy() {
		VFControlBlockModel copy = new VFControlBlockModel((IShape) getGeometry().getCopy(), (AffineTransform) getTransform().getCopy(), getFill(), getEffect(), getControlBlockType());
		return copy;
	}

}
