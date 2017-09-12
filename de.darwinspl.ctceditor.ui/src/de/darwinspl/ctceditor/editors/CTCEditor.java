package de.darwinspl.ctceditor.editors;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.models.FocusModel;
import org.eclipse.gef.mvc.fx.models.HoverModel;
import org.eclipse.gef.mvc.fx.models.SelectionModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.ui.actions.FitToViewportAction;
import org.eclipse.gef.mvc.fx.ui.actions.FitToViewportLockAction;
import org.eclipse.gef.mvc.fx.ui.actions.ScrollActionGroup;
import org.eclipse.gef.mvc.fx.ui.actions.ZoomActionGroup;
import org.eclipse.gef.mvc.fx.ui.parts.AbstractFXEditor;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import com.google.inject.Guice;
import com.google.inject.util.Modules;

import de.christophseidl.util.eclipse.ResourceUtil;
import de.christophseidl.util.ecore.EcoreIOUtil;
import de.darwinspl.ctceditor.CTCEditorUiModule;
import editor.ArithmeticalOperatorShapeService;
import editor.FeatureShapeService;
import editor.GraphicalEditor;
import editor.GraphicalEditorModule;
import editor.GraphicalEditorViewersComposite;
import editor.OperatorShapeService;
import editor.ShapeService;
import editor.constraints.ContentViewService;
import editor.model.AbstractGeometricElement;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel.ArithmeticalOperatorType;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorMovableBlock;
import editor.model.arithmetical.ArithmeticalTextFielOperatorBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel.MinMaxType;
import editor.model.arithmetical.NegationOperatorBlockModel;
import editor.model.arithmetical.NumericalObjectReferenceOperator;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxModel;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorAndBlockModel.OperatorAndORType;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonType;
import editor.model.operator.OperatorFeatureIsSelectedModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorMovableBlockModel;
import editor.model.operator.OperatorNotBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import editor.model.operator.OperatorWithChoiceBoxAndTextField;
import editor.parts.choiceboxes.AbstractChoiceBoxPart;
import editor.parts.evolution.slider.EvolutionSliderPart;
import editor.parts.root.EvolutionViewRootPart;
import editor.parts.root.PaletteRootPart;
import eu.hyvar.context.HyContextInformationFactory;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.context.HyContextualInformationEnum;
import eu.hyvar.context.HyContextualInformationNumber;
import eu.hyvar.context.information.util.ContextInformationResolverUtil;
import eu.hyvar.context.information.util.HyContextInformationUtil;
import eu.hyvar.dataValues.HyDataValuesFactory;
import eu.hyvar.dataValues.HyEnumLiteral;
import eu.hyvar.dataValues.HyEnumValue;
import eu.hyvar.dataValues.HyNumberValue;
import eu.hyvar.feature.HyFeature;
import eu.hyvar.feature.HyFeatureAttribute;
import eu.hyvar.feature.HyFeatureModel;
import eu.hyvar.feature.constraint.HyConstraint;
import eu.hyvar.feature.constraint.HyConstraintFactory;
import eu.hyvar.feature.constraint.HyConstraintModel;
import eu.hyvar.feature.expression.HyAndExpression;
import eu.hyvar.feature.expression.HyAttributeReferenceExpression;
import eu.hyvar.feature.expression.HyBinaryExpression;
import eu.hyvar.feature.expression.HyBooleanValueExpression;
import eu.hyvar.feature.expression.HyContextInformationReferenceExpression;
import eu.hyvar.feature.expression.HyEqualExpression;
import eu.hyvar.feature.expression.HyExpression;
import eu.hyvar.feature.expression.HyExpressionFactory;
import eu.hyvar.feature.expression.HyFeatureReferenceExpression;
import eu.hyvar.feature.expression.HyGreaterExpression;
import eu.hyvar.feature.expression.HyGreaterOrEqualExpression;
import eu.hyvar.feature.expression.HyLessExpression;
import eu.hyvar.feature.expression.HyLessOrEqualExpression;
import eu.hyvar.feature.expression.HyNegationExpression;
import eu.hyvar.feature.expression.HyNestedExpression;
import eu.hyvar.feature.expression.HyNotEqualExpression;
import eu.hyvar.feature.expression.HyNotExpression;
import eu.hyvar.feature.expression.HyOrExpression;
import eu.hyvar.feature.expression.HyUnaryExpression;
import eu.hyvar.feature.expression.HyValueExpression;
import eu.hyvar.feature.util.HyFeatureResolverUtil;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;

public class CTCEditor extends AbstractFXEditor {


	private ZoomActionGroup zoomActionGroup;
	private ScrollActionGroup scrollActionGroup;
	private FitToViewportLockAction fitToViewportLockAction;
	
	
	public CTCEditor() {
		super(Guice.createInjector(Modules.override(new GraphicalEditorModule())
				.with(new CTCEditorUiModule())));


		
		
	}
	
	
	public boolean needToRefreshFeatureModel = false;
	
	/**
	 *Needed to refresh the choices provided to the users 
	 *if the context or feature model changed
	 * 
	 */
	@Override
	public void setFocus() {

		if(needToRefreshFeatureModel == true){
			

        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        
        
        HyConstraintModel model;
		try {
			model = (HyConstraintModel) EcoreIOUtil.loadModel(file);			
		} catch (Exception e) {
			//e.printStackTrace();
			model = HyConstraintFactory .eINSTANCE.createHyConstraintModel();
			HyContextualInformationEnum enumC =(HyContextualInformationEnum) model.getConstraints().get(0);
			enumC.getEnumType();
			EcoreIOUtil.saveModelAs(model, file);
		}
        
		
		HyFeatureModel featureModel = null;
		
		try{
			featureModel = (HyFeatureModel) EcoreIOUtil.loadModel(featureModelFile);
		} catch(Exception e){
			e.printStackTrace();
		}
		

		
		HyContextModel contextModel = ContextInformationResolverUtil.getAccompanyingContextModel(model);
		if(contextModel == null){
			contextModel = HyContextInformationFactory.eINSTANCE.createHyContextModel();
			IPath containerFullPath = file.getFullPath().removeFileExtension();
			containerFullPath.addFileExtension(HyContextInformationUtil.getContextModelFileExtensionForConcreteSyntax());
			 
			
			EcoreIOUtil.saveModelAs(contextModel, ResourceUtil.getLocalFile(containerFullPath));
		}
		
		        
        Map<Object, IContentPart<? extends Node>> contents = getContentViewer().getContentPartMap();
		contents.putAll(getPaletteViewerFeatures().getContentPartMap());
		contents.putAll(getPaletteViewerContexts().getContentPartMap());
		contents.putAll(getEvolutionViewer().getContentPartMap());
		Collection<IContentPart<? extends Node>> collection = contents.values();
        
        for(IContentPart<? extends Node> c: collection){
       	 if(c instanceof AbstractChoiceBoxPart){
       		 
       		 ((AbstractChoiceBoxPart<?>) c).getContent().refreshContents(featureModel, contextModel);
       		 c.refreshVisual();
       		 
       		 
       		 

       	 }
       	 if(c instanceof EvolutionViewRootPart){
       		HyFeatureModelWrapped modelWrapped = new HyFeatureModelWrapped(featureModel);
    		List<Date> dates  = modelWrapped.getDates();
       		
       		 ((EvolutionSliderPart) c).getContent().setDatesSlider(dates);
       		 c.refreshVisual();
       	 }
       	 
       	 
        //}
     }
        needToRefreshFeatureModel = false;
		}
		
		super.setFocus();
	}
	
	public class MyResourceChangeListener implements IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
	    	 
	  
	        for(IResourceDelta affected: event.getDelta().getAffectedChildren()){
	
	        	for(IResourceDelta affected2:affected.getAffectedChildren()){
	        		String fileExtension = affected2.getFullPath().getFileExtension();
	              
	   	         if(fileExtension.equals(("hyfeature")) || fileExtension.equals("hycontextinformation")){
	   	         needToRefreshFeatureModel = true;
	   	      }
	        		
	        	}
	        }
	

	         }
	   };
	     
	   
	IFile featureModelFile;
	MyResourceChangeListener myResourceChangeListener;
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	
		super.init(site, input);
		
		

		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		((PaletteRootPart) getPaletteViewer().getRootPart()).setFile(file);
		
	
	

		/*
		 * I removed the change listener, because I am not sure if it is working propertly
		 */
//		myResourceChangeListener = new MyResourceChangeListener();
//			
//		
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(
//			      myResourceChangeListener, IResourceChangeEvent.POST_CHANGE);

	
		HyConstraintModel model;
		try {
			model = (HyConstraintModel) EcoreIOUtil.loadModel(file);			
		} catch (Exception e) {
			//e.printStackTrace();
			model = HyConstraintFactory .eINSTANCE.createHyConstraintModel();
			HyContextualInformationEnum enumC =(HyContextualInformationEnum) model.getConstraints().get(0);
			EcoreIOUtil.saveModelAs(model, file);
		}

		HyFeatureModel featureModel = HyFeatureResolverUtil.getAccompanyingFeatureModel(model);
		
		featureModelFile = EcoreIOUtil.getFile(featureModel);
		
		HyFeatureModelWrapped modelWrapped = new HyFeatureModelWrapped(featureModel);
		List<Date> list  = modelWrapped.getDates();
		

		HyContextModel contextModel = ContextInformationResolverUtil.getAccompanyingContextModel(model);
		if(contextModel == null){
			contextModel = HyContextInformationFactory.eINSTANCE.createHyContextModel();
			
			IPath containerFullPath = file.getFullPath().removeFileExtension();
			containerFullPath.addFileExtension(HyContextInformationUtil.getContextModelFileExtensionForConcreteSyntax());
			 
			
			EcoreIOUtil.saveModelAs(contextModel, ResourceUtil.getLocalFile(containerFullPath));
		}


		getContentViewer().getContents()
				.setAll(ContentViewService.createContentViewerContentFromConstraintModel(featureModel, model, contextModel));


		getPaletteViewer().getContents().setAll(ShapeService.createControlPaletteViewerContents(featureModel, contextModel));
		getPaletteViewerFeatures().getContents()
				.setAll(FeatureShapeService.createFeaturePaletteViewerContents(featureModel.getFeatures(), featureModel, contextModel));

		getPaletteViewerOperators().getContents().setAll(OperatorShapeService.createPaletteOperatorsViewContents());
		getPaletteViewerArithmetics().getContents().setAll(ArithmeticalOperatorShapeService.createPaletteArithmeticViewContents(featureModel, contextModel));
		getPaletteViewerContexts().getContents()
				.setAll(ShapeService.createContextPaletteContent(featureModel,contextModel, featureModel.getFeatures()));
	
		getEvolutionViewer().getContents().setAll(GraphicalEditor.createEvolutionSliderViewContents(featureModelFile, list));
	
		
	}

	public void dispose() {

	
		// clear viewer models
		getContentViewer().getAdapter(SelectionModel.class).clearSelection();
		getContentViewer().getAdapter(HoverModel.class).clearHover();
		getContentViewer().getAdapter(FocusModel.class).setFocus(null);
		getContentViewer().contentsProperty().clear();
		getPaletteViewer().getAdapter(SelectionModel.class).clearSelection();
		getPaletteViewer().getAdapter(HoverModel.class).clearHover();
		getPaletteViewer().getAdapter(FocusModel.class).setFocus(null);
		getPaletteViewer().contentsProperty().clear();

		getPaletteViewerContexts().getAdapter(SelectionModel.class).clearSelection();
		getPaletteViewerContexts().getAdapter(HoverModel.class).clearHover();
		getPaletteViewerContexts().getAdapter(FocusModel.class).setFocus(null);
		getPaletteViewerFeatures().contentsProperty().clear();

		getPaletteViewerFeatures().getAdapter(SelectionModel.class).clearSelection();
		getPaletteViewerFeatures().getAdapter(HoverModel.class).clearHover();
		getPaletteViewerFeatures().getAdapter(FocusModel.class).setFocus(null);
		getPaletteViewerFeatures().contentsProperty().clear();
		// dispose actions
		if (zoomActionGroup != null) {
			zoomActionGroup.dispose();
			zoomActionGroup = null;
		}
		if (scrollActionGroup != null) {
			scrollActionGroup.dispose();
			scrollActionGroup = null;
		}
		if (fitToViewportLockAction != null) {
			fitToViewportLockAction.dispose();
			fitToViewportLockAction = null;
		}

		
		//
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		workspace.removeResourceChangeListener(myResourceChangeListener);
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		ObservableList<Object> allContents = getContentViewer().getContents();
		
	
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();

		HyConstraintModel model = (HyConstraintModel) EcoreIOUtil.loadModel(file);

		List<HyConstraint> oldConstraints = model.getConstraints();
		model.getConstraints().removeAll(oldConstraints);
		EcoreIOUtil.saveModel(model);

		for (Object content : allContents) {

			if (content instanceof ControlIfBlockModel) {
				if (((ControlIfBlockModel) content).getParentBlock() == null) {

					
					ControlIfBlockModel controlIfBlockModel = (ControlIfBlockModel) content;
					
					if(controlIfBlockModel.getFixedChildOperatorBlockModel()!=null){
						if(controlIfBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock()!=null){
							HyExpression impliesExpression = resolveHyImpliesOrEquivalenceExpression(controlIfBlockModel);
							
							HyConstraint constraint = HyConstraintFactory.eINSTANCE.createHyConstraint();
							
							constraint.setRootExpression(impliesExpression);
							//if(controlIfBlockModel.getValidSince()!=null){
								constraint.setValidSince(controlIfBlockModel.getValidSince());
							
							//}
							//if(controlIfBlockModel.getValidUntil()!=null){
								constraint.setValidUntil(controlIfBlockModel.getValidUntil());
							//}
							
							model.getConstraints().add(constraint);
							
						}
					}
					
				
				}
			}
			
			if(content instanceof ControlValidateOperatorBlockModel){
				if(((ControlValidateOperatorBlockModel) content).getParentBlock()== null){
					ControlValidateOperatorBlockModel controlBlock = (ControlValidateOperatorBlockModel) content;
					OperatorFixedBlockModel fixedModel = ((ControlValidateOperatorBlockModel) content).getFixedChildOperatorBlockModel();
					
				
					if(fixedModel != null){
						OperatorMovableBlockModel  operatorMovableChildBlock = fixedModel.getMovableChildBlock();
						if(operatorMovableChildBlock != null){
							HyExpression expression = resolveOperatorMovableBlock(operatorMovableChildBlock);
							if(expression!=null){
								HyConstraint constraint = HyConstraintFactory.eINSTANCE.createHyConstraint();
								constraint.setRootExpression(expression);
								
								if(controlBlock.getValidSince()!=null){
									constraint.setValidSince(controlBlock.getValidSince());
								
								}
								if(controlBlock.getValidUntil()!=null){
									constraint.setValidUntil(controlBlock.getValidUntil());
								}
								
								model.getConstraints().add(constraint);
								
								
							}
								
						
						}
						
						
					}

				}
			}

		}

		EcoreIOUtil.saveModel(model);
		

	}
	
	/**
	 * 
	 * 
	 * @param type of the ControlBlock
	 * @return HyImpliesExpression or HyEquivalenceExpression
	 */
	private HyBinaryExpression createImpliesOrEquivalenceExpression(ControlBlockType type){
		if(type.equals(ControlBlockType.EQUIVALENCE) || type.equals(ControlBlockType.NOT_EQUIVALENCE)){
			return HyExpressionFactory.eINSTANCE.createHyEquivalenceExpression();
		} else if(type.equals(ControlBlockType.IMPLIES) || type.equals(ControlBlockType.NOT_IMPLIES)){
			return HyExpressionFactory.eINSTANCE.createHyImpliesExpression();
		}
		return null;
	}
	
	private HyExpression resolveHyImpliesOrEquivalenceExpression(ControlIfBlockModel controlIfBlockModel) {
		HyBinaryExpression impliesExpression = createImpliesOrEquivalenceExpression(controlIfBlockModel.getControlBlockType());
//		HyImpliesExpression impliesExpression = HyExpressionFactory.eINSTANCE.createHyImpliesExpression();
		HyExpression ex1 = resolveOperatorMovableBlock(controlIfBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock());
		if(ex1!=null){
			impliesExpression.setOperand1(ex1);
			if(!(controlIfBlockModel.getChildBlocks().isEmpty())){
				
			HyNestedExpression nestedExpression = HyExpressionFactory.eINSTANCE.createHyNestedExpression();
			
			int size = controlIfBlockModel.getChildBlocks().size();
			List<AbstractGeometricElement<? extends IGeometry>> list = controlIfBlockModel.getChildBlocks();
			
			if(size>1){
				nestedExpression = resolveChildBlocks(size, 0, list, controlIfBlockModel);
				
				if(nestedExpression!=null){
				impliesExpression.setOperand2(nestedExpression);
				
				if(controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_EQUIVALENCE) || controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_IMPLIES)){
					HyNotExpression hyNotExpression = HyExpressionFactory.eINSTANCE.createHyNotExpression();
					hyNotExpression.setOperand(impliesExpression);
					return hyNotExpression;
				}else{
				
				return impliesExpression;
				}
				}
				
				
			}else{
			
				if(list.get(0) instanceof ControlValidateOperatorBlockModel){
					ControlValidateOperatorBlockModel controlValidateOperatorBlockModel = (ControlValidateOperatorBlockModel) list.get(0);
				
					if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel()!=null){
						if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock()!=null){
							HyExpression ex2 = resolveOperatorMovableBlock(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock());
							if(ex2!=null){
								nestedExpression.setOperand(ex2);
								impliesExpression.setOperand2(nestedExpression);
								if(controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_EQUIVALENCE) || controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_IMPLIES)){
									HyNotExpression hyNotExpression = HyExpressionFactory.eINSTANCE.createHyNotExpression();
									hyNotExpression.setOperand(impliesExpression);
									return hyNotExpression;
								}else{
								
								return impliesExpression;
								}
								
								
								
							}else{
								return null;
							}
						}
					}	
				}
				if(list.get(0) instanceof ControlIfBlockModel){
					ControlIfBlockModel controlIf= (ControlIfBlockModel) list.get(0);
					HyExpression operand2 = resolveHyImpliesOrEquivalenceExpression(controlIf);
				
					if(operand2 != null){
						nestedExpression.setOperand(operand2);
						impliesExpression.setOperand2(nestedExpression);
						if(controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_EQUIVALENCE) || controlIfBlockModel.getControlBlockType().equals(ControlBlockType.NOT_IMPLIES)){
							HyNotExpression hyNotExpression = HyExpressionFactory.eINSTANCE.createHyNotExpression();
							hyNotExpression.setOperand(impliesExpression);
							return hyNotExpression;
						}else{
						
						return impliesExpression;
						}
						
					}
					else{
						return null;
					}
				}
				
	
				
			}
			}
			
		}
		return null;
		
	}
	

	
	

	private HyNestedExpression resolveChildBlocks(int size, int currentPosition, List<AbstractGeometricElement<? extends IGeometry>> list,ControlIfBlockModel controlIfBlockModel) {
		HyNestedExpression nestedExpression = HyExpressionFactory.eINSTANCE.createHyNestedExpression();
		if(currentPosition == size-1){
			if(list.get(currentPosition) instanceof ControlValidateOperatorBlockModel){
				ControlValidateOperatorBlockModel controlValidateOperatorBlockModel = (ControlValidateOperatorBlockModel) list.get(0);
			
				if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel()!=null){
					if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock()!=null){
						HyExpression ex1 = resolveOperatorMovableBlock(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock());
						if(ex1!=null){
							
						    nestedExpression.setOperand(ex1);
						    return nestedExpression;

						}else{
							return null;
						}
					}
				}	
			}	
			
		} else if(currentPosition<size-1){
			HyAndExpression hyAndExpression = HyExpressionFactory.eINSTANCE.createHyAndExpression();
			
			if(list.get(currentPosition) instanceof ControlValidateOperatorBlockModel){
				ControlValidateOperatorBlockModel controlValidateOperatorBlockModel = (ControlValidateOperatorBlockModel) list.get(0);
			
				if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel()!=null){
					if(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock()!=null){
						HyExpression ex1 = resolveOperatorMovableBlock(controlValidateOperatorBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock());
						if(ex1!=null){
							
							hyAndExpression.setOperand1(ex1);
							currentPosition++;
							HyNestedExpression nestedExpression2 = resolveChildBlocks(size, currentPosition, list, controlIfBlockModel);
							if(nestedExpression2!=null){
								hyAndExpression.setOperand2(nestedExpression2);
								nestedExpression.setOperand(hyAndExpression);
								return nestedExpression;
							}
						   
							
						}else{
							return null;
						}
					}
				}	
			}	
			
			
		}
		
		return null;
		
		
	}

	private HyExpression resolveOperatorMovableBlock(OperatorMovableBlockModel operatorMovableChildBlock) {
		if(operatorMovableChildBlock instanceof OperatorComparisonBlockModel){
			OperatorComparisonBlockModel comparisonBlockModel = (OperatorComparisonBlockModel) operatorMovableChildBlock;
			OperatorComparisonType comparisonType = comparisonBlockModel.getComparisonType();
			//OperatorComparisonValueType valueType = ((OperatorComparisonBlockModel) operatorMovableChildBlock).getComparisonValueType();

			switch(comparisonType){
			case NOT_EQUALS: 
				HyNotEqualExpression notEqualExpression = HyExpressionFactory.eINSTANCE.createHyNotEqualExpression();
				return resolveHyBinaryExpression(notEqualExpression, comparisonBlockModel);
			case EQUALS:
				HyEqualExpression equalExpression = HyExpressionFactory.eINSTANCE.createHyEqualExpression();
				return resolveHyBinaryExpression(equalExpression, comparisonBlockModel);
			case BIGGER: 
				HyGreaterExpression greaterExpression = HyExpressionFactory.eINSTANCE.createHyGreaterExpression();
				return resolveHyBinaryExpression(greaterExpression, comparisonBlockModel);
			case EQUALS_OR_BIGGER:
				HyGreaterOrEqualExpression greaterOrEqualExpression = HyExpressionFactory.eINSTANCE.createHyGreaterOrEqualExpression();
				return resolveHyBinaryExpression(greaterOrEqualExpression, comparisonBlockModel);
			case SMALLER:
				HyLessExpression lessExpression = HyExpressionFactory.eINSTANCE.createHyLessExpression();
				return resolveHyBinaryExpression(lessExpression, comparisonBlockModel);
			case EQUALS_OR_SMALLER:
				HyLessOrEqualExpression lessOrEqualExpression = HyExpressionFactory.eINSTANCE.createHyLessOrEqualExpression();
				return resolveHyBinaryExpression(lessOrEqualExpression, comparisonBlockModel);
			default: 
				return null;
			
			}
			
		}
		else if(operatorMovableChildBlock instanceof OperatorAndBlockModel){
			OperatorAndBlockModel operatorAndBlockModel  = (OperatorAndBlockModel) operatorMovableChildBlock;
			if(operatorAndBlockModel.getFixedChildOperator1()!= null && operatorAndBlockModel.getFixedChildOperator2()!=null){
				HyNestedExpression nestedExpression = HyExpressionFactory.eINSTANCE.createHyNestedExpression();
				if(operatorAndBlockModel.getOperatorAndORType().equals(OperatorAndORType.AND)){
				HyAndExpression andExpression = HyExpressionFactory.eINSTANCE.createHyAndExpression();	
					
				andExpression = (HyAndExpression) resolveHyAndORExpression(andExpression, operatorAndBlockModel);
				nestedExpression.setOperand(andExpression);
				return nestedExpression;
				}
				if(operatorAndBlockModel.getOperatorAndORType().equals(OperatorAndORType.OR)){
					HyOrExpression orExpression = HyExpressionFactory.eINSTANCE.createHyOrExpression();
					orExpression = (HyOrExpression) resolveHyAndORExpression(orExpression, operatorAndBlockModel);

					nestedExpression.setOperand(orExpression);
					return nestedExpression;
					
				}
				
			}
			
		}else if(operatorMovableChildBlock instanceof OperatorNotBlockModel){
			OperatorNotBlockModel operatorNotBlockModel = (OperatorNotBlockModel) operatorMovableChildBlock;
			if(operatorNotBlockModel.getFixedChildOperatorBlockModel()!=null){
				if(operatorNotBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock()!=null){
					HyNotExpression notExpression = HyExpressionFactory.eINSTANCE.createHyNotExpression();
					HyExpression expression = resolveOperatorMovableBlock(operatorNotBlockModel.getFixedChildOperatorBlockModel().getMovableChildBlock());
				
					if(expression!=null){
						notExpression.setOperand(expression);
						return notExpression;
					}
					
					
					
				}
			}
		}else if(operatorMovableChildBlock instanceof OperatorFeatureIsSelectedModel){
			OperatorFeatureIsSelectedModel operatorFeatureIsSelectedModel = (OperatorFeatureIsSelectedModel) operatorMovableChildBlock;
			if(operatorFeatureIsSelectedModel.getChoiceBoxContextChild()!=null){
				if(operatorFeatureIsSelectedModel.getChoiceBoxContextChild().getSelectedValue()!=null){
					HyFeatureReferenceExpression featureReferenceExpression = HyExpressionFactory.eINSTANCE.createHyFeatureReferenceExpression();
					featureReferenceExpression.setFeature((HyFeature) operatorFeatureIsSelectedModel.getChoiceBoxContextChild().getSelectedValue());
					
					return featureReferenceExpression;
				}
			}
		} else if(operatorMovableChildBlock instanceof OperatorNumercialComparisonBlockModel){
			return resolveNumericalComparisonOperator((OperatorNumercialComparisonBlockModel) operatorMovableChildBlock);
			
			
		}
		
		return null;
		
	}
	
	
	private HyBinaryExpression createHyComparisonExpression(OperatorComparisonType type){
		switch(type){
		case NOT_EQUALS: 
			return HyExpressionFactory.eINSTANCE.createHyNotEqualExpression();

		case EQUALS:
			return HyExpressionFactory.eINSTANCE.createHyEqualExpression();
		case BIGGER: 
			return HyExpressionFactory.eINSTANCE.createHyGreaterExpression();
		case EQUALS_OR_BIGGER:
			return HyExpressionFactory.eINSTANCE.createHyGreaterOrEqualExpression();
		case SMALLER:
			return HyExpressionFactory.eINSTANCE.createHyLessExpression();
		case EQUALS_OR_SMALLER:
			return HyExpressionFactory.eINSTANCE.createHyLessOrEqualExpression();
		default: 
			return null;
		
		}
	}
	
	private HyExpression resolveNumericalComparisonOperator(
			OperatorNumercialComparisonBlockModel operatorMovableChildBlock) {
		
		HyBinaryExpression expression = createHyComparisonExpression(operatorMovableChildBlock.getComparisonType());
		ArithmeticalOperatorFixedBlock fixedOperand1 = operatorMovableChildBlock.getFixedChildOperator1();
		if(fixedOperand1!=null){
			if(fixedOperand1.getMovableChildBlock()!=null){
				HyExpression operand1 = resolveArithmeticalOperatorBlock(fixedOperand1.getMovableChildBlock());
				if(operand1!=null){
					expression.setOperand1(operand1);
					
					ArithmeticalOperatorFixedBlock fixedOperand2 = operatorMovableChildBlock.getFixedChildOperator2();
					if(fixedOperand2!=null){
						HyExpression operand2 = resolveArithmeticalOperatorBlock(fixedOperand2.getMovableChildBlock());
						if(operand2!=null){
							expression.setOperand2(operand2);
							return expression;
						}
					}
				}
			}
		}
		return null;
	}

	private HyBinaryExpression resolveHyAndORExpression(HyBinaryExpression expression, OperatorAndBlockModel model){
		if(model.getFixedChildOperator1().getMovableChildBlock()!=null){
			HyExpression ex1 = resolveOperatorMovableBlock(model.getFixedChildOperator1().getMovableChildBlock());
			if(ex1 != null){
				expression.setOperand1(ex1);
				if (model.getFixedChildOperator2().getMovableChildBlock()!=null){
					HyExpression ex2 = resolveOperatorMovableBlock(model.getFixedChildOperator2().getMovableChildBlock());
				if(ex2 != null){
					expression.setOperand2(ex2);
					return expression;
				}
				
				}
			}
			
		} 
		return null;
	}
	
	
	
	
	
	private HyBinaryExpression resolveArithmeticalExpression(HyBinaryExpression binaryExpression, ArithmeticalOperationOperatorBlockModel model){
		
		
		if(model.getFixedChildOperator1().getMovableChildBlock()!=null){
		HyExpression operand1 = resolveArithmeticalOperatorBlock(model.getFixedChildOperator1().getMovableChildBlock());
		
		if(operand1 != null){
			
			binaryExpression.setOperand1(operand1);
			
			if(model.getFixedChildOperator2().getMovableChildBlock()!=null){
			HyExpression operand2 = resolveArithmeticalOperatorBlock(model.getFixedChildOperator2().getMovableChildBlock());
			if(operand2 != null){
				binaryExpression.setOperand2(operand2);
				return binaryExpression;
			}
			}
		}
		}
		
		return null;
		
	}
	
	private HyBinaryExpression createArithmeticalOperationExpression(ArithmeticalOperatorType type){
		switch (type) {
		case ADDITION:
			return HyExpressionFactory.eINSTANCE.createHyAdditionExpression();
		case DIVISION:
			return HyExpressionFactory.eINSTANCE.createHyDivisionExpression();
		case MODULO:
			return HyExpressionFactory.eINSTANCE.createHyModuloExpression();
		case MULTIPLICATION:
			return HyExpressionFactory.eINSTANCE.createHyMultiplicationExpression();
		case SUBTRACTION:
			return HyExpressionFactory.eINSTANCE.createHySubtractionExpression();
		default:
			return null;
		}
	}
	
	
	
	private HyExpression resolveArithmeticalOperatorBlock(ArithmeticalOperatorMovableBlock movableChildBlock) {
		HyNestedExpression nestedExpression = HyExpressionFactory.eINSTANCE.createHyNestedExpression();
		HyExpression operand = null;
		if(movableChildBlock instanceof ArithmeticalOperationOperatorBlockModel){
			
			operand = resolveArithmeticalExpression(createArithmeticalOperationExpression(((ArithmeticalOperationOperatorBlockModel) movableChildBlock).getArithmeticalOperatorType()), (ArithmeticalOperationOperatorBlockModel) movableChildBlock);
		
		}
		else if(movableChildBlock instanceof NegationOperatorBlockModel){
			operand = resolveNegationExpression((NegationOperatorBlockModel) movableChildBlock);
		}
		else if(movableChildBlock instanceof ArithmeticalTextFielOperatorBlockModel){
			return resolveTextFieldOperator((ArithmeticalTextFielOperatorBlockModel) movableChildBlock);
		}
		else if(movableChildBlock instanceof MinMaxObjectReferenceBlockModel){
			return resolveMinMaxOperator((MinMaxObjectReferenceBlockModel) movableChildBlock);
		}
		else if(movableChildBlock instanceof NumericalObjectReferenceOperator){
			return resolveNumericalReference((NumericalObjectReferenceOperator) movableChildBlock);
			
		}
		if(operand!=null){
			nestedExpression.setOperand(operand);
			return nestedExpression;
					
		}
		return null;
	}
	

	private HyExpression resolveTextFieldOperator(ArithmeticalTextFielOperatorBlockModel movableChildBlock) {
		if(movableChildBlock.getTextFieldModel()!=null){
			String enteredValue = movableChildBlock.getTextFieldModel().getEnteredValue();
			if(enteredValue!=null){
				Integer value = Integer.parseInt(enteredValue);
				
				HyValueExpression valueExpression = HyExpressionFactory.eINSTANCE.createHyValueExpression();
				HyNumberValue numberValue = HyDataValuesFactory.eINSTANCE.createHyNumberValue();
				numberValue.setValue(value);
				valueExpression.setValue(numberValue);
				return valueExpression;
				
			
			}
		}
		return null;
	}

	private HyExpression resolveNumericalReference(NumericalObjectReferenceOperator movableChildBlock) {
		
		
		ChoiceBoxModel<?> choiceBoxModel = movableChildBlock.getContextChoiceBox();
		if(choiceBoxModel.getSelectedValue()!=null){
			if(choiceBoxModel.getSelectedValue() instanceof HyFeatureAttribute){
				HyAttributeReferenceExpression attributeExpression = HyExpressionFactory.eINSTANCE.createHyAttributeReferenceExpression();
				attributeExpression.setAttribute((HyFeatureAttribute) choiceBoxModel.getSelectedValue());
				return attributeExpression;
			}
			if( choiceBoxModel.getSelectedValue() instanceof HyContextualInformationNumber){
				
				HyContextInformationReferenceExpression contextExpression = HyExpressionFactory.eINSTANCE.createHyContextInformationReferenceExpression();
				contextExpression.setContextInformation((HyContextualInformation) choiceBoxModel.getSelectedValue());
				return contextExpression;
			}
			
			
		}
		return null;
	}

	private HyExpression resolveMinMaxOperator(MinMaxObjectReferenceBlockModel movableChildBlock) {
		
		HyUnaryExpression expression = createMinMaxExpression(movableChildBlock.getMinMaxType());
		
		HyExpression operand = resolveNumericalReference(movableChildBlock);
		if(operand!=null){
		 expression.setOperand(operand);
		 return expression;
		}
		
//		ChoiceBoxModel<?> choiceBoxModel = movableChildBlock.getContextChoiceBox();
//		if(choiceBoxModel.getSelectedValue()!=null){
//			if(choiceBoxModel.getSelectedValue() instanceof HyFeatureAttribute){
//				HyAttributeReferenceExpression attributeExpression = HyExpressionFactory.eINSTANCE.createHyAttributeReferenceExpression();
//				attributeExpression.setAttribute((HyFeatureAttribute) choiceBoxModel.getSelectedValue());
//				expression.setOperand(attributeExpression);
//				return expression;
//			}
//			if( choiceBoxModel.getSelectedValue() instanceof HyContextualInformationNumber){
//				
//				HyContextInformationReferenceExpression contextExpression = HyExpressionFactory.eINSTANCE.createHyContextInformationReferenceExpression();
//				contextExpression.setContextInformation((HyContextualInformation) choiceBoxModel.getSelectedValue());
//				expression.setOperand(contextExpression);
//				return expression;
//			}
//			
//			
//		}
		return null;
	}
	
	private HyUnaryExpression createMinMaxExpression(MinMaxType type){
		switch (type) {
		case MAX:
			return HyExpressionFactory.eINSTANCE.createHyMaximumExpression();
		case MIN:
			return HyExpressionFactory.eINSTANCE.createHyMinimumExpression();

		default:
			return null;
		}
		
	}

	private HyExpression resolveNegationExpression(NegationOperatorBlockModel movableChildBlock) {
		HyNegationExpression negationExpression = HyExpressionFactory.eINSTANCE.createHyNegationExpression();
		
		if(movableChildBlock.getFixedChildOperator().getMovableChildBlock()!=null){
		HyExpression expression = resolveArithmeticalOperatorBlock(movableChildBlock.getFixedChildOperator().getMovableChildBlock());
		if(expression!=null){
			negationExpression.setOperand(expression);
			return negationExpression;
		}
		}
		
		
		return null;
	}

	private HyBinaryExpression resolveHyBinaryExpression(HyBinaryExpression binaryExpression, OperatorComparisonBlockModel model){

		
		ChoiceBoxContextModel object = (ChoiceBoxContextModel) model.getChoiceBoxContextChild();
		if(object!=null && object.getSelectedValue()!=null){
				HyContextInformationReferenceExpression contextReference = HyExpressionFactory.eINSTANCE.createHyContextInformationReferenceExpression();
				contextReference.setContextInformation(object.getSelectedValue());
				binaryExpression.setOperand1(contextReference);
				
				if(model instanceof OperatorWithChoiceBoxAndTextField){
					OperatorWithChoiceBoxAndTextField operatorWithChoiceBoxAndTextField = (OperatorWithChoiceBoxAndTextField) model;
					if(operatorWithChoiceBoxAndTextField.getTextFieldModel()!=null || operatorWithChoiceBoxAndTextField.getTextFieldModel().getEnteredValue()!=null){
					
						Integer enteredValue = Integer.parseInt(operatorWithChoiceBoxAndTextField.getTextFieldModel().getEnteredValue());
						//Integer enteredValue = Integer.getInteger(operatorWithChoiceBoxAndTextField.getTextFieldModel().getEnteredValue());
						HyValueExpression valueExpression = HyExpressionFactory.eINSTANCE.createHyValueExpression();
						HyNumberValue numberValue = HyDataValuesFactory.eINSTANCE.createHyNumberValue();
						numberValue.setValue(enteredValue);
						valueExpression.setValue(numberValue);
						
						binaryExpression.setOperand2(valueExpression);
						return binaryExpression;
						
					}
					return null;
				}
				ChoiceBoxValueModel valueModel = (ChoiceBoxValueModel) model.getChoiceBoxValueChild();
				if(valueModel!=null && valueModel.getSelectedValue()!= null){
					
					if(valueModel.getSelectedValue() instanceof String){
						HyBooleanValueExpression booleanValueExpression = HyExpressionFactory.eINSTANCE.createHyBooleanValueExpression();
						if(valueModel.getSelectedValue().equals("True")){
							booleanValueExpression.setValue(true);
							binaryExpression.setOperand2(booleanValueExpression);
							return binaryExpression;
						}
						if(valueModel.getSelectedValue().equals("False")){
							booleanValueExpression.setValue(false);
							binaryExpression.setOperand2(booleanValueExpression);
							return binaryExpression;
						}
						
						return null;
						
					}
					if(valueModel.getSelectedValue() instanceof HyContextualInformation){
						HyContextInformationReferenceExpression contextReference2 = HyExpressionFactory.eINSTANCE.createHyContextInformationReferenceExpression();
						contextReference2.setContextInformation((HyContextualInformation) valueModel.getSelectedValue());
						binaryExpression.setOperand2(contextReference2);
						return binaryExpression;
						
					}
					if(valueModel.getSelectedValue() instanceof HyFeatureAttribute){
						HyAttributeReferenceExpression attributeReferenceExpression = HyExpressionFactory.eINSTANCE.createHyAttributeReferenceExpression();
						attributeReferenceExpression.setAttribute((HyFeatureAttribute) valueModel.getSelectedValue());
						
						attributeReferenceExpression.setFeature(((HyFeatureAttribute) valueModel.getSelectedValue()).getFeature());
						binaryExpression.setOperand2(attributeReferenceExpression);
						return binaryExpression;
						
					}
					if(valueModel.getSelectedValue() instanceof HyEnumLiteral){
						//HyContextInformationReferenceExpression contextualInformationReferenceExpression = HyExpressionFactory.eINSTANCE.createHyContextInformationReferenceExpression();
					
						HyEnumLiteral enumLiteral = (HyEnumLiteral) valueModel.getSelectedValue();
						HyValueExpression valueExpression = HyExpressionFactory.eINSTANCE.createHyValueExpression();
					    HyEnumValue enumValue = HyDataValuesFactory.eINSTANCE.createHyEnumValue();
					enumValue.setEnum(enumLiteral.getEnum());
					enumValue.setEnumLiteral(enumLiteral);
					enumValue.setEnum(enumLiteral.getEnum());
					valueExpression.setValue(enumValue);
					
					binaryExpression.setOperand2(valueExpression);
					
				
					return binaryExpression;
					
					
					}
					
					
				}else{
					return null;
				}
				
			
			
		}else{
			return null;
		}
		return null;
		
		
		
	}



	@Override
	public void doSaveAs() {
		
		
	}
	@Override
	public boolean isSaveAsAllowed() {
		
		return false;
	}
	
	protected IViewer getPaletteViewer() {
		return getDomain().getAdapter(AdapterKey.get(IViewer.class,
				GraphicalEditorModule.PALETTE_VIEWER_ROLE));
	}
	
	
	protected IViewer getPaletteViewerFeatures(){
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_FEATURES));
	}

	protected IViewer getPaletteViewerContexts(){
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_CONTEXTS));
	}
	
	protected IViewer getEvolutionViewer(){
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.EVOLUTION_VIEWER_ROLE));
	}
	
	protected IViewer getPaletteViewerArithmetics(){
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_ARITHMETICS));
	}
	protected IViewer getPaletteViewerOperators(){
		return getDomain().getAdapter(AdapterKey.get(IViewer.class, GraphicalEditorModule.PALETTE_VIEWER_ROLE_OPERATORS));
	}
	

	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// create actions
		zoomActionGroup = new ZoomActionGroup(new FitToViewportAction());
		zoomActionGroup.init(getContentViewer());
		fitToViewportLockAction = new FitToViewportLockAction();
		fitToViewportLockAction.init(getContentViewer());
		scrollActionGroup = new ScrollActionGroup();
		scrollActionGroup.init(getContentViewer());
		// contribute to toolbar
		IActionBars actionBars = getEditorSite().getActionBars();
		IToolBarManager mgr = actionBars.getToolBarManager();
		zoomActionGroup.fillActionBars(actionBars);
		mgr.add(new Separator());
		mgr.add(fitToViewportLockAction);
		mgr.add(new Separator());
		scrollActionGroup.fillActionBars(actionBars);
	}
	
	@Override
	protected void hookViewers() {
		// build viewers composite		
		GraphicalEditorViewersComposite viewersComposite = new GraphicalEditorViewersComposite(
				getContentViewer(), getPaletteViewer(), getPaletteViewerFeatures(), getPaletteViewerContexts(), getPaletteViewerOperators(), getPaletteViewerArithmetics(), getEvolutionViewer());
		// create scene and populate canvas
		getCanvas().setScene(new Scene(viewersComposite.getRootVBox()));
	}
}


