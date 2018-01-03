/*******************************************************************************
 * Copyright (c) 2016 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package editor.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.fx.nodes.InfiniteCanvas;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnDragHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.operations.DeselectOperation;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.parts.LayeredRootPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;
import org.eclipse.gef.mvc.fx.policies.CreationPolicy;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.gef.mvc.fx.viewer.InfiniteCanvasViewer;

import com.google.common.collect.HashMultimap;

import editor.GraphicalEditorModule;
import editor.model.GeometricShape;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.nodes.TextModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorMovableBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.TextFieldPart;
import editor.parts.arithmetical.ArithmeticalOperatorFixedBlockPart;
import editor.parts.choiceboxes.ChoiceBoxFeatureAttributePart;
import editor.parts.choiceboxes.ChoiceBoxFeaturePart;
import editor.parts.choiceboxes.ChoiceBoxTemporalElementPart;
import editor.parts.choiceboxes.ChoiceBoxValuePart;
import editor.parts.nodes.TextPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * handler for on click events on the palette on blocks
 * 
 * provides the functionality to create the blocks on the canvas
 * 
 * @author Anna-Liisa
 *
 */
public class CreateShapeOnClickHandler extends AbstractHandler implements IOnDragHandler {
	private GeometricShapePart createdShapePart;
	private Map<AdapterKey<? extends IOnDragHandler>, IOnDragHandler> dragPolicies;

	@Override
	public void abortDrag() {
		// super.abortDrag();
		if (createdShapePart == null) {
			return;
		}

		// // forward event to bend target part
		// if (dragPolicies != null) {
		// for (IOnDragHandler dragPolicy : dragPolicies.values()) {
		// dragPolicy.abortDrag();
		// }
		// }

		createdShapePart = null;
		dragPolicies = null;
	}

	@Override
	public void drag(MouseEvent event, Dimension delta) {

		// super.drag(event, delta);
		if (createdShapePart == null) {
			return;
		}
		
	}

	@Override
	public void endDrag(MouseEvent e, Dimension delta) {
		// super.endDrag(e, delta);
		if (createdShapePart == null) {
			return;
		}

		createdShapePart = null;
		dragPolicies = null;

	}

	protected IViewer getContentViewer() {
		return getHost().getRoot().getViewer().getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
	}

	protected IViewer getPaletteViewer() {
		return getHost().getRoot().getViewer().getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE));
	}

	@Override
	public GeometricShapePart getHost() {
		return (GeometricShapePart) super.getHost();
	}

	protected Point getLocation(MouseEvent e) {
		Point2D location = ((InfiniteCanvasViewer) getHost().getRoot().getViewer()).getCanvas().getContentGroup()
				.sceneToLocal(e.getSceneX(), e.getSceneY());
		return new Point(location.getX(), location.getY());
	}

	@Override
	public void hideIndicationCursor() {
	}

	@Override
	public boolean showIndicationCursor(KeyEvent event) {
		return false;
	}

	@Override
	public boolean showIndicationCursor(MouseEvent event) {
		return false;
	}

	@Override
	public void startDrag(MouseEvent event) {

		// find model part
		IRootPart<? extends Node> contentRoot = getContentViewer().getRootPart();
		// getContentViewer().getRootPart();
		Node visual = contentRoot.getVisual();

		InfiniteCanvas parent = (InfiniteCanvas) visual.getParent().getParent().getParent();
		double width = parent.getWidth();
		// copy the prototype
		Object o = getHost().getContent();

		GeometricShape copy = ((GeometricShape) o).getCopy();
		Point2D localToScene = getHost().getVisual().localToScene(0, 0);
		Point2D originInModel = ((LayeredRootPart) getContentViewer().getRootPart()).getContentLayer()
				.sceneToLocal(localToScene.getX(), localToScene.getY());
		// initially move to the originInModel
		double[] matrix = copy.getTransform().getMatrix();
//		copy.getTransform().setTransform(matrix[0], matrix[1], matrix[2], matrix[3], originInModel.getX(),
//				originInModel.getY());
		//TODO: Die position sollte lieber so bestimmt werden: originInModel.getX() + width of Palette
		
		copy.getTransform().setTransform(matrix[0], matrix[1], matrix[2], matrix[3], originInModel.getX() + width,
				originInModel.getY());

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		linked = getAnchoredsRecursive(linked, getHost());

		// create copy of host's geometry using CreationPolicy from root
		// part
		CreationPolicy creationPolicy = contentRoot.getAdapter(CreationPolicy.class);
		init(creationPolicy);
		createdShapePart = (GeometricShapePart) creationPolicy.create(copy, contentRoot,
				HashMultimap.<IContentPart<? extends Node>, String> create());

		GeometricShapePart copy2 = createdShapePart;

		commit(creationPolicy);

		for (IContentPart<? extends Node> link : linked) {
			// setLinked.put(link, "link");

			if (link instanceof GeometricShapePart) {
				GeometricShape choiceBoxModelCopy = ((GeometricShapePart) link).getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());

				init(creationPolicy);
				GeometricShapePart part = (GeometricShapePart) creationPolicy.create(choiceBoxModelCopy, contentRoot,
						HashMultimap.<IContentPart<? extends Node>, String> create());

				// part.getContent().addAnchorage(createdShapePart.getContent());
				// part.attachToAnchorage(copy2, "movable_child");

				commit(creationPolicy);
				part.refreshVisual();
			}

			if (link instanceof ArithmeticalOperatorFixedBlockPart) {
				ArithmeticalOperatorFixedBlock choiceBoxModelCopy = ((ArithmeticalOperatorFixedBlockPart) link)
						.getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				ArithmeticalOperatorFixedBlockPart part = (ArithmeticalOperatorFixedBlockPart) creationPolicy.create(
						choiceBoxModelCopy, contentRoot, HashMultimap.<IContentPart<? extends Node>, String> create());

				// part.getContent().addAnchorage(createdShapePart.getContent());
				// part.attachToAnchorage(copy2,
				// AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);

				commit(creationPolicy);
				// part.refreshContentAnchorages();
				part.refreshVisual();
			}

			if (link instanceof OperatorMovableBlockPart) {
				OperatorMovableBlockModel choiceBoxModelCopy = ((OperatorMovableBlockPart) link).getContent().getCopy();

				init(creationPolicy);
				OperatorMovableBlockPart part = (OperatorMovableBlockPart) creationPolicy.create(choiceBoxModelCopy,
						contentRoot, HashMultimap.<IContentPart<? extends Node>, String> create());

				// part.getContent().addAnchorage(createdShapePart.getContent());
				part.attachToAnchorage(copy2, "movable_child");

				commit(creationPolicy);
				part.refreshVisual();
			}
			if (link instanceof OperatorFixedBlockPart) {
				OperatorFixedBlockModel choiceBoxModelCopy = ((OperatorFixedBlockPart) link).getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				OperatorFixedBlockPart part = (OperatorFixedBlockPart) creationPolicy.create(choiceBoxModelCopy,
						contentRoot, HashMultimap.<IContentPart<? extends Node>, String> create());

				// part.getContent().addAnchorage(createdShapePart.getContent());
				// part.attachToAnchorage(copy2,
				// AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);

				commit(creationPolicy);
				// part.refreshContentAnchorages();
				part.refreshVisual();
			}

			if (link instanceof TextPart) {
				TextModel textModelCopy = ((TextPart) link).getContent().getCopy();
				// textModelCopy.addAnchorage(copy2);

				textModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				TextPart textpart = (TextPart) creationPolicy.create(textModelCopy, contentRoot,
						HashMultimap.<IContentPart<? extends Node>, String> create());

				// textpart.attachToAnchorage(copy2,
				// AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
				commit(creationPolicy);
				textpart.refreshVisual();
			}

			if (link instanceof TextFieldPart) {
				TextFieldModel textModelCopy = ((TextFieldPart) link).getContent().getCopy();
				// textModelCopy.addAnchorage(copy2);

				textModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				TextFieldPart textpart = (TextFieldPart) creationPolicy.create(textModelCopy, contentRoot,
						HashMultimap.<IContentPart<? extends Node>, String> create());

				// textpart.attachToAnchorage(copy2,
				// AbstractBlockElement.FIXED_CHILD_ANCHORED_TYPE);
				commit(creationPolicy);
				textpart.refreshVisual();
			}

			if (link instanceof ChoiceBoxTemporalElementPart) {
				ChoiceBoxContextModel choiceBoxModelCopy = ((ChoiceBoxTemporalElementPart) link).getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				ChoiceBoxTemporalElementPart part = (ChoiceBoxTemporalElementPart) creationPolicy.create(
						choiceBoxModelCopy, contentRoot, HashMultimap.<IContentPart<? extends Node>, String> create());



				commit(creationPolicy);
				part.refreshVisual();
			}
			
			
			if (link instanceof ChoiceBoxFeaturePart) {
				ChoiceBoxFeatureModel choiceBoxModelCopy = ((ChoiceBoxFeaturePart) link).getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				ChoiceBoxFeaturePart part = (ChoiceBoxFeaturePart) creationPolicy.create(choiceBoxModelCopy, contentRoot,
						HashMultimap.<IContentPart<? extends Node>, String> create());

				// part.getContent().addAnchorage(createdShapePart.getContent());
				// part.attachToAnchorage(copy2, "childblock");

				commit(creationPolicy);
				part.refreshVisual();
			}
			if (link instanceof ChoiceBoxValuePart) {
				ChoiceBoxValueModel choiceBoxModelCopy = ((ChoiceBoxValuePart) link).getContent().getCopy();

				choiceBoxModelCopy.setParentBlock(copy2.getContent());
				init(creationPolicy);
				ChoiceBoxValuePart part = (ChoiceBoxValuePart) creationPolicy.create(choiceBoxModelCopy, contentRoot,
						HashMultimap.<IContentPart<? extends Node>, String> create());

		

				commit(creationPolicy);
				part.refreshVisual();
			}

//			if (link instanceof ChoiceBoxPart) {
//				ChoiceBoxModel<?> choiceBoxModelCopy = ((ChoiceBoxPart) link).getContent().getCopy();
//
//				choiceBoxModelCopy.setParentBlock(copy2.getContent());
//				init(creationPolicy);
//				ChoiceBoxPart part = (ChoiceBoxPart) creationPolicy.create(choiceBoxModelCopy, contentRoot,
//						HashMultimap.<IContentPart<? extends Node>, String> create());
//
//		
//
//				commit(creationPolicy);
//				part.refreshVisual();
//			}
	

			if (link instanceof ChoiceBoxFeatureAttributePart) {
				ChoiceBoxFeatureAttributeModel choiceBoxModelCopy = ((ChoiceBoxFeatureAttributePart) link).getContent()
						.getCopy();
				choiceBoxModelCopy.setParentBlock(copy2.getContent());

				init(creationPolicy);
				ChoiceBoxFeatureAttributePart part = (ChoiceBoxFeatureAttributePart) creationPolicy.create(
						choiceBoxModelCopy, contentRoot, HashMultimap.<IContentPart<? extends Node>, String> create());



				commit(creationPolicy);
				part.refreshVisual();
			}

		}

		// build operation to deselect all but the new part
		List<IContentPart<? extends Node>> toBeDeselected = new ArrayList<>(
				getContentViewer().getAdapter(SelectionModel.class).getSelectionUnmodifiable());
		toBeDeselected.remove(createdShapePart);

		DeselectOperation deselectOperation = new DeselectOperation(getContentViewer(), toBeDeselected);

		// execute on stack
		try {
			getHost().getRoot().getViewer().getDomain().execute(deselectOperation, new NullProgressMonitor());
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		// }
		// // determine coordinates of prototype's origin in model coordinates
		// Point2D localToScene = getHost().getVisual().localToScene(0, 0);
		// Point2D originInModel = ((LayeredRootPart)
		// getContentViewer().getRootPart()).getContentLayer()
		// .sceneToLocal(localToScene.getX(), localToScene.getY());
		// // initially move to the originInModel
		// double[] matrix = copy.getTransform().getMatrix();
		// copy.getTransform().setTransform(matrix[0], matrix[1], matrix[2],
		// matrix[3], originInModel.getX(),
		// originInModel.getY());
		//
		// // create copy of host's geometry using CreationPolicy from root part
		// CreationPolicy creationPolicy =
		// contentRoot.getAdapter(CreationPolicy.class);
		// init(creationPolicy);
		// createdShapePart = (GeometricShapePart) creationPolicy.create(copy,
		// contentRoot,
		// HashMultimap.<IContentPart<? extends Node>, String> create());
		// commit(creationPolicy);
		//
		// // disable refresh visuals for the created shape part
		// //storeAndDisableRefreshVisuals(createdShapePart);
		//
		// // build operation to deselect all but the new part
		// List<IContentPart<? extends Node>> toBeDeselected = new ArrayList<>(
		// getPaletteViewer().getAdapter(SelectionModel.class).getSelectionUnmodifiable());
		// toBeDeselected.remove(createdShapePart);
		// DeselectOperation deselectOperation = new
		// DeselectOperation(getContentViewer(), toBeDeselected);
		//
		// // execute on stack
		// try {
		// getHost().getRoot().getViewer().getDomain().execute(deselectOperation,
		// new NullProgressMonitor());
		// } catch (ExecutionException e) {
		// throw new RuntimeException(e);
		// }

		// dragPolicies =
		// createdShapePart.getAdapters(ClickDragGesture.ON_DRAG_POLICY_KEY);
		//
		//
		//
		//
		// if (dragPolicies != null) {
		// for (IOnDragHandler dragPolicy : dragPolicies.values()) {

		//
		// dragPolicy.startDrag(event);
		// }
		// }

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
