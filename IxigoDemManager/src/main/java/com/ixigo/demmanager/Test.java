package com.ixigo.demmanager;

import java.lang.reflect.InvocationTargetException;

import com.ixigo.demmanager.enums.PlayerSide;

public class Test {
	public static void main(String[] args) {
		String side = "CT";
		try {
			var m = PlayerSide.class.getMethod("valueOf", String.class);
			var pside = PlayerSide.class.cast(m.invoke(null, side));
			System.out.println(pside);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
