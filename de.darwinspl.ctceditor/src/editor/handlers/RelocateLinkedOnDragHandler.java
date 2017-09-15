/*******************************************************************************
 * Copyright (c) 2015, 2016 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *******************************************************************************/
package editor.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.mvc.fx.handlers.TranslateSelectedOnDragHandler;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.PartUtils;

import editor.model.AbstractGeometricElement;
import editor.parts.AbstractGeometricElementPart;
import javafx.scene.Node;
import sun.text.normalizer.CharTrie.FriendAgent;

/**
 * Provides the functionality to move the blocks including child blocks
 * 
 * @author Anna-Liisa
 *
 */
public class RelocateLinkedOnDragHandler extends TranslateSelectedOnDragHandler {

	@Override
	public List<IContentPart<? extends Node>> getTargetParts() {
		List<IContentPart<? extends Node>> selected = super.getTargetParts();
		
		
//		IContentPart<? extends Node> firstSelectedPart = selected.get(0);
//		if(firstSelectedPart instanceof AbstractGeometricElementPart<?>){
//			if(((AbstractGeometricElementPart<?>) firstSelectedPart).getContent().getParentBlock()!=null){
//			 List<IContentPart<? extends Node>> newList = new ArrayList<IContentPart<? extends Node>>();
//			 return newList;
//			}		
//		}
		

		List<IContentPart<? extends Node>> linked = new ArrayList<>();
		for (IContentPart<? extends Node> cp : selected) {
			

			linked = getAnchoredsRecursive(linked, cp);

		}

		
		SelectionModel selectionModel = getHost().getRoot().getViewer().getAdapter(SelectionModel.class);
		linked.removeAll(selectionModel.getSelectionUnmodifiable());

		return linked;
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
