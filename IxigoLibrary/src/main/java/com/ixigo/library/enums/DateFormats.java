package com.ixigo.library.enums;

public enum DateFormats {
	/**
	 * yyyyMMdd
	 */
	FOLDER_NAME("yyyyMMdd"),
	/**
	 * yyyyMMdd_HHmmss
	 */
	FILE_NAME("yyyyMMdd_HHmmss"),
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	DB_TIME_STAMP("yyyy-MM-dd'T'HH:mm:ss"),
	/**
	 * yyyy-MM-dd
	 */
	DB_DATE("yyyy-MM-dd");

	private final String format;

	private DateFormats(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
}