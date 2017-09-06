package editor.model.control;

import java.util.Date;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ControlBlockModel extends GeometricShape {

	public enum ControlValidateOperatorBlockType {
		VALIDATE, VALIDATE_NOT
	}

	public static final String VALID_SINCE_PROPERTY = "valid_since_property";
	public static final String VALID_UNITL_PROPERTY = "valid_until_property";

	private final ObjectProperty<Date> validSinceProperty = new SimpleObjectProperty<>(this, VALID_SINCE_PROPERTY);
	private final ObjectProperty<Date> validUntilProperty = new SimpleObjectProperty<>(this, VALID_UNITL_PROPERTY);

	public ControlBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect) {
		super(shape, transform, fill, effect);
	}

	public ObjectProperty<Date> getValidSinceProperty() {
		return validSinceProperty;
	}

	public void setValidSince(Date date) {
		validSinceProperty.set(date);
	}
	
	public void setValidSinceNull(){
		validSinceProperty.set(null);
	}

	public Date getValidSince() {
		return validSinceProperty.get();
	}

	public ObjectProperty<Date> getValidUntilProperty() {
		return validUntilProperty;
	}

	public Date getValidUntil() {
		return validUntilProperty.get();
	}

	public void setValidUntil(Date date) {
		validUntilProperty.set(date);
	}

	@Override
	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {
	
	

		if (parentBlock instanceof ControlIfBlockModel) {
			parentBlockProperty.set(parentBlock);
			((ControlIfBlockModel) parentBlock).addChildBlock(this);
		}
	}

	@Override
	public void removeParentBlock() {

		if (getParentBlock() != null) {
			((ControlIfBlockModel) getParentBlock()).removeChildBlock(this);
			parentBlockProperty.set(null);

		}

	}

	@Override
	public ControlIfBlockModel getParentBlock() {
		
		return (ControlIfBlockModel) super.getParentBlock();
	}

}
