package editor.parts.root;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.parts.LayeredRootPart;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class PaletteFeatureRootPart extends LayeredRootPart {

	@Override
	protected Group createContentLayer() {
		Group contentLayer = super.createContentLayer();
		VBox vbox = new VBox();
		vbox.setPickOnBounds(true);
		// define padding and spacing
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(10d);
		// fixed at top/right position
		vbox.setAlignment(Pos.TOP_LEFT);
		contentLayer.getChildren().add(vbox);
		return contentLayer;
	}

	@Override
	protected void doAddChildVisual(IVisualPart<? extends Node> child, int index) {
		if (child instanceof IContentPart) {
			int contentLayerIndex = 0;
			for (int i = 0; i < index; i++) {
				if (i < getChildrenUnmodifiable().size() && getChildrenUnmodifiable().get(i) instanceof IContentPart) {
					contentLayerIndex++;
				}
			}

			// IVisualPart<? extends Node> parentBlock =
			// getAnchoragesParentBlock(child, "link");
			// if(parentBlock != null){
			// ObservableList<Node> vBox = ((VBox)
			// getContentLayer().getChildren().get(0)).getChildren();
			//
			// for(Node node: vBox){
			// if(node instanceof Group){
			// Group g = (Group) node;
			// if(g.getChildren().contains(parentBlock)){
			// g.getChildren().add(child.getVisual());
			// return;
			// }
			// }
			// }
			//
			// }

			// Group group = new Group(child.getVisual());
			//
			//
			// ((VBox)
			// getContentLayer().getChildren().get(0)).getChildren().add(contentLayerIndex,
			// group);

			((VBox) getContentLayer().getChildren().get(0)).getChildren().add(contentLayerIndex,
					new Group(child.getVisual()));

		} else {
			super.doAddChildVisual(child, index);
		}
	}

	@Override
	protected void doRemoveChildVisual(IVisualPart<? extends Node> child, int index) {
		if (child instanceof IContentPart) {
			((VBox) getContentLayer().getChildren().get(0)).getChildren().remove(index);
		} else {
			super.doRemoveChildVisual(child, index);
		}
	}

	// private IVisualPart<? extends Node>
	// getAnchoragesParentBlock(IVisualPart<? extends Node> child, String role){
	//
	// ObservableSetMultimap<IVisualPart<? extends Node>, String> anch =
	// child.getAnchoragesUnmodifiable();
	//
	// for(Entry<IVisualPart<? extends Node>, String> entry: anch.entries()){
	// if(entry.getValue().equals(role)){
	// return entry.getKey();
	// }
	// }
	//
	// return null;
	//
	// }
	// /**
	// *
	// *
	// * @param linked
	// * @param parent
	// * @return
	// */
	// private List<IContentPart<? extends Node>>
	// getAnchoredsRecursive(List<IContentPart<? extends Node>> linked,
	// IVisualPart<? extends Node> parent){
	// @SuppressWarnings("unchecked")
	// List<IContentPart<? extends Node>> anch =
	// PartUtils.filterParts(PartUtils.getAnchoreds(parent, "link"),
	// IContentPart.class);
	//
	//
	// for(IContentPart<? extends Node> node: anch){
	// if(!(linked.contains(node))){
	// linked.add(node);
	// if(!(node.getAnchoredsUnmodifiable().isEmpty())){
	// linked = getAnchoredsRecursive(linked, node);
	// }
	//
	// }
	// }
	//
	// return linked;
	//
	//
	// }

}
