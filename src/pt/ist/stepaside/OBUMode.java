package pt.ist.stepaside;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.BaseMessage;
import pt.ist.stepaside.models.Message;
import pt.ist.stepaside.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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
	public void onMessageReceived(BaseMessage response) {
		logTextView.setText(logTextView.getText() +"\n" + response.toString());
		if(response instanceof Message)
			Utils.writeToFile(((Message) response).toStats(), Environment.getExternalStorageDirectory()+"/StepAsideStats.txt");

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked)
			sTACU.startRepeatingListen();
		else
			sTACU.stopRepeatingListen();

	}

}