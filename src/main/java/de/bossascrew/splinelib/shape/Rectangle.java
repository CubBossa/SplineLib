package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Rectangle implements Shape {

	private final Pose pose;
	private final double sizeX;
	private final double sizeY;

	public Rectangle(Pose pose, double sizeX, double sizeY) {
		this.pose = pose;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	@Override
	public Pose getPose() {
		return pose;
	}

	@Override
	public List<BezierVector> getBezierVectors() {
		Vector base = pose.getPos();
		Vector a = pose.getDir().clone().normalize().multiply(sizeX);
		Vector b = pose.getDir().crossProduct(pose.getUp()).clone().normalize().multiply(sizeY);

		List<BezierVector> result = new ArrayList<>();
		result.add(new BezierVector(base.clone().add(a).add(b), null, null));
		result.add(new BezierVector(base.clone().add(a).subtract(b), null, null));
		result.add(new BezierVector(base.clone().subtract(a).add(b), null, null));
		result.add(new BezierVector(base.clone().subtract(a).subtract(b), null, null));
		return result;
	}
}
