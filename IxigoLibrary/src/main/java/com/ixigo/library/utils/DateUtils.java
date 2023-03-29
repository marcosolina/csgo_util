package com.ixigo.library.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.ixigo.library.enums.DateFormats;

public class DateUtils {
	private DateUtils() {}
	
	public static LocalDateTime getCurrentUtcDateTime() {
		return LocalDateTime.now(ZoneOffset.UTC);
	}
	
	public static LocalDate getCurrentUtcDate() {
		return LocalDate.now(ZoneOffset.UTC);
	}
	
	/**
	 * It parse the string into a LocalDate object
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static LocalDate fromStringToLocalDate(String value, DateFormats format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getFormat());
		return LocalDate.parse(value, formatter);
	}

	/**
	 * It converts the LocalDate into a String
	 * 
	 * @param localDate
	 * @param format
	 * @return
	 */
	public static String fromLocalDateToString(LocalDate localDate, DateFormats format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getFormat());
		return localDate.format(formatter);
	}
	
	/**
	 * It parses a string into a local date time
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static LocalDateTime fromStringToLocalDateTime(String value, DateFormats format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getFormat());
		return LocalDateTime.parse(value, formatter);
	}

	/**
	 * It converts a local date time into a string
	 * 
	 * @param localDateTime
	 * @param format
	 * @return
	 */
	public static String fromLocalDateTimeToString(LocalDateTime localDateTime, DateFormats format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getFormat());
		return localDateTime.format(formatter);
	}
}
