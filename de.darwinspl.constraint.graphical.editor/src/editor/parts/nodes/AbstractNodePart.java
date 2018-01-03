package editor.parts.nodes;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.geometry.convert.fx.FX2Geometry;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.parts.AbstractContentPart;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IResizableContentPart;
import org.eclipse.gef.mvc.fx.parts.ITransformableContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

public class AbstractNodePart<N extends Node> extends AbstractContentPart<N>
		implements ITransformableContentPart<N>, IResizableContentPart<N> {

	private final ChangeListener<Object> contentObserver = new ChangeListener<Object>() {

		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			refreshVisual();
		}
	};

	
	protected IViewer getContentViewer() {
		return getRoot().getViewer().getDomain().getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
	}
	
	@Override
	public AbstractBlockElement getContent() {
		return (AbstractBlockElement) super.getContent();
	}

	@Override
	protected void doActivate() {
		super.doActivate();
		contentProperty().addListener(contentObserver);

	}

	@Override
	protected void doDeactivate() {
		contentProperty().removeListener(contentObserver);
		super.doDeactivate();
	}

	@Override
	public Dimension getContentSize() {
		
		return null;
	}

	@Override
	public void setContentSize(Dimension totalSize) {
		

	}

	@Override
	public Affine getContentTransform() {
		
		return Geometry2FX.toFXAffine(getContent().getTransform());
	}

	@Override
	public void setContentTransform(Affine totalTransform) {
		getContent().setTransform(FX2Geometry.toAffineTransform(totalTransform));
	}

	@Override
	protected List<? extends Object> doGetContentChildren() {
		return Collections.emptyList();
	}

	@Override
	protected N doCreateVisual() {
	
		return null;
	}

	@Override
	protected void doRefreshVisual(N visual) {

		AffineTransform transform = getContent().getTransform();
		if (transform != null) {
			setVisualTransform(Geometry2FX.toFXAffine(transform));
		}

		if (getContent().getBoundsInParent() != visual.getBoundsInParent()) {
			getContent().setBoundsInParent(visual.getBoundsInParent());
		}

	}

	/**
	 * 
	 * 
	 * @param linked
	 * @param parent
	 * @return
	 */
	protected List<IContentPart<? extends Node>> getAnchoredsRecursive(List<IContentPart<? extends Node>> linked,
			IContentPart<? extends Node> parent) {
		@SuppressWarnings("unchecked")
		
		List<IContentPart<? extends Node>> anch = PartUtils.filterParts(parent.getAnchoredsUnmodifiable(),
				IContentPart.class);

		for (IContentPart<? extends Node> node : anch) {
			if (!(linked.contains(node))) {
				linked.add(node);
				if (!(node.getAnchoredsUnmodifiable().isEmpty())) {
					linked = getAnchoredsRecursive(linked, node);
				}

			}
		}

		return linked;

	}

	public void resizedIncludingParent(int widthDifference) {
		

	}

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
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {
		if (anchorage.getVisual().getParent() != null) {
			if (!getVisual().getParent().equals(anchorage.getVisual().getParent())) {

				Group parenttthis = (Group) getVisual().getParent();
				Group parentparent = (Group) anchorage.getVisual().getParent();

				parenttthis.getChildren().remove(getVisual());
				parentparent.getChildren().add(getVisual());

			}
		}
	}

	@Override
	protected void doDetachFromAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {

	}

}
