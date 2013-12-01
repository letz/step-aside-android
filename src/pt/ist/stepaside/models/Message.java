package pt.ist.stepaside.models;

import android.location.Location;

public class Message {

	private int mId;
	private Location mLocation;

	public Message(int id, Location location){
		mId = id;
		mLocation = location;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(Location location) {
		this.mLocation = location;
	}

	public String getStringCoordinates(){
		return mLocation.getLatitude() + " " + mLocation.getLongitude();
	}

	@Override
	public String toString(){
		return mId + " - " + mLocation.getLatitude() + " " + mLocation.getLongitude();
	}


}
