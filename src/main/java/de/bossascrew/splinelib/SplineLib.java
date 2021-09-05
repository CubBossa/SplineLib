package de.bossascrew.splinelib;

import de.bossascrew.splinelib.shape.Shape;
import de.bossascrew.splinelib.util.*;
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
		return new CurveBuilder<>(this, vectors.stream().map(this::convertBezierVector).toList());
	}

	@SafeVarargs
	public final CurveBuilder<V> newCurveBuilderFrom(V... vectors) {
		return new CurveBuilder<>(this, Arrays.stream(vectors).map(this::convertBezierVector).toList());
	}

	public abstract Vector convertVector(V value);

	public abstract V convertVectorBack(Vector value);

	public abstract BezierVector convertBezierVector(V value);

	public abstract V convertBezierVectorBack(BezierVector value);

	public List<V> convert(Curve curve) {
		return curve.stream().map(this::convertVectorBack).toList();
	}

	public List<V> convert(Spline spline) {
		return spline.stream().map(this::convertBezierVectorBack).toList();
	}

	public Curve newCurve(List<V> vectors) {
		return vectors.stream().map(this::convertVector).collect(Collectors.toCollection(Curve::new));
	}

	public Spline newSpline(List<V> vectors) {
		return vectors.stream().map(this::convertBezierVector).collect(Collectors.toCollection(Spline::new));
	}

	public Vector newVector(double x, double y, double z) {
		return new Vector(x, y, z);
	}

	public Vector newVector(V pos) {
		return convertVector(pos);
	}

	public BezierVector newBezierVector(V pos) {
		return newBezierVector(pos, null, null);
	}

	public BezierVector newBezierVector(V pos, @Nullable V left, @Nullable V right) {
		return new BezierVector(convertVector(pos), left == null ? null : convertVector(left), right == null ? null : convertVector(right));
	}

	public Pose newPose(V pos, V dir, V up) {
		return new Pose(convertVector(pos), convertVector(dir), convertVector(up));
	}

	public Pose newPose(V pos, V dir) {
		return newPose(convertVector(pos), convertVector(dir));
	}

	public Pose newPose(V pos) {
		return newPose(convertVector(pos));
	}

	public Pose newPose(Vector pos, Vector dir, Vector up) {
		return new Pose(pos, dir, up);
	}

	public Pose newPose(Vector pos, Vector dir) {
		return new Pose(pos, dir, Vector.Y);
	}

	public Pose newPose(Vector pos) {
		return new Pose(pos, Vector.Z, Vector.Y);
	}
}
