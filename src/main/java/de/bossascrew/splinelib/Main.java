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


		SplineLib<int[]> lib = new SplineLib<>();
		lib.register(ints -> new Vector(ints[0], ints[1], ints[2]),
				vector -> new int[]{(int) vector.getX(), (int) vector.getY(), (int) vector.getZ()},
				ints -> new BezierVector(ints[0], ints[1], ints[2], null, null),
				vector -> new int[]{(int) vector.getX(), (int) vector.getY(), (int) vector.getZ()});

		Pose central = new Pose(new Vector(200, 200, 0), new Vector(1, 0, 0), new Vector(0, 0, 1));

		CurveBuilder<int[]> builder = lib.newCurveBuilder(Shapes.star(central, 5, 20, 90, 150))
				.withRoundingInterpolation(Interpolation.bezierInterpolation(10))
				.withSpacingInterpolation(Interpolation.naturalInterpolation(8))
				.withClosedPath(true);


		Curve curve = builder.build();
		Screen screen = new Screen(graphics -> {

			curve.rotate(new Vector(200, 200, 0), new Vector(0, 1, 0), 2);
			curve.translate(Vector.Y.clone().multiply(0.1));

			Graphics2D g2d = (Graphics2D) graphics;

			/*g2d.setColor(new Color(255, 0, 0));
			for (Vector p : builder.getRoundedPath().keySet()) {
				g2d.fillOval((int) p.getX() - 2, (int) p.getY() - 2, 5, 5);
			}*/

			g2d.setColor(new Color(0, 0, 0));
			for (Vector p : curve) {
				double val = (p.getZ() + 200) / (double) 400;
				int size = (int) (val * 7);
				int col = (int) (val * 205 + 50);

				size = Integer.min(Integer.max(1, size), 7);
				col = Integer.min(Integer.max(0, col), 255);

				g2d.setColor(new Color(col, col, col));
				//g2d.setColor(Color.getHSBColor((float) points.indexOf(p) / points.size() * 255f, 1, 1));
				g2d.fillOval((int) p.getX() - 1, (int) p.getY() - 1, size, size);
			}
		});
	}
}
