package editor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.mvc.fx.domain.HistoricizingDomain;
import org.eclipse.gef.mvc.fx.domain.IDomain;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.inject.Guice;
import com.google.inject.Module;

import editor.model.evolutionslider.EvolutionSliderModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureModel;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GraphicalEditor extends Application {

	private HistoricizingDomain domain;

	private Stage primaryStage;


	protected IViewer getContentViewer() {
		return domain.getAdapter(AdapterKey.get(IViewer.class, IDomain.CONTENT_VIEWER_ROLE));
	}

	protected IDomain getDomain() {
		return domain;
	}

	protected Stage getPrimaryStage() {
		return primaryStage;
	}

	protected void hookViewers() {

		getPrimaryStage().setScene(new Scene(new GraphicalEditorViewersComposite(getContentViewer(), getPaletteViewer(),
				getPaletteViewerFeatures(), getPaletteViewerContexts(), getPaletteViewerOperators(),
				getPaletteViewerArithmetics(), getEvolutionViewer(), domain).getRootVBox()));
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		// create domain using guice
		this.domain = Guice.createInjector(createModule()).getInstance(HistoricizingDomain.class);

		// create viewers
		hookViewers();

		// set-up stage
		primaryStage.setResizable(true);
		primaryStage.setWidth(640);
		primaryStage.setHeight(480);
		primaryStage.setTitle("DarwinSPL Editor");
		primaryStage.sizeToScene();
		primaryStage.show();

		// activate domain
		domain.activate();

		// load contents
		loadViewerContents();

	}

	public static void main(String[] args) {
		launch();
	}

	protected IViewer getPaletteViewer() {
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE));
	}

	protected IViewer getPaletteViewerFeatures() {
		return getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_FEATURES));
	}

	protected IViewer getPaletteViewerContexts() {
		return getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_CONTEXTS));
	}

	protected IViewer getPaletteViewerArithmetics() {
		return getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_ARITHMETICS));
	}

	protected IViewer getPaletteViewerOperators() {
		return getDomain()
				.getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_OPERATORS));
	}

	protected IViewer getEvolutionViewer() {
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.EVOLUTION_VIEWER_ROLE));
	}

	protected Module createModule() {
		return new GraphicalEditorModule();
	}

	public void loadViewerContents() {

		List<HyFeature> list = new ArrayList<>();
		HyContextModel contextModel = null;
		HyFeatureModel featureModel = null;
		getContentViewer().getContents().setAll(OperatorShapeService.createOperatorAndShapeGroup());
		getPaletteViewer().getContents().setAll(ShapeService.createControlPaletteViewerContents(featureModel, contextModel));
		getPaletteViewerOperators().getContents().setAll(OperatorShapeService.createPaletteOperatorsViewContents());
		getPaletteViewerArithmetics().getContents()
				.setAll(ArithmeticalOperatorShapeService.createPaletteArithmeticViewContents(featureModel, contextModel));
		getPaletteViewerFeatures().getContents()
				.setAll(FeatureShapeService.createFeaturePaletteViewerContents(list, featureModel, contextModel));
		getPaletteViewerContexts().getContents().setAll(ShapeService.createContextPaletteContent(featureModel, contextModel, list));
		List<Date> dates = new ArrayList<>();
		getEvolutionViewer().getContents().setAll(createEvolutionSliderViewContents(null, dates));
		getPrimaryStage().sizeToScene();
	}

	public static List<Object> createEvolutionSliderViewContents(IFile file, List<Date> dates) {
		List<Object> visualParts = new ArrayList<>();
		final EvolutionSliderModel model = new EvolutionSliderModel(file, dates);
		visualParts.add(model);
		return visualParts;

	}
	
	

}
