package editor.parts.choiceboxes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.operator.OperatorWithChoiceBoxModel;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.dataValues.HyEnumLiteral;
import eu.hyvar.evolution.HyName;
import eu.hyvar.feature.HyFeatureAttribute;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class ChoiceBoxValuePart extends AbstractChoiceBoxPart<Object> {

	@Override
	public ChoiceBoxValueModel getContent() {
		
		return (ChoiceBoxValueModel) super.getContent();
	}

	private final ChangeListener<HyContextualInformation> selectedContextListener = new ChangeListener<HyContextualInformation>() {

		@Override
		public void changed(ObservableValue<? extends HyContextualInformation> observable,
				HyContextualInformation oldValue, HyContextualInformation newValue) {
		

			getContent().setParentChoiceBoxSelection(newValue);
			refreshVisual();

		}
	};

	public class MyConverter extends StringConverter<Object> {

		@Override
		public Object fromString(String string) {
			
			return null;
		}

		@Override
		public String toString(Object object) {
			if (object instanceof HyContextualInformation) {
				return ((HyContextualInformation) object).getName();
			}
			if (object instanceof HyFeatureAttribute) {

				StringBuilder stringBuilder = new StringBuilder();

				String fName = null;

				if (getContent().getCurrentSelectedDate() != null) {
					Date currentDate = getContent().getCurrentSelectedDate();
					HyFeatureAttribute attribute = (HyFeatureAttribute) object;
					for (HyName name : attribute.getNames()) {
						if ((name.getValidSince() == null || !(name.getValidSince().after(currentDate)))
								&& (name.getValidUntil() == null || !(name.getValidUntil().before(currentDate)))) {
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
			if (object instanceof HyEnumLiteral) {
				return ((HyEnumLiteral) object).getName();
			} else {
				return (String) object;
			}

		}

	}

	@Override
	protected ChoiceBox<Object> doCreateVisual() {

		ChoiceBox<Object> choiceBox = super.doCreateVisual();

		MyConverter myConverter = new MyConverter();
		choiceBox.setConverter(myConverter);

	
		choiceBox.getStylesheets().addAll(styleValue);

		choiceBox.setTooltip(new Tooltip("Select Value"));

		if (getContent().getChoiceBoxType().equals(ChoiceBoxType.VALUE_ENUM)) {

			if (getContent().getParentBlock() instanceof OperatorWithChoiceBoxModel) {
				OperatorWithChoiceBoxModel op = (OperatorWithChoiceBoxModel) getContent().getParentBlock();

				if (((OperatorWithChoiceBoxModel) getContent().getParentBlock())
						.getChoiceBoxContextChild() instanceof ChoiceBoxContextModel) {
					((ChoiceBoxContextModel) ((OperatorWithChoiceBoxModel) getContent().getParentBlock())
							.getChoiceBoxContextChild()).getSelectedValueProperty()
									.addListener(selectedContextListener);
				}

			}
		}

		return choiceBox;
	}

	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {

		double distanceX = 0;
		double distanceY = 0;
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

			double nX = b11.getMinX() + 170;
			double nY = b11.getMinY() + 2;

			distanceX = nX - b22.getMinX();
			distanceY = nY - b22.getMinY();

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

		if (distanceX != 0 && distanceY != 0) {

			List<IContentPart<? extends Node>> linked = new ArrayList<>();
			linked = getAnchoredsRecursive(linked, this);

			for (IContentPart<? extends Node> anch : linked) {

				if (anch instanceof OperatorFixedBlockPart) {
					((OperatorFixedBlockPart) anch).getContent().getTransform().translate(distanceX, distanceY);

					anch.refreshVisual();
				}
			}
		}
	}

}
