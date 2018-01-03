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
package editor.parts.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.mvc.fx.parts.DefaultHoverIntentHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.IHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import editor.parts.BlockContextViewHandlePart;
import editor.parts.GeometricShapePart;
import editor.parts.HoverHandleContainerPart;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.scene.Node;

public class GraphicalEditorHoverHandlePartFactory extends DefaultHoverIntentHandlePartFactory {

	@Inject
	private Injector injector;

	@Override
	protected List<IHandlePart<? extends Node>> createHoverHandlePartsForPolygonalOutline(
			IVisualPart<? extends Node> target, Map<Object, Object> contextMap,
			Provider<BezierCurve[]> segmentsProvider) {
		List<IHandlePart<? extends Node>> handles = new ArrayList<>();
		if ((target instanceof GeometricShapePart)) {
			// create root handle part

			HoverHandleContainerPart parentHp = new HoverHandleContainerPart();
			injector.injectMembers(parentHp);
			handles.add(parentHp);

			if (target instanceof OperatorMovableBlockPart || target instanceof ControlBlockPart
					|| target instanceof ArithmeticalOperatorMovableBlockPart) {
				// FIXME: addChild() should be called automatically?
				BlockContextViewHandlePart deleteHp = new BlockContextViewHandlePart();
				injector.injectMembers(deleteHp);
				parentHp.addChild(deleteHp);
			}

			// GeometricCurveCreationHoverHandlePart createCurveHp = new
			// GeometricCurveCreationHoverHandlePart();
			// injector.injectMembers(createCurveHp);
			// parentHp.addChild(createCurveHp);
		}
		return handles;
	}

	@Override
	protected List<IHandlePart<? extends Node>> createHoverHandlePartsForRectangularOutline(
			IVisualPart<? extends Node> target, Map<Object, Object> contextMap,
			Provider<BezierCurve[]> segmentsProvider) {
		return createHoverHandlePartsForPolygonalOutline(target, contextMap, segmentsProvider);
	}
}
