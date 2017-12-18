/*******************************************************************************
 * Copyright (c) 2014, 2016 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API & implementation
 *
 *******************************************************************************/
package editor.handlers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import editor.model.AbstractBlockElement;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel.ArithmeticalOperatorType;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel.MinMaxType;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlBlockModel;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorAndBlockModel.OperatorAndORType;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonType;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonValueType;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.parts.GeometricShapePart;
import editor.parts.arithmetical.ArithmeticalOperationOperatorPart;
import editor.parts.arithmetical.ArithmeticalOperatorMovableBlockPart;
import editor.parts.control.ControlBlockPart;
import editor.parts.operator.OperatorAndBlockPart;
import editor.parts.operator.OperatorMovableBlockPart;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
/**
 * 
 * @author Anna-Liisa
 *
 * Class provides the context menu for the blocks
 *
 */
public class CreateContextMenuOnClickHandler extends AbstractHandler implements IOnClickHandler {

	private IVisualPart<? extends Node> targetBlock;

	
	private final EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {

			if (((OperatorMovableBlockPart) getTargetBlock()).getContent() instanceof OperatorComparisonBlockModel) {
				OperatorComparisonBlockModel target = (OperatorComparisonBlockModel) ((OperatorMovableBlockPart) getTargetBlock())
						.getContent();
				String text = ((MenuItem) event.getTarget()).getText();
				target.getFixedChildTextModel().setText(text);

				switch (text) {
				case "=":
					target.setComparisonType(OperatorComparisonType.EQUALS);
					break;
				case "!=":
					target.setComparisonType(OperatorComparisonType.NOT_EQUALS);
					break;
				case "<":
					target.setComparisonType(OperatorComparisonType.SMALLER);
					break;
				case ">":
					target.setComparisonType(OperatorComparisonType.BIGGER);
					break;
				case "<=":
					target.setComparisonType(OperatorComparisonType.EQUALS_OR_SMALLER);
					break;
				case ">=":
					target.setComparisonType(OperatorComparisonType.EQUALS_OR_BIGGER);
					break;
				default:
					break;
				}

				getTargetBlock().refreshVisual();
				for (IVisualPart<? extends Node> anchored : getTargetBlock().getAnchoredsUnmodifiable()) {
					anchored.refreshVisual();
				}

			}
			if (((OperatorMovableBlockPart) getTargetBlock()).getContent() instanceof OperatorNumercialComparisonBlockModel) {
				OperatorNumercialComparisonBlockModel target = (OperatorNumercialComparisonBlockModel) ((OperatorMovableBlockPart) getTargetBlock())
						.getContent();
				String text = ((MenuItem) event.getTarget()).getText();
				target.getFixedChildTextModel().setText(text);

				switch (text) {
				case "=":
					target.setComparisonType(OperatorComparisonType.EQUALS);
					break;
				case "!=":
					target.setComparisonType(OperatorComparisonType.NOT_EQUALS);
					break;
				case "<":
					target.setComparisonType(OperatorComparisonType.SMALLER);
					break;
				case ">":
					target.setComparisonType(OperatorComparisonType.BIGGER);
					break;
				case "<=":
					target.setComparisonType(OperatorComparisonType.EQUALS_OR_SMALLER);
					break;
				case ">=":
					target.setComparisonType(OperatorComparisonType.EQUALS_OR_BIGGER);
					break;
				default:
					break;
				}

				getTargetBlock().refreshVisual();
				for (IVisualPart<? extends Node> anchored : getTargetBlock().getAnchoredsUnmodifiable()) {
					anchored.refreshVisual();
				}

			}

		}
	};
	
	
	private final EventHandler<ActionEvent> changeArithmeticOperatorHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {

			if (((ArithmeticalOperationOperatorPart) getTargetBlock()).getContent() instanceof ArithmeticalOperationOperatorBlockModel) {
				ArithmeticalOperationOperatorBlockModel target = (ArithmeticalOperationOperatorBlockModel) ((ArithmeticalOperationOperatorPart) getTargetBlock())
						.getContent();
				String text = ((MenuItem) event.getTarget()).getText();
				target.getFixedChildTextModel().setText(text);

				switch (text) {
				case "+":
					target.setArithmeticalOperatorType(ArithmeticalOperatorType.ADDITION);
					break;
				case "-":
					target.setArithmeticalOperatorType(ArithmeticalOperatorType.SUBTRACTION);
					break;
				case "/":
					target.setArithmeticalOperatorType(ArithmeticalOperatorType.DIVISION);
					break;
				case "%":
					target.setArithmeticalOperatorType(ArithmeticalOperatorType.MODULO);
					break;
				case "*":
					target.setArithmeticalOperatorType(ArithmeticalOperatorType.MULTIPLICATION);
					break;
				default:
					break;
				}

				getTargetBlock().refreshVisual();
				for (IVisualPart<? extends Node> anchored : getTargetBlock().getAnchoredsUnmodifiable()) {
					anchored.refreshVisual();
				}

			}
		}
	};

	private final EventHandler<ActionEvent> changeMinMaxHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			MinMaxObjectReferenceBlockModel target = (MinMaxObjectReferenceBlockModel) ((ArithmeticalOperatorMovableBlockPart) getTargetBlock())
					.getContent();

			String text = ((MenuItem) event.getTarget()).getText();
			target.getFixedChildTextModel().setText(text);

			switch (text) {
			case "Min":
				target.setMinMaxType(MinMaxType.MIN);
				break;

			case "Max":
				target.setMinMaxType(MinMaxType.MAX);
				break;
			default:
				break;
			}

			getTargetBlock().refreshVisual();
			for (IVisualPart<? extends Node> anchored : getTargetBlock().getAnchoredsUnmodifiable()) {
				anchored.refreshVisual();
			}
		}

	};

	private final EventHandler<ActionEvent> changeAndOrHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {

			if(getTargetBlock() instanceof OperatorAndBlockPart){
			
			OperatorAndBlockModel target = ((OperatorAndBlockPart) getTargetBlock()).getContent();

			String text = ((MenuItem) event.getTarget()).getText();
			target.getFixedChildTextModel().setText(text);

			switch (text) {
			case "And":
				target.setOperatorAndORType(OperatorAndORType.AND);
				break;

			case "Or":
				target.setOperatorAndORType(OperatorAndORType.OR);
				break;
			default:
				break;
			}
			
			}
			
			if(getTargetBlock() instanceof ControlBlockPart){
				
				ControlAndOrBlock target = (ControlAndOrBlock) ((ControlBlockPart) getTargetBlock()).getContent();

				String text = ((MenuItem) event.getTarget()).getText();
				target.getFixedChildTextModel().setText(text);

				switch (text) {
				case "And":
					target.setType(OperatorAndORType.AND);
					break;

				case "Or":
					target.setType(OperatorAndORType.OR);
					break;
				default:
					break;
				}
				
				}
			
			

			getTargetBlock().refreshVisual();
			for (IVisualPart<? extends Node> anchored : getTargetBlock().getAnchoredsUnmodifiable()) {
				anchored.refreshVisual();
			}
			
			
			
		}
	};

	@Override
	public void click(MouseEvent e) {

		targetBlock = getTargetPart();
		if (targetBlock instanceof IContentPart) {
			// delete the part

			if (targetBlock instanceof OperatorMovableBlockPart || targetBlock instanceof ControlBlockPart
					|| targetBlock instanceof ArithmeticalOperatorMovableBlockPart) {

				ContextMenu contextMenu = new ContextMenu();

				if (((AbstractBlockElement) ((IContentPart) targetBlock).getContent()).getParentBlock() != null) {
					MenuItem detatchFromParent = new MenuItem("Detach From Block Parent");

					contextMenu.getItems().add(detatchFromParent);

					detatchFromParent.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							for (java.util.Map.Entry<IVisualPart<? extends Node>, String> entry : targetBlock
									.getAnchoragesUnmodifiable().entries()) {
								if (entry.getValue().equals(AbstractBlockElement.MOVABLE_CHILD_ANCHORED_TYPE)) {
									((GeometricShapePart) targetBlock).getContent().removeParentBlock();
									((GeometricShapePart) targetBlock).refreshContentAnchorages();
									((GeometricShapePart) targetBlock).refreshVisual();

								}
							}

						}
					});

				}
				if (targetBlock instanceof OperatorMovableBlockPart) {

					if (((OperatorMovableBlockPart) targetBlock).getContent() instanceof OperatorComparisonBlockModel) {

						OperatorComparisonBlockModel model = ((OperatorComparisonBlockModel) ((OperatorMovableBlockPart) targetBlock)
								.getContent());

						contextMenu.getItems().add(
								createMenuForTypeChange(model.getComparisonValueType(), model.getComparisonType()));

					}
					if(((OperatorMovableBlockPart) targetBlock).getContent() instanceof OperatorNumercialComparisonBlockModel){

						
						OperatorNumercialComparisonBlockModel model = ((OperatorNumercialComparisonBlockModel) ((OperatorMovableBlockPart) targetBlock)
								.getContent());

						contextMenu.getItems().add(
								createMenuForTypeChange(OperatorComparisonValueType.NUMBER, model.getComparisonType()));
					}
				}
				
				if(targetBlock instanceof ControlBlockPart && ((ControlBlockPart) targetBlock).getContent() instanceof ControlAndOrBlock){
					ControlAndOrBlock controlBlock = (ControlAndOrBlock) ((GeometricShapePart) targetBlock).getContent();
					
					contextMenu.getItems().add(createMenuForAndOrChange(controlBlock.getType()));
				}

				if (targetBlock instanceof ArithmeticalOperatorMovableBlockPart) {
					ArithmeticalOperatorMovableBlockPart part = (ArithmeticalOperatorMovableBlockPart) targetBlock;
					if (((IContentPart) targetBlock).getContent() instanceof MinMaxObjectReferenceBlockModel) {
						MinMaxObjectReferenceBlockModel targetModel = (MinMaxObjectReferenceBlockModel) ((IContentPart) targetBlock)
								.getContent();
						contextMenu.getItems().add(createMenuForMinMaxChange(targetModel.getMinMaxType()));

					}
				}
				if(targetBlock instanceof ArithmeticalOperationOperatorPart){
					contextMenu.getItems().add(createMenuForAritheticOperationChange(((ArithmeticalOperationOperatorPart) targetBlock).getContent().getArithmeticalOperatorType()));
				}
				if (targetBlock instanceof OperatorAndBlockPart) {
					OperatorAndBlockModel targetModel = ((OperatorAndBlockPart) targetBlock).getContent();
					contextMenu.getItems().add(createMenuForAndOrChange(targetModel.getOperatorAndORType()));
				}

				if (targetBlock instanceof ControlBlockPart) {
					if (((ControlBlockPart) targetBlock).getContent().getParentBlock() == null) {
						ControlBlockModel controlBlockModel = ((ControlBlockPart) targetBlock).getContent();
						MenuItem changeTemporalValidity = new MenuItem("Change Temporal Validity");

						contextMenu.getItems().add(changeTemporalValidity);
						changeTemporalValidity.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								Dialog<Pair<DatePicker, DatePicker>> dialog = new Dialog<>();
								dialog.setTitle("Temporal Validity");

								dialog.setHeaderText("Change the limited time span, in which the element is valid");
								Text sinceLabel = new Text("Valid since: ");
								DatePicker datePickerSince = new DatePicker();
								if (controlBlockModel.getValidSince() != null) {
									LocalDate sinceDate = LocalDate.from(controlBlockModel.getValidSince().toInstant()
											.atZone(ZoneId.systemDefault()).toLocalDate());
									datePickerSince.setValue(sinceDate);
								}

								Text untilLabel = new Text("Valid until: ");
								DatePicker datePickerUntil = new DatePicker();
								if (controlBlockModel.getValidUntil() != null) {
									LocalDate untilDate = LocalDate.from(controlBlockModel.getValidUntil().toInstant()
											.atZone(ZoneId.systemDefault()).toLocalDate());
									datePickerUntil.setValue(untilDate);
								}

								datePickerSince.setMinWidth(170);

								GridPane grid = new GridPane();

								grid.setHgap(10);
								grid.setVgap(10);
								grid.setPadding(new Insets(20, 150, 10, 10));

								AnchorPane.setTopAnchor(grid, 0d);
								AnchorPane.setLeftAnchor(grid, 0d);
								AnchorPane.setRightAnchor(grid, 0d);
								grid.add(sinceLabel, 0, 0);
								grid.add(datePickerSince, 1, 0);
								grid.add(untilLabel, 0, 1);
								grid.add(datePickerUntil, 1, 1);

								grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

								ButtonType buttontypeSave = new ButtonType("Finish", ButtonData.FINISH);
								dialog.getDialogPane().getButtonTypes().add(buttontypeSave);

								ButtonType buttontypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
								dialog.getDialogPane().getButtonTypes().add(buttontypeCancel);

								dialog.getDialogPane().setContent(grid);

								dialog.setResultConverter(dialogButton -> {
									if (dialogButton == buttontypeSave) {
										return new Pair<>(datePickerSince, datePickerUntil);
									}
									return null;
								});

								datePickerSince.getEditor().textProperty().isEmpty().addListener(new ChangeListener<Boolean>() {

									@Override
									public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
											Boolean newValue) {
										 if (newValue){
								              datePickerSince.setValue(null);
								            }
										
									}
								});
								
								datePickerUntil.getEditor().textProperty().isEmpty().addListener(new ChangeListener<Boolean>() {

									@Override
									public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
											Boolean newValue) {
										 if (newValue){
								              datePickerUntil.setValue(null);
								            }
										
									}
								});
								
								
								
								Optional<Pair<DatePicker, DatePicker>> result = dialog.showAndWait();

								result.ifPresent(dates -> {

									if (dates.getKey().getValue() != null) {

										Date sinceDate = Date.from(dates.getKey().getValue()
												.atStartOfDay(ZoneId.systemDefault()).toInstant());
										((ControlBlockPart) targetBlock).getContent().setValidSince(sinceDate);
									}else{
										((ControlBlockPart) targetBlock).getContent().setValidSince(null);
										
									}
									
									
									if(dates.getValue().getValue() != null) {
										
										Date untilDate = Date.from(dates.getValue().getValue()
												.atStartOfDay(ZoneId.systemDefault()).toInstant());
										((ControlBlockPart) targetBlock).getContent().setValidUntil(untilDate);
									}else{
										((ControlBlockPart) targetBlock).getContent().setValidUntil(null);
									}

								});

							}
						});
					}
				}

				contextMenu.show(targetBlock.getVisual(), Side.RIGHT, 10, 10);
			}
		}

	}

	private Menu createMenuForAndOrChange(OperatorAndORType andOrType) {
		Menu menu = new Menu("Change Type");
		MenuItem itemAnd = new MenuItem("And");
		MenuItem itemOr = new MenuItem("Or");

		switch (andOrType) {
		case AND:
			itemAnd.setDisable(true);
			break;
		case OR:
			itemOr.setDisable(true);

		default:
			break;
		}

		menu.getItems().addAll(itemAnd, itemOr);
		itemAnd.onActionProperty().set(changeAndOrHandler);
		itemOr.onActionProperty().set(changeAndOrHandler);

		return menu;
	}
	
	

	private Menu createMenuForMinMaxChange(MinMaxType minMaxType) {

		Menu menu = new Menu("Change Type");
		MenuItem itemMin = new MenuItem("Min");
		MenuItem itemMax = new MenuItem("Max");

		switch (minMaxType) {
		case MAX:
			itemMax.setDisable(true);
			break;
		case MIN:
			itemMin.setDisable(true);

		default:
			break;
		}

		menu.getItems().addAll(itemMin, itemMax);
		itemMin.onActionProperty().set(changeMinMaxHandler);
		itemMax.onActionProperty().set(changeMinMaxHandler);

		return menu;
	}
	
	private Menu createMenuForAritheticOperationChange(ArithmeticalOperatorType type){
		Menu menu = new Menu("Change Type");
		MenuItem addition = new MenuItem("+");
		MenuItem subtraction = new MenuItem("-");
		MenuItem multiplication = new MenuItem("*");
		MenuItem division = new MenuItem("/");
		MenuItem modulo = new MenuItem("%");
		
		menu.getItems().addAll(addition, subtraction, multiplication, division, modulo);
		
		switch (type) {
		case ADDITION:
			addition.setDisable(true);
			break;
		case DIVISION:
			division.setDisable(true);
break;
		case MODULO:
			modulo.setDisable(true);
			break;
		case MULTIPLICATION:
			multiplication.setDisable(true);
			break;
		case SUBTRACTION:
			subtraction.setDisable(true);
			break;
		default:
			break;
		}
		for (MenuItem i : menu.getItems()) {
			i.onActionProperty().set(changeArithmeticOperatorHandler);
		}

		return menu;
	
	
	}
	

	private Menu createMenuForTypeChange(OperatorComparisonValueType valueType, OperatorComparisonType type) {
		Menu menu = new Menu("Change Type");
		MenuItem equals = new MenuItem("=");
		MenuItem notEquals = new MenuItem("!=");
		menu.getItems().addAll(equals, notEquals);
		if (!(valueType.equals(OperatorComparisonValueType.NUMBER))) {
			switch (type) {
			case EQUALS:
				equals.setDisable(true);
				break;
			case NOT_EQUALS:
				notEquals.setDisable(true);
				break;
			default:
				break;
			}
		}

		if (valueType.equals(OperatorComparisonValueType.NUMBER)) {

			MenuItem smaller = new MenuItem("<");
			MenuItem bigger = new MenuItem(">");
			MenuItem smallerOrEquals = new MenuItem("<=");
			MenuItem biggerOrEquals = new MenuItem(">=");
			menu.getItems().addAll(smaller, bigger, smallerOrEquals, biggerOrEquals);

			switch (type) {
			case EQUALS:
				equals.setDisable(true);
				break;
			case NOT_EQUALS:
				notEquals.setDisable(true);
				break;
			case BIGGER:
				bigger.setDisable(true);
				break;
			case SMALLER:
				smaller.setDisable(true);
				break;
			case EQUALS_OR_BIGGER:
				biggerOrEquals.setDisable(true);
				break;
			case EQUALS_OR_SMALLER:
				smallerOrEquals.setDisable(true);
				break;
			default:
				break;
			}

		}
		for (MenuItem i : menu.getItems()) {
			i.onActionProperty().set(eventHandler);
		}

		return menu;
	}

	/**
	 * Returns the target {@link IVisualPart} for this policy. Per default the
	 * first anchorage is returned.
	 *
	 * @return The target {@link IVisualPart} for this policy.
	 */
	protected IVisualPart<? extends Node> getTargetPart() {

		return getHost().getAnchoragesUnmodifiable().keySet().iterator().next();

	}

	public IVisualPart<? extends Node> getTargetBlock() {
		return targetBlock;
	}

	public void setTargetBlock(IVisualPart<? extends Node> targetBlock) {
		this.targetBlock = targetBlock;
	}
}
