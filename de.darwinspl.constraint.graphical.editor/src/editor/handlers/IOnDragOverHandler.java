package editor.handlers;

import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.mvc.fx.gestures.ClickDragGesture;
import org.eclipse.gef.mvc.fx.handlers.IHandler;

import javafx.scene.input.MouseDragEvent;

/**
 * An interaction handler that implements the {@link IOnDragOverHandler}
 * interface will be notified about mouse press-drag-release events by the
 * {@link ClickDragGesture}
 * 
 * @author Anna-Liisa
 *
 */
public interface IOnDragOverHandler extends IHandler {

	void mouseDragOver(MouseDragEvent event, Dimension delta);

	void mouseDragEnteredTarget(MouseDragEvent event, Dimension delta);

	void mouseDragExitedTarget(MouseDragEvent event, Dimension delta);

	void mouseDragReleased(MouseDragEvent event, Dimension delta);

	void abortDrag();

	void mouseDragReleaseOnScene(MouseDragEvent event, Dimension delta);
}
