package de.bossascrew.splinelib;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class Screen {

	private final int updateDelay;
	private final Consumer<Graphics> graphicsConsumer;

	public Screen(int updateDelay, Consumer<Graphics> graphicsConsumer) {
		this.updateDelay = updateDelay;
		this.graphicsConsumer = graphicsConsumer;
		buildScreen();
	}

	public void buildScreen() {

		JFrame screenFrame = new JFrame();
		screenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Canvas canvas = new Canvas(graphicsConsumer);
		canvas.setBackground(Color.BLACK);
		screenFrame.add(canvas);

		screenFrame.pack();
		screenFrame.setVisible(true);

		startRoutine(canvas.getGraphics(), canvas);
	}

	public void startRoutine(Graphics g, Canvas canvas) {
		ActionListener taskPerformer = e -> {
			canvas.paintComponent(g);
		};
		new Timer(updateDelay, taskPerformer).start();
	}


	@RequiredArgsConstructor
	public static class Canvas extends JPanel {

		private final Consumer<Graphics> graphicsConsumer;

		protected void paintComponent() {
			paintComponent(this.getGraphics());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			graphicsConsumer.accept(g);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 800);
		}
	}
}
