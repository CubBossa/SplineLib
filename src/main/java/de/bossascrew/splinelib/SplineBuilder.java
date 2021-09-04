package de.bossascrew.splinelib;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class SplineBuilder {

	private final List<BezierVector> baseVectors;
	private Map<BezierVector, List<Vector>> roundedPath = new LinkedHashMap<>();
	private Map<BezierVector, List<Vector>> filteredRoundedPath = new LinkedHashMap<>();
	private List<Vector> distancedPath = new ArrayList<>();
	private List<Vector> filteredPath = new ArrayList<>();
	private List<Supplier<List<Vector>>> rotationHandler = new ArrayList<>();

	private RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> roundingInterpolator = (points, closedPath) -> {
		return points.stream().collect(Collectors.toMap(v -> v, v -> Lists.newArrayList(v.toVector())));
	};

	private SpacingInterpolator<Map<BezierVector, List<Vector>>, List<Vector>> spacingInterpolator = (points, closedPath) -> {
		List<Vector> list = new ArrayList<>();
		if (points == null || points.isEmpty()) {
			return new ArrayList<>();
		}
		points.forEach((bezierVector, vectors) -> list.addAll(vectors));
		return list;
	};

	private Predicate<Vector> pointFilter = null;
	private final Map<Predicate<BezierVector>, Predicate<Vector>> segmentFilterMap;

	private boolean closePath = false;

	public SplineBuilder(SplineBuilder other) {
		this.baseVectors = new ArrayList<>(other.baseVectors);
		this.roundedPath = new LinkedHashMap<>(other.roundedPath);
		this.filteredRoundedPath = new LinkedHashMap<>(other.filteredRoundedPath);
		this.distancedPath = new ArrayList<>(other.distancedPath);
		this.filteredPath = new ArrayList<>(other.filteredPath);
		this.roundingInterpolator = other.roundingInterpolator;
		this.spacingInterpolator = other.spacingInterpolator;
		this.pointFilter = other.pointFilter;
		this.rotationHandler = new ArrayList<>(other.rotationHandler);
		this.segmentFilterMap = new LinkedHashMap<>(other.segmentFilterMap);
		this.closePath = other.closePath;
	}

	public SplineBuilder(List<BezierVector> baseVectors) {
		this.baseVectors = baseVectors;
		this.segmentFilterMap = new LinkedHashMap<>();
	}

	public SplineBuilder(Shape shape) {
		this.baseVectors = shape.getSpline();
		this.segmentFilterMap = new LinkedHashMap<>();
		this.closePath = false;
	}

	public SplineBuilder withRoundingInterpolator(RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> roundingInterpolator) {
		this.roundingInterpolator = roundingInterpolator;
		return this;
	}

	public SplineBuilder withSpacingInterpolator(SpacingInterpolator<Map<BezierVector, List<Vector>>, List<Vector>> spacingInterpolator) {
		this.spacingInterpolator = spacingInterpolator;
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

	public SplineBuilder withRotation(Vector pivot, Vector axis, double angle) {
		rotationHandler.add(() -> this.rotateAroundNonUnitAxis(pivot, axis, angle * 0.01745329));
		return this;
	}

	public void interpolateRounding() {
		interpolateRounding(this.roundingInterpolator);
	}

	public void interpolateRounding(RoundingInterpolator<List<BezierVector>, Map<BezierVector, List<Vector>>> interpolator) {
		roundedPath = interpolator.interpolate(baseVectors, closePath);
	}

	public void interpolateSpacing() {
		interpolateSpacing(this.spacingInterpolator);
	}

	public void interpolateSpacing(SpacingInterpolator<Map<BezierVector, List<Vector>>, List<Vector>> interpolator) {
		distancedPath = interpolator.interpolate(filteredRoundedPath, closePath);
	}

	public void filterSegments() {
		filterSegments(this.segmentFilterMap);
	}

	public void filterSegments(Map<Predicate<BezierVector>, Predicate<Vector>> segmentFilterMap) {
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
	}

	public void filterPoints() {
		filterPoints(this.pointFilter);
	}

	public void filterPoints(Predicate<Vector> pointFilter) {
		if (pointFilter != null) {
			filteredPath = distancedPath.stream().filter(pointFilter).collect(Collectors.toList());
		} else {
			filteredPath = new ArrayList<>(distancedPath);
		}
	}

	public void applyRotationHandlers() {
		for (Supplier<List<Vector>> handler : rotationHandler) {
			handler.get();
		}
	}

	public List<Vector> build() {

		//interpolate spline shape
		interpolateRounding(this.roundingInterpolator);

		//filtering per segment
		filterSegments(this.segmentFilterMap);

		//interpolate distance
		interpolateSpacing(this.spacingInterpolator);

		//TODO filter again with different filter set

		//filter final points globally
		filterPoints(this.pointFilter);

		//rotate
		applyRotationHandlers();

		return new ArrayList<>(filteredPath);
	}

	public List<Vector> rotateAroundNonUnitAxis(Vector pivot, Vector axis, double angle) throws IllegalArgumentException {
		return rotateAroundNonUnitAxis(filteredPath, pivot, axis, angle);
	}

	public List<Vector> rotateAroundNonUnitAxis(List<Vector> points, Vector pivot, Vector axis, double angle) throws IllegalArgumentException {
		Preconditions.checkArgument(axis != null, "The provided axis vector was null");
		axis = axis.isNormalized() ? axis : axis.normalize();

		double cosTheta = Math.cos(angle);
		double sinTheta = Math.sin(angle);
		double x2 = axis.getX();
		double y2 = axis.getY();
		double z2 = axis.getZ();

		for (Vector vector : points) {
			vector.subtract(pivot);
			double x = vector.getX();
			double y = vector.getY();
			double z = vector.getZ();
			double dotProduct = vector.dot(axis);
			double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
			double yPrime = y2 * dotProduct * (1.0D - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
			double zPrime = z2 * dotProduct * (1.0D - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
			vector.setX(xPrime).setY(yPrime).setZ(zPrime);
			vector.add(pivot);
		}
		return points;
	}
}