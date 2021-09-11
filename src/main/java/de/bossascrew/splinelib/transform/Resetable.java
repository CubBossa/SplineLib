package de.bossascrew.splinelib.transform;

import de.bossascrew.splinelib.util.Vector;

public interface Resetable<T> {

	T applyTranslation();

	T applyRotation();

	T applyScale();

	T applyTransformation();

	T resetTranslation();

	T resetRotation();

	T resetScale();

	T resetScale(Vector pivot);

	T resetTransformation();
}
