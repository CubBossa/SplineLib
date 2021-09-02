package de.bossascrew.splinelib.interpolate.attribute;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.interpolate.Interpolator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NaturalInterpolation implements Interpolator<List<Vector>, Vector> {

	private final int segments;

	/**
	 * divides every curve segment (basepoint to basepoint) into the given amount of smaller segments
	 *
	 * @param segments the amount of segments between two basepoints
	 */
	public NaturalInterpolation(int segments) {
		this.segments = segments;
	}

	@Override
	public List<Vector> interpolate(List<List<Vector>> points) {
		Preconditions.checkArgument(!points.isEmpty());

		List<Vector> result = new ArrayList<>();

		for (List<Vector> group : points) {
			double groupLength = 0;
			for (int i = 0; i < group.size() - 1; i++) {
				groupLength += group.get(i).distance(group.get(i + 1));
			}
			double segmentLength = groupLength / segments;
			List<List<Vector>> param = new ArrayList<>();
			param.add(group);
			result.addAll(new EquidistantInterpolation(segmentLength).interpolate(param));
		}
		return result;
	}
}
