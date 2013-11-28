package pt.ist.stepaside.utils;

import android.location.Location;

public class Utils {


	public static String locationToString(Location l){
		return l.getLatitude() + " " + l.getLongitude();
	}

}
