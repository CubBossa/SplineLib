package de.bossascrew.splinelib;

import com.google.common.collect.Lists;
import de.bossascrew.splinelib.interpolate.Interpolation;
import de.bossascrew.splinelib.shape.Oval;
import de.bossascrew.splinelib.shape.Shapes;
import de.bossascrew.splinelib.util.BezierVector;
import de.bossascrew.splinelib.util.Pose;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class test {

	public static void main(String[] args) {
		JFrame frmMain = new JFrame();
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frmMain.add(new Panel(new SplineBuilder(new Oval(new Pose(
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

		frmMain.add(new Panel(new SplineBuilder(Shapes.star(new Pose(
				new Vector(200, 200, 200),
				new Vector(1, 0, 0),
				new Vector(0, 0, 1)), 8, 20, 0, 90, 150))
				.withClosedPath(true)
				.withRoundingInterpolator(Interpolation.bezierInterpolation(10))
				.withSpacingInterpolator(Interpolation.angularInterpolation(10))
				.build()));

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
				.withRoundingInterpolator(Interpolation.bezierInterpolation(20))
				.withSpacingInterpolator(Interpolation.equidistantInterpolation(4))
				.withClosedPath(true)
				.build()));

		frmMain.pack();
		frmMain.setVisible(true);
	}

	public static class Panel extends JPanel {

		private final List<Vector> points;

		public Panel(List<Vector> points) {
			this.points = points;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(new Color(255, 0, 0));
			g2d.fillOval(200, 100, 5, 5);
			g2d.fillOval(300, 200, 5, 5);
			g2d.fillOval(200, 300, 5, 5);

			g2d.setColor(new Color(0, 0, 0));
			for (Vector p : points) {
				//g2d.setColor(Color.getHSBColor((float) points.indexOf(p) / points.size() * 255f, 1, 1));
				g2d.fillOval(p.getBlockX(), p.getBlockY(), 3, 3);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400, 400);
		}
	}
}
