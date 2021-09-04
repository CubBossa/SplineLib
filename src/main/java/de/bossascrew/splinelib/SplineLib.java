package de.bossascrew.splinelib;

import de.bossascrew.splinelib.shape.Shape;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Spline;
import de.bossascrew.splinelib.util.Vector;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class SplineLib<V> {

	private Function<V, Vector> vectorConverter;
	private Function<V, BezierVector> bezierVectorConverter;
	private Function<Vector, V> backConverter;
	private Function<BezierVector, V> bezierBackConverter;

	public void register(Function<V, Vector> vectorConverter,
						 Function<Vector, V> backConverter,
						 Function<V, BezierVector> bezierVectorConverter,
						 Function<BezierVector, V> bezierBackConverter) {
		this.vectorConverter = vectorConverter;
		this.backConverter = backConverter;
		this.bezierVectorConverter = bezierVectorConverter;
		this.bezierBackConverter = bezierBackConverter;
	}

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
		return new CurveBuilder<>(this, vectors.stream().map(vectorConverter)
				.map(v -> new BezierVector(v, null, null)).toList());
	}

	@SafeVarargs
	public final CurveBuilder<V> newCurveBuilderFrom(V... vectors) {
		return new CurveBuilder<>(this, Arrays.stream(vectors).map(vectorConverter)
				.map(v -> new BezierVector(v, null, null)).toList());
	}

	public Vector convert(V value) {
		return vectorConverter.apply(value);
	}

	public V convertBack(Vector value) {
		return backConverter.apply(value);
	}

	public List<V> convert(Curve curve) {
		return curve.stream().map(backConverter).toList();
	}

	public List<V> convert(Spline spline) {
		return spline.stream().map(bezierBackConverter).toList();
	}

	public Curve newCurve(List<V> vectors) {
		return vectors.stream().map(vectorConverter).collect(Collectors.toCollection(Curve::new));
	}

	public Spline newSpline(List<V> vectors) {
		return vectors.stream().map(bezierVectorConverter).collect(Collectors.toCollection(Spline::new));
	}
}
