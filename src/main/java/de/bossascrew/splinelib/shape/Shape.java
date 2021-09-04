package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.Spline;
import de.bossascrew.splinelib.util.Pose;

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
