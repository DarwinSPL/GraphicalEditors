package editor.parts.operator;

import org.eclipse.gef.geometry.planar.IGeometry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.operator.OperatorFeatureIsSelectedModel;

public class OperatorFeatureIsSelectedBlockPart extends OperatorMovableBlockPart {

	@Override
	public OperatorFeatureIsSelectedModel getContent() {
		
		return (OperatorFeatureIsSelectedModel) super.getContent();
	}

	@Override
	protected SetMultimap<? extends Object, String> doGetContentAnchorages() {
		SetMultimap<Object, String> anchorages = HashMultimap.create();
		if (getContent().getParentBlock() != null) {
			anchorages.put(getContent().getParentBlock(), AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE);
		}

		for (AbstractGeometricElement<? extends IGeometry> anchorage : getContent().getAnchorages()) {
			anchorages.put(anchorage, "link");
		}

		return anchorages;
	}

	@Override
	protected void doAttachToContentAnchorage(Object contentAnchorage, String role) {
		if (!(contentAnchorage instanceof AbstractGeometricElement)) {
			throw new IllegalArgumentException("Cannot attach to content anchorage: wrong type!");
		}
		if (role.equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
			getContent().setParentBlock((AbstractGeometricElement<?>) contentAnchorage);
			return;
		}

		getContent().getAnchorages().add((AbstractGeometricElement<?>) contentAnchorage);
	}

	@Override
	protected void doDetachFromContentAnchorage(Object contentAnchorage, String role) {

		if (role.equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
			getContent().removeParentBlock();
			return;
		}
		getContent().getAnchorages().remove(contentAnchorage);
	}
}
