/**
 * 
 */
package com.bourntec.mailpoller.utils;

/**
 * @author gopal
 * @since 2020-07-23 09:20
 * @version 1.3
 *
 */
public class PrintUtil {
	/**
	 * <h1> ANSI Color Constants </h1>
	 * <p> Constant values for all possible ANSI color codes that can be used in print stream. </p>
	 * 
	 * @author gopal
	 */
	enum ColorCodes {
		//Color end string, color reset
		RESET("\033[0m"),
		INVERSE("\u001b[7m"),
		NONE(""),

		// Regular Colors. Normal color, no bold, background color etc.
		BLACK("\033[0;30m"),    // BLACK
		RED("\033[0;31m"),      // RED
		GREEN("\033[0;32m"),    // GREEN
		YELLOW("\033[0;33m"),   // YELLOW
		BLUE("\033[0;34m"),     // BLUE
		MAGENTA("\033[0;35m"),  // MAGENTA
		CYAN("\033[0;36m"),     // CYAN
		WHITE("\033[0;37m"),    // WHITE

		// Bold
		BLACK_BOLD("\033[1;30m"),   // BLACK
		RED_BOLD("\033[1;31m"),     // RED
		GREEN_BOLD("\033[1;32m"),   // GREEN
		YELLOW_BOLD("\033[1;33m"),  // YELLOW
		BLUE_BOLD("\033[1;34m"),    // BLUE
		MAGENTA_BOLD("\033[1;35m"), // MAGENTA
		CYAN_BOLD("\033[1;36m"),    // CYAN
		WHITE_BOLD("\033[1;37m"),   // WHITE

		// Underline
		BLACK_UNDERLINED("\033[4;30m"),     // BLACK
		RED_UNDERLINED("\033[4;31m"),       // RED
		GREEN_UNDERLINED("\033[4;32m"),     // GREEN
		YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
		BLUE_UNDERLINED("\033[4;34m"),      // BLUE
		MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
		CYAN_UNDERLINED("\033[4;36m"),      // CYAN
		WHITE_UNDERLINED("\033[4;37m"),     // WHITE

		// Background
		BLACK_BACKGROUND("\033[40m"),   // BLACK
		RED_BACKGROUND("\033[41m"),     // RED
		GREEN_BACKGROUND("\033[42m"),   // GREEN
		YELLOW_BACKGROUND("\033[43m"),  // YELLOW
		BLUE_BACKGROUND("\033[44m"),    // BLUE
		MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
		CYAN_BACKGROUND("\033[46m"),    // CYAN
		WHITE_BACKGROUND("\033[47m"),   // WHITE

		// High Intensity
		BLACK_BRIGHT("\033[0;90m"),     // BLACK
		RED_BRIGHT("\033[0;91m"),       // RED
		GREEN_BRIGHT("\033[0;92m"),     // GREEN
		YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
		BLUE_BRIGHT("\033[0;94m"),      // BLUE
		MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
		CYAN_BRIGHT("\033[0;96m"),      // CYAN
		WHITE_BRIGHT("\033[0;97m"),     // WHITE

		// Bold High Intensity
		BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
		RED_BOLD_BRIGHT("\033[1;91m"),      // RED
		GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
		YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
		BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
		MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
		CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
		WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE

		// High Intensity backgrounds
		BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
		RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
		GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
		YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
		BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
		MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
		CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
		WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE

		private final String code;

		ColorCodes(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return code;
		}


	}

	/**
	 * <h1> Available Colors in ANSI </h1> 
	 * <p> Basic Color constants that can be used to fetch color code depending on the requirement. </p>
	 * 
	 * @author gopal
	 */
	public enum Colors { 
		BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, RESET, NONE
	}

	/**
	 * Static variables to get formatting options
	 */
	private static Colors textColor = Colors.BLACK;
	private static Colors backgroundColor = Colors.NONE;
	private static boolean isBold = false;
	private static boolean isUnderlined = false;
	private static boolean isBrightText = false;
	private static boolean isBrightBackground = false;
	private static boolean printLineNumbers = true;
	private static ColorCodes textColorCode;
	private static ColorCodes backgroundColorCode;
	private static ColorCodes linkColorCode = ColorCodes.CYAN;

	/**
	 * <h1> Formatting options Provider </h1>
	 * <p> Method to set formatting options to provided by user </p> 
	 * 
	 * @param _textColor - Text color of the console output - [Defaults to AUTO]
	 * @param _backgroundColor - Background color of the console output [Defaults to AUTO]
	 * @param _isBold - Whether Console out text to be formatted using bold font [Defaults to false]
	 * @param _isUnderlined - Whether Console out text to be formatted with underline [Defaults to false]
	 * @param _isBrightText - Whether Console out text to be formatted using bright colors [Defaults to false]
	 * @param _isBrightBackground - Whether Console out text to be formatted using bright background color [Defaults to false]
	 * @param _printLineNumbers - Whether line number to be printed [Defaults to true]
	 * 
	 * @since 2019-08-16 09:20
	 * @version 1.3 
	 * @author gopal
	 * 
	 */
	public static void setProperties(
			Colors _textColor, Colors _backgroundColor, boolean _isBold, boolean _isUnderlined, boolean _isBrightText, boolean _isBrightBackground, boolean _printLineNumbers) {
		textColor = _textColor;
		backgroundColor = _backgroundColor;
		isBold = _isBold;
		isUnderlined = _isUnderlined;
		isBrightText = _isBrightText;
		isBrightBackground = _isBrightBackground;
		printLineNumbers = _printLineNumbers;
		backgroundColorCode = getBackgroundColorCode();
		textColorCode = getTextColorCode();
	}


	/**
	 * Used to fetch ANSI code for Bright Background Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */
	private static ColorCodes getBrightBackgroundColor(){
		switch (backgroundColor) {
		case BLACK:
			return ColorCodes.BLACK_BACKGROUND_BRIGHT;
		case RED:
			return ColorCodes.RED_BACKGROUND_BRIGHT;
		case GREEN:
			return ColorCodes.GREEN_BACKGROUND_BRIGHT;
		case YELLOW:
			return ColorCodes.YELLOW_BACKGROUND_BRIGHT;
		case BLUE:
			return ColorCodes.BLUE_BACKGROUND_BRIGHT;
		case MAGENTA:
			return ColorCodes.MAGENTA_BACKGROUND_BRIGHT;
		case CYAN:
			return ColorCodes.CYAN_BACKGROUND_BRIGHT;
		case WHITE:
			return ColorCodes.WHITE_BACKGROUND_BRIGHT;
		default:
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch ANSI code for Normal Background Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getNoramlBackgroundColor(){
		switch (backgroundColor) {
		case BLACK:
			return ColorCodes.BLACK_BACKGROUND;
		case RED:
			return ColorCodes.RED_BACKGROUND;
		case GREEN:
			return ColorCodes.GREEN_BACKGROUND;
		case YELLOW:
			return ColorCodes.YELLOW_BACKGROUND;
		case BLUE:
			return ColorCodes.BLUE_BACKGROUND;
		case MAGENTA:
			return ColorCodes.MAGENTA_BACKGROUND;
		case CYAN:
			return ColorCodes.CYAN_BACKGROUND;
		case WHITE:
			return ColorCodes.WHITE_BACKGROUND;
		default:
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch ANSI code for Bright and Bold Text Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getBrightBoldTextColor() {
		switch (textColor) {
		case BLACK:
			return ColorCodes.BLACK_BOLD_BRIGHT;
		case RED:
			return ColorCodes.RED_BOLD_BRIGHT;
		case GREEN:
			return ColorCodes.GREEN_BOLD_BRIGHT;
		case YELLOW:
			return ColorCodes.YELLOW_BOLD_BRIGHT;
		case BLUE:
			return ColorCodes.BLUE_BOLD_BRIGHT;
		case MAGENTA:
			return ColorCodes.MAGENTA_BOLD_BRIGHT;
		case CYAN:
			return ColorCodes.CYAN_BOLD_BRIGHT;
		case WHITE:
			return ColorCodes.WHITE_BOLD_BRIGHT;
		default:
			return ColorCodes.NONE;
		}
	}
	/**
	 * Used to fetch ANSI code for Bright Text Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getBrightTextColor( ) {
		switch (textColor) {
		case BLACK:
			return ColorCodes.BLACK_BRIGHT;
		case RED:
			return ColorCodes.RED_BRIGHT;
		case GREEN:
			return ColorCodes.GREEN_BRIGHT;
		case YELLOW:
			return ColorCodes.YELLOW_BRIGHT;
		case BLUE:
			return ColorCodes.BLUE_BRIGHT;
		case MAGENTA:
			return ColorCodes.MAGENTA_BRIGHT;
		case CYAN:
			return ColorCodes.CYAN_BRIGHT;
		case WHITE:
			return ColorCodes.WHITE_BRIGHT;
		default:
			return ColorCodes.NONE;
		}
	}


	/**
	 * Used to fetch ANSI code for Underlined Text Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getNormalUnderlinedTextColor() {
		switch (textColor) {
		case BLACK:
			return ColorCodes.BLACK_UNDERLINED;
		case RED:
			return ColorCodes.RED_UNDERLINED;
		case GREEN:
			return ColorCodes.GREEN_UNDERLINED;
		case YELLOW:
			return ColorCodes.YELLOW_UNDERLINED;
		case BLUE:
			return ColorCodes.BLUE_UNDERLINED;
		case MAGENTA:
			return ColorCodes.MAGENTA_UNDERLINED;
		case CYAN:
			return ColorCodes.CYAN_UNDERLINED;
		case WHITE:
			return ColorCodes.WHITE_UNDERLINED;
		default:
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch ANSI code for Normal Bold Text Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getNormalBoldTextColor() {
		switch (textColor) {
		case BLACK:
			return ColorCodes.BLACK_BOLD;
		case RED:
			return ColorCodes.RED_BOLD;
		case GREEN:
			return ColorCodes.GREEN_BOLD;
		case YELLOW:
			return ColorCodes.YELLOW_BOLD;
		case BLUE:
			return ColorCodes.BLUE_BOLD;
		case MAGENTA:
			return ColorCodes.MAGENTA_BOLD;
		case CYAN:
			return ColorCodes.CYAN_BOLD;
		case WHITE:
			return ColorCodes.WHITE_BOLD;
		default:
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch ANSI code for Text Color depending on user's color choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getNormalTextColor() {
		switch (textColor) {
		case BLACK:
			return ColorCodes.BLACK;
		case RED:
			return ColorCodes.RED;
		case GREEN:
			return ColorCodes.GREEN;
		case YELLOW:
			return ColorCodes.YELLOW;
		case BLUE:
			return ColorCodes.BLUE;
		case MAGENTA:
			return ColorCodes.MAGENTA;
		case CYAN:
			return ColorCodes.CYAN;
		case WHITE:
			return ColorCodes.WHITE;
		default:
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch and set ANSI code for Background formatting based on user's choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getBackgroundColorCode() {
		if(backgroundColor != null && !backgroundColor.equals(Colors.NONE) && !backgroundColor.equals(Colors.RESET)) { 
			if(isBrightBackground) { 
				return getBrightBackgroundColor();
			} else {
				return getNoramlBackgroundColor();
			}
		} else {
			return ColorCodes.NONE;
		}
	}

	/**
	 * Used to fetch and set ANSI code for Text formatting based on user's choice 
	 * 
	 * @return ColorCodes - Static ANSI Code for Color Formating
	 * 
	 * @author gopal
	 * 
	 */	
	private static ColorCodes getTextColorCode() {
		if(textColor != null && !textColor.equals(Colors.NONE) && !textColor.equals(Colors.RESET)) { 
			if(isBrightText) {
				if(isBold) {
					return getBrightBoldTextColor();
				} else {
					return getBrightTextColor();
				}
			} else {
				if(isBold) {
					return getNormalBoldTextColor();
				} else if(isUnderlined){
					return getNormalUnderlinedTextColor();
				} else {
					return getNormalTextColor();
				}
			}
		} else {
			return ColorCodes.NONE;
		}
	}


	/**
	 * <h1> Console Print Formatter </h1>
	 * <p> Overriding Java Print Stream to print line numbers along with link with custom formatting. </p>
	 * 
	 * @since 2020-07-23 09:20
	 * @version 1.3 
	 * @author gopal
	 * 
	 */	
	public static void 	printCustomizedOutputWithLineNumbers() {
		System.setOut(new java.io.PrintStream(System.out) {
			private StackTraceElement getCallSite() {
				for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
					if (!e.getMethodName().equals("getStackTrace") && !e.getClassName().equals(getClass().getName())) {
						return e; 
					}
				}
				return null;
			}
			@Override
			public void println(String s) {
				println((Object) s);
			}
			@Override
			public void println(Object o) {	
				StackTraceElement e = getCallSite();

				/** 
				 * Get call site and line number
				 */
				String callSite = (e == null) ? "??" : ( String.format("%s (%s:%d) %s", linkColorCode,  e.getFileName(), e.getLineNumber(), ColorCodes.RESET)) ;

				/**
				 *  If line number need to be printed
				 */
				if(printLineNumbers) {
					/**
					 *  If background color is not defined then append empty string
					 */
					if(backgroundColorCode.equals(ColorCodes.NONE) || backgroundColorCode.equals(ColorCodes.RESET)) {
						super.println(textColorCode + "#### [DEV] \t --- \t " + o + "\t --- \t ### \t"+ ColorCodes.RESET + callSite );
					} else {
						super.println(textColorCode +" "+ backgroundColorCode + "#### [DEV] \t --- \t " + o + "\t --- \t ### \t"+ ColorCodes.RESET + callSite);
					} 
				} else {
					if(backgroundColorCode.equals(ColorCodes.NONE) || backgroundColorCode.equals(ColorCodes.RESET)) {
						super.println(textColorCode + "#### [DEV] \t --- \t " + o + "\t --- \t ### \t"+ ColorCodes.RESET);
					} else {
						super.println(textColorCode +" "+ backgroundColorCode + "#### [DEV] \t --- \t " + o + "\t --- \t ### \t"+ ColorCodes.RESET) ;
					} 
				}

			}
		});
	}
}
