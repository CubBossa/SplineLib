package de.bossascrew.splinelib.interpolate.spacing;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NaturalInterpolation implements SpacingInterpolator<List<Vector>, Vector> {

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
	public List<Vector> interpolate(List<List<Vector>> points, boolean closed) {
		Preconditions.checkArgument(!points.isEmpty());

		List<Vector> result = new ArrayList<>();

		//calculate length of each curve between two beziervectors and use equidistant interpolation for divided length

		int groupIndex = 0;
		for (List<Vector> group : points) {
			if (points.isEmpty()) {
				groupIndex++;
				continue;
			}

			//find total length of curve between bezier points
			double groupLength = 0;
			for (int i = 0; i < group.size(); i++) {

				//add distance to first point of next segment
				Vector second;
				if (i == group.size() - 1) {
					if (groupIndex > points.size() - 2) {
						if (closed) {
							second = points.get(0).get(0);
						} else {
							continue;
						}
					} else {
						second = points.get(groupIndex + 1).get(0);
					}
				} else {
					second = group.get(i + 1);
				}
				groupLength += group.get(i).distance(second);
			}

			double segmentLength = groupLength / segments;
			List<List<Vector>> param = new ArrayList<>();
			List<Vector> g = new ArrayList<>(group);

			//adding the first point of each following group to prevent holes in spline
			if (groupIndex > points.size() - 2) {
				if (closed) {
					g.add(points.get(0).get(0));
				}
			} else {
				g.add(points.get(groupIndex + 1).get(0));
			}

			param.add(g);
			result.addAll(new EquidistantInterpolation(segmentLength).interpolate(param, false));

			groupIndex ++;
		}
		return result;
	}
}
