package editor.model.choiceboxes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import editor.model.arithmetical.NumericalObjectReferenceOperator;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorWithChoiceBoxModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.context.HyContextualInformationBoolean;
import eu.hyvar.context.HyContextualInformationEnum;
import eu.hyvar.context.HyContextualInformationNumber;
import eu.hyvar.evolution.HyTemporalElement;
import eu.hyvar.feature.HyBooleanAttribute;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureAttribute;
import eu.hyvar.feature.HyFeatureModel;
import eu.hyvar.feature.HyNumberAttribute;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ChoiceBoxValueModel extends ChoiceBoxModel<Object> {

	public static final String PARENT_CHOICE_BOX_SELECTION_PROPERTY = "parent_choice_box_property";

	private final ObjectProperty<HyTemporalElement> parentChoiceBoxSelectionProperty = new SimpleObjectProperty<>(this,
			PARENT_CHOICE_BOX_SELECTION_PROPERTY);

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parent) {
		if(parent instanceof NumericalObjectReferenceOperator){
			((NumericalObjectReferenceOperator) parent).setContextChoiceBox(this);
			parentBlockProperty.set(parent);
		}
		if (parent instanceof OperatorComparisonBlockModel) {
			((OperatorComparisonBlockModel) parent).setChoiceBoxValueChild(this);
			parentBlockProperty.set(parent);
		}
	}

	@Override
	public GeometricShape getParentBlock() {

		if(super.getParentBlock() instanceof NumericalObjectReferenceOperator){
			return (NumericalObjectReferenceOperator) parentBlockProperty.get();
		} else{
		return (OperatorWithChoiceBoxModel) parentBlockProperty.get();
		}
	}
	
	@Override
	public void removeParentBlock() {
		if(super.getParentBlock() != null){
		if(super.getParentBlock() instanceof NumericalObjectReferenceOperator){
			((NumericalObjectReferenceOperator) super.getParentBlock()).setContextChoiceBox(null);
			parentBlockProperty.set(null);
		}
		if (getParentBlock() instanceof OperatorComparisonBlockModel) {
			((OperatorComparisonBlockModel) getParentBlock()).setChoiceBoxValueChild(null);
			parentBlockProperty.set(null);
		}
		}
	}

	public ChoiceBoxValueModel(AffineTransform transform, HyContextModel contextModel, HyFeatureModel featureModel,
			ChoiceBoxType type) {
		super(transform, contextModel, featureModel, type);
		refreshChoiceList();
	}

	@Override
	public void refreshChoiceList() {

		getChoices().removeAll(getChoices());
		List<HyContextualInformation> contexts = getContextModel().getContextualInformations();
		List<HyFeature> features = getFeatureModel().getFeatures();
		List<HyFeatureAttribute> attributes = new ArrayList<HyFeatureAttribute>();
		for (HyFeature feature : features) {
			attributes.addAll(feature.getAttributes());
		}
		if (getChoiceBoxType().equals(ChoiceBoxType.VALUE_NUMBER)) {
			for (HyContextualInformation context : contexts) {
				if (context instanceof HyContextualInformationNumber) {
					addChoiceIfValidAtSelectedDate(context);

				}
			}
			
			for (HyFeatureAttribute attribute : attributes) {
				if (attribute instanceof HyNumberAttribute) {
					addChoiceIfValidAtSelectedDate(attribute);
				}
			}

		}
		if (getChoiceBoxType().equals(ChoiceBoxType.VALUE_BOOLEAN)) {
			addChoice("true");
			addChoice("false");

			for (HyContextualInformation context : contexts) {
				if (context instanceof HyContextualInformationBoolean) {
					addChoiceIfValidAtSelectedDate(context);
				}
			}
		
			for (HyFeatureAttribute attribute : attributes) {
				if (attribute instanceof HyBooleanAttribute) {
					addChoiceIfValidAtSelectedDate(attribute);
				}
			}

		}
		if (getChoiceBoxType().equals(ChoiceBoxType.VALUE_ENUM)) {

			if (getParentChoiceBoxSelection() != null) {
				if (getParentChoiceBoxSelection() instanceof HyContextualInformationEnum) {

					getChoices().addAll(
							((HyContextualInformationEnum) getParentChoiceBoxSelection()).getEnumType().getLiterals());
				}
			}

		}
		
		super.refreshChoiceList();
	}

	public void addChoiceIfValidAtSelectedDate(HyTemporalElement element) {
		if (getCurrentSelectedDate() != null) {
			if (element.getValidSince() != null) {
				if (element.getValidSince().before(getCurrentSelectedDate())
						|| element.getValidSince().equals(getCurrentSelectedDate())) {
					if (element.getValidUntil() != null) {
						if (!(element.getValidUntil().before(getCurrentSelectedDate()))) {
							addChoice(element);
						}
					} else {
						addChoice(element);
					}
				} else {
					// nothing
				}
			} else {
				addChoice(element);
			}

		} else {

			addChoice(element);
		}
	}

	@Override
	public ChoiceBoxValueModel getCopy() {
		ChoiceBoxValueModel copy = new ChoiceBoxValueModel(getTransform().getCopy(), getContextModel(),
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

	public ObjectProperty<? extends HyTemporalElement> getParentChoiceBoxSelectionProperty() {
		return parentChoiceBoxSelectionProperty;
	}

	public void setParentChoiceBoxSelection(HyContextualInformation choiceBoxModel) {
		parentChoiceBoxSelectionProperty.set(choiceBoxModel);
		refreshChoiceList();

	}

	public HyTemporalElement getParentChoiceBoxSelection() {
		return parentChoiceBoxSelectionProperty.get();
	}
}
