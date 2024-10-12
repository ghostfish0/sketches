/*
  Originally written by Bruce A. Maxwell a long time ago.
  Updated by Brian Eastwood and Stephanie Taylor more recently
  Updated by Bruce again in Fall 2018

  Creates a window using the JFrame class.

  Creates a drawable area in the window using the JPanel class.

  The JPanel calls the Landscape's draw method to fill in content, so the
  Landscape class needs a draw method.

  Students should not *need* to edit anything outside of the main method,
  but are free to do so if they wish.
*/

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Displays a Landscape graphically using Swing. The Landscape
 * contains a grid which can be displayed at any scale factor.
 *
 * @author bseastwo
 */
public class LandscapeDisplay {
	JFrame win;
	protected Landscape scape;
	private LandscapePanel canvas;
	private int gridScale; // width (and height) of each square in the grid

	/**
	 * Initializes a display window for a Landscape.
	 *
	 * @param scape the Landscape to display
	 * @param scale controls the relative size of the display
	 */
	public LandscapeDisplay(Landscape scape, int scale) {
		// setup the window
		this.win = new JFrame("Game of Life");
		this.win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.scape = scape;
		this.gridScale = scale;

		// create a panel in which to display the Landscape
		// put a buffer of two rows around the display grid
		this.canvas = new LandscapePanel((int)(this.scape.getCols()) * this.gridScale,
				 (int)(this.scape.getRows()) * this.gridScale);

		// add the panel to the window, layout, and display
		this.win.add(this.canvas);
		this.win.pack();
		this.win.setVisible(true);

		// Add mouse listener to handle mouse events
		this.win.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
                // pass to setCellAlive ratios between 0 and 1 as mousePosition
				scape.setCellAlive((double)e.getX() / win.getWidth(),
					   (double)e.getY() / win.getHeight());
			}
		});

		this.win.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
                // pass to setCellAlive ratios between 0 and 1 as mousePosition
				scape.setCellAlive((double)e.getX() / win.getWidth(),
					   (double)e.getY() / win.getHeight());
			}
		});
	}

	/**
	 * Saves an image of the display contents to a file. The supplied
	 * filename should have an extension supported by javax.imageio, e.g.
	 * "png" or "jpg".
	 *
	 * @param filename the name of the file to save
	 */
	public void saveImage(String filename) {
		// get the file extension from the filename
		String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());

		// create an image buffer to save this component
		Component tosave = this.win.getRootPane();
		BufferedImage image = new BufferedImage(tosave.getWidth(), tosave.getHeight(), BufferedImage.TYPE_INT_RGB);

		// paint the component to the image buffer
		Graphics g = image.createGraphics();
		tosave.paint(g);
		g.dispose();

		// save the image
		try {
			ImageIO.write(image, ext, new File(filename));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	/**
	 * This inner class provides the panel on which Landscape elements
	 * are drawn.
	 */
	private class LandscapePanel extends JPanel {
		/**
		 * Creates the panel.
		 *
		 * @param width  the width of the panel in pixels
		 * @param height the height of the panel in pixels
		 */
		public LandscapePanel(int width, int height) {
			super();
			this.setPreferredSize(new Dimension(width, height));
			this.setBackground(Color.BLACK);
		}

		/**
		 * Method overridden from JComponent that is responsible for
		 * drawing components on the screen. The supplied Graphics
		 * object is used to draw.
		 *
		 * @param g the Graphics object used for drawing
		 */
		public void paintComponent(Graphics g) {
			// take care of housekeeping by calling parent paintComponent
			super.paintComponent(g);

			// call the Landscape draw method here
			scape.draw(g, gridScale);
		} // end paintComponent

	} // end LandscapePanel

	public void repaint() { this.win.repaint(); }

	public static void main(String[] args) throws InterruptedException {
        // sample grids, String[] because a boolean grid is too unreadable
		final String[] blinker = {
		        "_____", //
		        "_____", //
		        "_xxx_", //
		        "_____", //
		        "_____", //
		};
		final String[] pulsar = {
		        "_____________________", //
		        "_____________________", //
		        "_____________________", //
		        "_____________________", //
		        "_____xxx___xxx_______", //
		        "_____________________", //
		        "___x____x_x____x_____", //
		        "___x____x_x____x_____", //
		        "___x____x_x____x_____", //
		        "_____xxx___xxx_______", //
		        "_____________________", //
		        "_____xxx___xxx_______", //
		        "___x____x_x____x_____", //
		        "___x____x_x____x_____", //
		        "___x____x_x____x_____", //
		        "_____________________", //
		        "_____xxx___xxx_______", //
		        "_____________________", //
		        "_____________________", //
		        "_____________________", //
		};
        // Uncomment to use sample grids
        //Landscape scape = new Landscape(pulsar);
        Landscape scape = new Landscape(100, 100, 0);
        // fixed size window, 600 is the size of the longer length/width of the window
        LandscapeDisplay display = new LandscapeDisplay(scape, 600 / Math.max(scape.getRows(),scape.getCols()));


		// Uncomment below when advance() has been finished
		int whenToAdvance = 0;
		while (true) {
			// Refresh delay in ms, refresh_rate = 1000 ms/refresh_delay (times/ 1s)
			Thread.sleep(20);
			// whenToAdvance % N means  the advance delay is N times the refresh delay
			// or                       the advance rate is 1/N times the refresh rate
			if (whenToAdvance++ % 2 == 0) {
				scape.advance();
			}
			display.repaint();
			// Uncomment when saving photos
			// display.saveImage("frame_" + whenToAdvance + ".jpg");
		}
	}
}
