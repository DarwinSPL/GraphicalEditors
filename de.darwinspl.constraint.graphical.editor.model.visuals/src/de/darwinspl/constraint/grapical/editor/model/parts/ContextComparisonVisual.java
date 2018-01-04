package de.darwinspl.constraint.grapical.editor.model.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.BezierCurve;
import org.eclipse.gef.geometry.planar.CurvedPolygon;
import org.eclipse.gef.geometry.planar.Line;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ContextComparisonVisual extends Region {

	public static CurvedPolygon createOperatorShapeCustomLength(double l) {
		List<BezierCurve> segments = new ArrayList<>();
		segments.add(new Line(0, 15, 10, 0));
		segments.add(new Line(10, 0, l, 0));
		segments.add(new Line(l, 0, l + 10, 15));
		segments.add(new Line(l + 10, 15, l, 30));
		segments.add(new Line(l, 30, 10, 30));
		segments.add(new Line(10, 30, 0, 15));
		return new CurvedPolygon(segments);
	}

	private Text operatorType;

	private GeometryNode<CurvedPolygon> shape;

	private ChoiceBox<String> operand1;

	private ChoiceBox<String> operand2;

	private HBox contentHBox;

	public ContextComparisonVisual() {
		shape = new GeometryNode<>(createOperatorShapeCustomLength(320));
		shape.setFill(Color.GREEN);

		operand1 = new ChoiceBox<>();

		operand2 = new ChoiceBox<String>();

		operatorType = new Text("=");
		operatorType.setTextOrigin(VPos.CENTER);

		contentHBox = new HBox();
		contentHBox.setSpacing(25d);
		contentHBox.setFillHeight(true);
		contentHBox.setAlignment(Pos.CENTER);
		contentHBox.setPadding(new Insets(2, 25, 3, 25));

		// contentHBox.setStyle("-fx-border-color: blue;");

		prefHeightProperty().bind(shape.heightProperty());
		prefWidthProperty().bind(shape.widthProperty());

		shape.setMinHeight(30);
		shape.setMinWidth(300);

		contentHBox.prefWidthProperty().bind(shape.widthProperty());
		contentHBox.prefHeightProperty().bind(shape.heightProperty().subtract(25));
		//

		operand1.setMaxWidth(Double.MAX_VALUE);
		operand2.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(operand1, Priority.ALWAYS);
		HBox.setHgrow(operand2, Priority.ALWAYS);
		HBox.setHgrow(operatorType, Priority.ALWAYS);

		shape.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				operand1.setMaxHeight(newValue.doubleValue() - 6);
				operand1.setMinHeight(newValue.doubleValue() - 6);
				operand2.setMaxHeight(newValue.doubleValue() - 6);
				operand2.setMinHeight(newValue.doubleValue() - 6);

			}
		});

		operand1.setMaxHeight(24);
		operand1.setMinHeight(24);
		operand2.setMaxHeight(24);
		operand2.setMinHeight(24);

		contentHBox.getChildren().addAll(operand1, operatorType, operand2);

		getChildren().addAll(new Group(shape), new Group(contentHBox));

	}

	public GeometryNode<CurvedPolygon> getGeometryNode() {
		return shape;
	}

	public ChoiceBox<String> getOperand1() {
		return operand1;
	}

	public ChoiceBox<String> getOperand2() {
		return operand2;
	}

	public Text getOperatorType() {
		return operatorType;
	}

	public void setGeometryNode(GeometryNode<CurvedPolygon> shape) {
		this.shape = shape;
	}

	public void setOperand1(ChoiceBox<String> operand1) {
		this.operand1 = operand1;
	}

	public void setOperand2(ChoiceBox<String> operand2) {
		this.operand2 = operand2;
	}

	public void setOperatorType(Text operatorType) {
		this.operatorType = operatorType;
	}

}
