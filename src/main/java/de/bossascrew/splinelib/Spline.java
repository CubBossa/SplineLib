package de.bossascrew.splinelib;

import de.bossascrew.splinelib.util.BezierVector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Spline extends Transformable<BezierVector, Vector> {

	private boolean closed;

	public Spline() {
		super();
	}

	public Spline(Collection<BezierVector> vectors) {
		this.addAll(vectors);
	}

	@Override
	public List<Vector> getVectors() {
		List<Vector> result = new ArrayList<>();
		for (BezierVector v : this) {
			result.add(v);
			result.add(v.leftControlPoint);
			result.add(v.rightControlPoint);
		}
		return result;
	}
}
