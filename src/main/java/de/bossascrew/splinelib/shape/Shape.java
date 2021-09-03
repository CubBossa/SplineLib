package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public interface Shape {

	boolean isPathClosedByDefault();

	/**
	 * @return the pose consisting of position, direction and up vector
	 */
	Pose getPose();

	/**
	 * @return a list of BezierVectors that will, if combined with proper interpolators, depict the given shape
	 */
	List<BezierVector> getBezierVectors();
}
