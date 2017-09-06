package editor.parts.root;

import org.eclipse.gef.mvc.fx.parts.LayeredRootPart;

import javafx.scene.Group;

public class EvolutionViewRootPart extends LayeredRootPart {

	// @Inject
	// private Injector injector;
	//
	//
	// private CTCEditor editor = null;
	// = injector.getInstance(CTCEditor.class);

	@Override
	protected Group createContentLayer() {
		// TODO Auto-generated method stub
		Group contentLayer = super.createContentLayer();

		// HyFeatureModel model = getEditor().getInputFile();
		// IEditorReference[] refs =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		// IEditorPart editor =
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		//
		//
		// IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
		////
		// HyConstraintModel model;
		// try {
		// model = (HyConstraintModel) EcoreIOUtil.loadModel(file);
		// } catch (Exception e) {
		// //e.printStackTrace();
		// model = HyConstraintFactory .eINSTANCE.createHyConstraintModel();
		//// HyContextualInformationEnum enumC =(HyContextualInformationEnum)
		// model.getConstraints().get(0);
		//// enumC.getEnumType();
		// EcoreIOUtil.saveModelAs(model, file);
		// }
		//
		//
		//
		//// HyConstraintModel model =
		// HyconstraintsResourceUtil.getResourceContent(urithis);
		// if(model==null){
		// model = HyConstraintFactory .eINSTANCE.createHyConstraintModel();
		// EcoreIOUtil.saveModelAs(model, file);
		//
		// }
		//
		//// //IHyconstraintsTextResource resource =
		// (IHyconstraintsTextResource)
		// editingDomain.getResourceSet().getResource(urithis, false);
		// HyFeatureModel featureModel =
		// HyFeatureResolverUtil.getAccompanyingFeatureModel(model);

		// Slider evolutionSlider = new Slider();
		//
		//
		//
		// TextField widthSliderText = new TextField();
		// widthSliderText.textProperty()
		// .bind(evolutionSlider.valueProperty().asString());
		// widthSliderText.setPrefWidth(50);
		//
		// layout slider and textfield in a box
		// HBox hBox = new HBox();
		// hBox.setStyle(
		// "-fx-background-color: white;");
		//// hBox.getChildren().addAll(new Text("Width: "), evolutionSlider,
		//// widthSliderText);
		//
		//
		// hBox.setAlignment(Pos.CENTER);
		// hBox.setMaxSize(Double.MAX_VALUE, Double.MIN_NORMAL);
		//
		//
		// contentLayer.getChildren().add(hBox);

		// contentLayer.setStyle("-fx-background-color: white;");

		return contentLayer;

	}

	// @Override
	// protected void doAddChildVisual(IVisualPart<? extends Node> child, int
	// index) {
	// if (child instanceof IContentPart) {
	// int contentLayerIndex = 0;
	// for (int i = 0; i < index; i++) {
	// if (i < getChildrenUnmodifiable().size() &&
	// getChildrenUnmodifiable().get(i) instanceof IContentPart) {
	// contentLayerIndex++;
	// }
	// }
	// ((HBox)
	// getContentLayer().getChildren().get(0)).getChildren().add(contentLayerIndex,
	// child.getVisual());
	//
	// HBox.setHgrow(child.getVisual(), Priority.NEVER);
	// } else {
	// super.doAddChildVisual(child, index);
	// }
	//
	//
	// }
	//
	// @Override
	// protected void doRemoveChildVisual(IVisualPart<? extends Node> child, int
	// index) {
	// if (child instanceof IContentPart) {
	// ((HBox)
	// getContentLayer().getChildren().get(0)).getChildren().remove(index);
	// } else {
	// super.doRemoveChildVisual(child, index);
	// }
	// }

	// public CTCEditor getEditor() {
	// return editor;
	// }
	//
	// public void setEditor(CTCEditor editor) {
	// this.editor = editor;
	// }
	//

	// List<Date> modelwrapped;
	// @Override
	// protected Group createContentLayer() {
	//
	// //IEditorReference[] refs =
	// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
	//
	//
	// // TODO Auto-generated method stub
	// Group contentLayer = super.createContentLayer();
	//
	//// List<Date> dates = new ArrayList<Date>();
	// //modelWrapped.getDates();
	////
	//// int size = dates.size();
	//// Composite buttonGroup = new Composite(null, SWT.NONE);
	// RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
	//// rowLayout.justify = true;
	//// buttonGroup.setLayout(rowLayout);
	//
	//
	// Button currentDate = new Button(buttonGroup, SWT.PUSH);
	//
	//
	// if(dates.size() > 0)
	// currentDate.setText(dates.get(0).toString());
	// else{
	// currentDate.setText((new Date()).toString());
	// }
	//
	// Label minStateLabel = new Label(buttonGroup, SWT.CENTER);
	// minStateLabel.setText("Date range from ");
	// Combo minState = new Combo (buttonGroup, SWT.READ_ONLY);
	// for(Date date : dates){
	// minState.add(date.toString());
	// }
	// minState.select(0);
	// minState.setEnabled(size > 1);
	//
	//
	// Label maxStateLabel = new Label(buttonGroup, SWT.NATIVE);
	// maxStateLabel.setText(" to ");
	// Combo maxState = new Combo (buttonGroup, SWT.NATIVE);
	//
	// for(Date date : dates){
	// maxState.add(date.toString());
	// }
	// maxState.select(dates.size()-1);
	// maxState.setEnabled(size > 1);
	//
	//
	// Scale scale = new Scale(buttonGroup, SWT.FILL);
	// scale.setMinimum(0);
	// scale.setMaximum(size-1);
	// scale.setLayoutData(new RowData(300, SWT.DEFAULT));
	// scale.setEnabled(size > 1);
	// scale.setSelection(dates.indexOf(currentSelectedDate));
	//
	//
	//
	//
	// Button addDate = new Button(buttonGroup, SWT.PUSH);
	// addDate.setText("Add Date");
	//
	//
	// if(dates.size() > 0)
	// setCurrentSelectedDate(currentSelectedDate);
	// else{
	// Date now = new Date();
	// modelWrapped.addDate(now);
	// setCurrentSelectedDate(now);
	// }
	// }

}
