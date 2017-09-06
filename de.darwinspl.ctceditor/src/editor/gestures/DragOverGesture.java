package editor.gestures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.mvc.fx.gestures.AbstractGesture;
import org.eclipse.gef.mvc.fx.handlers.IHandler;
import org.eclipse.gef.mvc.fx.parts.PartUtils;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.handlers.IOnDragOverHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;

/**
 * Catches Drag over gestures and notifies the respective handlers
 * 
 * needed to attach the blocks
 * 
 * @author Anna-Liisa
 *
 */
public class DragOverGesture extends AbstractGesture {

	/**
	 *
	 */
	public static final Class<IOnDragOverHandler> ON_DRAG_OVER_POLICY_KEY = IOnDragOverHandler.class;

	private final Set<Scene> scenes = Collections.newSetFromMap(new IdentityHashMap<>());

	private IViewer activeViewer;
	private Node pressed;

	/**
	 * This {@link EventHandler} is registered as an event filter on the
	 * {@link Scene} to handle drag and release events.
	 */
	private EventHandler<? super MouseDragEvent> mouseFilter = new EventHandler<MouseDragEvent>() {
		@Override
		public void handle(MouseDragEvent event) {
			// determine pressed/dragged/released state
			EventType<? extends Event> type = event.getEventType();

			if (type.equals(MouseDragEvent.MOUSE_DRAG_RELEASED)) {

				EventTarget target = event.getTarget();

				Object source = event.getSource();
				if (source instanceof GeometryNode) {

					pressed = (Node) target;
					prepareActionOnEvent(pressed, event);

					mouseDragReleased(event, event.getSceneX(), event.getSceneY());
				}

				if (source instanceof Scene) {

					prepareActionOnEvent(pressed, event);

					mouseDragReleasedOnScene(event, event.getSceneX(), event.getSceneY());

				}

			}

			if (type.equals(MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET)) {
				EventTarget target = event.getTarget();
				if (target instanceof GeometryNode) {

					pressed = (Node) target;
					prepareActionOnEvent(pressed, event);

					mouseDragEnteredTarget(event, event.getSceneX(), event.getSceneY());
					pressed.setOnMouseDragReleased(mouseFilter);
				}
			}

			if (type.equals(MouseDragEvent.MOUSE_DRAG_EXITED_TARGET)) {
				EventTarget target = event.getTarget();
				if (target instanceof GeometryNode) {

					pressed = (Node) target;
					prepareActionOnEvent(pressed, event);

					mouseDragExitedTarget(event, event.getSceneX(), event.getSceneY());
				}
			}

		}

	};

	@Override
	protected void doActivate() {
		super.doActivate();
		for (final IViewer viewer : getDomain().getViewers().values()) {
			// register a viewer focus change listener
			viewer.viewerFocusedProperty().addListener(viewerFocusChangeListener);

			Scene scene = viewer.getCanvas().getScene();

			if (scenes.contains(scene)) {
				// already registered for this scene
				continue;
			}

			// // register mouse move filter for forwarding events to drag
			// policies
			// // that can show a mouse cursor to indicate their action
			// scene.addEventFilter(MouseEvent.MOUSE_MOVED,
			// indicationCursorMouseMoveFilter);
			// // register key event filter for forwarding events to drag
			// policies
			// // that can show a mouse cursor to indicate their action
			// scene.addEventFilter(KeyEvent.ANY, indicationCursorKeyFilter);
			// register mouse filter for forwarding press, drag, and release
			// events
			scene.addEventFilter(MouseDragEvent.ANY, mouseFilter);
			scenes.add(scene);
		}
	}

	@Override
	protected void doDeactivate() {
		for (Scene scene : new ArrayList<>(scenes)) {
			scene.removeEventFilter(MouseDragEvent.ANY, mouseFilter);
			// scene.removeEventFilter(MouseDragEvent.MOUSE_MOVED,
			// indicationCursorMouseMoveFilter);

		}
		for (final IViewer viewer : getDomain().getViewers().values()) {
			viewer.viewerFocusedProperty().removeListener(viewerFocusChangeListener);
		}
		super.doDeactivate();
	}

	/**
	 * @author Anna-Liisa
	 * @param viewer
	 *            kk
	 * @return jj
	 */
	@SuppressWarnings("unchecked")
	public List<IOnDragOverHandler> getActiveOverHandlers(IViewer viewer) {
		return (List<IOnDragOverHandler>) super.getActiveHandlers(viewer);
	}

	protected void prepareActionOnEvent(Node target, MouseDragEvent event) {

		activeViewer = PartUtils.retrieveViewer(getDomain(), target);

		// determine drag policies
		List<? extends IOnDragOverHandler> policies = null;
		if (activeViewer != null) {
			// XXX: A click policy could have changed the visual
			// hierarchy so that the viewer cannot be determined for
			// the target node anymore. If that is the case, no drag
			// policies should be notified about the event.
			policies = getTargetPolicyResolver().resolve(DragOverGesture.this, target, activeViewer,
					ON_DRAG_OVER_POLICY_KEY);
		}

		// abort processing of this gesture if no drag policies
		// could be found
		if (policies == null || policies.isEmpty()) {

			policies = null;
			return;
		}

		setActiveHandlers(activeViewer, policies);
	}

	/**
	 *
	 * 
	 * @param target
	 *            k
	 * @param event
	 *            j
	 * @param dx
	 *            j
	 * @param dy
	 *            j
	 *
	 */
	protected void mouseDragEnteredTarget(MouseDragEvent event, double dx, double dy) {

	
		if (activeViewer == null || getActiveHandlers(activeViewer).isEmpty()) {
			return;
		}

		for (IOnDragOverHandler policy : getActiveOverHandlers(activeViewer)) {
			policy.mouseDragEnteredTarget(event, new Dimension(dx, dy));
		}
	}

	protected void mouseDragExitedTarget(MouseDragEvent event, double dx, double dy) {
		if (activeViewer == null || getActiveHandlers(activeViewer).isEmpty()) {
			return;
		}

		for (IOnDragOverHandler policy : getActiveOverHandlers(activeViewer)) {
			policy.mouseDragExitedTarget(event, new Dimension(dx, dy));
		}
	}

	protected void mouseDragReleasedOnScene(MouseDragEvent event, double dx, double dy) {
		if (activeViewer == null || getActiveHandlers(activeViewer).isEmpty()) {
			return;
		}

		for (IOnDragOverHandler policy : getActiveOverHandlers(activeViewer)) {
			policy.mouseDragReleaseOnScene(event, new Dimension(dx, dy));
		}
	}

	protected void mouseDragReleased(MouseDragEvent event, double dx, double dy) {
		if (activeViewer == null || getActiveHandlers(activeViewer).isEmpty()) {
			return;
		}

		for (IOnDragOverHandler policy : getActiveOverHandlers(activeViewer)) {
			policy.mouseDragReleased(event, new Dimension(dx, dy));
		}
	}

	private ChangeListener<Boolean> viewerFocusChangeListener = new ChangeListener<Boolean>() {
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			// cannot abort if no activeViewer
			if (activeViewer == null) {
				return;
			}
			// check if any viewer is focused
			for (IViewer v : getDomain().getViewers().values()) {
				if (v.isViewerFocused()) {
					return;
				}
			}
			// no viewer is focused => abort
			// cancel target policies
			for (IHandler handler : getActiveHandlers(activeViewer)) {
				if (handler instanceof IOnDragOverHandler) {
					((IOnDragOverHandler) handler).abortDrag();
				}
			}
			// clear active policies
			clearActiveHandlers(activeViewer);
			activeViewer = null;
			// close execution transaction
			getDomain().closeExecutionTransaction(DragOverGesture.this);
		}
	};

}
