package de.bossascrew.splinelib;

import com.google.common.collect.Lists;
import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
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

	private RoundingInterpolator<BezierVector, List<Vector>> roundingInterpolator = (points, closedPath) -> {
		return points.stream().map(v -> (List<Vector>) Lists.newArrayList((Vector) v)).toList();
	};
	private SpacingInterpolator<List<Vector>, Vector> pointDistanceInterpolator = (points, closedPath) -> {
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

	public SplineBuilder withRoundingInterpolator(RoundingInterpolator<BezierVector, List<Vector>> roundingInterpolator) {
		this.roundingInterpolator = roundingInterpolator;
		return this;
	}

	public SplineBuilder withSpacingInterpolator(SpacingInterpolator<List<Vector>, Vector> spacingInterpolator) {
		this.pointDistanceInterpolator = spacingInterpolator;
		return this;
	}

	public SplineBuilder withSegmentedPointFilter(BezierVector segmentStart, Predicate<Vector> pointFilter) {
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
		distancedPath = pointDistanceInterpolator.interpolate(roundedPath, closePath);
		if (pointFilter != null) {
			processedPath = distancedPath.stream().filter(pointFilter).collect(Collectors.toList());
		} else {
			processedPath = new ArrayList<>(distancedPath);
		}

		return processedPath;
	}
}
