package ncd.utils.colour;

public abstract class Palette2D {

	public static final String GREY = "Grey" ;
	public static final String INVERTED_GREY = "Inverted Grey" ;
	public static final String NCD_COLOURS = "NCD Colours" ;

	private String name = NCD_COLOURS ;
	
	public abstract int color(int colourIntensity) ;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * return the list of defined colour (NCD , Grey, Inverted Grey_) ;
	 * @return list of defined colours
	 */
		public static Palette2D[] LIST = { 
				NcdColour.getInstance() ,
				Grey.getInstance() ,
				InvertedGrey.getInstance()
		} ;

	public static Palette2D Factory(String name) {
		Palette2D colour = NcdColour.getInstance() ;
		if (name.equalsIgnoreCase(GREY) ) {
			colour = Grey.getInstance() ;			
		}
		if (name.equalsIgnoreCase(INVERTED_GREY) ) {
			colour = InvertedGrey.getInstance() ;			
		}
		return colour ;
	}
	
	public String toString() {
		return this.name ;
	}
}
