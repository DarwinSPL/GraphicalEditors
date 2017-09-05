package de.darwinspl.ctceditor.properties;

import org.eclipse.gef.mvc.fx.ui.properties.FXPaintPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import editor.model.GeometricShape;
import editor.model.choiceboxes.ChoiceBoxModel;
import javafx.scene.paint.Paint;

public class ChoiceBoxModelPropertySource implements IPropertySource {

	private static final IPropertyDescriptor FILL_PROPERTY_DESCRIPTOR = new FXPaintPropertyDescriptor(
			GeometricShape.FILL_PROPERTY, "Fill");

	private ChoiceBoxModel shape;

	public ChoiceBoxModelPropertySource(ChoiceBoxModel shape) {
		this.shape = shape;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] { FILL_PROPERTY_DESCRIPTOR };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (FILL_PROPERTY_DESCRIPTOR.getId().equals(id)) {
			return shape.getFill();
		} else {
			return null;
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		if (FILL_PROPERTY_DESCRIPTOR.getId().equals(id)) {
			return shape.getFill() != null;
		} else {
			return false;
		}
	}

	@Override
	public void resetPropertyValue(Object id) {
		if (FILL_PROPERTY_DESCRIPTOR.getId().equals(id)) {
			shape.setFill(null);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (FILL_PROPERTY_DESCRIPTOR.getId().equals(id)) {
			shape.setFill((Paint) value);
		}
	}

}

