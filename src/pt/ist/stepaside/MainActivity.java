package pt.ist.stepaside;

import pt.ist.stepaside.utils.MLocationManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MLocationManager.getInstance().startLocationUpdates();
	}

	public void obu(View v){
		Intent i = new Intent(getApplicationContext(), OBUMode.class);
		startActivity(i);
	}

	public void rsu(View v){
		Intent i = new Intent(getApplicationContext(), RSUMode.class);
		startActivity(i);

	}

	public void obuAmbulance(View v){
		Intent i = new Intent(getApplicationContext(), OBUAmbulance.class);
		startActivity(i);
	}
}
