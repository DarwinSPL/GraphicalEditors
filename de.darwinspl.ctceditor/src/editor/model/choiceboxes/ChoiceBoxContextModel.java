package editor.model.choiceboxes;

import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;

import eu.hyvar.context.HyContextModel;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.context.HyContextualInformationBoolean;
import eu.hyvar.context.HyContextualInformationEnum;
import eu.hyvar.context.HyContextualInformationNumber;
import eu.hyvar.feature.HyFeatureModel;

public class ChoiceBoxContextModel extends ChoiceBoxModel<HyContextualInformation> {

	public ChoiceBoxContextModel(AffineTransform transform, HyContextModel contextModel, HyFeatureModel featureModel,
			ChoiceBoxType type) {
		super(transform, contextModel, featureModel, type);
		refreshChoiceList();

	}

	@Override
	public ChoiceBoxContextModel getCopy() {
		ChoiceBoxContextModel copy = new ChoiceBoxContextModel(getTransform().getCopy(), getContextModel(),
				getFeatureModel(), getChoiceBoxType());
		if (getSelectedValue() != null) {
			copy.setSelectedValue(getSelectedValue());
		}
		if (!(getChoices().isEmpty())) {
			copy.setChoices(getChoices());
		}
		copy.setCurrentSelectedDate(getCurrentSelectedDate());
		return copy;
	}



	public void addChoiceIfValidAtSelectedDate(HyContextualInformation context) {
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

	@Override
	public void refreshChoiceList() {

		List<HyContextualInformation> contexts = getContextModel().getContextualInformations();
		getChoices().removeAll(getChoices());
		if (getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_BOOLEAN)) {
			for (HyContextualInformation context : contexts) {
				if (context instanceof HyContextualInformationBoolean) {
					addChoiceIfValidAtSelectedDate(context);
				}
			}
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_ENUM)) {
			for (HyContextualInformation context : contexts) {
				if (context instanceof HyContextualInformationEnum) {
					addChoiceIfValidAtSelectedDate(context);
				}
			}
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_NUMBER)) {
			for (HyContextualInformation context : contexts) {
				if (context instanceof HyContextualInformationNumber) {
					addChoiceIfValidAtSelectedDate(context);
				}
			}
		}
		
		super.refreshChoiceList();

	}

}
