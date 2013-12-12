package pt.ist.stepaside;

import pt.ist.stepaside.models.Message;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class OBUAmbulance extends Activity implements OnCheckedChangeListener{

	private StepAsideControlUnit sTACU = StepAsideControlUnit.getInstance();
	private TextView gps;
	private TextView message;
	private EditText id;
	private Switch startStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obu_ambulance_layout);
		gps = (TextView) findViewById(R.id.gpsLocation);
		message = (TextView) findViewById(R.id.messageId);
		id = (EditText) findViewById(R.id.editTextId);
		startStop = (Switch) findViewById(R.id.start_stop);
		startStop.setOnCheckedChangeListener(this);
	}

	public void _serviceRegistryCLick(View v) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			int idToSend = Integer.parseInt(id.getText().toString());
//			Message sentMessage = sTACU.sendMessage(idToSend);
//			gps.setText(sentMessage.getStringCoordinates());
//			message.setText(sentMessage.getId()+"");
			sTACU.startRepeatingSend(new Message(idToSend));
		}
		else
			sTACU.stopRepeatingSend();
	}

}
