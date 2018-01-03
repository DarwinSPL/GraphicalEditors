package editor.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.mvc.fx.handlers.DeleteSelectedOnTypeHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.parts.GeometricShapePart;
import editor.parts.control.ControlBlockPart;
import editor.parts.nodes.AbstractNodePart;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class DeleteIncludingAnchorages extends DeleteSelectedOnTypeHandler {

	@Override
	public void initialPress(KeyEvent event) {
		if (!isDelete(event)) {
			return;
		}

		// get current selection
		IViewer viewer = getHost().getRoot().getViewer();
		List<IContentPart<? extends Node>> selected = new ArrayList<>(
				viewer.getAdapter(SelectionModel.class).getSelectionUnmodifiable());

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		linked.addAll(selected);
		for (IContentPart<? extends Node> link : selected) {
			linked = getAnchoredsRecursive(linked, link);
		}

		// if no parts are selected, we do not delete anything
		if (linked.isEmpty()) {
			return;
		}

		// delete selected parts
		DeletionPolicy deletionPolicy = getHost().getRoot().getAdapter(DeletionPolicy.class);
		init(deletionPolicy);
		for (IContentPart<? extends Node> s : linked) {
			if(s instanceof ControlBlockPart){
				((ControlBlockPart) s).getContent().removeParentBlock();
			}
		
			deletionPolicy.delete(s);
			
		}
		commit(deletionPolicy);
	}

	/**
	 * 
	 * 
	 * @param linked
	 * @param parent
	 * @return
	 */
	private List<IContentPart<? extends Node>> getAnchoredsRecursive(List<IContentPart<? extends Node>> linked,
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

}
