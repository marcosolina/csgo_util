package com.ixigo.demmanager.misc;

import java.io.File;

public class Utils {
	public static String getMapNameFromFile(File f, boolean isCs2DemFile) {
		String[] tmp = f.getName().split("-");
		String mapName = isCs2DemFile ? tmp[3] : tmp[4];
		return mapName;
	}
}
