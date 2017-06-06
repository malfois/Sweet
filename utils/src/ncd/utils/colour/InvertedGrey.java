package ncd.utils.colour;

import java.awt.Color;


public class InvertedGrey extends Palette2D {
	
	private static volatile InvertedGrey INSTANCE;
	
	public InvertedGrey() {	
		this.setName(Palette2D.INVERTED_GREY) ;
	}

	// generate an instance
	private static synchronized InvertedGrey tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InvertedGrey();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 * 
	 * @return the instance of this class
	 */
	public static InvertedGrey getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		InvertedGrey s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

	@Override
	public int color(int colourIntensity) {
		int pixel = (new Color(colourIntensity, colourIntensity, colourIntensity)).getRGB();
		return pixel;
	}

}
