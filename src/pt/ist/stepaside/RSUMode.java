package pt.ist.stepaside;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RSUMode extends Activity {

	public enum TrafficLight {RED,YELLOW,GREEN};

	private ImageView red,yellow,green;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rsu_layout);
		red = (ImageView) findViewById(R.id.red_signal);
		yellow = (ImageView) findViewById(R.id.yellow_signal);
		green = (ImageView) findViewById(R.id.green_signal);
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

	public void _greenClick(View v) {
		changeSignal(TrafficLight.GREEN);
	}

	public void _yellowClick(View v) {
		changeSignal(TrafficLight.YELLOW);
	}

	public void _redClick(View v) {
		changeSignal(TrafficLight.RED);
	}
}
