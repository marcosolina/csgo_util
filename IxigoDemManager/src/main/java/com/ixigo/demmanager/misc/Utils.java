package com.ixigo.demmanager.misc;

import java.io.File;
import java.time.LocalDateTime;

import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.utils.DateUtils;

public class Utils {
	public static String getMapNameFromFile(File f, boolean isCs2DemFile) {
		String[] tmp = f.getName().split("-");
		String mapName = isCs2DemFile ? tmp[3] : tmp[4];
		return mapName;
	}
	
	public static LocalDateTime getDateTimeFromFileName(File f) {
		String[] tmp = f.getName().split("-");
		String date = tmp[1];
		String time = String.format("%-6s", tmp[2]).replace(' ', '0');

		LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(date + "_" + time, DateFormats.FILE_NAME);
		return ldt;
	}
}
