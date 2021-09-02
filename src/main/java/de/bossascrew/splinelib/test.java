package de.bossascrew.splinelib;

import de.bossascrew.splinelib.interpolate.Interpolation;
import de.bossascrew.splinelib.shape.Shapes;
import de.bossascrew.splinelib.util.Pose;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class test {

	public static void main(String[] args) {
		JFrame frmMain = new JFrame();
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frmMain.add(new Panel(new SplineBuilder(Shapes.star(new Pose(
				new Vector(200, 200, 200),
				new Vector(1, 0, 0),
				new Vector(0, 0, 1)), 3, 30, 50, 90))
				.withClosedPath(true)
				.withRoundingInterpolator(Interpolation.linearInterpolation(1))
				.withPointDistanceInterpolator(Interpolation.equidistantInterpolation(5))
				.build()));

		/*frmMain.add(new Panel(new SplineBuilder(Lists.newArrayList(new BezierVector(100, 100, 100,
						new Vector(50, 50, 50), new Vector(50, 50, 50)),
				new BezierVector(300, 300, 300, null, null),
				new BezierVector(200, 50, 200, new Vector(250, 50, 100), null)))
				.withRoundingInterpolator(Interpolation.bezierInterpolation(10))
				.withPointDistanceInterpolator(Interpolation.equidistantInterpolation(8))
				.withClosedPath(true)
				.build()));
*/
		frmMain.pack();
		frmMain.setVisible(true);
	}

	public static class Panel extends JPanel {

		private final List<Vector> points;

		public Panel(List<Vector> points) {
			this.points = points;
			System.out.println("Punkte " + points.size());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

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
