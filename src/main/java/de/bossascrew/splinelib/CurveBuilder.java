package de.bossascrew.splinelib;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.phase.RoundingPhase;
import de.bossascrew.splinelib.phase.SpacingPhase;
import de.bossascrew.splinelib.shape.Shape;
import de.bossascrew.splinelib.util.BezierVector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Setter
@Getter
public class CurveBuilder {

	private Spline spline;

	private RoundingPhase rounding = new RoundingPhase();
	private SpacingPhase spacing = new SpacingPhase();

	private Map<BezierVector, Curve> roundedPath = new LinkedHashMap<>();
	private Curve spacedPath = new Curve();

	private Boolean closedPath = null;

	public CurveBuilder(Spline spline) {
		this.spline = spline;
	}

	public CurveBuilder(Shape shape) {
		this.spline = shape.getSpline();
	}

	public CurveBuilder(Collection<BezierVector> vectors) {
		this.spline = new Spline(vectors);
	}

	public CurveBuilder(BezierVector... vectors) {
		this.spline = new Spline(List.of(vectors));
	}

	public CurveBuilder withRoundingInterpolation(RoundingInterpolator<Spline, Map<BezierVector, Curve>> interpolator) {
		this.rounding.setInterpolator(interpolator);
		return this;
	}

	public CurveBuilder withRoundingFilter(Predicate<Vector> filter) {
		this.rounding.setFilter(filter);
		return this;
	}

	public CurveBuilder withRoundingProcessor(Consumer<Map<BezierVector, Curve>> processor) {
		this.rounding.setProcessor(processor);
		return this;
	}

	public CurveBuilder withSpacingInterpolation(SpacingInterpolator<Map<BezierVector, Curve>, Curve> interpolator) {
		this.spacing.setInterpolator(interpolator);
		return this;
	}

	public CurveBuilder withSpacingFilter(Predicate<Vector> filter) {
		this.spacing.setFilter(filter);
		return this;
	}

	public CurveBuilder withSpacingProcessor(Consumer<Curve> processor) {
		this.spacing.setProcessor(processor);
		return this;
	}

	public CurveBuilder withClosedPath(boolean closedPath) {
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
}
