package editor.handlers;

import org.eclipse.gef.common.adapt.IAdaptable;
import org.eclipse.gef.mvc.fx.parts.IContentPart;

import javafx.scene.Node;

public abstract class AbstractCloneContentSupport extends IAdaptable.Bound.Impl<IContentPart<? extends Node>> {

	public abstract Object cloneContent();
}
