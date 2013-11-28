package pt.ist.stepaside.utils;

import pt.ist.stepaside.StepAsideApp;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Manages 4 providers :
 * 	- GPS Provider
 *  - Network Provider
 *  - Passive Provider
 *  - GMS Provide
 *
 * @author letz
 *
 * @see MLocationListener
 * @see LocationClient
 * @see LocationManager
 */
public class MLocationManager {

	public static final String TAG = MLocationManager.class.getName();

	public static final int MIN_INTERVAL = 5; // seconds
	public static final int MIN_DISTANCE = 10; // meters
	public static final boolean DEBUG_MODE = false;

	private static MLocationManager instance;

	private Context context;
	private LocationManager locationManager;
	private LocationListener gpsLocationListener,networkLocationListener, passiveLocationListener;
	private Location bestLocation;

	private MLocationManager(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		this.context = context;


		Location networkLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Location gpsLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location passiveLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

		if(gpsLastKnownLocation != null && MLocationListener.isBetterLocation(gpsLastKnownLocation, bestLocation))
			bestLocation = gpsLastKnownLocation;
		if(networkLastKnownLocation != null && MLocationListener.isBetterLocation(networkLastKnownLocation, bestLocation))
			bestLocation = networkLastKnownLocation;
		if(passiveLastKnownLocation != null && MLocationListener.isBetterLocation(passiveLastKnownLocation, bestLocation))
			bestLocation = passiveLastKnownLocation;
		if(bestLocation == null){
			bestLocation = new Location("location init");
			bestLocation.setAccuracy(300000);
			bestLocation.setTime(0);
		}

		gpsLocationListener = new MLocationListener(this, context);
		networkLocationListener = new MLocationListener(this, context);
		passiveLocationListener = new MLocationListener(this, context);
	}

	public static MLocationManager getInstance() {
		if (instance == null)
			instance = new MLocationManager(StepAsideApp.getContext());
		return instance;
	}

	/**
	 * Start listening all providers
	 */
	public void startLocationUpdates() {
		// only request for gps updates if the GPS is enabled
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			startGpsLocationUpdates();
		startNetworkLocationUpdates();
		startPassiveLocationUpdates();
	}

	/**
	 * Stop listening all providers
	 */
	public void stopLocationUpdates() {
		stopNetworkLocationUpdates();
		stopGpsLocationUpdates();
		stopPassiveLocationUpdates();
	}

	/**
	 * Start listening GPS provider
	 */
	public void startGpsLocationUpdates() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, gpsLocationListener);
	}

	/**
	 * Stop listening GPS provider
	 */
	public void stopGpsLocationUpdates() {
		locationManager.removeUpdates(gpsLocationListener);
	}

	/**
	 * Start listening Network provider
	 */
	public void startNetworkLocationUpdates() {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, networkLocationListener);
	}

	/**
	 * Stop listening Network provider
	 */
	public void stopNetworkLocationUpdates() {
		locationManager.removeUpdates(networkLocationListener);
	}

	/**
	 * Start listening Passive provider
	 */
	public void startPassiveLocationUpdates(){
		locationManager.removeUpdates(passiveLocationListener);
	}

	/**
	 * Stop listening Passive provider
	 */
	public void stopPassiveLocationUpdates(){
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_INTERVAL, MIN_DISTANCE, passiveLocationListener);
	}

	/**
	 * Give the best possible location between the enabled providers
	 * @see MLocationListener.isBetterLocation(newLocation, currentLocation)
	 * @return best location possible
	 */
	public Location getBestLocation() {
		return bestLocation;
	}

	/**
	 * Set current best location
	 * @param location
	 */
	public void setBestLocation(Location location) {
		this.bestLocation = location;
		if (!DEBUG_MODE) { return; }
		Log.w(TAG, "Set location to " + Utils.locationToString(location));
		Toast.makeText(context, "Set location to: " + Utils.locationToString(location) + " from "+ location.getProvider(), Toast.LENGTH_SHORT).show();
	}
}
