package com.bourntec.mailpoller.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.util.Base64Utils;

/**
 * @author gopal
 *
 */
public class GenericUtils {

	enum Environments {
		PRODUCTION, DEVELOPMENT, TESTING
	}
	enum Prefixes {
		DEV("[DEV] \t"),
		TEST("[TEST] \t"),
		PROD("[PROD] \t");

		private final String prefix;

		Prefixes(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public String toString() {
			return prefix;
		}
	}
	private static final Environments ENVIRONMENT = Environments.DEVELOPMENT;
	private static final Prefixes PREFIX = Prefixes.DEV;
	private static final Random random = new Random();

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return void
	 * @description Prints Output to Standard Console in New Line
	 */
	public static void coutLn(Object object) {
		if(!ENVIRONMENT.equals(Environments.PRODUCTION)) {
			System.out.print(PREFIX);
			System.out.println(object);
		}
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return void
	 * @description Prints Output to Standard Console  
	 */
	public static void cout(Object object) {
		if(!ENVIRONMENT.equals(Environments.PRODUCTION)) {
			System.out.print(PREFIX);
			System.out.print(object);
		}
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return void
	 * @description Prints Error Output to Standard Console in New Line
	 */
	public static void cerrLn(Object object) {
		if(!ENVIRONMENT.equals(Environments.PRODUCTION)) {
			System.err.print(PREFIX);
			System.err.println(object);
		}
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return void
	 * @description Prints Error Output to Standard Console
	 */
	public static void cerr(Object object) {
		if(!ENVIRONMENT.equals(Environments.PRODUCTION)) {
			System.err.print(PREFIX);
			System.err.print(object);
		}
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @param length: int
	 * @description Generates and return random string of length
	 */
	public static String getRandomString(int length) {
		String randomString = "";
		char[] word = new char[length]; 
		for(int j = 0; j < word.length; j++)
		{
			word[j] = (char)('a' + random.nextInt(26));
		}
		randomString = new String(word);

		return randomString;
	}


	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @description Generates and return random number as string
	 */
	public static String getRandomNumberString() {
		int number = random.nextInt(999999999);
		return String.format("%12d", number);
	}



	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @param str: String - String with special characters.
	 * @description Removes all special characters and return clean string.
	 */
	public static String removeSpecialChars(String str) {
		return str.replaceAll("[^a-zA-Z0-9](?U)\\s+?", "").replaceAll("\\+", "").replaceAll("^\"|\"$", ""); 
	}


	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @description Capitalize First Letter of String
	 */
	public static String capitalizeFirstLetter(String string) {
		return String.valueOf(string.charAt(0)).toUpperCase().concat(string.toLowerCase().trim().substring(1));
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return boolean
	 * @description Check if String is NULL or Empty
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return boolean
	 * @description Check if String is Not NULL and Not Empty
	 */
	public static boolean isNotNullOrEmpty(String str) {
		return !(str == null || str.trim().isEmpty());
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @param delimitedSting: String - A string separated by delimiter
	 * @param delimiter: String -  Delimiter to be used to split the string (Default delimiter = "," )
	 * @return array - Returns the list of Strings after splitting by delimiter
	 * @description Converts Comma Separated String List To Array
	 */
	public static List<String> splitToList(String delimitedSting, String delimiter) {
		if (isNullOrEmpty(delimiter)) {
			delimiter = ",";
		}
		if (isNullOrEmpty(delimitedSting)) {
			return Collections.emptyList();
		}
		return Arrays.stream(delimitedSting.split(delimiter))
				.map(String::trim)
				.collect(Collectors.toList());
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @param delimitedSting: String - A string separated by delimiter
	 * @param regex: String - Regex to be used to split the string (Default regex = "\\s" )
	 * @param splitParts: int - Split string into (splitParts) * parts by applying regex  (Ex.: str = "boo:bar:foo" splitParts = 2 will yield result as ["boo","bar:foo"] )
	 * @param partIndex: int - index of the array for the string required as result (Ex.: partIndex = 0 will return "bar:foo" as result)
	 * @return array - Returns the required string part after splitting by delimiter
	 * @description Extracts string from delimited string after splitting it into parts  
	 */
	public static String getSplittedString(String delimitedSting, String regex, int splitParts, int partIndex){
		if (isNullOrEmpty(regex)) {
			regex = "\\s";
		}
		if (isNullOrEmpty(delimitedSting)) {
			return "";
		}
		String[] str1 = delimitedSting.trim().split(regex, splitParts);
		return str1[partIndex];
	}


	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @param delimitedSting: String - A string separated by delimiters
	 * @param delimiter1: String -  Delimiter 1 to be used to split the string (Default delimiter = "," )
	 * @param delimiter2: String -  Delimiter 2 to be used to split the string (Default delimiter = "," )
	 * @return String - Returns string between two delimiters
	 * @description Extracts string between two delimiters
	 */
	public String getTextBetweenDelmiter(String delimitedSting, String delimiter1, String delimiter2) {
		if(isNullOrEmpty(delimiter1)) {
			delimiter1 = ",";
		} 
		if(isNullOrEmpty(delimiter2)) {
			delimiter2 = ",";
		}
		if (isNullOrEmpty(delimitedSting)) {
			return "";
		}
		String[] textAfterDelimiter = delimitedSting.split("\\" + delimiter1);
		String[] textBetweenDelimiter = textAfterDelimiter[1].split("\\" + delimiter2);
		return textBetweenDelimiter[0];
	}
	

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @param encodedText: String => Encoded base64 string 
	 * @description Returns decoded base64 string
	 */
	protected String decodeBase64(String encodedString) {
		return new String(Base64Utils.decodeFromString(encodedString));
	}
	
	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return String
	 * @param stringToEncode: String 
	 * @description Returns base64 encoded string
	 */
	protected String encodeBase64(String stringToEncode) {
		return Base64Utils.encodeToString(stringToEncode.getBytes());
	}

	/**
	 * @author gopal
	 * @Since 27-07-2020
	 * @return int
	 * @param min: int => Min Value of Range 
	 * @param max: int => Max Value of Range 
	 * @description Return Random Number Between Range
	 */
	public static int getRandomInRange(int min, int max){
		return (random.nextInt()*((max-min)+1)) + min;
	}



}
