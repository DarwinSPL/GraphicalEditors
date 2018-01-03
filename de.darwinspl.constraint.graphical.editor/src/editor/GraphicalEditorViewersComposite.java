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

import org.eclipse.gef.mvc.fx.viewer.IViewer;

import editor.handlers.PaletteFocusBehavior;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;



public class GraphicalEditorViewersComposite {
	
	private static final double PALETTE_INDICATOR_WIDTH = 10d;

	public AnchorPane getRootVBox() {
		return rootVBox;
	}

	private final HBox composite;


	private final AnchorPane rootVBox;
	
	
	public GraphicalEditorViewersComposite(IViewer contentViewer, IViewer paletteViewerControl, IViewer paletteViewerFeatures,
			IViewer paletteViewerContexts, IViewer paletteViewerOperators, IViewer paletteViewerArithmetics, IViewer evolutionViewer) {
		

		rootVBox = new AnchorPane();

		AnchorPane evolutionPane = (new GraphicalEditorEvolutionView(evolutionViewer)).getEvolutionPane();
		
		rootVBox.setStyle("-fx-background-color: white;");
		GraphicalEditorPaletteComposite paletteComposite = new GraphicalEditorPaletteComposite(paletteViewerControl, paletteViewerFeatures, paletteViewerContexts, paletteViewerOperators, paletteViewerArithmetics);
		VBox paletteRootNode = paletteComposite.getPaletteVbox();
		
	
		// determine viewers' root nodes
		Parent contentRootNode = contentViewer.getCanvas();
		
//		AnchorPane paletteRootNode = new AnchorPane();
//		paletteRootNode.getChildren().add(paletteVBox);
		// arrange viewers above each other
		
		AnchorPane viewersPane = new AnchorPane();
		viewersPane.getChildren().addAll(contentRootNode, paletteRootNode);
		
		// create palette indicator
		Pane paletteIndicator = new Pane();
		paletteIndicator.setStyle("-fx-background-color: rgba(128,128,128,1);");
		paletteIndicator.setMaxSize(PALETTE_INDICATOR_WIDTH, Double.MAX_VALUE);
		paletteIndicator.setMinSize(PALETTE_INDICATOR_WIDTH, 0d);

		// show palette indicator next to the viewer area
		composite = new HBox();
		
		// XXX: Set transparent background for the composite HBox, because
		// otherwise, the HBox will have a grey background.
		composite.setStyle("-fx-background-color: transparent;");
		composite.getChildren().addAll(paletteIndicator, viewersPane);

		// ensure composite fills the whole space
		composite.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		composite.setMinSize(0, 0);
		composite.setFillHeight(true);

		// no spacing between viewers and palette indicator
		composite.setSpacing(0d);

		
		// ensure viewers fill the space
		HBox.setHgrow(viewersPane, Priority.ALWAYS);
		
		
	
		
		viewersPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		double evolutionPaneHight =  evolutionPane.getHeight();
//		evolutionPane.heightProperty().addListener(new ChangeListener<Number>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				double evolutionPaneHight2 =  evolutionPane.getHeight();
//				AnchorPane.setBottomAnchor(contentRootNode, evolutionPaneHight2);
//				
//			}
//		});
		AnchorPane.setBottomAnchor(contentRootNode, 0d);
		AnchorPane.setLeftAnchor(contentRootNode, 0d);
		AnchorPane.setRightAnchor(contentRootNode, 0d);
		AnchorPane.setTopAnchor(contentRootNode, 0d);
		AnchorPane.setBottomAnchor(paletteRootNode, 0d);
		AnchorPane.setLeftAnchor(paletteRootNode, 0d);
		AnchorPane.setTopAnchor(paletteRootNode, 0d);
		
	
		
		rootVBox.getChildren().addAll(composite, evolutionPane);
		
		evolutionPane.setMaxSize(Double.MAX_VALUE, 40d);
		
		AnchorPane.setTopAnchor(composite, 0d);
		AnchorPane.setRightAnchor(composite, 0d);
		AnchorPane.setLeftAnchor(composite, 0d);
		AnchorPane.setBottomAnchor(composite, 50d);
		
		AnchorPane.setBottomAnchor(evolutionPane, 0d);
		AnchorPane.setLeftAnchor(evolutionPane, 0d);
		AnchorPane.setRightAnchor(evolutionPane, 0d);
		
		
		// disable grid layer for palette
//		paletteRootNode.setZoomGrid(false);
//		paletteRootNode.setShowGrid(false);
//
//		// disable horizontal scrollbar for palette
//		paletteRootNode.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);

		// set palette background
		paletteRootNode.setStyle(PaletteFocusBehavior.DEFAULT_STYLE);

		// hide palette at first
		paletteRootNode.setVisible(false);

		// register listener to show/hide palette
		paletteIndicator.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				paletteRootNode.setVisible(true);
			}
		});
		paletteIndicator.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(paletteRootNode.isVisible()){
					paletteRootNode.setVisible(false);
				}
				
			}
		});
		
		
		//register listener to hide palette when user clicks on contentrootnode
		contentRootNode.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				paletteRootNode.setVisible(false);
				
			}
			
			
		});
		
		paletteComposite.getClosePaletteButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				paletteRootNode.setVisible(false);
				
			}
		});
//		paletteRootNode.setOnMouseExited(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				paletteRootNode.setVisible(false);
//			}
//		});
		
//		rootVBox.getChildren().addAll(composite, evolutionPane);
//		
//		VBox.setVgrow(composite, Priority.ALWAYS);
////		VBox.setVgrow(evolutionPane, Priority.ALWAYS);
//		VBox.setVgrow(evolutionPane, Priority.NEVER);
//		rootVBox.set

		//		paletteRootNode.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
//			@Override
//			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
//				double scrollBarWidth = paletteRootNode.getVerticalScrollBar().isVisible()
//						? paletteRootNode.getVerticalScrollBar().getLayoutBounds().getWidth() : 0;
//				paletteRootNode.setPrefWidth(newValue.getWidth() + scrollBarWidth);
//			}
//		});
		
		
//		paletteRootNode.getVerticalScrollBar().visibleProperty().addListener(new ChangeListener<Boolean>() {
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				double contentWidth = paletteRootNode.getContentGroup().getLayoutBounds().getWidth();
//				double scrollBarWidth = newValue ? paletteRootNode.getVerticalScrollBar().getLayoutBounds().getWidth()
//						: 0;
//				paletteRootNode.setPrefWidth(contentWidth + scrollBarWidth);
//			}
//		});

		// hide palette when a palette element is pressed
//		paletteRootNode.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				if (event.getTarget() != paletteRootNode) {
//					paletteRootNode.setVisible(false);
//				}
//			}
//		});
	}
	
	



	public Parent getComposite() {
		return composite;
	}



	
	


}
