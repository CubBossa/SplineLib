package de.cubbossa.splinelib.util;

import org.jetbrains.annotations.NotNull;

public class Vector implements Cloneable {

	private static final double epsilon = 1.0E-6D;
	protected double x;
	protected double y;
	protected double z;

	public Vector() {
		this.x = 0.0D;
		this.y = 0.0D;
		this.z = 0.0D;
	}

	public Vector(int x, int y, int z) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(float x, float y, float z) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
	}

	@NotNull
	public Vector add(@NotNull Vector vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		return this;
	}

	@NotNull
	public Vector subtract(@NotNull Vector vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		return this;
	}

	@NotNull
	public Vector multiply(@NotNull Vector vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
		return this;
	}

	@NotNull
	public Matrix multiply(@NotNull Matrix matrix) {
		return this.toMatrix().multiply(matrix);
	}

	@NotNull
	public Vector divide(@NotNull Vector vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
		return this;
	}

	@NotNull
	public Vector copy(@NotNull Vector vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		return this;
	}

	public double length() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}

	public double lengthSquared() {
		return Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2);
	}

	public double distance(@NotNull Vector o) {
		return Math.sqrt(Math.pow(this.x - o.x, 2) + Math.pow(this.y - o.y, 2) + Math.pow(this.z - o.z, 2));
	}

	public double distanceSquared(@NotNull Vector o) {
		return Math.pow(this.x - o.x, 2) + Math.pow(this.y - o.y, 2) + Math.pow(this.z - o.z, 2);
	}

	public float angle(@NotNull Vector other) {
		double dot = Double.max(Double.min(this.dot(other) / (this.length() * other.length()), 1.0D), -1.0D);
		return (float) Math.acos(dot);
	}

	@NotNull
	public Vector midpoint(@NotNull Vector other) {
		this.x = (this.x + other.x) / 2.0D;
		this.y = (this.y + other.y) / 2.0D;
		this.z = (this.z + other.z) / 2.0D;
		return this;
	}

	@NotNull
	public Vector getMidpoint(@NotNull Vector other) {
		double x = (this.x + other.x) / 2.0D;
		double y = (this.y + other.y) / 2.0D;
		double z = (this.z + other.z) / 2.0D;
		return new Vector(x, y, z);
	}

	@NotNull
	public Vector multiply(int m) {
		this.x *= (double) m;
		this.y *= (double) m;
		this.z *= (double) m;
		return this;
	}

	@NotNull
	public Vector multiply(double m) {
		this.x *= m;
		this.y *= m;
		this.z *= m;
		return this;
	}

	@NotNull
	public Vector multiply(float m) {
		this.x *= (double) m;
		this.y *= (double) m;
		this.z *= (double) m;
		return this;
	}

	public double dot(@NotNull Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	@NotNull
	public Vector crossProduct(@NotNull Vector o) {
		double newX = this.y * o.z - o.y * this.z;
		double newY = this.z * o.x - o.z * this.x;
		double newZ = this.x * o.y - o.x * this.y;
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		return this;
	}

	@NotNull
	public Vector getCrossProduct(@NotNull Vector o) {
		double x = this.y * o.z - o.y * this.z;
		double y = this.z * o.x - o.z * this.x;
		double z = this.x * o.y - o.x * this.y;
		return new Vector(x, y, z);
	}

	@NotNull
	public Vector normalize() {
		double length = this.length();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		return this;
	}

	@NotNull
	public static Vector zero() {
		return new Vector(0, 0, 0);
	}

	@NotNull
	Vector normalizeZeros() {
		if (this.x == -0.0D) {
			this.x = 0.0D;
		}

		if (this.y == -0.0D) {
			this.y = 0.0D;
		}

		if (this.z == -0.0D) {
			this.z = 0.0D;
		}

		return this;
	}

	public boolean isInAABB(@NotNull Vector min, @NotNull Vector max) {
		return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
	}

	public boolean isInSphere(@NotNull Vector origin, double radius) {
		return Math.pow(origin.x - this.x, 2) + Math.pow(origin.y - this.y, 2) + Math.pow(origin.z - this.z, 2) <= Math.pow(radius, 2);
	}

	public boolean isNormalized() {
		return Math.abs(this.lengthSquared() - 1.0D) < getEpsilon();
	}

	@NotNull
	public Vector rotateAroundX(double angle) {
		double angleCos = Math.cos(angle);
		double angleSin = Math.sin(angle);
		double y = angleCos * this.getY() - angleSin * this.getZ();
		double z = angleSin * this.getY() + angleCos * this.getZ();
		return this.setY(y).setZ(z);
	}

	@NotNull
	public Vector rotateAroundY(double angle) {
		double angleCos = Math.cos(angle);
		double angleSin = Math.sin(angle);
		double x = angleCos * this.getX() + angleSin * this.getZ();
		double z = -angleSin * this.getX() + angleCos * this.getZ();
		return this.setX(x).setZ(z);
	}

	@NotNull
	public Vector rotateAroundZ(double angle) {
		double angleCos = Math.cos(angle);
		double angleSin = Math.sin(angle);
		double x = angleCos * this.getX() - angleSin * this.getY();
		double y = angleSin * this.getX() + angleCos * this.getY();
		return this.setX(x).setY(y);
	}

	@NotNull
	public Vector rotateAroundAxis(@NotNull Vector axis, double angle) throws IllegalArgumentException {
		return this.rotateAroundNonUnitAxis(axis.isNormalized() ? axis : axis.clone().normalize(), angle);
	}

	@NotNull
	public Vector rotateAroundNonUnitAxis(@NotNull Vector axis, double angle) throws IllegalArgumentException {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		double x2 = axis.getX();
		double y2 = axis.getY();
		double z2 = axis.getZ();
		double cosTheta = Math.cos(angle);
		double sinTheta = Math.sin(angle);
		double dotProduct = this.dot(axis);
		double xPrime = x2 * dotProduct * (1.0D - cosTheta) + x * cosTheta + (-z2 * y + y2 * z) * sinTheta;
		double yPrime = y2 * dotProduct * (1.0D - cosTheta) + y * cosTheta + (z2 * x - x2 * z) * sinTheta;
		double zPrime = z2 * dotProduct * (1.0D - cosTheta) + z * cosTheta + (-y2 * x + x2 * y) * sinTheta;
		return this.setX(xPrime).setY(yPrime).setZ(zPrime);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	@NotNull
	public Vector setX(int x) {
		this.x = (double) x;
		return this;
	}

	@NotNull
	public Vector setX(double x) {
		this.x = x;
		return this;
	}

	@NotNull
	public Vector setX(float x) {
		this.x = (double) x;
		return this;
	}

	@NotNull
	public Vector setY(int y) {
		this.y = (double) y;
		return this;
	}

	@NotNull
	public Vector setY(double y) {
		this.y = y;
		return this;
	}

	@NotNull
	public Vector setY(float y) {
		this.y = (double) y;
		return this;
	}

	@NotNull
	public Vector setZ(int z) {
		this.z = (double) z;
		return this;
	}

	@NotNull
	public Vector setZ(double z) {
		this.z = z;
		return this;
	}

	@NotNull
	public Vector setZ(float z) {
		this.z = (double) z;
		return this;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Vector)) {
			return false;
		} else {
			Vector other = (Vector) obj;
			return Math.abs(this.x - other.x) < 1.0E-6D && Math.abs(this.y - other.y) < 1.0E-6D && Math.abs(this.z - other.z) < 1.0E-6D && this.getClass().equals(obj.getClass());
		}
	}

	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
		hash = 79 * hash + (int) (Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
		hash = 79 * hash + (int) (Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
		return hash;
	}

	@NotNull
	public Vector clone() {
		try {
			return (Vector) super.clone();
		} catch (CloneNotSupportedException var2) {
			throw new Error(var2);
		}
	}

	public String toString() {
		return this.x + "," + this.y + "," + this.z;
	}

	public Matrix toMatrix() {
		Matrix m = new Matrix(3, 1);
		m.getData()[0][0] = x;
		m.getData()[1][0] = y;
		m.getData()[2][0] = z;
		return m;
	}

	public Matrix toMatrix4() {
		Matrix m = new Matrix(4, 1);
		m.getData()[0][0] = x;
		m.getData()[1][0] = y;
		m.getData()[2][0] = z;
		m.getData()[3][0] = 1;
		return m;
	}

	public static double getEpsilon() {
		return epsilon;
	}

	@NotNull
	public static Vector getMinimum(@NotNull Vector v1, @NotNull Vector v2) {
		return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
	}

	@NotNull
	public static Vector getMaximum(@NotNull Vector v1, @NotNull Vector v2) {
		return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
	}

	public static Vector one() {
		return new Vector(1, 1, 1);
	}

	public static Vector x() {
		return new Vector(1, 0, 0);
	}

	public static Vector y() {
		return new Vector(0, 1, 0);
	}

	public static Vector z() {
		return new Vector(0, 0, 1);
	}
}
