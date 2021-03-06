package editor.model.control;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.GeometricShape;
import editor.model.operator.OperatorAndBlockModel.OperatorAndORType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ControlAndOrBlock extends ControlBlockModel{
	
	public final static String SIZE_OPERAND1 = "size_operand1";
	
	public final static String SIZE_OPERAND2 = "size_operand2";
	
	public final static String CONTROL_BLOCK_OPERAND1 = "control_block_operand1";

	private final ObjectProperty<ControlBlockModel> controlBlockOperand1 = new SimpleObjectProperty<>(this,
			CONTROL_BLOCK_OPERAND1);
	
	public final ObjectProperty<Bounds> sizeOperand1 = new SimpleObjectProperty<>(this, SIZE_OPERAND1);
	
	public final ObjectProperty<Bounds> sizeOperand2 = new SimpleObjectProperty<>(this, SIZE_OPERAND2);

	
	public final static String CONTROL_BLOCK_OPERAND2 = "control_block_operand2";

	private final ObjectProperty<ControlBlockModel> controlBlockOperand2 = new SimpleObjectProperty<>(this,
			CONTROL_BLOCK_OPERAND2);
	
//	public enum ControlAndORType {
//		AND, OR
//	}
//	
	private OperatorAndORType type;

	public OperatorAndORType getType() {
		return type;
	}

	public void setType(OperatorAndORType type) {
		this.type = type;
	}

	public ControlAndOrBlock(IShape shape, AffineTransform transform, Paint fill, Effect effect, OperatorAndORType type) {
		super(shape, transform, fill, effect);
		setType(type);
		
	}
	
	@Override
	public GeometricShape getCopy() {
		
		ControlAndOrBlock copy = new ControlAndOrBlock((IShape)getGeometry().getCopy(), getTransform().getCopy(), getFill(), getEffect(), getType());
		return copy;
	}

	public ControlBlockModel getControlBlockOperand1() {
		return controlBlockOperand1.get();
	}
	
	public void setControlBlockOperand1(ControlBlockModel model){
		controlBlockOperand1.set(model);
	}
	
	public void removeControlBlockOperand1(){
		controlBlockOperand1.set(null);
	}

	public ControlBlockModel getControlBlockOperand2() {
		return controlBlockOperand2.get();
	}
	
	public void setControlBlockOperand2(ControlBlockModel model){
		controlBlockOperand2.set(model);
	}
	
	public void removeControlBlockOperand2(){
		controlBlockOperand2.set(null);
	}

	public ObjectProperty<Bounds> getSizeOperand1() {
		return sizeOperand1;
	}

	public ObjectProperty<Bounds> getSizeOperand2() {
		return sizeOperand2;
	}
	
	
	public void setSizeOperand1(Bounds value){
		sizeOperand1.set(value);
	}
	
	public void setSizeOperand2(Bounds value){
		sizeOperand2.set(value);
	}
	
	
	
	

}
