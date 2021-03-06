package pt.ist.stepaside.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.location.Location;

public class Utils {


	public static String locationToString(Location l){
		return l.getLatitude() + " " + l.getLongitude();
	}

	public static float distanceTo(Location l1, Location l2) {
		int earthRadius = 6371;
		float dLat = (float) Math.toRadians(l2.getLatitude() - l1.getLatitude());
		float dLon = (float) Math.toRadians(l2.getLongitude() - l1.getLongitude());
		float a = (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(l2.getLatitude()))
				* Math.cos(Math.toRadians(l1.getLatitude()))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2));
		float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
		float d = earthRadius * c;
		return d;
	}

	public static void writeToFile(String str, String path) {
		File logFile = new File(path);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,	true));
			buf.append(str);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
