package de.cubbossa.splinelib;

import de.cubbossa.splinelib.interpolate.RoundingInterpolator;
import de.cubbossa.splinelib.interpolate.SpacingInterpolator;
import de.cubbossa.splinelib.phase.RoundingPhase;
import de.cubbossa.splinelib.phase.SpacingPhase;
import de.cubbossa.splinelib.shape.Shape;
import de.cubbossa.splinelib.util.BezierVector;
import de.cubbossa.splinelib.util.Curve;
import de.cubbossa.splinelib.util.Spline;
import de.cubbossa.splinelib.util.Vector;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Setter
@Getter
public class CurveBuilder<V> {

	private SplineLib<V> splineLib;
	private Spline spline;

	private RoundingPhase rounding = new RoundingPhase();
	private SpacingPhase spacing = new SpacingPhase();

	private Map<BezierVector, Curve> roundedPath = new LinkedHashMap<>();
	private Curve spacedPath = new Curve();

	private Boolean closedPath = null;

	CurveBuilder(SplineLib<V> splineLib, Spline spline) {
		this.spline = spline;
		this.splineLib = splineLib;
	}

	CurveBuilder(SplineLib<V> splineLib, Shape shape) {
		this.spline = shape.getSpline();
		this.splineLib = splineLib;
	}

	CurveBuilder(SplineLib<V> splineLib, Collection<BezierVector> vectors) {
		this.spline = new Spline(vectors);
		this.splineLib = splineLib;
	}

	CurveBuilder(SplineLib<V> splineLib, BezierVector... vectors) {
		this.spline = new Spline(List.of(vectors));
		this.splineLib = splineLib;
	}

	public CurveBuilder<V> withRoundingInterpolation(RoundingInterpolator<Spline, Map<BezierVector, Curve>> interpolator) {
		this.rounding.setInterpolator(interpolator);
		return this;
	}

	public CurveBuilder<V> withRoundingFilter(Predicate<de.cubbossa.splinelib.util.Vector> filter) {
		this.rounding.setFilter(filter);
		return this;
	}

	public CurveBuilder<V> withRoundingProcessor(Consumer<Map<BezierVector, Curve>> processor) {
		this.rounding.setProcessor(processor);
		return this;
	}

	public CurveBuilder<V> withSpacingInterpolation(SpacingInterpolator<Map<BezierVector, Curve>, Curve> interpolator) {
		this.spacing.setInterpolator(interpolator);
		return this;
	}

	public CurveBuilder<V> withSpacingFilter(Predicate<Vector> filter) {
		this.spacing.setFilter(filter);
		return this;
	}

	public CurveBuilder<V> withSpacingProcessor(Consumer<Curve> processor) {
		this.spacing.setProcessor(processor);
		return this;
	}

	public CurveBuilder<V> withClosedPath(boolean closedPath) {
		this.closedPath = closedPath;
		return this;
	}

	public Map<BezierVector, Curve> runRoundingPhase() {
		roundedPath = rounding.build(spline, closedPath == null ? spline.isClosed() : closedPath);
		return roundedPath;
	}

	public Curve runSpacingPhase() {
		spacedPath = spacing.build(roundedPath, closedPath == null ? spline.isClosed() : closedPath);
		return spacedPath;
	}

	public Curve build() {
		runRoundingPhase();
		runSpacingPhase();
		return spacedPath;
	}

	public List<V> buildAndConvert() {
		return build().stream().map(splineLib::convertFromVector).toList();
	}
}
