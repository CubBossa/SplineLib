package de.bossascrew.splinelib.util;

import de.bossascrew.splinelib.transform.LocalTransformable;
import de.bossascrew.splinelib.transform.Resetable;
import de.bossascrew.splinelib.transform.Transformable;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TransformableList<E extends Vector, V extends Vector> extends ArrayList<E> implements
		Transformable<TransformableList<E, V>>,
		LocalTransformable<TransformableList<E, V>>,
		Resetable<TransformableList<E, V>> {
	@Getter
	private final Pose pose;

	private final Pose startPose;

	public TransformableList() {
		super();
		pose = new Pose(Vector.zero(), Vector.x(), Vector.y(), Vector.z());
		startPose = pose.clone();
	}

	public TransformableList(E element) {
		super();
		this.add(element);
		pose = new Pose(Vector.zero(), Vector.x(), Vector.y(), Vector.z());
		startPose = pose.clone();
	}

	public TransformableList(Collection<E> collection) {
		super(collection);
		pose = new Pose(Vector.zero(), Vector.x(), Vector.y(), Vector.z());
		startPose = pose.clone();
	}

	public TransformableList(Pose startPose) {
		super();
		this.startPose = startPose;
		pose = startPose.clone();
	}

	public TransformableList(Pose startPose, E element) {
		this(element);
	}

	public TransformableList(Pose startPose, Collection<E> collection) {
		super(collection);
		this.startPose = startPose;
		this.pose = startPose;
	}

	//Translation

	public TransformableList<E, V> translate(Vector direction) {
		if (direction.equals(Vector.zero())) {
			return this;
		}
		pose.translate(direction);
		return translatePoints(direction);
	}

	private TransformableList<E, V> translatePoints(Vector direction) {
		for (V vector : getVectors()) {
			vector.add(direction);
		}
		return this;
	}

	public TransformableList<E, V> translateLocal(Vector vector) {
		return translateLocal(vector.getX(), vector.getY(), vector.getZ());
	}

	public TransformableList<E, V> translateLocal(double x, double y, double z) {
		Vector direction = Vector.zero()
				.add(pose.getRight().clone().normalize().multiply(x))
				.add(pose.getUp().clone().normalize().multiply(y))
				.add(pose.getDir().clone().normalize().multiply(z));
		return translate(direction);
	}

	public TransformableList<E, V> resetTranslation() {
		return translate(startPose.getPos().clone().subtract(pose.getPos()));
	}

	public TransformableList<E, V> applyTranslation() {
		startPose.setPos(pose.getPos().clone());
		return this;
	}

	//Mirror

	public TransformableList<E, V> mirror(Vector point) {
		pose.mirror(point);
		for (V vector : getVectors()) {
			vector.add(point.clone().subtract(vector).multiply(2));
		}
		return this;
	}

	public TransformableList<E, V> mirror(Vector point, Vector axis) {
		pose.mirror(point, axis);
		return rotate(point, axis, 180);
	}

	public TransformableList<E, V> mirror(Vector point, Vector v, Vector w) {
		v.normalize();
		w.normalize();

		// check if vectors are similar (v = x * w)
		if (w.equals(v) || w.equals(v.clone().multiply(-1))) {
			throw new IllegalArgumentException("Vectors are similar (v = x * w) and cannot represent a plane.");
		}
		pose.mirror(point, v, w);
		VectorUtil.mirror(point, v, w, getVectors());
		return this;
	}

	//Rotate

	public TransformableList<E, V> rotate(@NonNull Vector axis, double degree) {
		return rotate(pose.getPos(), axis, degree);
	}

	public TransformableList<E, V> rotate(Vector point, @NonNull Vector axis, double degree) {
		return rotateRadian(point, axis, degree * 0.01745329);
	}

	public TransformableList<E, V> rotateRadian(@NonNull Vector axis, double radian) {
		return rotateRadian(pose.getPos(), axis, radian);
	}

	public TransformableList<E, V> rotateRadian(Vector point, @NonNull Vector axis, double radian) {
		axis.normalize();

		double cosO = Math.cos(radian);
		double sinO = Math.sin(radian);

		//rotation matrix for rotation around axis with angle O
		Matrix rot = new Matrix(new double[][]{
				new double[]{cosO + axis.x * axis.x * (1 - cosO),
						axis.x * axis.y * (1 - cosO) - axis.z * sinO,
						axis.x * axis.z * (1 - cosO) + axis.y * sinO},
				new double[]{axis.y * axis.x * (1 - cosO) + axis.z * sinO,
						cosO + axis.y * axis.y * (1 - cosO),
						axis.y * axis.z * (1 - cosO) - axis.x * sinO},
				new double[]{axis.z * axis.x * (1 - cosO) - axis.y * sinO,
						axis.z * axis.y * (1 - cosO) + axis.x * sinO,
						cosO + axis.z * axis.z * (1 - cosO)}});

		pose.rotateRadian(point, axis, radian);
		//translate to 0, 0, 0 -> rotate around origin
		translatePoints(point.clone().multiply(-1)); //maybe combine translation rotation and backtranslation in one matrix
		for (Vector v : getVectors()) {
			//rotate each vector by using the matrix
			rot.multiply(v);
		}
		//translate each vector back to its original position
		translatePoints(point);
		return this;
	}

	public TransformableList<E, V> rotateLocalX(double degree) {
		return rotate(pose.getPos(), pose.getRight(), degree);
	}

	public TransformableList<E, V> rotateLocalXRadian(double radian) {
		return rotateRadian(pose.getPos(), pose.getRight(), radian);
	}

	public TransformableList<E, V> rotateLocalY(double degree) {
		return rotate(pose.getPos(), pose.getUp(), degree);
	}

	public TransformableList<E, V> rotateLocalYRadian(double radian) {
		return rotateRadian(pose.getPos(), pose.getRight(), radian);
	}

	public TransformableList<E, V> rotateLocalZ(double degree) {
		return rotate(pose.getPos(), pose.getDir(), degree);
	}

	public TransformableList<E, V> rotateLocalZRadian(double radian) {
		return rotateRadian(pose.getPos(), pose.getDir(), radian);
	}

	public TransformableList<E, V> resetRotation() {
		rotateTo(startPose.getUp(), startPose.getDir());
		double rightLength = pose.getRight().length();
		double upLength = pose.getUp().length();
		double dirLength = pose.getDir().length();
		pose.setRight(startPose.getRight().clone().normalize().multiply(rightLength));
		pose.setUp(startPose.getUp().clone().normalize().multiply(upLength));
		pose.setDir(startPose.getDir().clone().normalize().multiply(dirLength));
		return this;
	}

	public TransformableList<E, V> rotateTo(Vector up, Vector dir) {
		rotateRadian(pose.getPos(), pose.getDir(), - pose.getUp().angle(up));
		rotateRadian(pose.getPos(), pose.getUp(), - pose.getDir().angle(dir));
		return this;
	}

	public TransformableList<E, V> applyRotation() {
		double rightLength = startPose.getRight().length();
		double upLength = startPose.getUp().length();
		double dirLength = startPose.getDir().length();
		startPose.setRight(pose.getRight().clone().normalize().multiply(rightLength));
		startPose.setUp(pose.getUp().clone().normalize().multiply(upLength));
		startPose.setDir(pose.getDir().clone().normalize().multiply(dirLength));
		return this;
	}

	//Scale

	public TransformableList<E, V> scale(double factor) {
		return scale(pose.getPos(), factor);
	}

	public TransformableList<E, V> scale(double factorX, double factorY, double factorZ) {
		return scale(pose.getPos(), factorX, factorY, factorZ);
	}

	public TransformableList<E, V> scale(Vector pivot, double factor) {
		return scale(pivot, factor, factor, factor);
	}

	public TransformableList<E, V> scale(Vector pivot, double factorX, double factorY, double factorZ) {
		pose.scale(pivot, factorX, factorY, factorZ);
		Matrix scaleMatrix = new Matrix(new double[][]{
				new double[]{factorX, 0, 0},
				new double[]{0, factorY, 0},
				new double[]{0, 0, factorZ}});

		//translate all points to origin
		translatePoints(pivot.clone().multiply(-1));
		for (V vector : getVectors()) {
			//scale point from origin
			scaleMatrix.multiply(vector);
		}
		//translate back
		translatePoints(pivot);
		return this;
	}

	public TransformableList<E, V> scaleLocal(double x, double y, double z) {
		return scaleLocal(pose.getPos(), x, y, z);
	}

	public TransformableList<E, V> scaleLocal(Vector pivot, double x, double y, double z) {
		//TODO zurückrotieren, zurücktranslaten, skalieren, translaten, rotieren
		return this;
	}

	public TransformableList<E, V> resetScale() {
		return resetScale(pose.getPos());
	}

	public TransformableList<E, V> resetScale(Vector pivot) {
		return scaleLocal(pivot, startPose.getRight().length() / pose.getRight().length(),
				startPose.getUp().length() / pose.getUp().length(),
				startPose.getDir().length() / pose.getDir().length());
	}

	public TransformableList<E, V> applyScale() {
		startPose.getRight().normalize().multiply(pose.getRight().length());
		startPose.getUp().normalize().multiply(pose.getUp().length());
		startPose.getDir().normalize().multiply(pose.getDir().length());
		return this;
	}

	//All Transformation

	public TransformableList<E, V> resetTransformation() {
		resetTranslation();
		resetRotation();
		resetScale();
		return this;
	}

	public TransformableList<E, V> applyTransformation() {
		applyTranslation();
		applyRotation();
		applyScale();
		return this;
	}

	public abstract List<V> getVectors();
}
