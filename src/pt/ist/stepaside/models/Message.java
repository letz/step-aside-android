package pt.ist.stepaside.models;

import java.util.Date;

import android.location.Location;

public class Message {

	private int mId;
	private double mDistance;
	private Date mTime;
	private int mSenderID;
	private double mVelocity;
	private int mAxis;

	public Message(int msgID) {
		setId(msgID);
	}
	public Message(int msgID, Date time, double distance, double velocity, int axis){
		setId(msgID);
		setTime(time);
		setVelocity(velocity);
		setDistance(distance);
		setAxis(axis);
	}

	@Override
	public String toString() {
		return "Message [mId=" + mId + ", mDistance=" + mDistance + ", mTime="
				+ mTime + ", mSenderID=" + mSenderID + ", mVelocity="
				+ mVelocity + ", mAxis=" + mAxis + "]";
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public double getDistance() {
		return mDistance;
	}

	public String getStringDistance() {
		return getDistance()+"";
	}
	
	public void setDistance(double distance) {
		this.mDistance = distance;
	}

	public Date getTime() {
		return mTime;
	}

	public void setTime(Date mTime) {
		this.mTime = mTime;
	}

	public double getVelocity() {
		return mVelocity;
	}

	public void setVelocity(double d) {
		this.mVelocity = d;
	}

	public int getAxis() {
		return mAxis;
	}

	public void setAxis(int Axis) {
		this.mAxis = Axis;
	}


}
