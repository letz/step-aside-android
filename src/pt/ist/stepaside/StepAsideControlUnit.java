package pt.ist.stepaside;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import android.content.Context;
import android.util.Log;

/**
 *
 * This class have the responsibility to receive, re-send messages according with the business rules of StepAside
 *
 * @author letz, diogo
 *
 */
public class StepAsideControlUnit implements MessageReceivedListener {

	public static final String TAG = StepAsideControlUnit.class.getName();

	private static StepAsideControlUnit instance;
	private WifiDirectControlUnit mWDCU;
	private Context mContext;

	public static StepAsideControlUnit getInstance(){
		if(instance == null)
			instance = new StepAsideControlUnit(StepAsideApp.getContext());
		return instance;
	}

	private StepAsideControlUnit(Context context) {
		mContext = context;
		mWDCU = new WifiDirectControlUnit(mContext);
		mWDCU.setMessageListener(this);
	}


	@Override
	public void onMessageReceived(Message response) {
		Log.v(TAG, "Message Received");
		Log.v(TAG, response.toString());

	}

}
