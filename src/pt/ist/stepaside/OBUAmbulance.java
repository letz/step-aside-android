package pt.ist.stepaside;

import pt.ist.stepaside.models.Message;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OBUAmbulance extends Activity{
	private StepAsideControlUnit sTACU = StepAsideControlUnit.getInstance();
	private TextView gps;
	private TextView message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obu_ambulance_layout);
		gps = (TextView) findViewById(R.id.gpsLocation);
		message = (TextView) findViewById(R.id.messageId);
	}

	public void _serviceRegistryCLick(View v) {
		Message sentMessage = sTACU.sendMessage();

		gps.setText(sentMessage.getStringCoordinates());        

		message.setText(sentMessage.getId()+"");
	}

}
