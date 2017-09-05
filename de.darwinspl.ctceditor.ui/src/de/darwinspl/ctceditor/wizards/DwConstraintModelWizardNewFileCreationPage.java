package de.darwinspl.ctceditor.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import de.christophseidl.util.eclipse.ResourceUtil;
import eu.hyvar.feature.constraint.util.HyConstraintUtil;

public class DwConstraintModelWizardNewFileCreationPage extends WizardNewFileCreationPage {

	public DwConstraintModelWizardNewFileCreationPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		
		setTitle("New DarwinSPL Constraint Model Wizard");
		setDescription("Select a File where to store the new DarwinSPL Constraint Model");
		setFileName("FeatureModel.hyconstraint");
	}

	@Override
	protected boolean validatePage() {
		return true;
	}
	

	public IFile getModelFile() {
		IPath containerFullPath = getContainerFullPath();
		String filename = getFileName();
		IPath filePath = containerFullPath.append(filename);
		String filePathString = filePath.toString();
		IFile file = ResourceUtil.getLocalFile(filePathString);
		String fileExtension = file.getFileExtension();
		
		//Ensure that the file has the right extension
		if (fileExtension.equalsIgnoreCase(HyConstraintUtil.getConstraintModelFileExtensionForXmi())) {
			return file;
		}
		
	
		return ResourceUtil.getLocalFile(filePathString + "."+HyConstraintUtil.getConstraintModelFileExtensionForXmi());
	}
	
}
