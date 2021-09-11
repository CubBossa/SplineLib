package de.bossascrew.splinelib.util;

import de.bossascrew.splinelib.transform.LocalTransformable;
import de.bossascrew.splinelib.transform.Transformable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class Pose implements Transformable<Pose>, LocalTransformable<Pose>, Cloneable {

	private Vector pos, right, dir, up;

	public Pose(Vector pos, Vector dir, Vector up) {
		this(pos, dir, up, dir.clone().crossProduct(up).normalize());
	}

	public Pose(Vector pos, Vector right, Vector up, Vector dir) {
		this.pos = pos;
		this.right = right;
		this.up = up;
		this.dir = dir;
	}

	//Translate

	public Pose translate(Vector direction) {
		pos.add(direction);
		return this;
	}

	public Pose translateLocal(Vector vector) {
		pos
				.add(right.clone().normalize().multiply(vector.getX()))
				.add(up.clone().normalize().multiply(vector.getY()))
				.add(dir.clone().normalize().multiply(vector.getZ()));

		return this;
	}

	public Pose translateLocal(double x, double y, double z) {
		return translateLocal(new Vector(x, y, z));
	}

	//Mirror

	public Pose mirror(Vector point) {
		pos = point.clone().subtract(pos.clone().subtract(point));
		right.multiply(-1);
		up.multiply(-1);
		dir.multiply(-1);
		return this;
	}

	public Pose mirror(Vector point, Vector axis) {
		rotate(point, axis, 180);
		return this;
	}

	public Pose mirror(Vector point, Vector v, Vector w) {
		Vector rightPos = pos.clone().add(right);
		Vector upPos = pos.clone().add(up);
		Vector dirPos = pos.clone().add(dir);
		VectorUtil.mirror(point, v, w, pos, rightPos, upPos, dirPos);
		right = rightPos.subtract(pos);
		up = upPos.subtract(pos);
		dir = dirPos.subtract(pos);
		return this;
	}

	//Rotate

	public Pose rotate(@NonNull Vector axis, double degree) {
		return rotate(pos, axis, degree);
	}

	public Pose rotate(Vector point, @NonNull Vector axis, double degree) {
		return rotateRadian(point, axis, degree * 0.01745329);
	}

	public Pose rotateRadian(@NonNull Vector axis, double radian) {
		return rotateRadian(pos, axis, radian);
	}

	public Pose rotateRadian(Vector point, @NonNull Vector axis, double radian) {
		VectorUtil.rotate(Vector.zero(), axis, radian, right, up, dir);
		if(!point.equals(pos)) {
			VectorUtil.rotate(point, axis, radian, pos);
		}
		return this;
	}

	public Pose rotateLocalX(double degree) {
		return rotateLocalXRadian(degree * 0.01745329);
	}

	public Pose rotateLocalXRadian(double radian) {
		VectorUtil.rotate(Vector.zero(), right, radian, dir, up);
		return null;
	}

	public Pose rotateLocalY(double degree) {
		return rotateLocalYRadian(degree * 0.01745329);
	}

	public Pose rotateLocalYRadian(double radian) {
		VectorUtil.rotate(Vector.zero(), up, radian, right, dir);
		return this;
	}

	public Pose rotateLocalZ(double degree) {
		return rotateLocalZRadian(degree * 0.01745329);
	}

	public Pose rotateLocalZRadian(double radian) {
		VectorUtil.rotate(Vector.zero(), up, radian, right, dir);
		return this;
	}

	//Scale

	public Pose scale(double factor) {
		return scale(pos, factor);
	}

	public Pose scale(double factorX, double factorY, double factorZ) {
		return scale(pos, factorX, factorY, factorZ);
	}

	public Pose scale(Vector pivot, double factor) {
		pos = pivot.clone().add(pos.clone().subtract(pivot).multiply(factor));
		right.multiply(factor);
		up.multiply(factor);
		dir.multiply(factor);
		return this;
	}

	public Pose scale(Vector pivot, double factorX, double factorY, double factorZ) {
		if (!pos.equals(pivot)) {
			Vector d = pos.clone().subtract(pivot);
			pos.setX(pivot.x + d.x * factorX).setY(pivot.y + d.y * factorY).setZ(pivot.z + d.z * factorZ);
		}
		Vector factor = new Vector(factorX, factorY, factorZ);
		right.multiply(factor);
		up.multiply(factor);
		dir.multiply(factor);
		return this;
	}

	public Pose scaleLocal(double right, double up, double dir) {
		this.right.multiply(right);
		this.up.multiply(up);
		this.dir.multiply(dir);
		return this;
	}

	public Pose scaleLocal(Vector pivot, double x, double y, double z) {
		return null; //TODO
	}

	//Other

	public Pose clone() {
		return new Pose(pos.clone(), right.clone(), up.clone(), dir.clone());
	}
}
