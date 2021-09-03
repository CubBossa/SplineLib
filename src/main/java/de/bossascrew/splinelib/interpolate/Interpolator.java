package de.bossascrew.splinelib.interpolate;

/**
 * the interpolator will accept a list of points and interpolates based on the implemented algorithm.
 *
 * @param <B> The input vector type (for example BezierVectors)
 * @param <V> The output vector type (most cases just Bukkit Vector class)
 */
public interface Interpolator<B, V> {

	/**
	 * interpolates the given sample points
	 *
	 * @param points sample points. Depending on the interpolation algorithm, more or less sample points are necessary
	 * @return returns a list of interpolated points
	 */
	V interpolate(B points, boolean closedPath);
}
