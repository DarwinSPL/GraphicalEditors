package editor.parts.context;

import java.util.Optional;

import editor.parts.nodes.AbstractNodePart;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class CreateNewContext extends AbstractNodePart<Button> {

	@Override
	protected Button doCreateVisual() {
		// TODO Auto-generated method stub
		Button button = new Button("Create new Context");
		button.setOnAction(myEventHandler);

		String style = getClass().getResource("/resources/css/createNewContextButton.css").toExternalForm();
		button.getStylesheets().addAll(style);
		return button;
	}

	EventHandler<ActionEvent> myEventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {

			Dialog<Pair<DatePicker, DatePicker>> dialog = new Dialog<>();
			dialog.setTitle("New Context");

			VBox box = new VBox();

			dialog.setHeaderText("Create new Contextual Information.\nSelect type.");
			Text typeLabel = new Text("Type: ");
			ChoiceBox<String> choices = new ChoiceBox<>();
			choices.getItems().add("Enumeration");
			choices.getItems().add("Numerical");
			choices.getItems().add("Boolean");

			GridPane grid = new GridPane();

			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			AnchorPane.setTopAnchor(grid, 0d);
			AnchorPane.setLeftAnchor(grid, 0d);
			AnchorPane.setRightAnchor(grid, 0d);
			grid.add(typeLabel, 0, 0);
			grid.add(choices, 1, 0);

			grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			Label nameL = new Label("Name*:");
			Label minL = new Label("Minimum Value*:");
			Label maxL = new Label("Maximum Value*:");
			Label sinceLabel = new Label("Valid Since:");
			Label untilLabel = new Label("Valid Until:");

			DatePicker datePickerSince = new DatePicker();

			DatePicker datePickerUntil = new DatePicker();

			datePickerSince.setMinWidth(170);

			GridPane gridN = new GridPane();

			gridN.setHgap(10);
			gridN.setVgap(10);
			gridN.setPadding(new Insets(20, 150, 10, 10));

			AnchorPane.setTopAnchor(gridN, 0d);
			AnchorPane.setLeftAnchor(gridN, 0d);
			AnchorPane.setRightAnchor(gridN, 0d);
			gridN.add(nameL, 0, 0);
			gridN.add(new TextField(), 1, 0);
			gridN.add(minL, 0, 1);
			gridN.add(new TextField(), 1, 1);
			gridN.add(maxL, 0, 3);
			gridN.add(new TextField(), 1, 3);
			gridN.add(sinceLabel, 0, 4);
			gridN.add(datePickerSince, 1, 4);

			gridN.add(untilLabel, 0, 5);
			gridN.add(datePickerUntil, 1, 5);

			gridN.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			// gridN.setDisable(true);

			GridPane gridB = new GridPane();

			gridB.setHgap(10);
			gridB.setVgap(10);
			gridB.setPadding(new Insets(20, 150, 10, 10));

			AnchorPane.setTopAnchor(gridB, 0d);
			AnchorPane.setLeftAnchor(gridB, 0d);
			AnchorPane.setRightAnchor(gridB, 0d);

			// gridB.add(nameL, 0, 0);
			// gridB.add(new TextField(), 1, 0);
			//
			// gridB.add(sinceLabel, 0, 2);
			// gridB.add(datePickerSince, 1, 2);
			//
			// gridB.add(untilLabel, 0, 2);
			// gridB.add(datePickerUntil, 1, 2);

			gridB.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			ButtonType buttontypeSave = new ButtonType("Finish", ButtonData.FINISH);
			// buttontypeSave.

			// ButtonType nextButton = new ButtonType("Next",
			// ButtonData.NEXT_FORWARD);
			dialog.getDialogPane().getButtonTypes().add(buttontypeSave);
			// dialog.getDialogPane().getButtonTypes().add(nextButton);

			box.getChildren().addAll(grid, gridN);

			ButtonType buttontypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dialog.getDialogPane().getButtonTypes().add(buttontypeCancel);

			dialog.getDialogPane().setContent(box);

			// dialog.setResultConverter(dialogButton -> {
			// if (dialogButton == buttontypeSave) {
			// return new Pair<>(datePickerSince, datePickerUntil);
			// }
			// return null;
			// });

			Optional<Pair<DatePicker, DatePicker>> result = dialog.showAndWait();
			//
			// result.ifPresent(dates -> {
			//
			// if (dates.getKey().getValue() != null) {
			//
			// Date sinceDate = Date.from(dates.getKey().getValue()
			// .atStartOfDay(ZoneId.systemDefault()).toInstant());
			// ((ControlBlockPart)
			// targetBlock).getContent().setValidSince(sinceDate);
			// }
			// if (dates.getValue().getValue() != null) {
			// Date untilDate = Date.from(dates.getValue().getValue()
			// .atStartOfDay(ZoneId.systemDefault()).toInstant());
			// ((ControlBlockPart)
			// targetBlock).getContent().setValidUntil(untilDate);
			// }
			//
			// });

		}
	};

}
