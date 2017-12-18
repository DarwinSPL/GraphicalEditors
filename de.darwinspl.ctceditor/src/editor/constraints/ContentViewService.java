package editor.constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.IGeometry;

import editor.ArithmeticalOperatorShapeService;
import editor.ControlShapeService;
import editor.OperatorShapeService;
import editor.model.AbstractBlockElement;
import editor.model.AbstractGeometricElement;
import editor.model.GeometricShape;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel;
import editor.model.arithmetical.ArithmeticalOperationOperatorBlockModel.ArithmeticalOperatorType;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock;
import editor.model.arithmetical.ArithmeticalOperatorFixedBlock.ArithmeticalOperatorFixedType;
import editor.model.arithmetical.ArithmeticalTextFielOperatorBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel;
import editor.model.arithmetical.MinMaxObjectReferenceBlockModel.MinMaxType;
import editor.model.arithmetical.NegationOperatorBlockModel;
import editor.model.arithmetical.NumericalObjectReferenceOperator;
import editor.model.choiceboxes.ChoiceBoxContextModel;
import editor.model.choiceboxes.ChoiceBoxFeatureAttributeModel;
import editor.model.choiceboxes.ChoiceBoxFeatureModel;
import editor.model.choiceboxes.ChoiceBoxModel.ChoiceBoxType;
import editor.model.choiceboxes.ChoiceBoxValueModel;
import editor.model.context.TextFieldModel;
import editor.model.control.ControlValidateOperatorBlockModel.ControlValidateOperatorBlockType;
import editor.model.control.ControlAndOrBlock;
import editor.model.control.ControlBlockModel;
import editor.model.control.ControlBlockModel.ControlBlockOperandType;
import editor.model.control.ControlIfBlockModel;
import editor.model.control.ControlIfBlockModel.ControlBlockType;
import editor.model.control.ControlValidateOperatorBlockModel;
import editor.model.nodes.TextModel;
import editor.model.nodes.TextModel.TextType;
import editor.model.operator.OperatorAndBlockModel;
import editor.model.operator.OperatorAndBlockModel.OperatorAndORType;
import editor.model.operator.OperatorComparisonBlockModel;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonType;
import editor.model.operator.OperatorComparisonBlockModel.OperatorComparisonValueType;
import editor.model.operator.OperatorFeatureIsSelectedModel;
import editor.model.operator.OperatorFixedBlockModel;
import editor.model.operator.OperatorFixedBlockModel.OperatorFixedBlockType;
import editor.model.operator.OperatorNotBlockModel;
import editor.model.operator.OperatorNumercialComparisonBlockModel;
import eu.hyvar.context.HyContextModel;
import eu.hyvar.context.HyContextualInformation;
import eu.hyvar.context.HyContextualInformationBoolean;
import eu.hyvar.context.HyContextualInformationEnum;
import eu.hyvar.dataValues.HyEnumLiteral;
import eu.hyvar.dataValues.HyEnumValue;
import eu.hyvar.dataValues.HyNumberValue;
import eu.hyvar.dataValues.HyValue;
import eu.hyvar.feature.HyFeatureModel;
import eu.hyvar.feature.constraint.HyConstraint;
import eu.hyvar.feature.constraint.HyConstraintModel;
import eu.hyvar.feature.expression.HyAdditionExpression;
import eu.hyvar.feature.expression.HyAndExpression;
import eu.hyvar.feature.expression.HyAtomicExpression;
import eu.hyvar.feature.expression.HyAttributeReferenceExpression;
import eu.hyvar.feature.expression.HyBinaryExpression;
import eu.hyvar.feature.expression.HyBooleanValueExpression;
import eu.hyvar.feature.expression.HyContextInformationReferenceExpression;
import eu.hyvar.feature.expression.HyDivisionExpression;
import eu.hyvar.feature.expression.HyEqualExpression;
import eu.hyvar.feature.expression.HyEquivalenceExpression;
import eu.hyvar.feature.expression.HyExpression;
import eu.hyvar.feature.expression.HyFeatureReferenceExpression;
import eu.hyvar.feature.expression.HyGreaterExpression;
import eu.hyvar.feature.expression.HyGreaterOrEqualExpression;
import eu.hyvar.feature.expression.HyImpliesExpression;
import eu.hyvar.feature.expression.HyLessExpression;
import eu.hyvar.feature.expression.HyLessOrEqualExpression;
import eu.hyvar.feature.expression.HyMaximumExpression;
import eu.hyvar.feature.expression.HyMinimumExpression;
import eu.hyvar.feature.expression.HyModuloExpression;
import eu.hyvar.feature.expression.HyMultiplicationExpression;
import eu.hyvar.feature.expression.HyNegationExpression;
import eu.hyvar.feature.expression.HyNestedExpression;
import eu.hyvar.feature.expression.HyNotEqualExpression;
import eu.hyvar.feature.expression.HyNotExpression;
import eu.hyvar.feature.expression.HyOrExpression;
import eu.hyvar.feature.expression.HySubtractionExpression;
import eu.hyvar.feature.expression.HyUnaryExpression;
import eu.hyvar.feature.expression.HyValueExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * This class provides the functionality to create the respective blocks from the constraint model
 * 
 * 
 * @author Anna-Liisa
 *
 */
public class ContentViewService {
	
	
	private static final ObjectProperty<HyFeatureModel> featureModelProperty = new SimpleObjectProperty<HyFeatureModel>();

	private static final ObjectProperty<HyContextModel> contextModelProperty = new SimpleObjectProperty<HyContextModel>();
	

	private static boolean isOperand1;
	
	
/**
 * Generates the visual block from the constraint model
 * 
 * @param featureModel
 * @param constraintModel
 * @param contextModel
 * @return
 */
public static List<AbstractBlockElement> createContentViewerContentFromConstraintModel(HyFeatureModel featureModel, HyConstraintModel constraintModel, HyContextModel contextModel){
	
	featureModelProperty.set(featureModel);
	contextModelProperty.set(contextModel);
	
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	for (HyConstraint constraint : constraintModel.getConstraints()) {
		HyExpression rootExpression = constraint.getRootExpression();
		
		if(rootExpression instanceof HyImpliesExpression){
			visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyImpliesExpression) rootExpression, null, constraint.getValidSince(), constraint.getValidUntil(), ControlBlockType.IMPLIES));
		}else if(rootExpression instanceof HyEquivalenceExpression){
			visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyEquivalenceExpression) rootExpression, null, constraint.getValidSince(), constraint.getValidUntil(), ControlBlockType.EQUIVALENCE));
		} else if(rootExpression instanceof HyNotExpression && (((HyNotExpression) rootExpression).getOperand() instanceof HyImpliesExpression)){
			visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyImpliesExpression) ((HyNotExpression)rootExpression).getOperand(), null, constraint.getValidSince(), constraint.getValidUntil(), ControlBlockType.NOT_IMPLIES));
		}else if(rootExpression instanceof HyNotExpression && (((HyNotExpression) rootExpression).getOperand() instanceof HyEquivalenceExpression)){
			visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyEquivalenceExpression) ((HyNotExpression)rootExpression).getOperand(), null, constraint.getValidSince(), constraint.getValidUntil(), ControlBlockType.NOT_EQUIVALENCE));
		}
		else{
			if(constraint.getRootExpression()!= null){
				
		if((rootExpression instanceof HyAndExpression || rootExpression instanceof HyOrExpression)){
			HyBinaryExpression binaryExpression = (HyBinaryExpression) rootExpression;
			
			final ControlAndOrBlock controlBlock = new ControlAndOrBlock(ControlShapeService.createCustomControlAndOrBlockShape(), new AffineTransform(1, 0, 0, 1, 1, 1), Color.YELLOW, null, OperatorAndORType.AND);
			final TextModel text = new TextModel("AND", new AffineTransform(1,0,0,1,1,1), TextType.AND_OR_CONTROL);
			
			if(rootExpression instanceof HyOrExpression){
				controlBlock.setType(OperatorAndORType.OR);
				text.setText("OR");
			}
			
			if(constraint.getValidSince()!=null){
				controlBlock.setValidSince(constraint.getValidSince());
			}
			if(constraint.getValidUntil()!=null){
				controlBlock.setValidUntil(constraint.getValidUntil());
			}
			
			
			text.setParentBlock(controlBlock);
			visualParts.add(controlBlock);
			visualParts.add(text);
			
			isOperand1=true;
			visualParts.addAll(resolveHyExpression(binaryExpression.getOperand1(), controlBlock));
			isOperand1=false;
			visualParts.addAll(resolveHyExpression(binaryExpression.getOperand2(), controlBlock));
			
		
		} else{		
				
		final ControlValidateOperatorBlockModel controlBlockModel = new ControlValidateOperatorBlockModel(
				ControlShapeService.createCustomControlValidateOperatorShape(30, 200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.YELLOW,
				null, ControlValidateOperatorBlockType.VALIDATE);

		final OperatorFixedBlockModel fixedOperatorModel = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(160),
				OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null,
				OperatorFixedBlockType.VALIDATE_OPERATOR);
		
	
		if(constraint.getValidSince()!=null){
			controlBlockModel.setValidSince(constraint.getValidSince());
		}
		if(constraint.getValidUntil()!=null){
			controlBlockModel.setValidUntil(constraint.getValidUntil());
		}

		fixedOperatorModel.setParentBlock(controlBlockModel);
		visualParts.add(controlBlockModel);
		visualParts.add(fixedOperatorModel);
		visualParts.addAll(resolveHyExpression(rootExpression, fixedOperatorModel));
		}
		}
		}

	

	}
	return visualParts;
	
	
	
	
}




private static List<? extends AbstractBlockElement> resolveHyExpression(HyExpression expression, GeometricShape parent){
	List<AbstractBlockElement> visualParts = new ArrayList<>();
	if(expression instanceof HyUnaryExpression){
		visualParts.addAll(resolveHyUnaryExpression((HyUnaryExpression) expression, parent));
		
	} else if (expression instanceof HyBinaryExpression){
		visualParts.addAll(resolveHyBinaryExpression((HyBinaryExpression) expression, parent));
	}else if(expression instanceof HyAtomicExpression){
		visualParts.addAll(resolveHyAtomicExpression((HyAtomicExpression) expression, parent));
	} 
	
	return visualParts;
	
}


private static List<? extends AbstractBlockElement> resolveHyUnaryExpression(HyUnaryExpression expression, GeometricShape parent) {
	if(expression instanceof HyMaximumExpression){
		
	} else if(expression instanceof HyMinimumExpression){
		return resolveMinMaxExpression((HyUnaryExpression) expression, parent,  MinMaxType.MIN, "min");
		
	}else if(expression instanceof HyMaximumExpression){
		return resolveMinMaxExpression((HyUnaryExpression) expression, parent, MinMaxType.MAX, "max");
	}
	else if (expression instanceof HyNegationExpression){
		return resolveNegationExpression((HyNegationExpression) expression, parent);
		
	} else if(expression instanceof HyNestedExpression){
		return resolveHyExpression(((HyNestedExpression) expression).getOperand(), parent);
		
	} else if(expression instanceof HyNotExpression){
		if(expression.getOperand() instanceof HyImpliesExpression){
			return resolveImpliesOrEquivalenceExpression((HyBinaryExpression) expression.getOperand(), parent, null, null, ControlBlockType.NOT_IMPLIES);
		} else if (expression.getOperand() instanceof HyEquivalenceExpression){
			return resolveImpliesOrEquivalenceExpression((HyBinaryExpression) expression.getOperand(), parent, null, null, ControlBlockType.NOT_EQUIVALENCE);
		}
		
		else{
		  return resolveNotExpression((OperatorFixedBlockModel) parent, (HyNotExpression) expression);
		}
		
	}
	return null;
	
}



private static List<? extends AbstractBlockElement> resolveNegationExpression(HyNegationExpression expression,
		GeometricShape parent) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	final NegationOperatorBlockModel operator = new NegationOperatorBlockModel(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(100, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(230, 204, 179), null);
	operator.setParentBlock(parent);

	final ArithmeticalOperatorFixedBlock operand = new ArithmeticalOperatorFixedBlock(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.NEG_OPERAND);
	operand.setParentBlock(operator);

	final TextModel text = new TextModel("¬", new AffineTransform(1, 0, 0, 1, 1, 1), TextType.NEGATION);
	text.setParentBlock(operator);

	visualParts.add(operator);
	visualParts.add(operand);
	visualParts.add(text);
	
	visualParts.addAll(resolveHyExpression(expression.getOperand(), operand));
	return visualParts;
}


/**
 * Resolves a HyMinimum- and HyMaximumExpression 
 * 
 * @param expression
 * @param parent
 * @param type
 * @param minMaxText
 * @return a list containing all block elements for the expression, including the respective attribute or context reference
 */
private static List<? extends AbstractBlockElement> resolveMinMaxExpression(HyUnaryExpression expression, GeometricShape parent, MinMaxType type, String minMaxText) {
	List<AbstractBlockElement> visualParts = new  ArrayList<AbstractBlockElement>();
	
	final MinMaxObjectReferenceBlockModel operator = new MinMaxObjectReferenceBlockModel(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(220, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(230, 204, 179), null, type);

	operator.setParentBlock(parent);
	visualParts.add(operator);
	
	if(expression.getOperand() instanceof HyContextInformationReferenceExpression){
		final ChoiceBoxContextModel choiceBox = new ChoiceBoxContextModel(new AffineTransform(1,0,0,1,1,1), getContextModel(), getFeatureModel(), ChoiceBoxType.CONTEXT_NUMBER);
		choiceBox.setSelectedValue(((HyContextInformationReferenceExpression) expression.getOperand()).getContextInformation());
		choiceBox.setParentBlock(operator);
		visualParts.add(choiceBox);
	}
	if(expression.getOperand() instanceof HyAttributeReferenceExpression){
		
		final ChoiceBoxFeatureAttributeModel choiceBoxContextModel = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), getFeatureModel(), getContextModel(), ChoiceBoxType.ATTRIBUTE_NUMBER);
		
		choiceBoxContextModel.setSelectedValue(((HyAttributeReferenceExpression) expression.getOperand()).getAttribute());

		choiceBoxContextModel.setParentBlock(operator);
		visualParts.add(choiceBoxContextModel);
	}
	

	final TextModel text = new TextModel(minMaxText, new AffineTransform(1, 0, 0, 1, 1, 1), TextType.MIN_MAX_OPERATOR);
	text.setParentBlock(operator);
	visualParts.add(text);
	
	
	
	return visualParts;

}



private static Collection<? extends AbstractBlockElement> resolveHyBinaryExpression(HyBinaryExpression expression, GeometricShape parent) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();
    if(expression instanceof HyAdditionExpression ){
    	return resolveArithmeticOperationExpression(expression, ArithmeticalOperatorType.ADDITION, "+", parent);
		
	} else if(expression instanceof HyAndExpression){
		return resolveAndOrExpression((OperatorFixedBlockModel) parent, expression);
		
	} else if (expression instanceof HyDivisionExpression){
		return resolveArithmeticOperationExpression(expression, ArithmeticalOperatorType.DIVISION, "/", parent);
	} else if(expression instanceof HyEqualExpression){
		return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.EQUALS, "=", expression);
	} else if(expression instanceof HyEquivalenceExpression){
		return resolveImpliesOrEquivalenceExpression((HyImpliesExpression) expression, parent, null, null, ControlBlockType.EQUIVALENCE);
	}
    else if (expression instanceof HyGreaterExpression){
    	return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.BIGGER, ">", expression);
   } else if(expression instanceof HyGreaterOrEqualExpression){
	   return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.EQUALS_OR_BIGGER, ">=", expression);
    } else if(expression instanceof HyImpliesExpression){
     visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyImpliesExpression) expression, parent, null, null, ControlBlockType.IMPLIES));	
     
    }

else if (expression instanceof HyLessExpression){
	return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.SMALLER, "<", expression);
	
} else if(expression instanceof HyLessOrEqualExpression){
	return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.EQUALS_OR_SMALLER, "<=", expression);
} else if(expression instanceof HyModuloExpression){
	return resolveArithmeticOperationExpression(expression, ArithmeticalOperatorType.MODULO, "%", parent);
	
}
else if (expression instanceof HyMultiplicationExpression){
	return resolveArithmeticOperationExpression(expression, ArithmeticalOperatorType.MULTIPLICATION, "*", parent);
	
} else if(expression instanceof HyNotEqualExpression){
	return resolveComparisonExpression2((OperatorFixedBlockModel) parent, OperatorComparisonType.NOT_EQUALS, "!=", expression);
	
} else if(expression instanceof HyOrExpression){
	//TODO: Speichern von rekursiven AND/OR Control Blocks
//	if(expression instanceof HyImpliesExpression || expression instanceof HyEquivalenceExpression || expression instanceof Hy){
//	
	
	return resolveAndOrExpression((OperatorFixedBlockModel) parent, expression);
}
else if (expression instanceof HySubtractionExpression){
	return resolveArithmeticOperationExpression(expression, ArithmeticalOperatorType.SUBTRACTION, "-" ,parent);
	
}return visualParts;
}

private static List<? extends AbstractBlockElement> resolveComparisonExpression2(OperatorFixedBlockModel parent, OperatorComparisonType comparisonType, String operatorText,
		HyBinaryExpression expression) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();
	
	if(expression.getOperand1() instanceof HyAtomicExpression){
		
		if(expression.getOperand1() instanceof HyContextInformationReferenceExpression){
			HyContextualInformation context =((HyContextInformationReferenceExpression) expression.getOperand1()).getContextInformation();
			OperatorComparisonValueType valueType = OperatorComparisonValueType.NUMBER;
			ChoiceBoxType choiceBoxType = ChoiceBoxType.CONTEXT_NUMBER;
			ChoiceBoxType valueChoiceBoxType = ChoiceBoxType.VALUE_NUMBER;
			if(context instanceof HyContextualInformationBoolean){
				valueType = OperatorComparisonValueType.BOOLEAN;
			 choiceBoxType = ChoiceBoxType.CONTEXT_BOOLEAN;
			 valueChoiceBoxType = ChoiceBoxType.VALUE_BOOLEAN;
				
			}
			if(context instanceof HyContextualInformationEnum){
				valueType = OperatorComparisonValueType.ENUM;
				choiceBoxType = ChoiceBoxType.CONTEXT_ENUM;
				 valueChoiceBoxType = ChoiceBoxType.VALUE_ENUM;
			}
			OperatorComparisonType type = getOperatorComparisonType(expression);
			
			

		final OperatorComparisonBlockModel operatorModel = new OperatorComparisonBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(92, 214, 92),
				null, type, valueType);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), getContextModel(), getFeatureModel(), choiceBoxType);
		choiceBoxContextModel.setParentBlock(operatorModel);
	
		
			
	    HyContextInformationReferenceExpression contextRef = (HyContextInformationReferenceExpression) expression.getOperand1();
	    //HyContextualInformationEnum contextualInformation = (HyContextualInformationEnum) contextRef.getContextInformation();
	    if(!(choiceBoxContextModel.getChoices().contains(contextRef.getContextInformation()))){
	    	choiceBoxContextModel.getChoices().add(contextRef.getContextInformation());
	    }
        choiceBoxContextModel.setSelectedValue(contextRef.getContextInformation());
        
    
		
		final ChoiceBoxValueModel choiceBoxValueModel = new ChoiceBoxValueModel(new AffineTransform(1, 0, 0, 1, 1, 1),
				getContextModel(), getFeatureModel(), valueChoiceBoxType);

		choiceBoxValueModel.setParentBlock(operatorModel);
		if(expression.getOperand2() instanceof HyContextInformationReferenceExpression ){
		
		choiceBoxValueModel.setSelectedValue(((HyContextInformationReferenceExpression) expression.getOperand2()).getContextInformation());
		}
		if(expression.getOperand2() instanceof HyAttributeReferenceExpression ){
			choiceBoxValueModel.setSelectedValue(((HyAttributeReferenceExpression) expression.getOperand2()).getAttribute());
		}
		if(expression.getOperand2() instanceof HyBooleanValueExpression){
			Boolean value = ((HyBooleanValueExpression) expression.getOperand2()).isValue();
			choiceBoxValueModel.setSelectedValue(value.toString());
		}
		if(expression.getOperand2() instanceof HyValueExpression){
			HyValue value = ((HyValueExpression) expression.getOperand2()).getValue();
			if(value instanceof HyEnumValue){
				choiceBoxValueModel.setParentChoiceBoxSelection(choiceBoxContextModel.getSelectedValue());
				choiceBoxValueModel.refreshChoiceList();
				HyEnumLiteral literal = ((HyEnumValue) value).getEnumLiteral();
				choiceBoxValueModel.setSelectedValue(literal);
			}
		}
		

		final TextModel textModel = new TextModel(operatorText, new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		textModel.setParentBlock(operatorModel);
		
		operatorModel.setParentBlock(parent);

		visualParts.add(operatorModel);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(choiceBoxValueModel);
		visualParts.add(textModel);
		}
	}
	else{
		return resolveArithemticalComparisonExpression(expression, parent, comparisonType, operatorText);
	}
	return visualParts;
}



private static List<? extends AbstractBlockElement> resolveArithemticalComparisonExpression(
		HyBinaryExpression expression, OperatorFixedBlockModel parent, OperatorComparisonType comparisonType, String operatorText) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	final OperatorNumercialComparisonBlockModel operator = new OperatorNumercialComparisonBlockModel(
			OperatorShapeService.createOperatorShapeCustomLength(200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(92, 214, 92),
			null, comparisonType);
	operator.setParentBlock(parent);

	final ArithmeticalOperatorFixedBlock operand1 = new ArithmeticalOperatorFixedBlock(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(60, 26),
			new AffineTransform(1, 0, 0, 1, 1, 1), Color.SADDLEBROWN, null,
			ArithmeticalOperatorFixedType.COMPARISON_OPERAND1);
	operand1.setParentBlock(operator);

	final ArithmeticalOperatorFixedBlock operand2 = new ArithmeticalOperatorFixedBlock(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(60, 26),
			new AffineTransform(1, 0, 0, 1, 1, 1), Color.SADDLEBROWN, null,
			ArithmeticalOperatorFixedType.COMPARISON_OPERAND2);
	operand2.setParentBlock(operator);

	final TextModel text = new TextModel(operatorText, new AffineTransform(1, 0, 0, 1, 1, 1), TextType.COMPARISON_OPERATOR);
	text.setParentBlock(operator);

	visualParts.add(operator);
	visualParts.add(operand1);
	visualParts.add(operand2);
	visualParts.add(text);
	
	visualParts.addAll(resolveHyExpression(expression.getOperand1(), operand1));
	visualParts.addAll(resolveHyExpression(expression.getOperand2(), operand2));

	return visualParts;

}



private static Collection<? extends AbstractBlockElement> resolveArithmeticOperationExpression(
		HyBinaryExpression expression, ArithmeticalOperatorType type, String operatorText, GeometricShape parent) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	final ArithmeticalOperationOperatorBlockModel operator = new ArithmeticalOperationOperatorBlockModel(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(130, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(230, 204, 179), null, type);
	operator.setParentBlock(parent);

	final ArithmeticalOperatorFixedBlock operand1 = new ArithmeticalOperatorFixedBlock(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND1);
	operand1.setParentBlock(operator);

	final ArithmeticalOperatorFixedBlock operand2 = new ArithmeticalOperatorFixedBlock(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(40, 26), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(115, 77, 38), null, ArithmeticalOperatorFixedType.ARITHMETICAL_OPERAND2);
	operand2.setParentBlock(operator);

	final TextModel text = new TextModel(operatorText, new AffineTransform(1, 0, 0, 1, 1, 1),
			TextType.ARITHMETICAL_OPERATOR);
	text.setParentBlock(operator);

	visualParts.add(operator);
	visualParts.add(operand1);
	visualParts.add(operand2);
	visualParts.add(text);
	
	visualParts.addAll(resolveHyExpression(expression.getOperand1(), operand1));
	visualParts.addAll(resolveHyExpression(expression.getOperand2(), operand2));
	

	return visualParts;
}



private static List<? extends AbstractBlockElement> resolveHyAtomicExpression(HyAtomicExpression expression, GeometricShape parent) {
	
	if(parent instanceof ControlAndOrBlock){
		List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();

		final ControlValidateOperatorBlockModel controlBlockModel = new ControlValidateOperatorBlockModel(
				ControlShapeService.createCustomControlValidateOperatorShape(30, 200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.YELLOW,
				null, ControlValidateOperatorBlockType.VALIDATE);

		final OperatorFixedBlockModel fixedOperatorModel = new OperatorFixedBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(160),
				OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null,
				OperatorFixedBlockType.VALIDATE_OPERATOR);

		if(parent instanceof ControlAndOrBlock){
			if(isOperand1){
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND2);
			}
		}
		
        controlBlockModel.setParentBlock(parent);
        
        fixedOperatorModel.setParentBlock(controlBlockModel);
		
		visualParts.add(controlBlockModel);
	    visualParts.add(fixedOperatorModel);
		visualParts.addAll(resolveHyAtomicExpression(expression, fixedOperatorModel));
		return visualParts;
	}
	
	 if (expression instanceof HyFeatureReferenceExpression){
		return resolveFeatureReferenceExpression((OperatorFixedBlockModel) parent,(HyFeatureReferenceExpression) expression);
		 
	 } else if(expression instanceof HyAttributeReferenceExpression){
		 return resolveNumericalObjectReference(expression, parent);
	 	
	 } else if(expression instanceof HyBooleanValueExpression){
		
	 }
	 else if (expression instanceof HyContextInformationReferenceExpression){
			return resolveNumericalObjectReference(expression, parent);
	 } else if(expression instanceof HyValueExpression){
		 
		 return resolveValueExpression((HyValueExpression) expression, parent);
	 } 
	 	return null;
	 	
}

private static List<? extends AbstractBlockElement> resolveValueExpression(HyValueExpression expression,
		GeometricShape parent) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();
	
if(expression.getValue() instanceof HyNumberValue){	

	final ArithmeticalTextFielOperatorBlockModel operator = new ArithmeticalTextFielOperatorBlockModel(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(120, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(230, 204, 179), null);
	operator.setParentBlock(parent);

	final TextFieldModel textField = new TextFieldModel(new AffineTransform(1, 0, 0, 1, 1, 1));
	textField.setParentBlock(operator);
	
	int thisValue = ((HyNumberValue) expression.getValue()).getValue();
	textField.setEnteredValue(Integer.toString(thisValue));

	visualParts.add(operator);
	visualParts.add(textField);
	return visualParts;
}
return null;
}



private static List<? extends AbstractBlockElement> resolveNumericalObjectReference(HyAtomicExpression expression,
		GeometricShape parent) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	final NumericalObjectReferenceOperator operator = new NumericalObjectReferenceOperator(
			ArithmeticalOperatorShapeService.createCustomArithmeticalBlockShape(180, 30), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.rgb(230, 204, 179), null);
	operator.setParentBlock(parent);
	visualParts.add(operator);

	if(expression instanceof HyContextInformationReferenceExpression){
	final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
			new AffineTransform(1, 0, 0, 1, 1, 1), getContextModel(), getFeatureModel(), ChoiceBoxType.CONTEXT_NUMBER);
	choiceBoxContextModel.setParentBlock(operator);
	choiceBoxContextModel.setSelectedValue(((HyContextInformationReferenceExpression) expression).getContextInformation());
	
	
	visualParts.add(choiceBoxContextModel);
	} else if( expression instanceof HyAttributeReferenceExpression){
		final ChoiceBoxFeatureAttributeModel choiceBoxContextModel = new ChoiceBoxFeatureAttributeModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), getFeatureModel(), getContextModel(), ChoiceBoxType.ATTRIBUTE_NUMBER);
		choiceBoxContextModel.setParentBlock(operator);
		choiceBoxContextModel.setSelectedValue(((HyAttributeReferenceExpression) expression).getAttribute());
		visualParts.add(choiceBoxContextModel);
	}


	

	return visualParts;
}



private static List<? extends AbstractBlockElement> resolveImpliesOrEquivalenceExpression(HyBinaryExpression implies, AbstractGeometricElement<? extends IGeometry> parentBlock, Date validSince, Date validUntil, ControlBlockType type){
	List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();
	// Create Model for block elements
	final ControlIfBlockModel controlBlock = new ControlIfBlockModel(ControlShapeService.createControlBlockShape(),
					new AffineTransform(1, 0, 0, 1, 23, 5), Color.YELLOW, null, type);
    final OperatorFixedBlockModel fixesOperatorBlock = new OperatorFixedBlockModel(
					OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 53, 8),
					Color.GREEN, null, OperatorFixedBlockType.IF_OPERATOR);
    
    // Add Parent to child block elements
	fixesOperatorBlock.setParentBlock(controlBlock);
	
	if(parentBlock != null){
		
		
		if(parentBlock instanceof ControlAndOrBlock){
			if(isOperand1==true){
			controlBlock.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlock.setOperandType(ControlBlockOperandType.OPERAND2);
				
			}
		}
		
		controlBlock.setParentBlock(parentBlock);
	}else{
		if(validSince!=null){
			controlBlock.setValidSince(validSince);
		}
		if(validUntil!=null){
			controlBlock.setValidUntil(validUntil);
		}
	}
	
	visualParts.add(controlBlock);
	visualParts.add(fixesOperatorBlock);
	if(type.equals(ControlBlockType.EQUIVALENCE)){
		final TextModel textModel = new TextModel("↔", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.EQUIVALENCE);
		textModel.setParentBlock(controlBlock);
		visualParts.add(textModel);
	}
	
	if(type.equals(ControlBlockType.NOT_EQUIVALENCE) || type.equals(ControlBlockType.NOT_IMPLIES)){
		final TextModel textModelNot = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 30, 10), TextType.IF);
		textModelNot.setParentBlock(controlBlock);
		visualParts.add(textModelNot);
	}
	
	
	
	visualParts.addAll(resolveOperand(implies.getOperand1(), fixesOperatorBlock));
	
	visualParts.addAll(resolveImpliesOperand2(implies.getOperand2(), controlBlock));
	
	return visualParts;
	
	
}

private static List<? extends AbstractBlockElement> resolveOperand(HyExpression operand, OperatorFixedBlockModel parent){
	List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();

	
	if(operand instanceof HyFeatureReferenceExpression){
		visualParts.addAll(resolveFeatureReferenceExpression(parent, (HyFeatureReferenceExpression) operand));
		
	} else if (operand instanceof HyAndExpression || operand instanceof HyOrExpression){
		visualParts.addAll(resolveAndOrExpression(parent, operand));
	} else if ((operand instanceof HyEqualExpression) ||( operand instanceof HyNotEqualExpression)
			||(operand instanceof HyGreaterExpression) ||(operand instanceof HyLessExpression)
			|| (operand instanceof HyGreaterOrEqualExpression) ||(operand instanceof HyLessOrEqualExpression)){
		visualParts.addAll(resolveHyExpression(operand, parent));
		visualParts.addAll(resolveHyExpression(operand, parent));
		
	} else if(operand instanceof HyNotExpression){
		visualParts.addAll(resolveNotExpression(parent, operand));
		
	} else if(operand instanceof HyImpliesExpression || operand instanceof HyImpliesExpression){
		
	} else if (operand instanceof HyNestedExpression){
		visualParts.addAll(resolveOperand(((HyNestedExpression) operand).getOperand(), parent));
	}
	return visualParts;
	
}

private static List<? extends AbstractBlockElement> resolveComparisonExpression(OperatorFixedBlockModel parentBlock,
		HyBinaryExpression operand, String operatorText) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();
	
	
	if(!checkIfExpressionArithmeticExpression(operand)){
		
		if(operand.getOperand1() instanceof HyContextInformationReferenceExpression){
			HyContextualInformation context =((HyContextInformationReferenceExpression) operand.getOperand1()).getContextInformation();
			OperatorComparisonValueType valueType = OperatorComparisonValueType.NUMBER;
			ChoiceBoxType choiceBoxType = ChoiceBoxType.CONTEXT_NUMBER;
			ChoiceBoxType valueChoiceBoxType = ChoiceBoxType.VALUE_NUMBER;
			if(context instanceof HyContextualInformationBoolean){
				valueType = OperatorComparisonValueType.BOOLEAN;
			 choiceBoxType = ChoiceBoxType.CONTEXT_BOOLEAN;
			 valueChoiceBoxType = ChoiceBoxType.VALUE_BOOLEAN;
				
			}
			if(context instanceof HyContextualInformationEnum){
				valueType = OperatorComparisonValueType.ENUM;
				choiceBoxType = ChoiceBoxType.CONTEXT_ENUM;
				 valueChoiceBoxType = ChoiceBoxType.VALUE_ENUM;
			}
			OperatorComparisonType type = getOperatorComparisonType(operand);
			
			

		final OperatorComparisonBlockModel operatorModel = new OperatorComparisonBlockModel(
				OperatorShapeService.createOperatorShapeCustomLength(320), new AffineTransform(1, 0, 0, 1, 1, 1), Color.rgb(92, 214, 92),
				null, type, valueType);

		final ChoiceBoxContextModel choiceBoxContextModel = new ChoiceBoxContextModel(
				new AffineTransform(1, 0, 0, 1, 1, 1), getContextModel(), getFeatureModel(), choiceBoxType);
		choiceBoxContextModel.setParentBlock(operatorModel);
	
		
			
	    HyContextInformationReferenceExpression contextRef = (HyContextInformationReferenceExpression) operand.getOperand1();
	    //HyContextualInformationEnum contextualInformation = (HyContextualInformationEnum) contextRef.getContextInformation();
	    if(!(choiceBoxContextModel.getChoices().contains(contextRef.getContextInformation()))){
	    	choiceBoxContextModel.getChoices().add(contextRef.getContextInformation());
	    }
        choiceBoxContextModel.setSelectedValue(contextRef.getContextInformation());
        
    
		
		final ChoiceBoxValueModel choiceBoxValueModel = new ChoiceBoxValueModel(new AffineTransform(1, 0, 0, 1, 1, 1),
				getContextModel(), getFeatureModel(), valueChoiceBoxType);

		choiceBoxValueModel.setParentBlock(operatorModel);
		if(operand.getOperand2() instanceof HyContextInformationReferenceExpression ){
		
		choiceBoxValueModel.setSelectedValue(((HyContextInformationReferenceExpression) operand.getOperand2()).getContextInformation());
		}
		if(operand.getOperand2() instanceof HyAttributeReferenceExpression ){
			choiceBoxValueModel.setSelectedValue(((HyAttributeReferenceExpression) operand.getOperand2()).getAttribute());
		}
		if(operand.getOperand2() instanceof HyBooleanValueExpression){
			Boolean value = ((HyBooleanValueExpression) operand.getOperand2()).isValue();
			choiceBoxValueModel.setSelectedValue(value.toString());
		}
		if(operand.getOperand2() instanceof HyValueExpression){
			HyValue value = ((HyValueExpression) operand.getOperand2()).getValue();
			if(value instanceof HyEnumValue){
				choiceBoxValueModel.setParentChoiceBoxSelection(choiceBoxContextModel.getSelectedValue());
				choiceBoxValueModel.refreshChoiceList();
				HyEnumLiteral literal = ((HyEnumValue) value).getEnumLiteral();
				choiceBoxValueModel.setSelectedValue(literal);
			}
		}
		

		final TextModel textModel = new TextModel(operatorText, new AffineTransform(1, 0, 0, 1, 1, 1), TextType.EQUALS);
		textModel.setParentBlock(operatorModel);
		
		operatorModel.setParentBlock(parentBlock);

		visualParts.add(operatorModel);
		visualParts.add(choiceBoxContextModel);
		visualParts.add(choiceBoxValueModel);
		visualParts.add(textModel);
		}
	}
	return visualParts;
}

private static OperatorComparisonType getOperatorComparisonType(HyBinaryExpression operand) {

	if(operand instanceof HyEqualExpression){
		return OperatorComparisonType.EQUALS;
	} else if(operand instanceof HyNotEqualExpression){
		return OperatorComparisonType.NOT_EQUALS;
	} else if(operand instanceof HyGreaterExpression){
		return OperatorComparisonType.BIGGER;
	} else if(operand instanceof HyLessExpression){
		return OperatorComparisonType.SMALLER;
	} else if(operand instanceof HyGreaterOrEqualExpression){
		return OperatorComparisonType.EQUALS_OR_BIGGER;
	} else if(operand instanceof HyLessOrEqualExpression){
		return OperatorComparisonType.EQUALS_OR_SMALLER;
	}
	return null;
	
}



private static boolean checkIfExpressionArithmeticExpression(HyExpression expression){
	if(!(expression instanceof HyBinaryExpression)){
		return false;
		
	}else if(expression instanceof HyAdditionExpression || expression instanceof HySubtractionExpression 
			|| expression instanceof HyMultiplicationExpression || expression instanceof HyDivisionExpression 
			|| expression instanceof HyModuloExpression || expression instanceof HyMinimumExpression
			|| expression instanceof HyMaximumExpression){
				
			
return true;
			}
	return false;
}



private static List<? extends AbstractBlockElement> resolveNotExpression(OperatorFixedBlockModel parentBlock,
		HyExpression operand) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();


	

	final OperatorNotBlockModel movableMovableModel = new OperatorNotBlockModel(
			OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null);

	final OperatorFixedBlockModel fixedBlockModel = new OperatorFixedBlockModel(OperatorShapeService.createOperatorShapeCustomLength(80),
			new AffineTransform(1, 0, 0, 1, 1, 1), Color.GREEN, null, OperatorFixedBlockType.VALIDATE_NOT_OPERATOR);


	final TextModel textModel = new TextModel("NOT", new AffineTransform(1, 0, 0, 1, 1, 1),
			TextType.VALIDATE_NOT_OPERATOR);
	movableMovableModel.setParentBlock(parentBlock);
	fixedBlockModel.setParentBlock(movableMovableModel);
	textModel.setParentBlock(movableMovableModel);

	

	
	visualParts.add(movableMovableModel);
	visualParts.add(fixedBlockModel);
	visualParts.add(textModel);
	
	
	visualParts.addAll(resolveOperand(((HyNotExpression) operand).getOperand(), fixedBlockModel));
	
	
	
	return visualParts;
}

private static Collection<? extends AbstractBlockElement> resolveAndOrExpression(OperatorFixedBlockModel parentBlock,
		HyExpression operand) {
	List<AbstractBlockElement> visualParts = new ArrayList<>();

	
	final OperatorAndBlockModel operatorAndBlockModel = new OperatorAndBlockModel(
			OperatorShapeService.createOperatorShapeCustomLength(140), new AffineTransform(1, 0, 0, 1, 1, 1), Color.LIGHTGREEN, null,
			OperatorAndORType.AND);
	if(operand instanceof HyOrExpression){
		operatorAndBlockModel.setOperatorAndORType(OperatorAndORType.OR);
	}

	final OperatorFixedBlockModel fixedOperator1 = new OperatorFixedBlockModel(OperatorShapeService.createOperatorShapeCustomLength(40),
			OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null, OperatorFixedBlockType.AND_OPERATOR1,
			operatorAndBlockModel);
	fixedOperator1.setParentBlock(operatorAndBlockModel);

	final OperatorFixedBlockModel fixedOperator2 = new OperatorFixedBlockModel(OperatorShapeService.createOperatorShapeCustomLength(40),
			OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null, OperatorFixedBlockType.AND_OPERATOR2,
			operatorAndBlockModel);
	fixedOperator2.setParentBlock(operatorAndBlockModel);

	final TextModel textModel = new TextModel("And", OperatorShapeService.createDefaultAffineTransformForPalette(),
			TextType.AND_OPERATOR);
	textModel.setParentBlock(operatorAndBlockModel);
	
	operatorAndBlockModel.setParentBlock(parentBlock);

	visualParts.add(operatorAndBlockModel);
	visualParts.add(fixedOperator1);
	visualParts.add(fixedOperator2);
	visualParts.add(textModel);
	
	
	
	visualParts.addAll(resolveHyExpression(((HyBinaryExpression) operand).getOperand1(), fixedOperator1));
	visualParts.addAll(resolveHyExpression(((HyBinaryExpression) operand).getOperand2(), fixedOperator2));
	
	
//	visualParts.addAll(resolveOperand(((HyBinaryExpression) operand).getOperand1(), fixedOperator1));
//	visualParts.addAll(resolveOperand(((HyBinaryExpression) operand).getOperand2(), fixedOperator2));
	
	
	return visualParts;
}



private static List<? extends AbstractBlockElement> resolveImpliesOperand2(HyExpression operand, AbstractGeometricElement<? extends IGeometry> parentBlock){
	List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();

	final ControlValidateOperatorBlockModel controlBlockModel = new ControlValidateOperatorBlockModel(
			ControlShapeService.createCustomControlValidateOperatorShape(30, 200), new AffineTransform(1, 0, 0, 1, 1, 1), Color.YELLOW,
			null, ControlValidateOperatorBlockType.VALIDATE);

	final OperatorFixedBlockModel fixedOperatorModel = new OperatorFixedBlockModel(
			OperatorShapeService.createOperatorShapeCustomLength(160),
			OperatorShapeService.createDefaultAffineTransformForPalette(), Color.GREEN, null,
			OperatorFixedBlockType.VALIDATE_OPERATOR);

	
	
//	visualParts.add(controlBlockModel);
//    visualParts.add(fixedOperatorModel);

	
	if(operand instanceof HyFeatureReferenceExpression){
		
		fixedOperatorModel.setParentBlock(controlBlockModel);

		
		
		if(parentBlock instanceof ControlAndOrBlock){
			if(isOperand1 == true){
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND2);
			}
		}
		
		controlBlockModel.setParentBlock(parentBlock);
		
		visualParts.add(controlBlockModel);
	    visualParts.add(fixedOperatorModel);
		visualParts.addAll(resolveFeatureReferenceExpression(fixedOperatorModel, (HyFeatureReferenceExpression) operand));
		
	} else if (operand instanceof HyAndExpression || operand instanceof HyOrExpression){
		fixedOperatorModel.setParentBlock(controlBlockModel);

		
		
		if(parentBlock instanceof ControlAndOrBlock){
			if(isOperand1 == true){
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND2);
			}
		}
		
		controlBlockModel.setParentBlock(parentBlock);
		
		visualParts.add(controlBlockModel);
	    visualParts.add(fixedOperatorModel);
		visualParts.addAll(resolveAndOrExpression(fixedOperatorModel, operand));
		
	} else if ((operand instanceof HyEqualExpression) ||( operand instanceof HyNotEqualExpression)
			||(operand instanceof HyGreaterExpression) ||(operand instanceof HyLessExpression)
			|| (operand instanceof HyGreaterOrEqualExpression) ||(operand instanceof HyLessOrEqualExpression)){
		fixedOperatorModel.setParentBlock(controlBlockModel);

		
		
		if(parentBlock instanceof ControlAndOrBlock){
			if(isOperand1 == true){
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND2);
			}
		}
		
		controlBlockModel.setParentBlock(parentBlock);
		visualParts.add(controlBlockModel);
	    visualParts.add(fixedOperatorModel);
		visualParts.addAll(resolveHyExpression(operand, fixedOperatorModel));
		
	} else if(operand instanceof HyNotExpression){
		
		if((((HyNotExpression) operand).getOperand() instanceof HyImpliesExpression || ((HyNotExpression) operand).getOperand() instanceof HyEquivalenceExpression)){
			visualParts.addAll(resolveHyExpression(operand, (GeometricShape) parentBlock));
		}else{
		fixedOperatorModel.setParentBlock(controlBlockModel);

		
		
		if(parentBlock instanceof ControlAndOrBlock){
			if(isOperand1 == true){
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND1);
			}else{
				controlBlockModel.setOperandType(ControlBlockOperandType.OPERAND2);
			}
		}
		controlBlockModel.setParentBlock(parentBlock);
		
		visualParts.add(controlBlockModel);
	    visualParts.add(fixedOperatorModel);
	
		visualParts.addAll(resolveHyExpression(operand, fixedOperatorModel));
		}
		
		
	} else if(operand instanceof HyImpliesExpression){
		visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyImpliesExpression)operand, parentBlock, null, null, ControlBlockType.IMPLIES));
	} else if(operand instanceof HyEquivalenceExpression){
		visualParts.addAll(resolveImpliesOrEquivalenceExpression((HyEquivalenceExpression)operand, parentBlock, null, null, ControlBlockType.EQUIVALENCE));
	
	
}else if (operand instanceof HyNestedExpression){
		visualParts.addAll(resolveImpliesOperand2(((HyNestedExpression) operand).getOperand(), parentBlock));
	}
	return visualParts;
	
}



/**
 * 
 * 
 * @param parentBlock
 * @return
 */
private static List<? extends AbstractBlockElement> resolveFeatureReferenceExpression(
		OperatorFixedBlockModel parentBlock, HyFeatureReferenceExpression expression) {
	
	List<AbstractBlockElement> visualParts = new ArrayList<AbstractBlockElement>();
	
	final OperatorFeatureIsSelectedModel operatorBlock = new OperatorFeatureIsSelectedModel(
			OperatorShapeService.createOperatorShapeCustomLength(250), new AffineTransform(1, 0, 0, 1, 1, 1),
			Color.LIGHTGREEN, null);
	final ChoiceBoxFeatureModel choiceBoxModel = new ChoiceBoxFeatureModel(
			new AffineTransform(1, 0, 0, 1, 1, 1), getContextModel(), getFeatureModel(), ChoiceBoxType.FEATURE);
	final TextModel textModel = new TextModel("is Selected", new AffineTransform(1, 0, 0, 1, 1, 1),
			TextType.IS_SELECTED);


	textModel.setParentBlock(operatorBlock);
	choiceBoxModel.setParentBlock(operatorBlock);
	
	choiceBoxModel.setSelectedValue(expression.getFeature());
	
	operatorBlock.setParentBlock(parentBlock);

	visualParts.add(operatorBlock);
	visualParts.add(choiceBoxModel);
	visualParts.add(textModel);
	return visualParts;
}



public static ObjectProperty<HyFeatureModel> getFeatureModelproperty() {
	return featureModelProperty;
}

public static HyFeatureModel getFeatureModel(){
	return featureModelProperty.get();
}

public static HyContextModel getContextModel(){
	return contextModelProperty.get();
}













}
