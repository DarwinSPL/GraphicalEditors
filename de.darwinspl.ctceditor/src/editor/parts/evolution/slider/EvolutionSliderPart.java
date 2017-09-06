package editor.parts.evolution.slider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.GraphicalEditorModule;
import editor.model.evolutionslider.EvolutionSliderModel;
import editor.parts.choiceboxes.AbstractChoiceBoxPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.nodes.AbstractNodePart;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EvolutionSliderPart extends AbstractNodePart<HBox> {
	@Override
	public EvolutionSliderModel getContent() {

		return (EvolutionSliderModel) super.getContent();

	}

	// private final ChangeListener<Date> minDateSelectionListener = new
	// ChangeListener<Date>() {
	//
	// @Override
	// public void changed(ObservableValue<? extends Date> arg0, Date oldValue,
	// Date newValue) {
	// getContent().setMinSelectedDate(newValue);
	// setVisibilityOfContentViewerBlocks();
	// }
	//
	// };

	private final ChangeListener<Date> currentSelectedDateListener = new ChangeListener<Date>() {

		@Override
		public void changed(ObservableValue<? extends Date> observable, Date oldValue, Date newValue) {
			
			getContent().setCurrentSelectedDate(newValue);
			setVisibilityOfContentViewerBlocks();
		}
	};

	// private final ChangeListener<Date> maxDateSelectionListener = new
	// ChangeListener<Date>() {
	//
	// @Override
	// public void changed(ObservableValue<? extends Date> arg0, Date oldValue,
	// Date newValue) {
	//
	// if(newValue.before(getContent().getMinSelectedDate())){
	// Alert alert = new Alert(AlertType.WARNING);
	// alert.setTitle("No valid selection");
	// alert.setContentText("Not valid to select date smaller then the minimum
	// date");
	// alert.show();
	// return;
	// }
	// getContent().setMaxSelectedDate(newValue);
	// setVisibilityOfContentViewerBlocks();
	// }
	//
	// };

	private void setVisibilityOfContentViewerBlocks() {
		Map<Object, IContentPart<? extends Node>> contents = getContentViewer().getContentPartMap();
		contents.putAll(getPaletteViewerFeatures().getContentPartMap());
		contents.putAll(getPaletteViewerContexts().getContentPartMap());
		Collection<IContentPart<? extends Node>> collection = contents.values();

		for (IContentPart<? extends Node> part : collection) {
			if (part instanceof ControlBlockPart) {
				if (((ControlBlockPart) part).getContent().getParentBlock() == null) {

					boolean isValid = checkIfBlockIsValidAtCurrentSelectedTime((ControlBlockPart) part);

					if (isValid) {
						((ControlBlockPart) part).getVisual().setVisible(true);

						List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
						linked = getAnchoredsRecursive(linked, part);

						for (IContentPart<? extends Node> link : linked) {
							link.getVisual().setVisible(true);
						}
						part.refreshVisual();

					}
					if (!isValid) {
						((ControlBlockPart) part).getVisual().setVisible(false);

						List<IContentPart<? extends Node>> linked = new ArrayList<IContentPart<? extends Node>>();
						linked = getAnchoredsRecursive(linked, part);

						for (IContentPart<? extends Node> link : linked) {
							link.getVisual().setVisible(false);
						}
						part.refreshVisual();
					}

				}

			}
			
			
			
			if (part instanceof AbstractChoiceBoxPart<?>) {
				((AbstractChoiceBoxPart<?>) part).getContent()
						.setCurrentSelectedDate(getContent().getCurrentSelectedDate());
				((AbstractChoiceBoxPart<?>) part).getContent().refreshChoiceList();
				part.refreshVisual();
			}
			if(part instanceof ControlBlockPart){
				((ControlBlockPart) part).getContent().setCurrentSelectedDate(getContent().getCurrentSelectedDate());
			}
		}
	}

	protected boolean checkIfBlockIsValidAtCurrentSelectedTime(ControlBlockPart part) {
		Date validSince = ((ControlBlockPart) part).getContent().getValidSince();
		Date validUntil = ((ControlBlockPart) part).getContent().getValidUntil();
		Date currentDate = getContent().getCurrentSelectedDate();

		if (validSince != null) {

			if (!(validSince.after(getContent().getCurrentSelectedDate()))) {

				if (validUntil != null) {

					if (!(validUntil.before(currentDate))) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}

			} else {
				return false;
			}
		} else {
			if (validUntil != null) {

				if (!(validUntil.before(getContent().getCurrentSelectedDate()))) {
					return true;

				} else {
					return false;
				}

			} else {
				return true;
			}
		}
	}

	protected IViewer getContentViewer() {

		return getRoot().getViewer().getDomain().getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
	}
	
	protected IViewer getPaletteViewerFeatures() {

		return getRoot().getViewer().getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_FEATURES));
	}
	
	protected IViewer getPaletteViewerContexts() {

		return getRoot().getViewer().getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_CONTEXTS));
	}


	@Override
	protected HBox doCreateVisual() {

		HBox hox = new HBox();
		

		List<Date> dates = getContent().getDates();

		ChoiceBox<Date> currentDate = new ChoiceBox<Date>();

		currentDate.getItems().addAll((Collection<? extends Date>) dates);
		currentDate.getSelectionModel().selectedItemProperty().addListener(currentSelectedDateListener);

		if (getContent().getCurrentSelectedDate() != null) {
			currentDate.setValue(getContent().getCurrentSelectedDate());
		} else {
			if (!(dates.isEmpty())) {
				currentDate.setValue(dates.get(0));

				// getContent().setMinSelectedDate(dates.get(0));
				// getContent().setMaxSelectedDate(dates.get(dates.size() - 1));

			}
		}

		// ChoiceBox<Date> choicesMinDate = new ChoiceBox<>();
		//
		//
		// choicesMinDate.getItems().addAll((Collection<? extends Date>) dates);
		//
		//
		//
		// choicesMinDate.getSelectionModel().selectedItemProperty().addListener(minDateSelectionListener);
		//
		// ChoiceBox<Date> choicesMaxDate = new ChoiceBox<>();
		//
		// choicesMaxDate.getItems().addAll((Collection<? extends Date>) dates);

		// if(!(dates.isEmpty())){
		// choicesMinDate.setValue(dates.get(0));
		// getContent().setMinSelectedDate(dates.get(0));
		// getContent().setMaxSelectedDate(dates.get(dates.size() - 1));

		// choicesMaxDate.setValue(dates.get(dates.size() - 1));
		// }
		//
		// choicesMaxDate.getSelectionModel().selectedItemProperty().addListener(maxDateSelectionListener);

		Slider slider = new Slider(0, (dates.size() - 1), 0);


		// javafx.scene.image.Image image = new
		// javafx.scene.image.Image("/icons/full/elcl16/synced.gif");
		//
		//
		// Button dependOnFeatureModel = new Button(null, new ImageView(image));
		// dependOnFeatureModel.setStyle("-fx-background-image:
		// url('/icons/full/elcl16/synced.gif')");
		//
		//
		//
		// ISharedImages imgDesc = PlatformUI.getWorkbench().getSharedImages();
		// org.eclipse.swt.graphics.Image image2 =
		// imgDesc.getImage(SharedImages.IMG_DEC_FIELD_ERROR);

		slider.setMajorTickUnit(1);
		// slider.setMinorTickCount(0);
		slider.valueProperty().addListener((obs, oldval, newVal) -> slider.setValue(Math.round(newVal.doubleValue())));
		slider.setMajorTickUnit(1);
		// slider.setShowTickMarks(true);
		// slider.setShowTickLabels(true);
		slider.setSnapToTicks(true);

		// TextField widthSliderText = new TextField();
		// widthSliderText.textProperty()
		// .bind(slider.valueProperty().asString());
		// widthSliderText.setPrefWidth(50);

		hox.setStyle("-fx-background-color: white;");
		// hBox.getChildren().addAll(new Text("Width: "), evolutionSlider,
		// widthSliderText);
		hox.getChildren().addAll(new Text("Current Date:  "), currentDate);
		hox.setAlignment(Pos.CENTER);
		hox.setMaxWidth(Double.MAX_VALUE);

		hox.setMaxHeight(Double.MAX_VALUE);

		VBox.setVgrow(hox, Priority.NEVER);
		HBox.setHgrow(slider, Priority.NEVER);
		return hox;
	}

}
