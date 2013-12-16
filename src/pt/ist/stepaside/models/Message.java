package pt.ist.stepaside.models;

import java.util.Calendar;
import java.util.Date;

import pt.ist.stepaside.StepAsideControlUnit;
import pt.ist.stepaside.utils.MLocationManager;
import pt.ist.stepaside.utils.Utils;

public class Message {

	private int mId;
	private double mDistance;
	private Date mTime;
	private int mSenderID;
	private double mVelocity;
	private int mAxis;

	private boolean mIsRetransmit;

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

	public String toStats() {
		return ((mTime.getTime() - Calendar.getInstance().getTimeInMillis()) / 1000 ) + " " + (mDistance - Utils.distanceTo(MLocationManager.getInstance().getBestLocation(), StepAsideControlUnit.RSU_LOC));
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
	public boolean isRetransmit() {
		return mIsRetransmit;
	}
	public void setIsRetransmit(boolean mIsRetransmit) {
		this.mIsRetransmit = mIsRetransmit;
	}


}
