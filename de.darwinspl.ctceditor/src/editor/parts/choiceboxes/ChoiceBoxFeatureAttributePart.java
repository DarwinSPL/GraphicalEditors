package editor.parts.choiceboxes;

import java.util.Date;

import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import eu.hyvar.evolution.HyName;
import eu.hyvar.feature.HyFeatureAttribute;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class ChoiceBoxFeatureAttributePart extends AbstractChoiceBoxPart<HyFeatureAttribute> {

	public class MyConverter extends StringConverter<HyFeatureAttribute> {

		@Override
		public HyFeatureAttribute fromString(String string) {

			return null;
		}

		@Override
		public String toString(HyFeatureAttribute object) {

			StringBuilder stringBuilder = new StringBuilder();

			String fName = null;

			if (getContent().getCurrentSelectedDate() != null) {
				Date currentDate = getContent().getCurrentSelectedDate();
				HyFeatureAttribute attribute = (HyFeatureAttribute) object;
				for (HyName name : attribute.getNames()) {
					if(isNameValid(name, currentDate)){
						fName = name.getName();
						
					}
				

				}
		
			}
			String feature = ((HyFeatureAttribute) object).getFeature().getNames().get(0).getName();
			if (fName == null) {
				fName = ((HyFeatureAttribute) object).getNames().get(0).getName();
			}

			stringBuilder.append(fName);
			stringBuilder.append(" (");
			stringBuilder.append(feature + ") ");
			return stringBuilder.toString();

		}

	};

	


	@Override
	public ChoiceBoxFeatureAttributeModel getContent() {
		
		return (ChoiceBoxFeatureAttributeModel) super.getContent();
	}

	@Override
	protected ChoiceBox<HyFeatureAttribute> doCreateVisual() {

		ChoiceBox<HyFeatureAttribute> choiceBox = super.doCreateVisual();
		MyConverter converter = new MyConverter();
		choiceBox.setConverter(converter);

		choiceBox.setTooltip(new Tooltip("Select Feature Attribute"));

	
		choiceBox.getStylesheets().addAll(styleFeature);
	


		return choiceBox;
	}



}
