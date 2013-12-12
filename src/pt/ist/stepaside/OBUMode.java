package pt.ist.stepaside;

import java.util.Calendar;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class OBUMode extends Activity implements MessageReceivedListener, OnCheckedChangeListener {

	private StepAsideControlUnit sTACU = StepAsideControlUnit.getInstance();
	private TextView logTextView;
	private Switch startStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obu_layout);
		logTextView = (TextView) findViewById(R.id.logGpsAndId);
		startStop = (Switch) findViewById(R.id.start_stop);
		sTACU.setMessageListener(this);
		startStop.setOnCheckedChangeListener(this);

	}

	@Override
	public void onMessageReceived(Message response) {
		logTextView.setText(logTextView.getText() +"\n" + response.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked)
			sTACU.startRepeatingListen();
		else
			sTACU.stopRepeatingListen();

	}

}
