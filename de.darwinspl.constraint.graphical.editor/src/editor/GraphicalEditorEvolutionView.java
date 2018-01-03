package editor;

import org.eclipse.gef.fx.nodes.InfiniteCanvas;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.gef.mvc.fx.viewer.InfiniteCanvasViewer;

import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;

public class GraphicalEditorEvolutionView {
	
	private final AnchorPane evolutionPane;
	
	
	public GraphicalEditorEvolutionView(IViewer viewer) {
		
		final InfiniteCanvas evolutionRootNode = ((InfiniteCanvasViewer) viewer).getCanvas();
        
		evolutionPane = new AnchorPane();
		evolutionPane.getChildren().add(evolutionRootNode);

		evolutionRootNode.setZoomGrid(false);
		evolutionRootNode.setShowGrid(false);
		

		evolutionRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		AnchorPane.setBottomAnchor(evolutionRootNode, 0d);
		AnchorPane.setLeftAnchor(evolutionRootNode, 0d);
		AnchorPane.setRightAnchor(evolutionRootNode, 0d);

		evolutionRootNode.setStyle("-fx-background-color: white;");
	}


	public AnchorPane getEvolutionPane() {
		return evolutionPane;
	}

}
