package de.bossascrew.splinelib.phase;

import de.bossascrew.splinelib.Curve;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpacingPhase extends CurveBuilderPhase<SpacingInterpolator<Map<BezierVector, Curve>, Curve>, Map<BezierVector, Curve>, Curve, Vector> {

	@Override
	public Curve applyInterpolation(Map<BezierVector, Curve> input, SpacingInterpolator<Map<BezierVector, Curve>, Curve> interpolator, boolean pathClosed) {
		if (interpolator == null) {
			return new Curve();
		}
		return interpolator.interpolate(input, pathClosed);
	}

	@Override
	public Curve applyFilter(Curve input, Predicate<Vector> filter) {
		if (filter == null) {
			return input;
		}
		return input.stream().filter(filter).collect(Collectors.toCollection(Curve::new));
	}

	@Override
	public Curve applyProcessor(Curve input, Consumer<Curve> processor) {
		if (processor == null) {
			return input;
		}
		processor.accept(input);
		return input;
	}
}
