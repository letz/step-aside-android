package pt.ist.stepaside;

import java.util.Random;

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

	public static StepAsideControlUnit getInstance(){
		if(instance == null)
			instance = new StepAsideControlUnit(StepAsideApp.getContext());
		return instance;
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

	}
	
	public Message sendMessage(){
	    Location location = mLocManager.getBestLocation();
	    //random message id
	    int min = 1;
	    int max = 10;
	    Random rand = new Random();
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;    
		Message toSend = new Message(randomNum, location);
		mWDCU.sendMessage(toSend);
		//Return message just for debug
		return toSend;
	}

}
