package de.bossascrew.splinelib;

import de.bossascrew.splinelib.interpolate.RoundingInterpolator;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.shape.Shape;
import de.bossascrew.splinelib.util.BezierVector;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class SplineBuilder {

	private final List<BezierVector> baseVectors;
	private Map<BezierVector, List<Vector>> roundedPath = new LinkedHashMap<>();
	private Map<BezierVector, List<Vector>> filteredRoundedPath = new LinkedHashMap<>();
	private List<Vector> distancedPath = new ArrayList<>();
	private List<Vector> filteredPath = new ArrayList<>();

	private RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> roundingInterpolator = (points, closedPath) -> {
		return null; //TODO
	};
	private SpacingInterpolator<Map<BezierVector, List<Vector>>, List<Vector>> pointDistanceInterpolator = (points, closedPath) -> {
		List<Vector> list = new ArrayList<>();
		if (points == null || points.isEmpty()) {
			return new ArrayList<>();
		}
		points.forEach((bezierVector, vectors) -> list.addAll(vectors));
		return list;
	};

	private Predicate<Vector> pointFilter = null;
	private final Map<Predicate<BezierVector>, Predicate<Vector>> segmentFilterMap = new LinkedHashMap<>();

	private boolean closePath = false;

	public SplineBuilder(List<BezierVector> baseVectors) {
		this.baseVectors = baseVectors;
	}

	public SplineBuilder(Shape shape) {
		this.baseVectors = shape.getBezierVectors();
		this.closePath = shape.isPathClosedByDefault();
	}

	public SplineBuilder withRoundingInterpolator(RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> roundingInterpolator) {
		this.roundingInterpolator = roundingInterpolator;
		return this;
	}

	public SplineBuilder withSpacingInterpolator(SpacingInterpolator<Map<BezierVector, List<Vector>>, List<Vector>> spacingInterpolator) {
		this.pointDistanceInterpolator = spacingInterpolator;
		return this;
	}

	public SplineBuilder withSegmentedPointFilter(Predicate<BezierVector> segmentedFilter, Predicate<Vector> pointFilter) {
		segmentFilterMap.put(segmentedFilter, pointFilter);
		return this;
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

		//interpolate spline shape
		roundedPath = roundingInterpolator.interpolate(baseVectors, closePath);

		//filtering per segment
		filteredRoundedPath = roundedPath;
		if (!segmentFilterMap.isEmpty()) {
			for (Predicate<BezierVector> segmentFilter : segmentFilterMap.keySet()) {
				Predicate<Vector> vectorFilter = segmentFilterMap.get(segmentFilter);
				for (BezierVector segment : baseVectors.stream().filter(segmentFilter).toList()) {
					if (!filteredRoundedPath.containsKey(segment)) {
						continue;
					}
					if (vectorFilter == null) {
						continue;
					}
					filteredRoundedPath.put(segment, filteredRoundedPath.get(segment).stream().filter(vectorFilter).toList());
				}
			}
		}

		//interpolate distance
		distancedPath = pointDistanceInterpolator.interpolate(filteredRoundedPath, closePath);

		//filter final points globally
		if (pointFilter != null) {
			filteredPath = distancedPath.stream().filter(pointFilter).collect(Collectors.toList());
		} else {
			filteredPath = new ArrayList<>(distancedPath);
		}

		return filteredPath;
	}
}
