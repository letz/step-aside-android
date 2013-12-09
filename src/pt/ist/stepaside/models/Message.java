package pt.ist.stepaside.models;

import java.util.Date;

import android.location.Location;

public class Message {

	private int mId;
	private Location mLocation;
	private Date mTime;
	private int mSenderID;

	public Message(int senderID, int msgID, Location location, Date time){
		setId(msgID);
		setSenderID(senderID);
		setTime(time);
		setLocation(location);
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

	public String getStringCoordinates() {
		return mLocation.getLatitude() + " " + mLocation.getLongitude();
	}

	@Override
	public String toString() {
		return mId + " - " + mLocation.getLatitude() + " " + mLocation.getLongitude();
	}

	public int getSenderID() {
		return mSenderID;
	}

	public void setSenderID(int mSenderID) {
		this.mSenderID = mSenderID;
	}

	public Date getTime() {
		return mTime;
	}

	public void setTime(Date mTime) {
		this.mTime = mTime;
	}


}
