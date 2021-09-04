package de.bossascrew.splinelib.interpolate.spacing;

import com.google.common.base.Preconditions;
import de.bossascrew.splinelib.Curve;
import de.bossascrew.splinelib.interpolate.SpacingInterpolator;
import de.bossascrew.splinelib.util.BezierVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaturalInterpolation implements SpacingInterpolator<Map<BezierVector, Curve>, Curve> {

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
	public Curve interpolate(Map<BezierVector, Curve> points, boolean closedPath) {
		Preconditions.checkArgument(!points.isEmpty());

		//TODO proper naming for variables

		Curve result = new Curve();

		//calculate length for each segment of two beziervectors and use equidistant interpolation for divided length

		List<Map.Entry<BezierVector, Curve>> entries = new ArrayList<>(points.entrySet());
		if (entries.isEmpty()) {
			return result;
		}
		for (int groupIndex = 0; groupIndex < entries.size(); groupIndex++) {
			Curve group = entries.get(groupIndex).getValue();

			//find total length of curve between bezier points
			double groupLength = 0;
			for (int i = 0; i < group.size(); i++) {

				//add distance to first point of next segment
				Vector second = null;
				if (i == group.size() - 1) {
					if (groupIndex > points.size() - 2) {
						if (closedPath) {
							Map.Entry<BezierVector, Curve> g = entries.get(0);
							if (g != null && !g.getValue().isEmpty()) {
								second = g.getValue().get(0);
							}
						} else {
							continue;
						}
					} else {
						Map.Entry<BezierVector, Curve> g = entries.get(groupIndex + 1);
						if (g != null && !g.getValue().isEmpty()) {
							second = g.getValue().get(0);
						}
					}
				} else {
					second = group.get(i + 1);
				}
				if (second != null) {
					groupLength += group.get(i).distance(second);
				}
			}

			double segmentLength = groupLength / segments;
			Map<BezierVector, Curve> param = new HashMap<>();
			Curve g = new Curve(group);

			//adding the first point of each following group to prevent holes in spline
			if (groupIndex > points.size() - 2) {
				if (closedPath) {
					Map.Entry<BezierVector, Curve> gg = entries.get(0);
					if (gg != null && !gg.getValue().isEmpty()) {
						g.add(gg.getValue().get(0));
					}
				}
			} else {
				Map.Entry<BezierVector, Curve> e = entries.get(groupIndex + 1);
				if (e != null && !e.getValue().isEmpty()) {
					g.add(e.getValue().get(0));
				}
			}

			param.put(entries.get(groupIndex).getKey(), g);
			result.addAll(new EquidistantInterpolation(segmentLength).interpolate(param, false));
		}
		return result;
	}
}
