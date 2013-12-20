package pt.ist.stepaside.models;

public class BaseMessage {

	private String mDefaultmsg;

	public BaseMessage(String msg) {
		setDefaultmsg(msg);
	}

	public String getDefaultmsg() {
		return mDefaultmsg;
	}

	public void setDefaultmsg(String mDefaultmsg) {
		this.mDefaultmsg = mDefaultmsg;
	}

	@Override
	public String toString() {
		return mDefaultmsg;
	}

}
