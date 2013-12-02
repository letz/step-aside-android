package pt.ist.stepaside;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OBUMode extends Activity implements MessageReceivedListener {

	private StepAsideControlUnit sTACU = StepAsideControlUnit.getInstance();
	private TextView logTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obu_layout);
		logTextView = (TextView) findViewById(R.id.logGpsAndId);
		sTACU.setMessageListener(this);
		sTACU.startListening();

	}

	@Override
	public void onMessageReceived(Message response) {
		logTextView.setText(logTextView.getText() +"\n" + "id: " + response.getId() + " location: " +  response.getStringCoordinates());
	}

}
