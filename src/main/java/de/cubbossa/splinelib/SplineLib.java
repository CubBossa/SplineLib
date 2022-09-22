package de.cubbossa.splinelib;

import de.cubbossa.splinelib.shape.Shape;
import de.cubbossa.splinelib.util.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class SplineLib<V> {

	public CurveBuilder<V> newCurveBuilder(Spline spline) {
		return new CurveBuilder<>(this, spline);
	}

	public CurveBuilder<V> newCurveBuilder(Shape shape) {
		return new CurveBuilder<>(this, shape);
	}

	public CurveBuilder<V> newCurveBuilder(Collection<BezierVector> vectors) {
		return new CurveBuilder<>(this, vectors);
	}

	public CurveBuilder<V> newCurveBuilder(BezierVector... vectors) {
		return new CurveBuilder<>(this, vectors);
	}

	public CurveBuilder<V> newCurveBuilderFrom(Collection<V> vectors) {
		return new CurveBuilder<>(this, vectors.stream().map(this::convertToBezierVector).toList());
	}

	@SafeVarargs
	public final CurveBuilder<V> newCurveBuilderFrom(V... vectors) {
		return new CurveBuilder<>(this, Arrays.stream(vectors).map(this::convertToBezierVector).toList());
	}

	public abstract Vector convertToVector(V value);

	public abstract V convertFromVector(Vector value);

	public abstract BezierVector convertToBezierVector(V value);

	public abstract V convertFromBezierVector(BezierVector value);

	public List<V> convert(Curve curve) {
		return curve.stream().map(this::convertFromVector).toList();
	}

	public List<V> convert(Spline spline) {
		return spline.stream().map(this::convertFromBezierVector).toList();
	}

	public Curve newCurve(List<V> vectors) {
		return vectors.stream().map(this::convertToVector).collect(Collectors.toCollection(Curve::new));
	}

	public Spline newSpline(List<V> vectors) {
		return vectors.stream().map(this::convertToBezierVector).collect(Collectors.toCollection(Spline::new));
	}

	public Vector newVector(double x, double y, double z) {
		return new Vector(x, y, z);
	}

	public Vector newVector(V pos) {
		return convertToVector(pos);
	}

	public BezierVector newBezierVector(V pos) {
		return newBezierVector(pos, null, null);
	}

	public BezierVector newBezierVector(V pos, @Nullable V left, @Nullable V right) {
		return new BezierVector(convertToVector(pos), left == null ? null : convertToVector(left), right == null ? null : convertToVector(right));
	}

	public Pose newPose(V pos, V dir, V up) {
		return new Pose(convertToVector(pos), convertToVector(dir), convertToVector(up));
	}

	public Pose newPose(V pos, V dir) {
		return newPose(convertToVector(pos), convertToVector(dir));
	}

	public Pose newPose(V pos) {
		return newPose(convertToVector(pos));
	}

	public Pose newPose(Vector pos, Vector dir, Vector up) {
		return new Pose(pos, dir, up);
	}

	public Pose newPose(Vector pos, Vector dir) {
		return new Pose(pos, dir, Vector.y());
	}

	public Pose newPose(Vector pos) {
		return new Pose(pos, Vector.z(), Vector.y());
	}
}
