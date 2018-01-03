package editor.model.choiceboxes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;

import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureModel;

public class ChoiceBoxFeatureModel extends ChoiceBoxModel<HyFeature>{

	public ChoiceBoxFeatureModel(AffineTransform transform, HyContextModel contextModel, HyFeatureModel featureModel,
			editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType type) {
		super(transform, contextModel, featureModel, type);
		
		refreshChoiceList();
		
	}
	
	@Override
	public ChoiceBoxFeatureModel getCopy() {
		ChoiceBoxFeatureModel copy = new ChoiceBoxFeatureModel(getTransform().getCopy(), getContextModel(), getFeatureModel(), getChoiceBoxType());
		if (getSelectedValue() != null) {
			copy.setSelectedValue(getSelectedValue());
		}
		if(!getChoices().isEmpty()){
			copy.setChoices(getChoices());
		}
		copy.setCurrentSelectedDate(getCurrentSelectedDate());
	return copy;
	
	}
	

	
	public void addChoiceIfValidAtSelectedDate(HyFeature context) {
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
				if(context.getValidUntil()!=null){
					if (!(context.getValidUntil().before(getCurrentSelectedDate()))) {
						addChoice(context);
					}
				} else{
					addChoice(context);
				}
			}

		} else {

			addChoice(context);
		}
	}
	
	
	
	@Override
	public void refreshChoiceList() {
		List<HyFeature> list = new ArrayList<>();
		setChoices(list);
		//getChoices().removeAll(getChoices());
		for(HyFeature feature: getFeatureModel().getFeatures()){
			
			
			addChoiceIfValidAtSelectedDate(feature);
			
			
		}
		
		super.refreshChoiceList();
	}
	

}
