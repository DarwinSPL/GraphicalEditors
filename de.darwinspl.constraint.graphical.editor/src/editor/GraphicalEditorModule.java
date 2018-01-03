/*******************************************************************************
 * Copyright (c) 2014, 2016 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander NyÃŸen (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package editor;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.common.adapt.inject.AdaptableScopes;
import org.eclipse.gef.common.adapt.inject.AdapterInjectionSupport;
import org.eclipse.gef.common.adapt.inject.AdapterInjectionSupport.LoggingMode;
import org.eclipse.gef.common.adapt.inject.AdapterMaps;
import org.eclipse.gef.mvc.fx.MvcFxModule;
import org.eclipse.gef.mvc.fx.behaviors.ContentPartPool;
import org.eclipse.gef.mvc.fx.behaviors.FocusBehavior;
import org.eclipse.gef.mvc.fx.behaviors.HoverBehavior;
import org.eclipse.gef.mvc.fx.behaviors.HoverIntentBehavior;
import org.eclipse.gef.mvc.fx.behaviors.SelectionBehavior;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.gestures.ClickDragGesture;
import org.eclipse.gef.mvc.fx.handlers.BendFirstAnchorageOnSegmentHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.FocusAndSelectOnClickHandler;
import org.eclipse.gef.mvc.fx.handlers.HoverOnHoverHandler;
import org.eclipse.gef.mvc.fx.handlers.ResizeTransformSelectedOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.ResizeTranslateFirstAnchorageOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.RotateSelectedOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.RotateSelectedOnRotateHandler;
import org.eclipse.gef.mvc.fx.handlers.SelectAllOnTypeHandler;
import org.eclipse.gef.mvc.fx.handlers.SelectFocusedOnTypeHandler;
import org.eclipse.gef.mvc.fx.handlers.TraverseFocusOnTypeHandler;
import org.eclipse.gef.mvc.fx.models.FocusModel;
import org.eclipse.gef.mvc.fx.models.HoverModel;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.CircleSegmentHandlePart;
import org.eclipse.gef.mvc.fx.parts.DefaultFocusFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultHoverFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultHoverIntentHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.IContentPartFactory;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.RectangleSegmentHandlePart;
import org.eclipse.gef.mvc.fx.parts.SquareSegmentHandlePart;
import org.eclipse.gef.mvc.fx.policies.ResizePolicy;
import org.eclipse.gef.mvc.fx.policies.TransformPolicy;
import org.eclipse.gef.mvc.fx.providers.DefaultAnchorProvider;
import org.eclipse.gef.mvc.fx.providers.GeometricOutlineProvider;
import org.eclipse.gef.mvc.fx.providers.ShapeBoundsProvider;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.multibindings.MapBinder;

//import editor.behaviors.PaletteFocusBehavior;
import editor.gestures.DragOverGesture;
import editor.handlers.CloneOnClickHandler;
import editor.handlers.CloneShapeSupport;
import editor.handlers.CreateContextMenuOnClickHandler;
import editor.handlers.CreateShapeOnClickHandler;
import editor.handlers.DeleteIncludingAnchorages;
import editor.handlers.OnDragOverNodeHandler;
import editor.handlers.RelocateLinkedOnDragHandler;

//TODO: wieder einfügen
//import editor.handlers.ResizeTransformIncludeAnchoragesHandler;
import editor.handlers.ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler;
import editor.handlers.TranslateOnDragHandler;
import editor.parts.BlockContextViewHandlePart;
import editor.parts.GeometricShapePart;
import editor.parts.TextFieldPart;
import editor.parts.arithmetical.ArithmeticalOperatorFixedBlockPart;
import editor.parts.choiceboxes.ChoiceBoxFeatureAttributePart;
import editor.parts.choiceboxes.ChoiceBoxFeaturePart;
import editor.parts.choiceboxes.ChoiceBoxTemporalElementPart;
import editor.parts.choiceboxes.ChoiceBoxValuePart;
import editor.parts.factories.GraphicalEditorContentPartFactory;
import editor.parts.factories.GraphicalEditorHoverHandlePartFactory;
import editor.parts.factories.GraphicalEditorSelectionHandlePartFactory;
import editor.parts.nodes.TextPart;
import editor.parts.operator.OperatorFixedBlockPart;
import editor.parts.root.EvolutionViewRootPart;
import editor.parts.root.PaletteContextsRootPart;
import editor.parts.root.PaletteFeatureRootPart;
import editor.parts.root.PaletteRootPart;
import editor.policies.ContentRestrictedChangeViewportPolicy;
import editor.policies.DeleteIncludingAnchoragesPolicy;
import javafx.scene.paint.Color;

public class GraphicalEditorModule extends MvcFxModule {

	public static final String PALETTE_VIEWER_ROLE = "paletteViewer";

	public static final String PALETTE_VIEWER_ROLE_FEATURES = "paletteViewerFeatures";

	public static final String PALETTE_VIEWER_ROLE_CONTEXTS = "paletteViewerContexts";

	public static final String PALETTE_VIEWER_ROLE_OPERATORS = "paletteViewerOperators";

	public static final String PALETTE_VIEWER_ROLE_ARITHMETICS = "paletteViewerArithmetics";

	public static final String EVOLUTION_VIEWER_ROLE = "evolutionViewer";

	@Override
	protected void bindAbstractContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);
		// select on click
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FocusAndSelectOnClickHandler.class);
		// select on type
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SelectFocusedOnTypeHandler.class);
	}

	protected void bindCircleSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(BendFirstAnchorageOnSegmentHandleDragHandler.class);
	}

	protected void bindContentPartPoolAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ContentPartPool.class);
	}

	/**
	 * Registers the {@link ContentRestrictedChangeViewportPolicy} as an adapter
	 * at the given {@link MapBinder}.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} where the
	 *            {@link ContentRestrictedChangeViewportPolicy} is registered.
	 */
	protected void bindContentRestrictedChangeViewportPolicyAsFXRootPartAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ContentRestrictedChangeViewportPolicy.class);
	}

	protected void bindContextMenuHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateContextMenuOnClickHandler.class);
	}

	protected void bindFocusFeedbackFactoryAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(FocusBehavior.FOCUS_FEEDBACK_PART_FACTORY))
				.to(DefaultFocusFeedbackPartFactory.class);
	}

	protected void bindFocusModelAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FocusModel.class);
	}

	/**
	 * Adds a binding for {@link FXPaletteViewer} to the {@link AdapterMap}
	 * binder for {@link IDomain}.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IDomain} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindFXPaletteViewerAsFXDomainAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_FEATURES)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_CONTEXTS)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_OPERATORS)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_ARITHMETICS)).to(IViewer.class);

		adapterMapBinder.addBinding(AdapterKey.role(EVOLUTION_VIEWER_ROLE)).to(IViewer.class);
	}

	protected void bindFXRectangleSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(BendFirstAnchorageOnSegmentHandleDragHandler.class);
	}

	protected void bindFXSquareSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// single selection: resize relocate on handle drag without modifier
		adapterMapBinder.addBinding(AdapterKey.defaultRole())
				.to(ResizeTranslateFirstAnchorageOnHandleDragHandler.class);
		// rotate on drag + control
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RotateSelectedOnHandleDragHandler.class);

		// multi selection: scale relocate on handle drag without modifier
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizeTransformSelectedOnHandleDragHandler.class);
	}



	protected void bindGeometricShapePartAdapterInPaletteViewerContext(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateShapeOnClickHandler.class);
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(GeometricOutlineProvider.class);
		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);
		// relocate on drag (including anchored elements, which are linked)
		// adapterMapBinder.addBinding(AdapterKey.role("AFTER_COPY_PALETTE_OBJECT_DRAG")).to(TranslateSelectedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RelocateLinkedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.get(MouseDragEvent.class).to(TranslateOnDragHandler.class);

	}

	protected void bindGeometricShapePartAdapterInPaletteViewerContext2(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateAndTranslateShapeOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
		// .to(GeometricOutlineProvider.class);
		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);
		// relocate on drag (including anchored elements, which are linked)
		// adapterMapBinder.addBinding(AdapterKey.role("AFTER_COPY_PALETTE_OBJECT_DRAG")).to(TranslateSelectedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RelocateLinkedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.get(MouseDragEvent.class).to(TranslateOnDragHandler.class);

	}


	protected void bindChoiceBoxPartAdapterInPaletteViewerContext(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateShapeOnClickHandler.class);
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(GeometricOutlineProvider.class);
		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);

		// relocate on drag (including anchored elements, which are linked)
		// adapterMapBinder.addBinding(AdapterKey.role("AFTER_COPY_PALETTE_OBJECT_DRAG")).to(TranslateSelectedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RelocateLinkedOnDragHandler.class);
		// adapterMapBinder.addBinding(AdapterKey.get(MouseDragEvent.class).to(TranslateOnDragHandler.class);

	}

	protected void bindGeometricShapePartAdaptersInContentViewerContext(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// hover on hover
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);

		// geometry provider for selection feedback
		adapterMapBinder
				.addBinding(AdapterKey.role(DefaultSelectionFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
				.toProvider(new Provider<ShapeBoundsProvider>() {
					@Override
					public ShapeBoundsProvider get() {
						return new ShapeBoundsProvider(0.5);
					}
				});
		// geometry provider for selection handles
		adapterMapBinder
				.addBinding(AdapterKey.role(DefaultSelectionHandlePartFactory.SELECTION_HANDLES_GEOMETRY_PROVIDER))
				.toProvider(new Provider<ShapeBoundsProvider>() {
					@Override
					public ShapeBoundsProvider get() {
						return new ShapeBoundsProvider(0.5);
					}
				});
		// adapterMapBinder
		// .addBinding(
		// AdapterKey.role(DefaultSelectionFeedbackPartFactory.SELECTION_LINK_FEEDBACK_GEOMETRY_PROVIDER))
		// .to(GeometricOutlineProvider.class);
		// geometry provider for hover feedback
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for hover handles
		adapterMapBinder
				.addBinding(AdapterKey.role(DefaultHoverIntentHandlePartFactory.HOVER_INTENT_HANDLES_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for focus feedback
		adapterMapBinder.addBinding(AdapterKey.role(DefaultFocusFeedbackPartFactory.FOCUS_FEEDBACK_GEOMETRY_PROVIDER))
				.toProvider(new Provider<ShapeBoundsProvider>() {
					@Override
					public ShapeBoundsProvider get() {
						return new ShapeBoundsProvider(0.5);
					}
				});

		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DeleteIncludingAnchoragesPolicy.class);

		// relocate on drag (including anchored elements, which are linked)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TranslateOnDragHandler.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RelocateLinkedOnDragHandler.class);

		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler.class);

		// clone
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CloneShapeSupport.class);

		// bind dynamic anchor provider
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);

		// clone on shift+click
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CloneOnClickHandler.class);

		// normalize connected on drag
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(NormalizeConnectedOnDragHandler.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(OnDragOverNodeHandler.class);

	}

	protected void bindGeometricShapePartAdaptersInContentViewerContext2(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// hover on hover
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);

		// adapterMapBinder
		// .addBinding(
		// AdapterKey.role(DefaultSelectionFeedbackPartFactory.SELECTION_LINK_FEEDBACK_GEOMETRY_PROVIDER))
		// .to(GeometricOutlineProvider.class);
		// geometry provider for hover feedback
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for hover handles
		adapterMapBinder
				.addBinding(AdapterKey.role(DefaultHoverIntentHandlePartFactory.HOVER_INTENT_HANDLES_GEOMETRY_PROVIDER))
				.to(ShapeBoundsProvider.class);
		// geometry provider for focus feedback
		adapterMapBinder.addBinding(AdapterKey.role(DefaultFocusFeedbackPartFactory.FOCUS_FEEDBACK_GEOMETRY_PROVIDER))
				.toProvider(new Provider<ShapeBoundsProvider>() {
					@Override
					public ShapeBoundsProvider get() {
						return new ShapeBoundsProvider(0.5);
					}
				});

		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler.class);

		// clone
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CloneShapeSupport.class);

		// bind dynamic anchor provider
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(OnDragOverNodeHandler.class);

	}

	protected void bindGeometricShapePartAdaptersInContentViewerContext3(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// hover on hover
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);


		// register resize/transform policies (writing changes also to model)
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler.class);

		// clone
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CloneShapeSupport.class);

		// bind dynamic anchor provider
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DefaultAnchorProvider.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(OnDragOverNodeHandler.class);

	}

	protected void bindHoverFeedbackFactoryAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(HoverBehavior.HOVER_FEEDBACK_PART_FACTORY))
				.to(DefaultHoverFeedbackPartFactory.class);
	}

	// TODO: wird benötigt für palette?
	protected void bindHoverHandleFactoryAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(HoverIntentBehavior.HOVER_INTENT_HANDLE_PART_FACTORY))
				.to(DefaultHoverIntentHandlePartFactory.class);
	}

	@Override
	protected void bindHoverHandlePartFactoryAsContentViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(HoverIntentBehavior.HOVER_INTENT_HANDLE_PART_FACTORY))
				.to(GraphicalEditorHoverHandlePartFactory.class);
	}

	protected void bindHoverModelAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverModel.class);
	}

	protected void bindIContentPartFactory() {
		binder().bind(IContentPartFactory.class).toInstance(new GraphicalEditorContentPartFactory());
	}

	protected void bindIContentPartFactoryAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(IContentPartFactory.class);
	}

	@Override
	protected void bindIDomainAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIDomainAdapters(adapterMapBinder);
		// bindDragOverGestureAsDomainAdapter(adapterMapBinder);

		bindPaletteViewerAsDomainAdapter(adapterMapBinder);

	}

	@Override
	protected void bindIRootPartAdaptersForContentViewer(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIRootPartAdaptersForContentViewer(adapterMapBinder);
//		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreationMenuOnClickHandler.class);
//		adapterMapBinder.addBinding(AdapterKey.role(CreationMenuOnClickHandler.MENU_ITEM_PROVIDER_ROLE))
//				.to(CreationMenuItemProvider.class);
		// interaction handler to delete on key type
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DeleteIncludingAnchorages.class);
		// interaction handler to rotate selected through rotate gesture
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RotateSelectedOnRotateHandler.class);
		// keyboard focus traversal through key navigation
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TraverseFocusOnTypeHandler.class);
		// select on type
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SelectFocusedOnTypeHandler.class);
		// hover behavior
		
		//TODO: wieder hinzufügen - beides
//		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverBehavior.class);
//		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverIntentBehavior.class);
		// select-all on type
		bindSelectAllOnTypeHandlerAsContentViewerRootPartAdapter(adapterMapBinder);
		// bindOnDragOverNodeHandlerAsIRootPartAdapter(adapterMapBinder);
	}



	protected void bindPaletteRootPartAdaptersInPaletteViewerContext(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {

		// register (default) interaction policies (which are based on viewer
		// models and do not depend on transaction policies)
		bindHoverOnHoverHandlerAsIRootPartAdapter(adapterMapBinder);
		bindPanOrZoomOnScrollHandlerAsIRootPartAdapter(adapterMapBinder);
		bindPanOnTypeHandlerAsIRootPartAdapter(adapterMapBinder);
		// register change viewport policy
		bindContentRestrictedChangeViewportPolicyAsFXRootPartAdapter(adapterMapBinder);
		// register default behaviors
		bindContentBehaviorAsIRootPartAdapter(adapterMapBinder);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverBehavior.class);
		// XXX: PaletteFocusBehavior only changes the viewer focus and default
		// styles.
		// bindPaletteFocusBehaviorAsFXRootPartAdapter(adapterMapBinder);
		// bind focus traversal policy
		bindFocusTraversalPolicyAsIRootPartAdapter(adapterMapBinder);
		// hover behavior
		bindCreationPolicyAsIRootPartAdapter(adapterMapBinder);
		// bindClickDragGestureAsDomainAdapter(adapterMapBinder);
		bindGridModelAsContentViewerAdapter(adapterMapBinder);
	}

	protected void bindPaletteRootPartAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE)).to(PaletteRootPart.class)
				.in(AdaptableScopes.typed(IViewer.class));

	}

	protected void bindPaletteRootPartAsPaletteViewerAdapterFeatures(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_FEATURES)).to(PaletteFeatureRootPart.class)
				.in(AdaptableScopes.typed(IViewer.class));

	}

	protected void bindPaletteRootPartAsPaletteContextsViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_CONTEXTS)).to(PaletteContextsRootPart.class)
				.in(AdaptableScopes.typed(IViewer.class));

	}

	protected void bindPaletteRootPartAsPaletteOperatorsViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_OPERATORS)).to(PaletteRootPart.class)
				.in(AdaptableScopes.typed(IViewer.class));

	}

	protected void bindPaletteRootPartAsPaletteArithmeticsViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_ARITHMETICS)).to(PaletteRootPart.class)
				.in(AdaptableScopes.typed(IViewer.class));

	}

	protected void bindEvolutionRootPartAsEvolutionViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(EVOLUTION_VIEWER_ROLE)).to(EvolutionViewRootPart.class);
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link IViewer} and all
	 * sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IViewer} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		// root part
		bindPaletteRootPartAsPaletteViewerAdapter(adapterMapBinder);

		// feedback and handles factories
		bindSelectionFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindFocusFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);


		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link IViewer} and all
	 * sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IViewer} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAdaptersFeatures(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		// root part
		bindPaletteRootPartAsPaletteViewerAdapterFeatures(adapterMapBinder);

		// feedback and handles factories
		bindSelectionFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindFocusFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link IViewer} and all
	 * sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IViewer} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAdaptersOperators(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		// root part
		bindPaletteRootPartAsPaletteOperatorsViewerAdapter(adapterMapBinder);

		// feedback and handles factories
		bindSelectionFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindFocusFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link IViewer} and all
	 * sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IViewer} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAdaptersArithmetics(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		// root part
		bindPaletteRootPartAsPaletteArithmeticsViewerAdapter(adapterMapBinder);

		// feedback and handles factories
		bindSelectionFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindFocusFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});
	}

	/**
	 * Adds (default) {@link AdapterMap} bindings for {@link IViewer} and all
	 * sub-classes. May be overwritten by sub-classes to change the default
	 * bindings.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IViewer} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAdaptersContexts(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		// root part
		bindPaletteRootPartAsPaletteContextsViewerAdapter(adapterMapBinder);

		// feedback and handles factories
		bindSelectionFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindFocusFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverFeedbackFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverHandleFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});
	}

	/**
	 * Adds a binding for {@link FXPaletteViewer} to the {@link AdapterMap}
	 * binder for {@link IDomain}.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IDomain} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindPaletteViewerAsDomainAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_FEATURES)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_CONTEXTS)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_OPERATORS)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(PALETTE_VIEWER_ROLE_ARITHMETICS)).to(IViewer.class);
		adapterMapBinder.addBinding(AdapterKey.role(EVOLUTION_VIEWER_ROLE)).to(IViewer.class);
	}

	protected void bindRectangleSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(BendFirstAnchorageOnSegmentHandleDragHandler.class);
	}

	protected void bindSelectAllOnTypeHandlerAsContentViewerRootPartAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SelectAllOnTypeHandler.class);
	}

	protected void bindSelectionFeedbackFactoryAsPaletteViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(SelectionBehavior.SELECTION_FEEDBACK_PART_FACTORY))
				.to(DefaultSelectionFeedbackPartFactory.class);
	}

	protected void bindSelectionHandleFactoryAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(SelectionBehavior.SELECTION_HANDLE_PART_FACTORY))
				.to(DefaultSelectionHandlePartFactory.class);
	}

	@Override
	protected void bindSelectionHandlePartFactoryAsContentViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(SelectionBehavior.SELECTION_HANDLE_PART_FACTORY))
				.to(GraphicalEditorSelectionHandlePartFactory.class);
	}

	protected void bindSelectionModelAsPaletteViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SelectionModel.class);
	}

	protected void bindSquareSegmentHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// single selection: resize relocate on handle drag without modifier
		// adapterMapBinder.addBinding(AdapterKey.defaultRole())
		// .to(ResizeTranslateFirstAnchorageOnHandleDragHandler.class);
		
		//TODO: Wieder einfügen
		//adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizeTransformIncludeAnchoragesHandler.class);
		// rotate on drag + control
		// adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(RotateSelectedOnHandleDragHandler.class);

		// multi selection: scale relocate on handle drag without modifier
		adapterMapBinder.addBinding(AdapterKey.defaultRole())
				.to(ResizeTransformSelectedIncludingAnchoragesOnHandleDragHandler.class);
	}

	@Override
	protected void configure() {
		super.configure();

		bindIContentPartFactory();

		bindDragOverGestureAsDomainAdapter(AdapterMaps.getAdapterMapBinder(binder(), IDomain.class));


		// content viewer
		bindGeometricShapePartAdaptersInContentViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));

		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(), TextPart.class,
				AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));


		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				ChoiceBoxFeatureAttributePart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));

		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));

		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				ArithmeticalOperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));

		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				ChoiceBoxTemporalElementPart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));
		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				ChoiceBoxFeaturePart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));
		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				ChoiceBoxValuePart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));
		bindGeometricShapePartAdaptersInContentViewerContext3(AdapterMaps.getAdapterMapBinder(binder(),
				TextFieldPart.class, AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE)));

		// node selection handles and multi selection handles
		bindSquareSegmentHandlePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), SquareSegmentHandlePart.class));

		// curve selection handles
		bindCircleSegmentHandlePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), CircleSegmentHandlePart.class));

		bindRectangleSegmentHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), RectangleSegmentHandlePart.class));

		// hover handles
		bindContextMenuHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), BlockContextViewHandlePart.class));
		// hover handles
//				bindDeleteHandlePartAdapters(
//						AdapterMaps.getAdapterMapBinder(binder(), GeometricElementDeletionHandlePart.class));
		


		bindPaletteViewerAdapters(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE)));
		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE)));

		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE)));
		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				ArithmeticalOperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE)));

		bindGeometricShapePartAdapterInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE)));

		bindPaletteViewerAdaptersOperators(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_OPERATORS)));
		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_OPERATORS)));

		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_OPERATORS)));
		bindGeometricShapePartAdapterInPaletteViewerContext2(
				AdapterMaps.getAdapterMapBinder(binder(), ArithmeticalOperatorFixedBlockPart.class,
						AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_OPERATORS)));

		bindGeometricShapePartAdapterInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_OPERATORS)));

		bindPaletteViewerAdaptersArithmetics(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_ARITHMETICS)));
		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_ARITHMETICS)));

		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_ARITHMETICS)));
		bindGeometricShapePartAdapterInPaletteViewerContext2(
				AdapterMaps.getAdapterMapBinder(binder(), ArithmeticalOperatorFixedBlockPart.class,
						AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_ARITHMETICS)));

		bindGeometricShapePartAdapterInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_ARITHMETICS)));


		bindPaletteViewerAdaptersFeatures(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_FEATURES)));

		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_FEATURES)));

		bindGeometricShapePartAdapterInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_FEATURES)));


		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(), TextPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_FEATURES)));
		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_FEATURES)));

		bindPaletteViewerAdaptersContexts(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_CONTEXTS)));
		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_CONTEXTS)));
		bindGeometricShapePartAdapterInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(),
				GeometricShapePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_CONTEXTS)));


		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				OperatorFixedBlockPart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_CONTEXTS)));

		bindGeometricShapePartAdapterInPaletteViewerContext2(AdapterMaps.getAdapterMapBinder(binder(),
				ChoiceBoxFeatureAttributePart.class, AdapterKey.get(IViewer.class, PALETTE_VIEWER_ROLE_CONTEXTS)));


		bindEvolutionViewerAdapters(AdapterMaps.getAdapterMapBinder(binder(), IViewer.class,
				AdapterKey.get(IViewer.class, EVOLUTION_VIEWER_ROLE)));
		bindPaletteRootPartAdaptersInPaletteViewerContext(AdapterMaps.getAdapterMapBinder(binder(), IRootPart.class,
				AdapterKey.get(IViewer.class, EVOLUTION_VIEWER_ROLE)));

	}

	protected void bindEvolutionViewerAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {

		// viewer models
		bindFocusModelAsPaletteViewerAdapter(adapterMapBinder);
		bindHoverModelAsPaletteViewerAdapter(adapterMapBinder);
		bindSelectionModelAsPaletteViewerAdapter(adapterMapBinder);

		bindEvolutionRootPartAsEvolutionViewerAdapter(adapterMapBinder);
		// content part factory and content part pool
		bindContentPartPoolAsPaletteViewerAdapter(adapterMapBinder);
		bindIContentPartFactoryAsPaletteViewerAdapter(adapterMapBinder);

		// change hover feedback color by binding a respective provider
		adapterMapBinder.addBinding(AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_COLOR_PROVIDER))
				.toInstance(new Provider<Color>() {
					@Override
					public Color get() {
						return Color.WHITE;
					}
				});

	}


	@Override
	protected void enableAdapterMapInjection() {
		install(new AdapterInjectionSupport(LoggingMode.PRODUCTION));
	}

	/**
	 * Binds {@link ClickDragGesture} to the {@link IDomain} adaptable scope.
	 */
	protected void bindDragOverGesture() {
		binder().bind(DragOverGesture.class);
	}

	/**
	 * Adds a binding for {@link ClickDragGesture} to the adapter map binder for
	 * {@link IDomain}.
	 *
	 * @param adapterMapBinder
	 *            The {@link MapBinder} to be used for the binding registration.
	 *            In this case, will be obtained from
	 *            {@link AdapterMaps#getAdapterMapBinder(Binder, Class)} using
	 *            {@link IDomain} as a key.
	 *
	 * @see AdapterMaps#getAdapterMapBinder(Binder, Class)
	 */
	protected void bindDragOverGestureAsDomainAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DragOverGesture.class);
	}

}