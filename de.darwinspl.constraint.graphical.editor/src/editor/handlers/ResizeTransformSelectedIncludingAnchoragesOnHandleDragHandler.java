package editor.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.mvc.fx.handlers.ResizeTransformSelectedOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import javafx.scene.Node;

public class ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler
		extends ResizeTransformSelectedOnHandleDragHandler {

	/**
	 * Returns a {@link List} containing all {@link IContentPart}s that should
	 * be scaled/relocated by this policy. Per default, the whole
	 * {@link SelectionModel selection} is returned.
	 *
	 * @return A {@link List} containing all {@link IContentPart}s that should
	 *         be scaled/relocated by this policy.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<IContentPart<? extends Node>> getTargetParts() {
		List<IContentPart<? extends Node>> selected = super.getTargetParts();

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		linked.addAll(selected);
		for (IContentPart<? extends Node> cp : selected) {
			// ensure that linked parts are moved with us during dragging

			List<IContentPart<? extends Node>> anch = PartUtils.filterParts(cp.getAnchoredsUnmodifiable(),
					IContentPart.class);
			for (IContentPart<? extends Node> node : anch) {
				if (!(selected.contains(node))) {
					linked.add(node);
				}
			}
			// linked.addAll((Collection<? extends IContentPart<? extends
			// Node>>) new ArrayList<>(
			// PartUtils.filterParts(PartUtils.getAnchoreds(cp, "link"),
			// IContentPart.class)));
		}

		// // remove all linked that are selected already (these will be
		// translated
		// // via the TranslateSelectedOnDragPolicy) already

		// SelectionModel selectionModel =
		// getHost().getRoot().getViewer().getAdapter(SelectionModel.class);
		// linked.removeAll(selectionModel.getSelectionUnmodifiable());

		return linked;
	}

}
