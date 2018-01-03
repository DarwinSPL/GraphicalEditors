package editor.model.choiceboxes;

import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;

import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyBooleanAttribute;
import eu.hyvar.feature.HyEnumAttribute;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureAttribute;
import eu.hyvar.feature.HyFeatureModel;
import eu.hyvar.feature.HyNumberAttribute;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ChoiceBoxFeatureAttributeModel extends ChoiceBoxModel<HyFeatureAttribute> {

	public static final String PARENT_CHOICE_BOX_SELECTION_PROPERTY = "parent_choice_box_property";

	private final ObjectProperty<HyFeature> parentChoiceBoxSelectionProperty = new SimpleObjectProperty<>(this,
			PARENT_CHOICE_BOX_SELECTION_PROPERTY);

	public ChoiceBoxFeatureAttributeModel(AffineTransform transform, HyFeatureModel featureModel,
			HyContextModel contextModel, ChoiceBoxType type) {
		super(transform, contextModel, featureModel, type);
		refreshChoiceList();

	}

	@Override
	public ChoiceBoxFeatureAttributeModel getCopy() {
		ChoiceBoxFeatureAttributeModel copy = new ChoiceBoxFeatureAttributeModel(getTransform().getCopy(),
				getFeatureModel(), getContextModel(), getChoiceBoxType());
		if (getSelectedValue() != null) {
			copy.setSelectedValue(getSelectedValue());
		}
		if (!(getChoices().isEmpty())) {
			copy.setChoices(getChoices());
		}
		copy.setCurrentSelectedDate(getCurrentSelectedDate());
		return copy;
	}

	

	public void addChoiceIfValidAtSelectedDate(HyFeatureAttribute context) {
		if (getCurrentSelectedDate() != null) {
			if (context.getValidSince() != null) {
				if (context.getValidSince().before(getCurrentSelectedDate())
						|| context.getValidSince().equals(getCurrentSelectedDate())) {
					if (context.getValidUntil() != null) {
						if (!(context.getValidUntil().before(getCurrentSelectedDate()))) {
							addChoice(context);
						}
					} else {
						addChoice(context);
					}
				} else {
					// nothing
				}
			} else {
				addChoice(context);
			}

		} else {

			addChoice(context);
		}
	}

	public ObjectProperty<HyFeature> getParentChoiceBoxSelectionProperty() {
		return parentChoiceBoxSelectionProperty;
	}

	public void setParentChoiceBoxSelection(HyFeature choiceBoxModel) {
		parentChoiceBoxSelectionProperty.set(choiceBoxModel);
		refreshChoiceList();

	}

	public HyFeature getParentChoiceBoxSelection() {
		return parentChoiceBoxSelectionProperty.get();
	}

	//
	@Override
	public void refreshChoiceList() {

	    getChoices().removeAll(getChoices());

		
		List<HyFeature> features = getFeatureModel().getFeatures();

		if (getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_NUMBER)) {
			for (HyFeature feature : features) {
				for (HyFeatureAttribute attribute : feature.getAttributes()) {
					if (attribute instanceof HyNumberAttribute) {
						addChoiceIfValidAtSelectedDate(attribute);
					}
				}
			}
		}

		if (getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_BOOLEAN)) {
			for (HyFeature feature : features) {
				for (HyFeatureAttribute attribute : feature.getAttributes()) {
					if (attribute instanceof HyBooleanAttribute) {
						addChoiceIfValidAtSelectedDate(attribute);
					}
				}
			}
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_ENUM)) {
			for (HyFeature feature : features) {
				for (HyFeatureAttribute attribute : feature.getAttributes()) {
					if (attribute instanceof HyEnumAttribute) {
						addChoiceIfValidAtSelectedDate(attribute);
					}
				}
			}
		}

		super.refreshChoiceList();
		
		// if (getParentChoiceBoxSelection() != null) {
		// for (HyFeatureAttribute attributes :
		// getParentChoiceBoxSelection().getAttributes()) {
		// addChoiceIfValidAtSelectedDate(attributes);
		// }
		// }
	}
}
