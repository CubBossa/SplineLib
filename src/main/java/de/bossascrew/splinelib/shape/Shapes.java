package de.bossascrew.splinelib.shape;

import de.bossascrew.splinelib.util.Pose;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Shapes {

	public Rectangle rectangle(Pose pose, double sizeX, double sizeY) {
		return new Rectangle(pose, sizeX, sizeY);
	}

	public Star star(Pose pose, int spikes, double roundness, double innerRadius, double outerRadius) {
		return new Star(pose, spikes, roundness, innerRadius, outerRadius);
	}
}
