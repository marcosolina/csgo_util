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
	DB_DATE("yyyy-MM-dd"),
	/**
	 * yyyyMMdd
	 */
	FILE_NAME_JUST_DATE("yyyyMMdd"),
	/**
	 * yyyyMMdd HHmmss
	 */
	FILE_NAME_WITH_SPACE("yyyyMMdd HHmmss")
	;

	private final String format;

	private DateFormats(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
}
