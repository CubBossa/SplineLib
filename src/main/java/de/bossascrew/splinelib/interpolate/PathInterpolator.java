package de.bossascrew.splinelib.interpolate;

import java.util.List;

public interface PathInterpolator<B, V> extends Interpolator<B, V> {

	/**
	 * extends the interpolator class by adding a closedPath parameter
	 *
	 * @param points     samplepoints
	 * @param closedPath if the interpolation algorithm should connect start and end point of the list
	 * @return interpolated point list
	 */
	List<V> interpolate(List<B> points, boolean closedPath);
}
