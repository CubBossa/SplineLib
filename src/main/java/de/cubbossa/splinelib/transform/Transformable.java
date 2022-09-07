package de.cubbossa.splinelib.transform;

import de.cubbossa.splinelib.util.Vector;
import lombok.NonNull;

public interface Transformable<T> {

	T translate(Vector direction);

	T mirror(Vector point);

	T mirror(Vector point, Vector axis);

	T mirror(Vector point, Vector v, Vector w);

	T rotate(@NonNull Vector axis, double degree);

	T rotateRadian(@NonNull Vector axis, double radian);

	T rotate(Vector point, @NonNull Vector axis, double degree);

	T rotateRadian(Vector point, @NonNull Vector axis, double radian);

	T scale(double factor);

	T scale(double factorX, double factorY, double factorZ);

	T scale(Vector pivot, double factor);

	T scale(Vector pivot, double factorX, double factorY, double factorZ);
}
