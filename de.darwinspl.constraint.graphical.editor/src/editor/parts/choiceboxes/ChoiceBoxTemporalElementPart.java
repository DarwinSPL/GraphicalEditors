package editor.parts.choiceboxes;

import editor.model.choiceboxes.ChoiceBoxContextModel;
import eu.hyvar.context.HyContextualInformation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class ChoiceBoxTemporalElementPart extends AbstractChoiceBoxPart<HyContextualInformation> {

	@Override
	public ChoiceBoxContextModel getContent() {
		return (ChoiceBoxContextModel) super.getContent();
	}

	public class MyConverter extends StringConverter<HyContextualInformation> {

		@Override
		public HyContextualInformation fromString(String string) {
		
			
			return null;
		}

		@Override
		public String toString(HyContextualInformation object) {
			return object.getName();

		}

	}

	@Override
	protected ChoiceBox<HyContextualInformation> doCreateVisual() {

		ChoiceBox<HyContextualInformation> choiceBox = super.doCreateVisual();

		MyConverter myConverter = new MyConverter();
		choiceBox.setConverter(myConverter);


		choiceBox.getStylesheets().addAll(styleContext);

		choiceBox.setTooltip(new Tooltip("Select Context"));
		

		return choiceBox;
	}


}
