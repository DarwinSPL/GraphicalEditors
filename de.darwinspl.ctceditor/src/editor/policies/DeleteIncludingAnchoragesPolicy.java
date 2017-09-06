package editor.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.mvc.fx.models.FocusModel;
import org.eclipse.gef.mvc.fx.operations.ChangeContentsOperation;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.ContentPolicy;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultiset;

import editor.model.AbstractBlockElement;
import javafx.scene.Node;

public class DeleteIncludingAnchoragesPolicy extends DeletionPolicy {

	/**
	 * Deletes the given {@link IContentPart} by removing the
	 * {@link IContentPart}'s content from the parent {@link IContentPart}'
	 * content and by detaching the contents of all anchored
	 * {@link IContentPart}s from the {@link IContentPart}'s content.
	 *
	 * @param contentPartToDelete
	 *            The {@link IContentPart} to mark for deletion.
	 */
	// TODO: offer a bulk operation to improve deselect (can remove all in one
	// operation pass)
	// this will break if being called one after another without commit
	@Override
	public void delete(IContentPart<? extends Node> contentPartToDelete) {
		checkInitialized();

		// clear viewer models so that anchoreds are removed
		IViewer viewer = getHost().getRoot().getViewer();
		getDeselectOperation().getToBeDeselected().add(contentPartToDelete);
		FocusModel focusModel = viewer.getAdapter(FocusModel.class);
		if (focusModel != null) {
			if (focusModel.getFocus() == contentPartToDelete) {
				getUnfocusOperation().setNewFocused(null);
			}
		}

		// XXX: Execute operations for changing the viewer models prior to
		// detaching anchoreds and removing children, so that no link to the
		// viewer is available for the removed part via selection, focus, or
		// hover feedback or handles.
		locallyExecuteOperation();

		// detach all content anchoreds
		for (IVisualPart<? extends Node> anchored : HashMultiset
				.create(contentPartToDelete.getAnchoredsUnmodifiable())) {

			if (anchored instanceof IContentPart) {

				DeletionPolicy anchoredDeletionPolicy = anchored.getAdapter(DeletionPolicy.class);
				// ContentPolicy anchoredContentPolicy = anchored
				// .getAdapter(ContentPolicy.class);
				if (anchoredDeletionPolicy != null) {

					anchoredDeletionPolicy.init();
					for (String role : anchored.getAnchoragesUnmodifiable().get(contentPartToDelete)) {

						if (role.equals(AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE)) {

							anchoredDeletionPolicy.delete((IContentPart<? extends Node>) anchored);
							// anchoredContentPolicy.detachFromContentAnchorage(
							// contentPartToDelete.getContent(), role);
							anchoredDeletionPolicy.commit();
						}
					}

					// ITransactionalOperation
					// detachFromContentAnchoredOperation =
					// anchoredContentPolicy
					// .commit();
					// if (detachFromContentAnchoredOperation != null
					// && !detachFromContentAnchoredOperation.isNoOp()) {
					// getDetachContentAnchoragesOperation()
					// .add(detachFromContentAnchoredOperation);
					// }
				}
			}
		}

		if (contentPartToDelete.getParent() instanceof IRootPart) {
			// remove content from viewer contents
			ChangeContentsOperation changeContentsOperation = new ChangeContentsOperation(viewer);
			List<Object> newContents = new ArrayList<>(viewer.getContents());
			newContents.remove(contentPartToDelete.getContent());
			changeContentsOperation.setNewContents(newContents);
			// getRemoveContentChildrenOperation().add(changeContentsOperation);
		} else {
			// remove from content parent
			ContentPolicy parentContentPolicy = contentPartToDelete.getParent().getAdapter(ContentPolicy.class);
			if (parentContentPolicy != null) {
				parentContentPolicy.init();
				parentContentPolicy.removeContentChild(contentPartToDelete.getContent());
				ITransactionalOperation removeFromParentOperation = parentContentPolicy.commit();
				// if (removeFromParentOperation != null
				// && !removeFromParentOperation.isNoOp()) {
				// getRemoveContentChildrenOperation()
				// .add(removeFromParentOperation);
				// }
			}
		}

		// TODO: Hover feedback needs to be removed

		locallyExecuteOperation();

		// verify that all anchoreds were removed
		if (!contentPartToDelete.getAnchoredsUnmodifiable().isEmpty()) {
			throw new IllegalStateException(
					"After deletion of <" + contentPartToDelete + "> there are still anchoreds remaining.");
		}
	}

}
