package ncd.utils.colour;

import java.awt.Color;

public class Grey extends Palette2D {

	private static volatile Grey INSTANCE;

	private Grey() {
		this.setName(Palette2D.GREY);
	}

	// generate an instance
	private static synchronized Grey tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Grey();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 * 
	 * @return the instance of this class
	 */
	public static Grey getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		Grey s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

	@Override
	public int color(int colourIntensity) {
		int pixel = (new Color((int) (255 - colourIntensity), (int) (255 - colourIntensity), (int) (255 - colourIntensity))).getRGB();
		return pixel;
	}

}
