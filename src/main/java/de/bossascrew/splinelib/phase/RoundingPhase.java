package de.bossascrew.splinelib.phase;

import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Spline;
import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RoundingPhase extends CurveBuilderPhase<RoundingInterpolator<Spline, Map<BezierVector, Curve>>, Spline, Map<BezierVector, Curve>, Vector> {

	@Override
	public Map<BezierVector, Curve> applyInterpolation(Spline input, RoundingInterpolator<Spline, Map<BezierVector, Curve>> interpolator, boolean pathClosed) {
		if (interpolator == null) {
			return new LinkedHashMap<>();
		}
		return interpolator.interpolate(input, pathClosed);
	}

	@Override
	public Map<BezierVector, Curve> applyFilter(Map<BezierVector, Curve> input, Predicate<Vector> filter) {
		if (filter == null) {
			return input;
		}
		for (Curve curve : input.values()) {
			Curve temp = curve.stream().filter(filter).collect(Collectors.toCollection(Curve::new));
			curve.clear();
			curve.addAll(temp);
		}
		return input;
	}

	@Override
	public Map<BezierVector, Curve> applyProcessor(Map<BezierVector, Curve> input, Consumer<Map<BezierVector, Curve>> processor) {
		if (processor == null) {
			return input;
		}
		processor.accept(input);
		return input;
	}
}
