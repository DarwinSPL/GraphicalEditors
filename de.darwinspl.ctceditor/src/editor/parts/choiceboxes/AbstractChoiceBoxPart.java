package editor.parts.choiceboxes;

import java.util.Date;

import org.eclipse.gef.common.collections.ObservableMultiset;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.choiceboxes.ChoiceBoxModel;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.nodes.AbstractNodePart;
import editor.parts.operator.OperatorMovableBlockPart;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.evolution.HyName;
import eu.hyvar.feature.HyFeatureAttribute;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

public class AbstractChoiceBoxPart<G> extends AbstractNodePart<ChoiceBox<G>> {

	protected static String styleFeature;
	protected static String styleContext;
	protected static String styleValue;
			
	
	private final ChangeListener<G> featureSelectionObserver = new ChangeListener<G>() {

		@Override
		public void changed(ObservableValue<? extends G> observable, G oldValue, G newValue) {
			
			if(newValue!=null){
			getContent().setSelectedValue(newValue);
			}
			if(getContent() instanceof ChoiceBoxValueModel){
				
				if((oldValue instanceof HyFeatureAttribute) && !(newValue instanceof HyFeatureAttribute)){
					getVisual().getStylesheets().remove(styleFeature);
				}
				if((oldValue instanceof HyContextualInformation) && !(newValue instanceof HyContextualInformation)){
					getVisual().getStylesheets().remove(styleContext);
				}
				
				if(!(oldValue instanceof HyFeatureAttribute) && newValue instanceof HyFeatureAttribute){
					
			
					getVisual().getStylesheets().addAll(styleFeature);
					
				} 
				if(!(oldValue instanceof HyContextualInformation) && newValue instanceof HyContextualInformation){
					
					getVisual().getStylesheets().addAll(styleContext);
					
				}
				
			}
			refreshVisual();

		}
	};
	
	protected boolean isNameValid(HyName context, Date date) {
		if (date != null) {
			if (context.getValidSince() != null) {
				if (context.getValidSince().before(date)
						|| context.getValidSince().equals(date)) {
					if (context.getValidUntil() != null) {
						if (!(context.getValidUntil().before(date))) {
							return true;
						}
					} else {
						return true;
					}
				} else {
					// nothing
				}
			} else {
				return true;
			}

		} else {

			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChoiceBoxModel<G> getContent() {
		return (ChoiceBoxModel<G>) super.getContent();
	}

	@Override
	protected ChoiceBox<G> doCreateVisual() {

		ChoiceBox<G> choiceBox = new ChoiceBox<G>();


		choiceBox.setMaxHeight(30);
		choiceBox.setMinHeight(30);
		choiceBox.setMaxWidth(130);
		choiceBox.setMinWidth(130);
		
		styleFeature = getClass().getResource("/resources/css/choicebox.css").toExternalForm();
		styleValue = getClass().getResource("/resources/css/choiceboxValue.css").toExternalForm();
		styleContext = getClass().getResource("/resources/css/choiceboxContext.css").toExternalForm();

		choiceBox.getSelectionModel().selectedItemProperty().addListener(featureSelectionObserver);
		
		return choiceBox;
	}

	@Override
	protected void doRefreshVisual(ChoiceBox<G> visual) {
		ChoiceBoxModel<G> content = getContent();

	
		if (content.getChoices() != null) {

			if (!(visual.getItems().equals(content.getChoices()))) {

			
				visual.setValue(null);
	
				visual.getItems().clear();
			
				for (G choice : content.getChoices()) {
					if(!visual.getItems().contains(choice)){
					visual.getItems().add(choice);
					}
				}
		

			}

		}
		if (content.getSelectedValue() != visual.getSelectionModel().getSelectedItem()) {
			
			visual.setValue(content.getSelectedValue());
		
			

		}


	
		super.doRefreshVisual(visual);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAttachToContentAnchorage(Object contentAnchorage, String role) {
		if (!(contentAnchorage instanceof AbstractGeometricElement)) {
			throw new IllegalArgumentException("Cannot attach to content anchorage: wrong type!");
		}
		if (role.equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
			getContent().setParentBlock((AbstractGeometricElement<? extends IGeometry>) contentAnchorage);
			return;
		}

		getContent().getAnchorages().add((AbstractGeometricElement<?>) contentAnchorage);
	}

	@Override
	protected SetMultimap<? extends Object, String> doGetContentAnchorages() {
		SetMultimap<Object, String> anchorages = HashMultimap.create();
		for (AbstractGeometricElement<? extends IGeometry> anchorage : getContent().getAnchorages()) {
			anchorages.put(anchorage, "link");
		}
		if (getContent().getParentBlock() != null) {
			anchorages.put(getContent().getParentBlock(), AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
		}

		return anchorages;
	}

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {

		if (role.equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {
			getContent().removeParentBlock();

			return;
		}

		getContent().getAnchorages().remove(contentAnchorage);

	}

	@Override
	public void setContent(Object model) {
		if (model != null && !(model instanceof ChoiceBoxModel)) {
			throw new IllegalArgumentException("Only IShape models are supported.");
		}
		super.setContent(model);
	}

	@Override
	public Dimension getContentSize() {
		return getContent().getDimension();
	}

	@Override
	public void setContentSize(Dimension totalSize) {
		getContent().setDimension(totalSize);

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

		if (anchorage instanceof ArithmeticalOperatorMovableBlockPart) {

			ArithmeticalOperatorMovableBlockPart p = (ArithmeticalOperatorMovableBlockPart) anchorage;

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

			if (p.getContent() instanceof MinMaxObjectReferenceBlockModel) {

				double nX = b11.getMaxX() - 140;
				double nY = b11.getMinY() + 2;

				getContent().getTransform().translate(nX - b22.getMaxX(), nY - b22.getMinY());

			

			} else {
				double nX = b11.getMinX() + 15;
				double nY = b11.getMinY() + 2;
			
				getContent().getTransform().translate(nX - b22.getMinX(), nY - b22.getMinY());
			}

			getVisual().toFront();
			refreshVisual();

		}
	}
}
