package pt.ist.stepaside;

import java.util.Calendar;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import pt.ist.stepaside.utils.MLocationManager;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
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

	public static int SENDER_ID;

	private int mIntervalR = 2000;
	private int mIntervalS = 5000;
	private Handler mHandlerR;
	private Handler mHandlerS;

	public static StepAsideControlUnit getInstance() {
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
		mHandlerR = new Handler();
		mHandlerS = new Handler();
	}

	public void setSenderID(int id){
		SENDER_ID = id;
	}

	@Override
	public void onMessageReceived(Message response) {
		Log.v(TAG, "Message Received");
		Log.v(TAG, response.toString());
		mListener.onMessageReceived(response);
		//startRepeatingSend(response.getId());
	}

	public void startListening() {
		mWDCU.receiveMessages();
	}

	public void sendMessage(Message toSend) {
		//Location location = mLocManager.getBestLocation();
		toSend.setDistance(20.0);
		toSend.setAxis(300);
		toSend.setTime(Calendar.getInstance().getTime());
		toSend.setVelocity(30.0);
		mWDCU.sendMessage(toSend);
	}

	public void stopListen() {
		try {
			mWDCU.cancelListen();
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "R: first stop");
		}

	}

	public void stopSending() {
		try {
			mWDCU.cancelSendingMessage();
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "S: first stop");
		}

	}


	Runnable mStatusRece = new Runnable() {
		@Override
		public void run() {
			stopListen();
			startListening();
			Log.d(TAG, "rece");
			mHandlerR.postDelayed(mStatusRece, mIntervalR);
		}
	};

	Runnable mStatusSender = new Runnable() {
		@Override
		public void run() {
			stopSending();
			sendMessage(mMessage);
			Log.d(TAG, "send");
			mHandlerS.postDelayed(mStatusSender, mIntervalS);
		}
	};

	private Message mMessage;

	public void startRepeatingListen() {
		mStatusRece.run();
	}

	public void stopRepeatingListen() {
		mHandlerR.removeCallbacks(mStatusRece);
		stopListen();
	}

	public void startRepeatingSend(Message msg) {
		mMessage = msg;
		mStatusSender.run();
	}

	public void stopRepeatingSend() {
		mHandlerS.removeCallbacks(mStatusSender);
		stopSending();
	}
}
