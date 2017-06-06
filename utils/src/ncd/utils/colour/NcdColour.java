package ncd.utils.colour;

import java.awt.Color;

public class NcdColour extends Palette2D {
	
	private static volatile NcdColour INSTANCE;
	
	private Color[] gammaII = new Color[256];
	
	private NcdColour() {	
		this.built() ;
		this.setName( Palette2D.NCD_COLOURS);
	}

	@Override
	public int color(int colourIntensity) {
		int pixel = this.gammaII[colourIntensity].getRGB();
		return pixel;
	}

	// generate an instance
	private static synchronized NcdColour tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NcdColour();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 * 
	 * @return the instance of this class
	 */
	public static NcdColour getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		NcdColour s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

	/**
	 * Determines the RGB color for the gammaII palette
	 */
	private void built() {
		int i;
		int[][] colRGB = new int[3][256];
		float a, b;
		 
		// evaluates the value of the red pixel
		for (i = 0; i < 256; i++) { 
			colRGB[0][i] = 0;
			if ((i > 47) && (i < 65)) {
				a = (float) ((81. - 0.) / (64 - 47));
				b = 81 - a * 64;
				colRGB[0][i] = (int) (a * i + b);
			}
			if ((i > 64) && (i < 80)) {
				a = (float) ((79. - 81.) / (79 - 65));
				b = 79 - a * 79;
				colRGB[0][i] = (int) (a * i + b);
			}
			if ((i > 79) && (i < 111)) {
				a = (float) ((255. - 79.) / (110 - 79));
				b = 255 - a * 110;
				colRGB[0][i] = (int) (a * i + b);
			}
			if ((i > 110) && (i < 163)) {
				colRGB[0][i] = (255);
			}
			if ((i > 162) && (i < 175)) {
				a = (float) ((163. - 255.) / (174 - 162));
				b = 163 - a * 174;
				colRGB[0][i] = (int) (a * i + b);
			}
			if ((i > 174) && (i < 193)) {
				a = (float) ((255. - 168.) / (192 - 175));
				b = 255 - a * 192;
				colRGB[0][i] = (int) (a * i + b);
			}
			if ((i > 192) && (i < 256)) {
				colRGB[0][i] = (255);
			}
		}

		// evaluates the value of the green pixel
		for (i = 0; i < 256; i++) { 
			colRGB[1][i] = 0;
			if ((i > 113) && (i < 146)) {
				a = (float) ((163. - 0.) / (145 - 113));
				b = 163 - a * 145;
				colRGB[1][i] = (int) (a * i + b);
			}
			if ((i > 145) && (i < 177)) {
				colRGB[1][i] = (int) (163.);
			}
			if ((i > 176) && (i < 192)) {
				a = (float) ((255. - 163.) / (191 - 176));
				b = 255 - a * 191;
				colRGB[1][i] = (int) (a * i + b);
			}
			if (i > 191) {
				colRGB[1][i] = (255);
			}
		}

		// evaluates the value of the blue pixel
		for (i = 0; i < 256; i++) { 
			colRGB[2][i] = 0;
			if ((i < 50)) {
				a = (float) ((255. - 0.) / (49. - 0.));
				b = 255 - a * 49;
				colRGB[2][i] = (int) (a * i + b);
			}
			if ((i > 49) && (i < 97)) {
				a = (float) ((0. - 255.) / (96. - 49.));
				b = 0 - a * 96;
				colRGB[2][i] = (int) (a * i + b);
			}
			if ((i > 128) && (i < 146)) {
				a = (float) ((82. - 0.) / (145. - 128.));
				b = (float) (82. - a * 145.);
				colRGB[2][i] = (int) (a * i + b);
			}
			if ((i > 145) && (i < 160)) {
				a = (float) ((0. - 82.) / (159. - 145.));
				b = (float) (0. - a * 159.);
				colRGB[2][i] = (int) (a * i + b);
			}
			if (i > 176) {
				a = (float) ((255. - 0.) / (255. - 176.));
				b = (float) (255. - a * 255.);
				colRGB[2][i] = (int) (a * i + b);
			}
		}

		// writes the RGB values of the GAMMAII palette in a 256 elements array.
		for (i = 0; i < 256; i++) {
			this.gammaII[i] = new Color(colRGB[0][i], colRGB[1][i], colRGB[2][i]);
		}

	}

}
