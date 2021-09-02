package de.bossascrew.splinelib;

import com.google.common.collect.Lists;
import de.bossascrew.splinelib.interpolate.Interpolator;
import de.bossascrew.splinelib.interpolate.PathInterpolator;
import de.bossascrew.splinelib.shape.Shape;
import de.bossascrew.splinelib.util.BezierVector;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class SplineBuilder {

	private final List<BezierVector> baseVectors;
	private List<List<Vector>> roundedPath = new ArrayList<>();
	private List<Vector> distancedPath = new ArrayList<>();
	private List<Vector> processedPath = new ArrayList<>();

	private PathInterpolator<BezierVector, List<Vector>> roundingInterpolator = new PathInterpolator<>() {
		@Override
		public List<List<Vector>> interpolate(List<BezierVector> points, boolean closedPath) {
			return points.stream().map(v -> (List<Vector>) Lists.newArrayList((Vector) v)).toList();
		}

		@Override
		public List<List<Vector>> interpolate(List<BezierVector> points) {
			return interpolate(points, false);
		}
	};
	private Interpolator<List<Vector>, Vector> pointDistanceInterpolator = points -> {
		List<Vector> list = new ArrayList<>();
		points.forEach(list::addAll);
		return list;
	};

	private Predicate<Vector> pointFilter = null;

	private boolean closePath = false;

	public SplineBuilder(List<BezierVector> baseVectors) {
		this.baseVectors = baseVectors;
	}

	public SplineBuilder(Shape shape) {
		this.baseVectors = shape.getBezierVectors();
	}

	public SplineBuilder withRoundingInterpolator(PathInterpolator<BezierVector, List<Vector>> roundingInterpolator) {
		this.roundingInterpolator = roundingInterpolator;
		return this;
	}

	public SplineBuilder withPointDistanceInterpolator(Interpolator<List<Vector>, Vector> pointDistanceInterpolator) {
		this.pointDistanceInterpolator = pointDistanceInterpolator;
		return this;
	}

	public SplineBuilder withSegmentedPointFilter(Predicate<Vector> pointFilter) {
		return this; //TODO
	}

	public SplineBuilder withPointFilter(Predicate<Vector> pointFilter) {
		this.pointFilter = pointFilter;
		return this;
	}

	public SplineBuilder withClosedPath(boolean closePath) {
		this.closePath = closePath;
		return this;
	}

	public List<Vector> build() {

		roundedPath = roundingInterpolator.interpolate(baseVectors, closePath);
		distancedPath = pointDistanceInterpolator.interpolate(roundedPath);
		if (pointFilter != null) {
			processedPath = distancedPath.stream().filter(pointFilter).collect(Collectors.toList());
		} else {
			processedPath = new ArrayList<>(distancedPath);
		}

		return processedPath;
	}
}
