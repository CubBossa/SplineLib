package de.bossascrew.splinelib;

import de.bossascrew.splinelib.interpolate.Interpolation;
import de.bossascrew.splinelib.shape.Shapes;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import de.bossascrew.splinelib.util.Vector;

import javax.swing.*;
import java.awt.*;

public class test {

	public static void main(String[] args) {

		SplineLib<int[]> lib = new SplineLib<>();
		lib.register(ints -> new Vector(ints[0], ints[1], ints[2]),
				vector -> new int[]{(int) vector.getX(), (int) vector.getY(), (int) vector.getZ()},
				ints -> new BezierVector(ints[0], ints[1], ints[2], null, null),
				vector -> new int[]{(int) vector.getX(), (int) vector.getY(), (int) vector.getZ()});

		Pose central = new Pose(new Vector(200, 200, 0), new Vector(1, 0, 0), new Vector(0, 0, 1));

		JFrame frmMain = new JFrame();
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CurveBuilder<int[]> builder = lib.newCurveBuilder(Shapes.star(central, 5, 20, 90, 150))
				.withRoundingInterpolation(Interpolation.bezierInterpolation(10))
				.withSpacingInterpolation(Interpolation.naturalInterpolation(8))
				.withClosedPath(true)
				.withSpacingProcessor(vectors -> vectors.rotate(new Vector(200, 200, 0), new Vector(0, 0, 1), 180));


		frmMain.add(new Panel<int[]>(builder));

		/*frmMain.add(new Panel(new SplineBuilder(new Oval(new Pose(
				new Vector(200, 200, 200),
				new Vector(0, 1, 0),
				new Vector(0, 0, 1)), 100, 1.8))
				.withClosedPath(true)
				.withRoundingInterpolator(Interpolation.bezierInterpolation(10))
				//.withSpacingInterpolator(Interpolation.angularInterpolation(10))
				.build()));

		frmMain.add(new Panel(new SplineBuilder(Shapes.square(new Pose(
				new Vector(200, 200, 200),
				new Vector(0, 1, 0),
				new Vector(0, 0, 1)), 100))
				.withClosedPath(true)
				.withRoundingInterpolator(Interpolation.bezierInterpolation(10))
				//.withSpacingInterpolator(Interpolation.angularInterpolation(10))
				.build()));
*/
/*
		frmMain.add(new Panel(new SplineBuilder(Shapes.star(new Pose(
				new Vector(200, 200, 200),
				new Vector(1, 0, 0),
				new Vector(0, 0, 1)), 8, 20, 0, 90, 150))
				.withClosedPath(true)
				.withRoundingInterpolator(new LeashInterpolation(30))
				.withSpacingInterpolator(Interpolation.naturalInterpolation(15))
				.withRotation(new Vector(200, 200, 200), new Vector(0, 0, 1), 90)
				.withRotation(new Vector(200, 200, 200), new Vector(1, 0, 0), 45)
				));


		frmMain.add(new Panel(new SplineBuilder(Lists.newArrayList(
				new BezierVector(200, 100, 0, new Vector(0, 100, 0), new Vector(400, 100, 0)),
				new BezierVector(200, 300, 0, new Vector(0, 300, 0), new Vector(400, 300, 0)),
				new BezierVector(300, 200, 0, new Vector(300, 210, 0), new Vector(300, 210, 0))))
				.withRoundingInterpolator(Interpolation.bezierInterpolation(50))
				.withSpacingInterpolator(Interpolation.angularInterpolation(10))
				.withClosedPath(false)
				.build()));

		frmMain.add(new Panel(new SplineBuilder(Lists.newArrayList(
				new BezierVector(100, 100, 0, null, new Vector(300, 0, 0)),
				new BezierVector(300, 200, 0, null, null),
				new BezierVector(100, 300, 0, null, null)))
				.withRoundingInterpolator(new LeashInterpolation(20))
				.withSpacingInterpolator(Interpolation.equidistantInterpolation(4))
				.withClosedPath(true)
				.build()));
*/
		frmMain.pack();
		frmMain.setVisible(true);
	}

	public static class Panel<V> extends JPanel {

		private final CurveBuilder<V> points;

		public Panel(CurveBuilder<V> points) {
			this.points = points;
			points.build();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(new Color(255, 0, 0));
			for (Vector p : points.getRoundedPath().keySet()) {
				g2d.fillOval((int) p.getX() - 2, (int) p.getY() - 2, 5, 5);
			}

			g2d.setColor(new Color(0, 0, 0));
			for (Vector p : points.build()) {
				//g2d.setColor(Color.getHSBColor((float) points.indexOf(p) / points.size() * 255f, 1, 1));
				g2d.fillOval((int) p.getX() - 1, (int) p.getY() - 1, 3, 3);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400, 400);
		}
	}
}
