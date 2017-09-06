package editor.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.IGeometry;

import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeatureModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Abstract class for all block elements.
 * 
 * 
 * 
 * @author Anna-Liisa
 *
 */
public abstract class AbstractBlockElement {

	public static final String FIXED_CHILD_ANCHORED_TYPE = "fixed_child";
	public static final String MOVABLE_CHILD_ANCHORED_TYPE = "movable_child";
	public static final String FEATURE_MODEL_FILE_PROPERTY = "feature_model_file_property";
	public static final String TRANSFORM_PROPERTY = "transform";
	public static final String SIZE_PROPERTY = "size";
	public static final String STROKE_WIDTH_PROPERTY = "strokeWidth";
	public static final String FEATURE_MODEL_PROPERTY = "feature_model_property";
	public static final String CONTEXT_MODEL_PROPERTY = "context_model_property";
	public final static String PARENT_BLOCK_PROPERTY = "parent_block_property";
	public final static String BOUNDS_IN_PARENT_PROPERTY = "bounds_in_parent_property";
	public static final String FILL_PROPERTY = "fill";
	public final static String CURRENT_SELECTED_DATE_PROPERTY = "current_selected_date_property";

	private ObjectProperty<Date> currentSelectedDateProperty = new SimpleObjectProperty<>(this,
			CURRENT_SELECTED_DATE_PROPERTY);


	private final ObjectProperty<HyFeatureModel> featureModelProperty = new SimpleObjectProperty<HyFeatureModel>(this,
			FEATURE_MODEL_PROPERTY);
	private final ObjectProperty<HyContextModel> contextModelProperty = new SimpleObjectProperty<HyContextModel>(this,
			CONTEXT_MODEL_PROPERTY);
	private final ObjectProperty<Dimension> dimensionProperty = new SimpleObjectProperty<Dimension>(this,
			SIZE_PROPERTY);
	private final ObjectProperty<AffineTransform> transformProperty = new SimpleObjectProperty<>(this,
			TRANSFORM_PROPERTY);
	private final Set<AbstractGeometricElement<? extends IGeometry>> anchorages = new HashSet<>();

	private final ObjectProperty<IFile> featureModelFileProperty = new SimpleObjectProperty<>(this,
			FEATURE_MODEL_FILE_PROPERTY);
	protected final ObjectProperty<? super AbstractGeometricElement<? extends IGeometry>> parentBlockProperty = new SimpleObjectProperty<>(
			this, PARENT_BLOCK_PROPERTY);
	private final ObjectProperty<Bounds> boundsInParentProperty = new SimpleObjectProperty<>(this,
			BOUNDS_IN_PARENT_PROPERTY);
	private final ObjectProperty<Paint> fillProperty = new SimpleObjectProperty<>(this, FILL_PROPERTY);
	private final DoubleProperty strokeWidthProperty = new SimpleDoubleProperty(this, STROKE_WIDTH_PROPERTY, 0.5);

	private Paint stroke = new Color(0, 0, 0, 1);
	private Effect effect;

	public AbstractBlockElement(AffineTransform transform, Paint stroke, double strokeWidth, Effect effect) {
		setTransform(transform);
		setStroke(stroke);
		setStrokeWidth(strokeWidth);
		setEffect(effect);

	}

	public AbstractBlockElement(AffineTransform transform) {
		setTransform(transform);
	}

	public AbstractBlockElement() {

	}

	public AffineTransform getTransform() {
		return transformProperty.get();
	}

	public Dimension getDimension() {
		return dimensionProperty.get();
	}

	public void setTransform(AffineTransform transform) {
		transformProperty.set(transform);
	}

	public void setDimension(Dimension dimension) {
		dimensionProperty.set(dimension);
	}

	public ObjectProperty<AffineTransform> getTransformProperty() {
		return transformProperty;
	}

	public ObjectProperty<Dimension> getDimensionProperty() {
		return dimensionProperty;
	}

	public void setStroke(Paint stroke) {
		this.stroke = stroke;
	}

	public void setStrokeWidth(double strokeWidth) {
		strokeWidthProperty.set(strokeWidth);
	}

	public Paint getStroke() {
		return stroke;
	}

	public double getStrokeWidth() {
		return strokeWidthProperty.get();
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public Effect getEffect() {
		return effect;
	}

	public void addAnchorage(AbstractGeometricElement<? extends IGeometry> anchorage) {
		this.anchorages.add(anchorage);
	}

	public Set<AbstractGeometricElement<? extends IGeometry>> getAnchorages() {
		return anchorages;
	}

	public void setParentBlock(AbstractGeometricElement<? extends IGeometry> parentBlock) {
		parentBlockProperty.set(parentBlock);

		// ((OperatorFixedBlockModel) parentBlock).setMovableChildBlock(this);
	}

	public GeometricShape getParentBlock() {
		return (GeometricShape) parentBlockProperty.get();
	}

	public void removeParentBlock() {
		parentBlockProperty.set(null);

	}

	public ObjectProperty<? super AbstractGeometricElement<? extends IGeometry>> getParentBlockProperty() {
		return parentBlockProperty;
	}

	public ObjectProperty<Bounds> getBoundsInParentProperty() {
		return boundsInParentProperty;
	}

	public Bounds getBoundsInParent() {
		return boundsInParentProperty.get();
	}

	public void setBoundsInParent(Bounds boundsInParent) {
		boundsInParentProperty.set(boundsInParent);
	}

	public Paint getFill() {
		return fillProperty.get();
	}

	public void setFill(Paint fill) {
		fillProperty.set(fill);
	}

	public ObjectProperty<Paint> fillProperty() {
		return fillProperty;
	}

	public ObjectProperty<IFile> getFeatureModelFileProperty() {
		return featureModelFileProperty;
	}

	public void setFeatureModelFile(IFile file) {
		featureModelFileProperty.set(file);
	}

	public IFile getFeatureModelFile() {
		return featureModelFileProperty.get();
	}

	public ObjectProperty<HyFeatureModel> getFeatureModelProperty() {
		return featureModelProperty;
	}

	public HyFeatureModel getFeatureModel() {
		return featureModelProperty.get();
	}

	public void setFeatureModel(HyFeatureModel featureModel) {
		featureModelProperty.set(featureModel);
	}

	public ObjectProperty<HyContextModel> getContextModelProperty() {
		return contextModelProperty;
	}

	public void setContextModel(HyContextModel contextModel) {
		contextModelProperty.set(contextModel);
	}

	public HyContextModel getContextModel() {
		return contextModelProperty.get();
	}

	public void refreshContents(HyFeatureModel featureModel, HyContextModel contextModel) {

		setFeatureModel(featureModel);
		setContextModel(contextModel);

	}
	
	public ObjectProperty<Date> getCurrentSelectedDateProperty() {
		return currentSelectedDateProperty;
	}

	public void setCurrentSelectedDate(Date currentSelectedDate) {
		this.currentSelectedDateProperty.set(currentSelectedDate);
	}

	public Date getCurrentSelectedDate() {
		return this.currentSelectedDateProperty.get();
	}

}
