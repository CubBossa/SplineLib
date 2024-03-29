package de.cubbossa.splinelib.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Spline extends TransformableList<BezierVector, Vector> {

	private boolean closed;

	public Spline() {
		super();
	}

	public Spline(Collection<BezierVector> vectors) {
		super(vectors);
	}

	public Spline(Pose startPose) {
		super(startPose);
	}

	public Spline(Pose startPose, Collection<BezierVector> vectors) {
		super(startPose, vectors);
	}

	@Override
	public BezierVector get(int index) {
		if (closed && index >= size()) {
			index = index % size();
		}
		return super.get(index);
	}

	@Override
	public List<Vector> getVectors() {
		List<Vector> result = new ArrayList<>();
		for (BezierVector v : this) {
			result.add(v);
			result.add(v.leftControlPoint);
			result.add(v.rightControlPoint);
		}
		return result;
	}
}
