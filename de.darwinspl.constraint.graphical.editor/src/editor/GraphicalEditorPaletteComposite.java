package editor;

import org.eclipse.gef.fx.nodes.InfiniteCanvas;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.gef.mvc.fx.viewer.InfiniteCanvasViewer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GraphicalEditorPaletteComposite {
	
	private final AnchorPane paletteContentPane;
	
	private final VBox paletteVbox;
	
	private final Button closePaletteButton;
	
	

	
	public GraphicalEditorPaletteComposite(IViewer paletteViewerControl, IViewer paletteViewerFeatures,
			IViewer paletteViewerContexts, IViewer paletteViewerOperators, IViewer paletteViewerArithmetics) {
		
		
		final InfiniteCanvas paletteRootNode = ((InfiniteCanvasViewer) paletteViewerControl).getCanvas();
		final InfiniteCanvas paletteRootNodeFeatures = ((InfiniteCanvasViewer) paletteViewerFeatures).getCanvas();
		final InfiniteCanvas paletteRootNodeContexts = ((InfiniteCanvasViewer) paletteViewerContexts).getCanvas();
		final InfiniteCanvas paletteRootNodeOperators = ((InfiniteCanvasViewer) paletteViewerOperators).getCanvas();
		final InfiniteCanvas paletteRootNodeArithmetics = ((InfiniteCanvasViewer) paletteViewerArithmetics).getCanvas();
		
		
		
		
		paletteContentPane = new AnchorPane();
		paletteContentPane.getChildren().addAll(paletteRootNode, paletteRootNodeOperators, paletteRootNodeArithmetics,
				paletteRootNodeFeatures, paletteRootNodeContexts);
		
		
		HBox buttonHbox = new HBox();
		
		VBox buttonVBoxRight = new VBox();
		
		VBox buttonVBoxLeft = new VBox();
		
		
		closePaletteButton = new Button("Close Palette");
		buttonVBoxLeft.getChildren().add(closePaletteButton);
		
		Button rootButton = new Button("Root");

		rootButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.BLUE));
		String rootStyle = getClass().getResource("/resources/css/rootButton.css").toExternalForm();
		rootButton.getStylesheets().addAll(rootStyle);

		Button featureButton = new Button("Features");

		featureButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.LIGHTBLUE));
		String style = getClass().getResource("/resources/css/featureButton.css").toExternalForm();
		featureButton.getStylesheets().addAll(style);

		Button controlButton = new Button("Controls");
		controlButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.YELLOW));
		String styleControlButton = getClass().getResource("/resources/css/controlButton.css").toExternalForm();
		controlButton.getStylesheets().addAll(styleControlButton);

		// Button operatorsButton = new Button("Operators");
		Button contextButton = new Button("Contexts");
		contextButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.ORANGE));
		String styleContextButton = getClass().getResource("/resources/css/contextButton.css").toExternalForm();
		contextButton.getStylesheets().addAll(styleContextButton);

		Button operatorButton = new Button("Operators");
		operatorButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.LIGHTGREEN));
		String styleOperatorButton = getClass().getResource("/resources/css/operatorButton.css").toExternalForm();
		operatorButton.getStylesheets().addAll(styleOperatorButton);

		Button arithmeticsButton = new Button("Arithmetics");
		arithmeticsButton.setGraphic(new javafx.scene.shape.Rectangle(7, 20, Color.SADDLEBROWN));
		String styleArithmeticsButton = getClass().getResource("/resources/css/arithmeticsButton.css").toExternalForm();
		arithmeticsButton.getStylesheets().addAll(styleArithmeticsButton);
		
		buttonHbox.getChildren().addAll(buttonVBoxRight, buttonVBoxLeft);
		
        HBox.setHgrow(buttonVBoxRight, Priority.ALWAYS);
		
		rootButton.setMaxWidth(Double.MAX_VALUE);
		featureButton.setMaxWidth(Double.MAX_VALUE);
		controlButton.setMaxWidth(Double.MAX_VALUE);

		contextButton.setMaxWidth(Double.MAX_VALUE);
		operatorButton.setMaxWidth(Double.MAX_VALUE);
		arithmeticsButton.setMaxWidth(Double.MAX_VALUE);

		rootButton.setAlignment(Pos.BASELINE_LEFT);
		featureButton.setAlignment(Pos.BASELINE_LEFT);
		controlButton.setAlignment(Pos.BASELINE_LEFT);
		// operatorsButton.setAlignment(Pos.BASELINE_LEFT);
		contextButton.setAlignment(Pos.BASELINE_LEFT);
		operatorButton.setAlignment(Pos.BASELINE_LEFT);
		arithmeticsButton.setAlignment(Pos.BASELINE_LEFT);

		buttonVBoxRight.getChildren().addAll(controlButton, operatorButton, arithmeticsButton, featureButton,
				contextButton);

		
		paletteVbox = new VBox();
		buttonVBoxRight.setStyle("-fx-border-color:transparent transparent lightblue transparent; ");
		paletteVbox.getChildren().addAll(buttonHbox, paletteContentPane);
		
		VBox.setVgrow(paletteContentPane, Priority.ALWAYS);
		
		
		AnchorPane.setBottomAnchor(paletteRootNode, 0d);
		AnchorPane.setLeftAnchor(paletteRootNode, 0d);
		AnchorPane.setTopAnchor(paletteRootNode, 0d);
		paletteRootNode.prefWidthProperty().bind(paletteContentPane.widthProperty());
		
		
		
		AnchorPane.setBottomAnchor(paletteRootNodeFeatures, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeFeatures, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeFeatures, 0d);
		paletteRootNodeFeatures.prefWidthProperty().bind(paletteContentPane.widthProperty());
		
		AnchorPane.setBottomAnchor(paletteRootNodeContexts, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeContexts, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeContexts, 0d);
//		paletteRootNodeContexts.prefWidthProperty().bind(palettePane.widthProperty());

		AnchorPane.setBottomAnchor(paletteRootNodeOperators, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeOperators, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeOperators, 0d);
		paletteRootNodeOperators.prefWidthProperty().bind(paletteContentPane.widthProperty());

		AnchorPane.setBottomAnchor(paletteRootNodeArithmetics, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeArithmetics, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeArithmetics, 0d);
		
		
		paletteRootNodeArithmetics.prefWidthProperty().bind(paletteContentPane.widthProperty());


//		paletteRootNodeFeatures.setStyle("-fx-background-color: white;");

		// disable grid layer for palette
		paletteRootNode.setZoomGrid(false);
		paletteRootNode.setShowGrid(false);

		paletteRootNodeFeatures.setZoomGrid(false);
		paletteRootNodeFeatures.setShowGrid(false);

		paletteRootNodeContexts.setZoomGrid(false);
		paletteRootNodeContexts.setShowGrid(false);

		paletteRootNodeOperators.setZoomGrid(false);
		paletteRootNodeOperators.setShowGrid(false);

		paletteRootNodeArithmetics.setZoomGrid(false);
		paletteRootNodeArithmetics.setShowGrid(false);
//
//		evolutionRootNode.setZoomGrid(false);
//		evolutionRootNode.setShowGrid(false);

		// disable horizontal scrollbar for palette
		paletteRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeFeatures.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeContexts.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeOperators.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeArithmetics.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
//
//		evolutionRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.AS_NEEDED);

		// show only control paletteview
		paletteRootNode.setVisible(true);
		paletteRootNodeFeatures.setVisible(false);
		paletteRootNodeContexts.setVisible(false);
		paletteRootNodeOperators.setVisible(false);
		paletteRootNodeArithmetics.setVisible(false);

		featureButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				paletteRootNode.setVisible(false);
				paletteRootNodeContexts.setVisible(false);
				paletteRootNodeOperators.setVisible(false);
				paletteRootNodeArithmetics.setVisible(false);
				paletteRootNodeFeatures.setVisible(true);

			}
		});

		controlButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				paletteRootNodeFeatures.setVisible(false);
				paletteRootNodeContexts.setVisible(false);
				paletteRootNodeOperators.setVisible(false);
				paletteRootNodeArithmetics.setVisible(false);
				paletteRootNode.setVisible(true);

			}
		});

		contextButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				paletteRootNode.setVisible(false);
				paletteRootNodeFeatures.setVisible(false);
				paletteRootNodeOperators.setVisible(false);
				paletteRootNodeArithmetics.setVisible(false);
				paletteRootNodeContexts.setVisible(true);

			}
		});

		operatorButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				paletteRootNode.setVisible(false);
				paletteRootNodeFeatures.setVisible(false);
				paletteRootNodeArithmetics.setVisible(false);
				paletteRootNodeContexts.setVisible(false);
				paletteRootNodeOperators.setVisible(true);

			}
		});

		arithmeticsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				paletteRootNode.setVisible(false);
				paletteRootNodeFeatures.setVisible(false);
				paletteRootNodeOperators.setVisible(false);
				paletteRootNodeContexts.setVisible(false);
				paletteRootNodeArithmetics.setVisible(true);

			}
		});
		
		
	}


	public AnchorPane getPaletteContentPane() {
		return paletteContentPane;
	}


	public VBox getPaletteVbox() {
		return paletteVbox;
	}


	public Button getClosePaletteButton() {
		return closePaletteButton;
	}
}
