package de.cubbossa.splinelib.util;

import java.util.Collection;
import java.util.List;

public class Curve extends TransformableList<Vector, Vector> {

	public Curve() {
		super();
	}

	public Curve(Vector vector) {
		super(vector);
	}

	public Curve(Collection<Vector> group) {
		super(group);
	}

	public Curve(Pose startPose) {
		super(startPose);
	}

	public Curve(Pose startPose, Vector vector) {
		super(startPose, vector);
	}

	public Curve(Pose startPose, Collection<Vector> group) {
		super(startPose, group);
	}

	@Override
	public List<Vector> getVectors() {
		return this;
	}
}