package com.bourntec.mailpoller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * <h1> All Date related common methods are available here. </h1>
 * 
 * @author gopal
 *
 */
public class DateUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MMM-dd HH:mm:ss z";
	public static final String DATE_FORMAT_LONG = "EE, MMMM dd, yyyy zzzz";
	public static final String DATE_FORMAT_MEDIUM = "E, MM dd, yyyy z";
	public static final String DATE_FORMAT_SHORT = "dd-MM-yyyy";
	public static final String TIME_FORMAT_LONG = "HH:mm:ss:SSS a zzzz";
	public static final String TIME_FORMAT_MEDIUM = "HH:mm:ss:sss a z";
	public static final String TIME_FORMAT_SHORT = "HH:mm:ss a z";

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "yyyy-MMM-dd HH:mm:ss z" 
	 */
	public static SimpleDateFormat getDateTimeFormat() {
		return new SimpleDateFormat(DATE_TIME_FORMAT);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "EE, MMMM dd, yyyy zzzz" 
	 */
	public static SimpleDateFormat getDateFormatLong() {
		return new SimpleDateFormat(DATE_FORMAT_LONG);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "E, MM dd, yyyy z" 
	 */
	public static SimpleDateFormat getDateFormatMedium() {
		return new SimpleDateFormat(DATE_FORMAT_MEDIUM);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "dd-MM-yyyy" 
	 */
	public static SimpleDateFormat getDateFormatShort() {
		return new SimpleDateFormat(DATE_FORMAT_SHORT);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "HH:mm:ss:SSS a zzzz" 
	 */
	public static SimpleDateFormat getTimeFormatLong() {
		return new SimpleDateFormat(TIME_FORMAT_LONG);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "HH:mm:ss:sss a z" 
	 */
	public static SimpleDateFormat getTimeFormatMedium() {
		return new SimpleDateFormat(TIME_FORMAT_MEDIUM);
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return SimpleDateFormat => "HH:mm:ss a z" 
	 */
	public static SimpleDateFormat getTimeFormatShort() {
		return new SimpleDateFormat(TIME_FORMAT_SHORT);
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param date: Date
	 * @return String
	 * @description - Returns formatted string for given date 
	 */
	public static String getFormattedDate(Date date) {
		return getDateTimeFormat().format(date);
	}
	
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param date: Date
	 * @param simpleDateFormat: SimpleDateFormat
	 * @return String
	 * @description - Returns formatted string for given date 
	 */
	public static String getFormattedDate(Date date, SimpleDateFormat simpleDateFormat) {
		return simpleDateFormat.format(date);
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param startDate: Date
	 * @param endDate: Date 
	 * @param simpleDateFormat: SimpleDateFormat
	 * @return String
	 * @description - Returns date difference in the format "A days, B hours, C minutes, D seconds"
	 */
	public static String calculateDifferenceOfDates(Date startDate, Date endDate) {
		long diff = endDate.getTime() - startDate.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays + "d " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s ";
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Date Time in format "yyyy-MMM-dd HH:mm:ss z" 
	 */
	public static String getCurrentDateTime(){
		return getDateTimeFormat().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Date in format "EE, MMMM dd, yyyy zzzz" 
	 */
	public static String getCurrentDateLong(){
		return getDateFormatLong().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Date in format "E, MM dd, yyyy z" 
	 */
	public static String getCurrentDateMedium(){
		return getDateFormatMedium().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Date in format "dd-MM-yyyy" 
	 */
	public static String getCurrentDateShort(){
		return getDateFormatShort().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Time in format "HH:mm:ss:SSS a zzzz" 
	 */
	public static String getCurrentTimeLong(){
		return getTimeFormatLong().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Time in format "HH:mm:ss:sss a z" 
	 */
	public static String getCurrentTimeMedium(){
		return getTimeFormatMedium().format(new Date());
	}
	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @return Date => Time in format "HH:mm:ss a z" 
	 */
	public static String getCurrentTimeShort(){
		return getTimeFormatShort().format(new Date());
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param dateTime1: Date
	 * @param dateTime2: Date 
	 * @param acceptedErrorOffset: Int - Accepted error offset in minutes (Ex. 5 mins)
	 * @return boolean
	 * @description Checks if dates are equal for provided error offset
	 */
	public boolean compareDateTime(Date dateTime1, Date dateTime2, int acceptedErrorOffset) {
		long diff = dateTime1.getTime() - dateTime2.getTime();
		diff = Math.abs(diff);
		long diffMinutes = diff / (60 * 1000) % 60;
		return diffMinutes <= acceptedErrorOffset;
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param dateString: String
	 * @param originalDateFormat: SimpleDateFormat - Date Format
	 * @param newDateFormat: SimpleDateFormat
	 * @return String
	 * @description Converts Current Date String to Other Format
	 * @throws ParseException
	 */
	public String parseDateToNewFormat(String dateString, SimpleDateFormat originalDateFormat, SimpleDateFormat newDateFormat) throws ParseException {
		return newDateFormat.format(originalDateFormat.parse(dateString));
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param dateTimeString: String
	 * @return Date
	 * @description Converts Date String to date
	 * @throws ParseException
	 */
	public static Date getParsedDate(String dateTimeString) throws ParseException {
		return getDateTimeFormat().parse(dateTimeString);
	}

	/**
	 * @author gopal
	 * @since 27-07-2020
	 * @param dateTimeString: String
	 * @return Date
	 * @description Converts Date String to date
	 * @throws ParseException
	 */
	public static Date getParsedDate(String dateTimeString, SimpleDateFormat simpleDateFormat) throws ParseException {
		return simpleDateFormat.parse(dateTimeString);
	}

}
