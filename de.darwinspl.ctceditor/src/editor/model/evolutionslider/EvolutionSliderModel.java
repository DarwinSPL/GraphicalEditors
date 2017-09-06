package editor.model.evolutionslider;

import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;

import editor.model.AbstractBlockElement;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class EvolutionSliderModel extends AbstractBlockElement {

	public final static String DATES_SLIDER_PROPERTY = "dates_slider_property";
	public final static String MIN_SELECTED_DATE_PROPERTY = "min_selected_date_property";
	public final static String MAX_SELECTED_DATE_PROPERTY = "max_selected_date_property";
	public final static String CURRENT_SELECTED_DATE_PROPERTY = "current_selected_date_property";

	private final ObjectProperty<List<Date>> datesSliderProperty = new SimpleObjectProperty<>(this,
			DATES_SLIDER_PROPERTY);
	private final ObjectProperty<Date> minSelectedDateProperty = new SimpleObjectProperty<>(this,
			MIN_SELECTED_DATE_PROPERTY);
	private final ObjectProperty<Date> maxSelectedDateProperty = new SimpleObjectProperty<>(this,
			MAX_SELECTED_DATE_PROPERTY);
	private final ObjectProperty<Date> currentSelectedDateProperty = new SimpleObjectProperty<>(this,
			CURRENT_SELECTED_DATE_PROPERTY);

	public EvolutionSliderModel(IFile file, List<Date> dates) {

		if (file != null) {
			setFeatureModelFile(file);
		}
		setDatesSlider(dates);
	}
	
	public EvolutionSliderModel(List<Date> dates) {
		setDatesSlider(dates);
	}


	public ObjectProperty<List<Date>> getDatesSliderProperty() {
		return datesSliderProperty;
	}

	public void setDatesSlider(List<Date> dates) {
		datesSliderProperty.set(dates);
	}

	public List<Date> getDates() {
		return datesSliderProperty.get();
	}

	public ObjectProperty<Date> getMinSelectedDateProperty() {
		return minSelectedDateProperty;
	}

	public void setMinSelectedDate(Date date) {
		minSelectedDateProperty.set(date);
	}

	public Date getMinSelectedDate() {
		return minSelectedDateProperty.get();
	}

	public ObjectProperty<Date> getMaxSelectedDateProperty() {
		return maxSelectedDateProperty;
	}

	public void setMaxSelectedDate(Date date) {
		maxSelectedDateProperty.set(date);
	}

	public Date getMaxSelectedDate() {
		return maxSelectedDateProperty.get();
	}

	public ObjectProperty<Date> getCurrentSelectedDateProperty() {
		return currentSelectedDateProperty;
	}

	public void setCurrentSelectedDate(Date date) {
		currentSelectedDateProperty.set(date);
	}

	public Date getCurrentSelectedDate() {
		return currentSelectedDateProperty.get();
	}
}
