package editor.model.operator;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.choiceboxes.ChoiceBoxModel;
import eu.hyvar.feature.HyFeature;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class OperatorComparisonFeatureAttributeBlockModel extends OperatorComparisonBlockModel {

	public final static String CHOICEBOX_FEATURE_PROPERTY = "choiebox_feature_property";

	private final ObjectProperty<ChoiceBoxModel<HyFeature>> choiceBoxFeatureProperty = new SimpleObjectProperty<>(this,
			CHOICEBOX_FEATURE_PROPERTY);

	public OperatorComparisonFeatureAttributeBlockModel(IShape shape, AffineTransform transform, Paint fill,
			Effect effect, OperatorComparisonType type, OperatorComparisonValueType valueType) {
		super(shape, transform, fill, effect, type, valueType);

	}

	@Override
	public OperatorComparisonFeatureAttributeBlockModel getCopy() {

		OperatorComparisonFeatureAttributeBlockModel copy = new OperatorComparisonFeatureAttributeBlockModel(
				(IShape) getGeometry().getCopy(), (AffineTransform) getTransform().getCopy(), getFill(), getEffect(),
				getComparisonType(), getComparisonValueType());

		return copy;
	}

	public ObjectProperty<ChoiceBoxModel<HyFeature>> getChoiceBoxFeatureProperty() {
		return choiceBoxFeatureProperty;
	}

	public ChoiceBoxModel<HyFeature> getChoiceBoxFeature() {
		return choiceBoxFeatureProperty.get();
	}

	public void setChoiceBoxFeature(ChoiceBoxModel<HyFeature> choiceBoxModel) {
		choiceBoxFeatureProperty.set(choiceBoxModel);

	}

	public void removeChoiceBoxFeature() {

		choiceBoxFeatureProperty.set(null);

	}

}
