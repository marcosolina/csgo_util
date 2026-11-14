package com.ixigo.library.utils;

import java.io.File;
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
	
	/**
	 * It extract the date time from the DEM file name
	 * @param f
	 * @return
	 */
	public static LocalDateTime fromDemFileNameToLocalDateTime(File f) {
		String[] tmp = f.getName().split("-");
		String date = tmp[1];
		String time = String.format("%-6s", tmp[2]).replace(' ', '0');

		LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(date + "_" + time, DateFormats.FILE_NAME);
		return ldt;
	}
}
