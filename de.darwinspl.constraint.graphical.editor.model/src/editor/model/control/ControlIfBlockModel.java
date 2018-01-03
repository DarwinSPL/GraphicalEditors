package editor.model.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.geometry.planar.IShape;

import editor.model.AbstractGeometricElement;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

public class ControlIfBlockModel extends ControlBlockModel {
	
	public enum ControlBlockType{
		IMPLIES, EQUIVALENCE, NOT_IMPLIES, NOT_EQUIVALENCE
	}

	private List<AbstractGeometricElement<? extends IGeometry>> childBlocks = new ArrayList<AbstractGeometricElement<? extends IGeometry>>();

	public ControlIfBlockModel(IShape shape, AffineTransform transform, Paint fill, Effect effect, ControlBlockType type) {
		super(shape, transform, fill, effect);
		setControlBlockType(type);

	}
	
	private ControlBlockType controlBlockType;

	@Override
	public ControlIfBlockModel getCopy() {
		ControlIfBlockModel copy = new ControlIfBlockModel((IShape) getGeometry().getCopy(), getTransform().getCopy(),
				getFill(), getEffect(), getControlBlockType());
		return copy;
	}



	public List<AbstractGeometricElement<? extends IGeometry>> getChildBlocks() {
		return childBlocks;
	}

	public void setChildBlocks(List<AbstractGeometricElement<? extends IGeometry>> resBlocks) {
		this.childBlocks = resBlocks;
	}

	public void addChildBlock(AbstractGeometricElement<? extends IGeometry> resBlock) {
	
		childBlocks.add(resBlock);
	}

	public void removeChildBlock(AbstractGeometricElement<? extends IGeometry> resBlock) {
		childBlocks.remove(resBlock);
	}



	public ControlBlockType getControlBlockType() {
		return controlBlockType;
	}



	public void setControlBlockType(ControlBlockType controlBlockType) {
		this.controlBlockType = controlBlockType;
	}


}
