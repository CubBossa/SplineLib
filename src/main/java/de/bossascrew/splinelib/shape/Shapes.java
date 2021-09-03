package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.util.Pose;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@UtilityClass
public class Shapes {

	private final Vector UP = new Vector(0, 1, 0);

	public Oval circle(Pose pose, double radius) {
		return new Oval(pose, radius, 1);
	}

	public Oval circle(Location location, double radius) {
		return new Oval(fromLocation(location), radius, 1);
	}

	public Oval oval(Pose pose, double radius, double ratio) {
		return new Oval(pose, radius, ratio);
	}

	public Oval oval(Location location, double radius, double ratio) {
		return new Oval(fromLocation(location), radius, ratio);
	}

	public Rectangle square(Pose pose, double size) {
		return new Rectangle(pose, size, size);
	}

	public Rectangle square(Location location, double size) {
		return new Rectangle(fromLocation(location), size, size);
	}

	public Rectangle rectangle(Pose pose, double sizeX, double sizeY) {
		return new Rectangle(pose, sizeX, sizeY);
	}

	public Rectangle rectangle(Location location, double sizeX, double sizeY) {
		return new Rectangle(fromLocation(location), sizeX, sizeY);
	}

	public Star star(Pose pose, int spikes, double innerRadius, double outerRadius) {
		return new Star(pose, spikes, 0, 0, innerRadius, outerRadius);
	}

	public Star star(Location location, int spikes, double innerRadius, double outerRadius) {
		return new Star(fromLocation(location), spikes, 0, 0, innerRadius, outerRadius);
	}

	public Star star(Pose pose, int spikes, double roundness, double innerRadius, double outerRadius) {
		return new Star(pose, spikes, roundness, roundness, innerRadius, outerRadius);
	}

	public Star star(Location location, int spikes, double roundness, double innerRadius, double outerRadius) {
		return new Star(fromLocation(location), spikes, roundness, roundness, innerRadius, outerRadius);
	}

	public Star star(Pose pose, int spikes, double innerRoundness, double outerRoundness, double innerRadius, double outerRadius) {
		return new Star(pose, spikes, innerRoundness, outerRoundness, innerRadius, outerRadius);
	}

	public Star star(Location location, int spikes, double innerRoundness, double outerRoundness, double innerRadius, double outerRadius) {
		return new Star(fromLocation(location), spikes, innerRoundness, outerRoundness, innerRadius, outerRadius);
	}

	private Pose fromLocation(Location location) {
		return new Pose(location.toVector(), location.getDirection(), UP);
	}
}
