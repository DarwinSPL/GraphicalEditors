

import org.eclipse.gef.geometry.planar.Rectangle;

public class ComparisonBlockNode extends AbstractMindMapItem {

	/**
	 *
	 */
	private static final long serialVersionUID = 5990088395499349534L;

	public static final String PROP_BOUNDS = "bounds";

	private Rectangle bounds;

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		pcs.firePropertyChange(PROP_BOUNDS, this.bounds, (this.bounds = bounds.getCopy()));
	}
}
