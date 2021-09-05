package de.bossascrew.splinelib;

import de.bossascrew.splinelib.interpolate.Interpolation;
import de.bossascrew.splinelib.shape.Shapes;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Curve;
import de.bossascrew.splinelib.util.Pose;
import de.bossascrew.splinelib.util.Vector;

import java.awt.*;

public class Main {

	public static void main(String[] args) {

		SplineLib<org.bukkit.util.Vector> lib = new SplineLib<>() {
			@Override
			public Vector convertToVector(org.bukkit.util.Vector value) {
				return new Vector(value.getX(), value.getY(), value.getZ());
			}

			@Override
			public org.bukkit.util.Vector convertFromVector(Vector value) {
				return new org.bukkit.util.Vector(value.getX(), value.getY(), value.getZ());
			}

			@Override
			public BezierVector convertToBezierVector(org.bukkit.util.Vector value) {
				return new BezierVector(value.getX(), value.getY(), value.getZ(), null, null);
			}

			@Override
			public org.bukkit.util.Vector convertFromBezierVector(BezierVector value) {
				return new org.bukkit.util.Vector(value.getX(), value.getY(), value.getZ());
			}
		};

		Pose central = new Pose(new Vector(400, 400, 0), new Vector(1, 0, 0), new Vector(0, 0, 1));

		CurveBuilder<org.bukkit.util.Vector> builder = lib.newCurveBuilder(Shapes.star(central, 4, 30, -200, 100, 300))
				.withRoundingInterpolation(Interpolation.bezierInterpolation(8))
				.withSpacingInterpolation(Interpolation.naturalInterpolation(12))
				.withClosedPath(true);

		Curve curve = builder.build();

		Screen screen = new Screen(16, graphics -> {

			//curve.rotate(central.getPos(), new Vector(0, 1, 0), 2);
			//curve.translate(Vector.Y.clone().multiply(0.1));

			Graphics2D g2d = (Graphics2D) graphics;

			g2d.setColor(new Color(255, 0, 0));
			for (BezierVector p : builder.getSpline()) {
				if (p.getRightControlPoint() != null && p.getLeftControlPoint() != null) {

					//linie
					g2d.setColor(new Color(0, 150, 0));
					g2d.drawLine((int) p.getRightControlPoint().getX(), (int) p.getRightControlPoint().getY(),
							(int) p.getLeftControlPoint().getX(), (int) p.getLeftControlPoint().getY());

					//kontrollpunkte
					g2d.setColor(new Color(0, 255, 0));
					g2d.fillOval((int) p.getRightControlPoint().getX() - 2, (int) p.getRightControlPoint().getY() - 2, 5, 5);
					g2d.fillOval((int) p.getLeftControlPoint().getX() - 2, (int) p.getLeftControlPoint().getY() - 2, 5, 5);
				}

				//Bezierpunkt
				g2d.setColor(new Color(255, 0, 0));
				g2d.fillOval((int) p.getX() - 3, (int) p.getY() - 3, 7, 7);
			}

			g2d.setColor(new Color(0, 0, 0));
			for (Vector p : curve) {
				double val = (p.getZ() + 400) / (double) 800;
				int size = (int) (val * 12) + 1;
				int col = (int) (val * 235 + 20);

				size = Integer.min(Integer.max(1, size), 7);
				col = Integer.min(Integer.max(0, col), 255);

				g2d.setColor(new Color(255, 255, 255));
				//g2d.setColor(new Color(col, col, col));
				g2d.fillOval((int) p.getX() - 1, (int) p.getY() - 1, size, size);
			}
		});
	}
}
