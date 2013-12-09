package pt.ist.stepaside;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class RSUMode extends Activity implements MessageReceivedListener {

	public static final String TAG = RSUMode.class.getName();

	public enum TrafficLight {RED,YELLOW,GREEN};

	private ImageView red,yellow,green;
	private StepAsideControlUnit sTACU = StepAsideControlUnit.getInstance();

	private int mIntervalYellow = 1500;
	private int mIntervalRed = 3000;
	private TrafficLight mStatusTrafic = TrafficLight.GREEN;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rsu_layout);
		sTACU.setMessageListener(this);
		mHandler = new Handler();
		red = (ImageView) findViewById(R.id.red_signal);
		yellow = (ImageView) findViewById(R.id.yellow_signal);
		green = (ImageView) findViewById(R.id.green_signal);
		changeSignal(mStatusTrafic);
	}

	public void changeSignal(TrafficLight light) {

		if(TrafficLight.RED.equals(light)){
			red.setBackground(getResources().getDrawable(R.drawable.circle_red));
			yellow.setBackground(getResources().getDrawable(R.drawable.circle));
			green.setBackground(getResources().getDrawable(R.drawable.circle));
		}
		else if(TrafficLight.YELLOW.equals(light)) {
			yellow.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
			red.setBackground(getResources().getDrawable(R.drawable.circle));
			green.setBackground(getResources().getDrawable(R.drawable.circle));
		}
		else {
			green.setBackground(getResources().getDrawable(R.drawable.circle_green));
			red.setBackground(getResources().getDrawable(R.drawable.circle));
			yellow.setBackground(getResources().getDrawable(R.drawable.circle));
		}
	}

	@Override
	public void onMessageReceived(Message response) {
		startRepeating();
	}

	Runnable mStatus = new Runnable() {
		@Override
		public void run() {
			if(mStatusTrafic.equals(TrafficLight.GREEN)) {
				mStatusTrafic = TrafficLight.YELLOW;
				changeSignal(mStatusTrafic);
				mHandler.postDelayed(mStatus, mIntervalYellow);
			}
			else if(mStatusTrafic.equals(TrafficLight.YELLOW)) {
				mStatusTrafic = TrafficLight.RED;
				changeSignal(mStatusTrafic);
				mHandler.postDelayed(mStatus, mIntervalRed);
			}
			else {
				mStatusTrafic = TrafficLight.GREEN;
				changeSignal(mStatusTrafic);
				stopRepeating();
			}
		}
	};

	private void startRepeating() {
		mStatus.run();
	}

	private void stopRepeating() {
		mHandler.removeCallbacks(mStatus);
	}
}
