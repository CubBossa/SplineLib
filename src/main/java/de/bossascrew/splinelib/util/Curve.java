package de.bossascrew.splinelib.util;

import java.util.Collection;
import java.util.List;

public class Curve extends Transformable<Vector, Vector> {

	public Curve() {
		super();
	}

	public Curve(Vector vector) {
		super(vector);
	}

	public Curve(Collection<Vector> group) {
		super(group);
	}

	@Override
	public List<Vector> getVectors() {
		return this;
	}
}