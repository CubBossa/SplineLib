package de.cubbossa.splinelib.shape;

import de.cubbossa.splinelib.util.Spline;
import de.cubbossa.splinelib.util.Pose;

public interface Shape {

	/**
	 * @return the pose consisting of position, direction and up vector
	 */
	Pose getPose();

	/**
	 * @return a list of BezierVectors that will, if combined with proper interpolators, depict the given shape
	 */
	Spline getSpline();
}
