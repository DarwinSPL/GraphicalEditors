package editor.model.choiceboxes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.NumericalObjectReferenceOperator;
import editor.model.operator.OperatorComparisonFeatureAttributeBlockModel;
import editor.model.operator.OperatorWithChoiceBoxModel;
import editor.model.vf.VFControlBlockModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ChoiceBoxModel<G> extends AbstractBlockElement {

	private boolean isChoiceInvalid = false;
	
	public enum ChoiceBoxType {
		CONTEXT_BOOLEAN, CONTEXT_ENUM, CONTEXT_NUMBER, VALUE_BOOLEAN, VALUE_NUMBER, VALUE_ENUM, FEATURE, ATTRIBUTE_BOOLEAN, ATTRIBUTE_ENUM, ATTRIBUTE_NUMBER
	}



	public static final String SELECTED_VALUE_PROPERTY = "selected_value_property";

	private final ObjectProperty<G> selectedFeatureProperty = new SimpleObjectProperty<>(this, SELECTED_VALUE_PROPERTY);

	private final ChoiceBoxType choiceBoxType;
	// private final ValueType valueType;

	public ChoiceBoxType getChoiceBoxType() {
		return choiceBoxType;
	}

	private final Set<AbstractGeometricElement<? extends IGeometry>> anchorages = new HashSet<>();

	private List<G> choices = new ArrayList<>();

	public ChoiceBoxModel(AffineTransform transform, List<G> choices, ChoiceBoxType type) {

		setTransform(transform);
		if (choices != null) {
			this.choices = choices;

			if (!(choices.isEmpty())) {
				if(getSelectedValue()==null){
				setSelectedValue(choices.get(0));
				}
			}
		}

		this.choiceBoxType = type;
		setFill(null);

	}
	
	
	

	public ChoiceBoxModel(AffineTransform transform, HyContextModel contextModel, HyFeatureModel featureModel,
			ChoiceBoxType type) {
		setTransform(transform);
		setFeatureModel(featureModel);
		setContextModel(contextModel);
		this.choiceBoxType = type;

	}

	public void addAnchorage(AbstractGeometricElement<? extends IGeometry> anchorage) {
		this.anchorages.add(anchorage);

	}

	public Set<AbstractGeometricElement<? extends IGeometry>> getAnchorages() {
		return anchorages;
	}

	public List<G> getChoices() {
		return choices;
	}

	public void addChoice(G choice) {
		getChoices().add(choice);

	}

	public void setChoices(List<G> choices) {
		this.choices = choices;
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parent) {

		parentBlockProperty.set(parent);

		
		if(parent instanceof NumericalObjectReferenceOperator){
			((NumericalObjectReferenceOperator) parent).setContextChoiceBox((ChoiceBoxModel<?>) this);
			return;
		}

		
		if(parent instanceof VFControlBlockModel){
			((VFControlBlockModel) parent).setChoiceBoxContextChild(this);
		}
	
		
		if (parent instanceof OperatorComparisonFeatureAttributeBlockModel
				&& getChoiceBoxType().equals(ChoiceBoxType.FEATURE)) {
			((OperatorComparisonFeatureAttributeBlockModel) parent)
					.setChoiceBoxFeature((ChoiceBoxModel<HyFeature>) this);
			return;
		}

		if (getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_BOOLEAN)
				|| getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_ENUM)
				|| getChoiceBoxType().equals(ChoiceBoxType.CONTEXT_NUMBER)
				|| getChoiceBoxType().equals(ChoiceBoxType.FEATURE)
				|| getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_BOOLEAN)
				|| getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_ENUM)
				|| getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_NUMBER)) {
			if (parent instanceof OperatorWithChoiceBoxModel) {
				((OperatorWithChoiceBoxModel) parent).setChoiceBoxContextChild(this);
			}
		
			
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.VALUE_BOOLEAN)
				|| getChoiceBoxType().equals(ChoiceBoxType.VALUE_ENUM)
				|| getChoiceBoxType().equals(ChoiceBoxType.VALUE_NUMBER)) {
			if (parent instanceof OperatorWithChoiceBoxModel) {
				((OperatorWithChoiceBoxModel) parent).setChoiceBoxValueChild(this);
			}
			
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.ATTRIBUTE_BOOLEAN)) {
			if (parent instanceof OperatorComparisonFeatureAttributeBlockModel) {
				((OperatorComparisonFeatureAttributeBlockModel) parent).setChoiceBoxContextChild(this);
			}
		}

	

	}

	public ObjectProperty<G> getSelectedValueProperty() {
		return selectedFeatureProperty;
	}

	public void setSelectedValue(G feature) {
		selectedFeatureProperty.set(feature);
	}

	public G getSelectedValue() {
		return selectedFeatureProperty.get();
	}

	public ChoiceBoxModel<G> getCopy() {

		ChoiceBoxModel<G> copy = new ChoiceBoxModel<>(getTransform().getCopy(), getChoices(), getChoiceBoxType());
		if (getSelectedValue() != null) {
			copy.setSelectedValue(getSelectedValue());
		}
		return copy;
	}

	public void refreshChoiceList() {
		if(getSelectedValue()!=null){
			if(!getChoices().contains(getSelectedValue())){
			
				addChoice(getSelectedValue());
			} else {
				
			}
		}

	}


	
	@Override
	public void refreshContents(HyFeatureModel featureModel, HyContextModel contextModel) {
		super.refreshContents(featureModel, contextModel);
	
		G selectedValue = getSelectedValue();
		
		refreshChoiceList();
		setSelectedValue(selectedValue);
	
	}




	public boolean isChoiceInvalid() {
		return isChoiceInvalid;
	}




	public void setChoiceInvalid(boolean isChoiceInvalid) {
		this.isChoiceInvalid = isChoiceInvalid;
	}
	
	

}
