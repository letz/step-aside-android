package pt.ist.stepaside.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MLocationListener implements LocationListener {
	public static final String TAG = MLocationListener.class.getName();
	public static final int SIGNIFICANT_TIME_GAP = 2 * 60 * 1000;
	public static final boolean DEBUG_MODE = false;

	MLocationManager mLocationManager;
	Context context;

	public MLocationListener(MLocationManager mLocationManager, Context context) {
		this.context = context;
		this.mLocationManager = mLocationManager;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (isBetterLocation(location, mLocationManager.getBestLocation()))
			mLocationManager.setBestLocation(location);
		if (!DEBUG_MODE) { return; }
		Log.w(TAG, "Location changed to " + Utils.locationToString(location));
		Toast.makeText(context, location.getProvider() + " changed to " + Utils.locationToString(location), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		if (provider == LocationManager.GPS_PROVIDER)
			mLocationManager.stopGpsLocationUpdates();
		if (!DEBUG_MODE) { return; }
		if (provider == LocationManager.GPS_PROVIDER) {
			Toast.makeText(context, "GPS disabled", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "GPS disabled");
		} else if (provider == LocationManager.NETWORK_PROVIDER) {
			Toast.makeText(context, "Network disabled", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Network disabled");
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		if (provider == LocationManager.GPS_PROVIDER) {
			mLocationManager.startGpsLocationUpdates();
		} else if (provider == LocationManager.NETWORK_PROVIDER) {
			mLocationManager.startNetworkLocationUpdates();
		}
		if (!DEBUG_MODE) { return; }
		if (provider == LocationManager.GPS_PROVIDER) {
			Toast.makeText(context, "GPS disabled", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "GPS enabled");
		} else if (provider == LocationManager.NETWORK_PROVIDER) {
			Toast.makeText(context, "Network disabled", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Network enabled");
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (!DEBUG_MODE) { return; }
		if (provider == LocationManager.GPS_PROVIDER) {
			Toast.makeText(context, "GPS status changed", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "GPS status changed");
		} else if (provider == LocationManager.NETWORK_PROVIDER) {
			Toast.makeText(context, "Network status changed", Toast.LENGTH_SHORT).show();
			Log.w(TAG, "Network status changed");
		}
	}

	/** Determines whether one Location reading is better than the current Location fix
	 * @param location  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	 */
	public static boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > SIGNIFICANT_TIME_GAP;
		boolean isSignificantlyOlder = timeDelta < -SIGNIFICANT_TIME_GAP;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = MLocationListener.isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
