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
package editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.gef.fx.nodes.InfiniteCanvas;
import org.eclipse.gef.mvc.fx.domain.HistoricizingDomain;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.gef.mvc.fx.viewer.InfiniteCanvasViewer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GraphicalEditorViewersComposite {

	public BorderPane getRootVBox() {
		return rootVBox;
	}

	private final HBox composite;

	private final VBox compositeVbox;

	private final BorderPane rootVBox;

	public GraphicalEditorViewersComposite(IViewer contentViewer, IViewer paletteViewer, IViewer paletteViewerFeatures,
			IViewer paletteViewerContexts, IViewer paletteViewerOperators, IViewer paletteViewerArithmetics,
			IViewer evolutionViewer, HistoricizingDomain domain) {
		// determine viewers' root nodes

		Parent contentRootNode = contentViewer.getCanvas();
		final InfiniteCanvas paletteRootNode = ((InfiniteCanvasViewer) paletteViewer).getCanvas();
		final InfiniteCanvas paletteRootNodeFeatures = ((InfiniteCanvasViewer) paletteViewerFeatures).getCanvas();
		final InfiniteCanvas paletteRootNodeContexts = ((InfiniteCanvasViewer) paletteViewerContexts).getCanvas();
		final InfiniteCanvas evolutionRootNode = ((InfiniteCanvasViewer) evolutionViewer).getCanvas();
		final InfiniteCanvas paletteRootNodeOperators = ((InfiniteCanvasViewer) paletteViewerOperators).getCanvas();
		final InfiniteCanvas paletteRootNodeArithmetics = ((InfiniteCanvasViewer) paletteViewerArithmetics).getCanvas();

		// arrange viewers above each other
		AnchorPane viewersPane = new AnchorPane();
		viewersPane.getChildren().addAll(contentRootNode);

		AnchorPane palettePane = new AnchorPane();
		palettePane.getChildren().addAll(paletteRootNode, paletteRootNodeOperators, paletteRootNodeArithmetics,
				paletteRootNodeFeatures, paletteRootNodeContexts);

		AnchorPane evolutionPane = new AnchorPane();
		evolutionPane.getChildren().add(evolutionRootNode);

	
		compositeVbox = new VBox();

		HBox buttonHbox = new HBox();
		VBox buttonVboxRightSide = new VBox();
		
		
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



		HBox.setHgrow(buttonVboxRightSide, Priority.ALWAYS);
		
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

		buttonVboxRightSide.getChildren().addAll(controlButton, operatorButton, arithmeticsButton, featureButton,
				contextButton);
		buttonHbox.getChildren().addAll(buttonVboxRightSide);
		buttonHbox.setStyle("-fx-border-color:transparent transparent lightblue transparent; ");
		compositeVbox.getChildren().addAll(buttonHbox, palettePane);
		compositeVbox.setStyle("-fx-background-color: transparent; -fx-border-color:lightblue;");
		
		composite = new HBox();
		// XXX: Set transparent background for the composite HBox, because
		// otherwise, the HBox will have a grey background.
		composite.setStyle("-fx-background-color: transparent;");
		composite.getChildren().addAll(compositeVbox, viewersPane);

		// ensure composite fills the whole space
		composite.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		composite.setMinSize(0, 0);
		composite.setFillHeight(true);

		// no spacing between viewers and palette indicator
		composite.setSpacing(0d);

		rootVBox = new BorderPane();
		rootVBox.setStyle("-fx-background-color: transparent;");
//		rootVBox.setTop(createButtonBar(domain));
    	rootVBox.setCenter(viewersPane);
		rootVBox.setBottom(evolutionPane);
		rootVBox.setLeft(compositeVbox);
//		rootVBox.getChildren().addAll(composite, evolutionPane);
		rootVBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		rootVBox.setFillWidth(true);
//		rootVBox.setSpacing(0d);
//		VBox.setVgrow(composite, Priority.ALWAYS);
//		VBox.setVgrow(evolutionPane, Priority.NEVER);



		// ensure viewers fill the space
		HBox.setHgrow(viewersPane, Priority.ALWAYS);

		
		

//		viewersPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		// palettePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		AnchorPane.setBottomAnchor(contentRootNode, 0d);
//		AnchorPane.setLeftAnchor(contentRootNode, 0d);
		AnchorPane.setRightAnchor(contentRootNode, 0d);
		AnchorPane.setTopAnchor(contentRootNode, 0d);
		AnchorPane.setBottomAnchor(paletteRootNode, 0d);
		AnchorPane.setLeftAnchor(paletteRootNode, 0d);
		AnchorPane.setTopAnchor(paletteRootNode, 0d);
		paletteRootNode.prefWidthProperty().bind(palettePane.widthProperty());
		
		AnchorPane.setBottomAnchor(paletteRootNodeFeatures, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeFeatures, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeFeatures, 0d);
		paletteRootNodeFeatures.prefWidthProperty().bind(palettePane.widthProperty());
		
		AnchorPane.setBottomAnchor(paletteRootNodeContexts, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeContexts, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeContexts, 0d);
//		paletteRootNodeContexts.prefWidthProperty().bind(palettePane.widthProperty());

		AnchorPane.setBottomAnchor(paletteRootNodeOperators, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeOperators, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeOperators, 0d);
		paletteRootNodeOperators.prefWidthProperty().bind(palettePane.widthProperty());

		AnchorPane.setBottomAnchor(paletteRootNodeArithmetics, 0d);
		AnchorPane.setLeftAnchor(paletteRootNodeArithmetics, 0d);
		AnchorPane.setTopAnchor(paletteRootNodeArithmetics, 0d);
		paletteRootNodeArithmetics.prefWidthProperty().bind(palettePane.widthProperty());

		AnchorPane.setBottomAnchor(evolutionRootNode, 0d);
		AnchorPane.setLeftAnchor(evolutionRootNode, 0d);
		AnchorPane.setRightAnchor(evolutionRootNode, 0d);
		AnchorPane.setTopAnchor(evolutionRootNode, 0d);

		evolutionRootNode.setStyle("-fx-background-color: white;");
		paletteRootNodeFeatures.setStyle("-fx-background-color: white;");

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

		evolutionRootNode.setZoomGrid(false);
		evolutionRootNode.setShowGrid(false);

		// disable horizontal scrollbar for palette
		paletteRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeFeatures.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeContexts.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeOperators.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		paletteRootNodeArithmetics.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);

		evolutionRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.AS_NEEDED);

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

	public Parent getComposite() {
		return composite;
	}

	public Parent getVBoxComposite() {
		return compositeVbox;
	}
	
//	/**
//	 * Creates the undo/redo buttons
//	 *
//	 * @return
//	 */
//	private Node createButtonBar(HistoricizingDomain domain) {
//		Button undoButton = new Button("Undo");
//		undoButton.setDisable(true);
//		undoButton.setOnAction((e) -> {
//			try {
//				domain.getOperationHistory().undo(domain.getUndoContext(), null, null);
//			} catch (ExecutionException e1) {
//				e1.printStackTrace();
//			}
//		});
//
//		Button redoButton = new Button("Redo");
//		redoButton.setDisable(true);
//		redoButton.setOnAction((e) -> {
//			try {
//				domain.getOperationHistory().redo(domain.getUndoContext(), null, null);
//			} catch (ExecutionException e1) {
//				e1.printStackTrace();
//			}
//		});
//
//		// add listener to the operation history of our domain
//		// to enable/disable undo/redo buttons as needed
//		domain.getOperationHistory().addOperationHistoryListener((e) -> {
//			IUndoContext ctx = domain.getUndoContext();
//			undoButton.setDisable(!e.getHistory().canUndo(ctx));
//			redoButton.setDisable(!e.getHistory().canRedo(ctx));
//		});
//
//		return new HBox(10, undoButton, redoButton);
//	}
	
	
	
	


}
