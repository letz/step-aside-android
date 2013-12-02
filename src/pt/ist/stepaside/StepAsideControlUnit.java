package pt.ist.stepaside;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import pt.ist.stepaside.utils.MLocationManager;
import android.content.Context;
import android.location.Location;
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
	private MLocationManager mLocManager;
	private Context mContext;
	private MessageReceivedListener mListener;


	public static StepAsideControlUnit getInstance(){
		if(instance == null)
			instance = new StepAsideControlUnit(StepAsideApp.getContext());
		return instance;
	}
	public void setMessageListener(MessageReceivedListener listener){
		mListener = listener;
	}
	private StepAsideControlUnit(Context context) {
		mContext = context;
		mWDCU = new WifiDirectControlUnit(mContext);
		mWDCU.setMessageListener(this);
		mLocManager = MLocationManager.getInstance();
	}


	@Override
	public void onMessageReceived(Message response) {
		Log.v(TAG, "Message Received");
		Log.v(TAG, response.toString());
		mListener.onMessageReceived(response);

	}
	
	public void startListening(){
		mWDCU.receiveMessages();
	}
	public Message sendMessage(int id){
	    Location location = mLocManager.getBestLocation();
		Message toSend = new Message(id, location);
		mWDCU.sendMessage(toSend);
		//Return message just for debug
		return toSend;
	}

}
