package editor.parts.choiceboxes;

import java.util.Date;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxModel;
import editor.parts.control.ControlBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import eu.hyvar.evolution.HyName;
import eu.hyvar.feature.HyFeature;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class ChoiceBoxFeaturePart extends AbstractChoiceBoxPart<HyFeature> {
	
	@Override
	public ChoiceBoxFeatureModel getContent() {
		return (ChoiceBoxFeatureModel) super.getContent();
	}

	public class MyConverter extends StringConverter<HyFeature> {

		@Override
		public HyFeature fromString(String string) {
			for (HyFeature feature : getContent().getChoices()) {
				if (feature.getNames().get(0).getName().equals(string)) {
					
					return feature;
				}

			}
			return null;
		}

		@Override
		public String toString(HyFeature object) {
			Date currentDate = getContent().getCurrentSelectedDate();
			String fname;
			for (HyName name : object.getNames()) {
				if(isNameValid(name, currentDate)){
					fname = name.getName();
					return fname;
				}
			

			}
			return object.getNames().get(0).getName();
			
			
		

		}

	}
	
	

	@Override
	protected ChoiceBox<HyFeature> doCreateVisual() {

		ChoiceBox<HyFeature> choiceBox = super.doCreateVisual();

		MyConverter myConverter = new MyConverter();
		choiceBox.setConverter(myConverter);

		choiceBox.setTooltip(new Tooltip("Select Feature"));


		choiceBox.getStylesheets().addAll(styleFeature);
	
		return choiceBox;
	}

//	@Override
//	protected void doRefreshVisual(ChoiceBox<HyFeature> visual) {
//		ChoiceBoxFeatureModel content = getContent();
//
//		if (content.getChoices() != null) {
//
//			if (!(visual.getItems().equals(content.getChoices()))) {
//
//				for (HyFeature choice : content.getChoices()) {
//					visual.getItems().add(choice);
//
//				}
//			}
//
//		}
//
//		if (content.getSelectedValue() != visual.getSelectionModel().getSelectedItem()) {
//			
//			visual.setValue(content.getSelectedValue());
//			
//
//		}
//
//		// AffineTransform transform = content.getTransform();
//		// if (transform != null) {
//		// setVisualTransform(Geometry2FX.toFXAffine(transform));
//		// }
//
//		super.doRefreshVisual(visual);
//	}



	@Override
	public void setContent(Object model) {
		if (model != null && !(model instanceof ChoiceBoxModel)) {
			throw new IllegalArgumentException("Only IShape models are supported.");
		}
		super.setContent(model);
	}

	/**
	 * Attaches this part's visual to the visual of the given anchorage.
	 *
	 * @param anchorage
	 *            The anchorage {@link IVisualPart}.
	 * @param role
	 *            The anchorage role.
	 */
	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {

		ObservableMultiset<IVisualPart<? extends Node>> allAn = anchorage.getAnchoredsUnmodifiable();

		if (anchorage instanceof OperatorMovableBlockPart) {

			
			OperatorMovableBlockPart p = (OperatorMovableBlockPart) anchorage;

			if (p.getVisual().getParent() != null) {
				if (!getVisual().getParent().equals(p.getVisual().getParent())) {

					Group parenttthis = (Group) getVisual().getParent();
					Group parentparent = (Group) p.getVisual().getParent();

					parenttthis.getChildren().remove(getVisual());
					parentparent.getChildren().add(getVisual());

				}
			}
			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			double nX = b11.getMinX() + 15;
			double nY = b11.getMinY() + 2;

			getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			getVisual().toFront();
			refreshVisual();

		}
		
		/**
		 * Is used for VF Block
		 */
           if (anchorage instanceof ControlBlockPart) {

			
			ControlBlockPart p = (ControlBlockPart) anchorage;

			if (p.getVisual().getParent() != null) {
				if (!getVisual().getParent().equals(p.getVisual().getParent())) {

					Group parenttthis = (Group) getVisual().getParent();
					Group parentparent = (Group) p.getVisual().getParent();

					parenttthis.getChildren().remove(getVisual());
					parentparent.getChildren().add(getVisual());

				}
			}
			Bounds b11 = p.getVisual().getBoundsInParent();
			Bounds b22 = getVisual().getBoundsInParent();

			double nX = b11.getMinX() + 15;
			double nY = b11.getMinY() + 2;

			getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());

			getVisual().toFront();
			refreshVisual();

		}
	}

}
