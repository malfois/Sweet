package ncd.utils.colour;

import javafx.scene.paint.Color;

/**
 * Create the palette for 1D plotting
 *
 * @author M. Malfois
 *
 */
public class Palette1D {

	private static volatile Palette1D	INSTANCE;

	private Color[]						colours	= new Color[] {
			Color.RED,
			Color.BLUE,
			Color.GREEN,
			Color.BLACK,
			Color.MAGENTA,
			Color.CYAN,
			Color.YELLOW,
			Color.GRAY,
			Color.ORANGE,
			Color.SEAGREEN,
			Color.VIOLET,
			Color.BROWN,
			Color.OLIVE,
			Color.PINK,
			Color.INDIGO };

	private static int					nCalled	= 0;

	private Palette1D() {
	}

	public Color getNextColor() {
		int nColours = this.colours.length;
		int i = nCalled % nColours;
		nCalled++;
		return this.colours[i];
	}

	public void reset() {
		nCalled = 0;
	}

	public static String toText(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}

	public static Color toColor(String color) {
		return Color.web(color);
	}

	// generate an instance
	private static synchronized Palette1D tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Palette1D();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 *
	 * @return the instance of this class
	 */
	public static Palette1D getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		Palette1D s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

}
