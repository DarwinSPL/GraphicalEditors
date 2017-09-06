package editor.handlers;

import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.mvc.fx.handlers.TranslateSelectedOnDragHandler;

import javafx.scene.input.MouseEvent;

public class TranslateOnDragHandler extends TranslateSelectedOnDragHandler {

	@Override
	public void startDrag(MouseEvent e) {
		// getHost().getVisual().startFullDrag();
		getHost().getVisual().setMouseTransparent(true);

		super.startDrag(e);
	}

	@Override
	public void endDrag(MouseEvent e, Dimension delta) {
		getHost().getVisual().setMouseTransparent(false);
		super.endDrag(e, delta);

	}
}
