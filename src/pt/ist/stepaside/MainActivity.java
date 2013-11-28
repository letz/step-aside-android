package pt.ist.stepaside;

import java.util.HashMap;
import java.util.Map;

import pt.ist.stepaside.utils.MLocationManager;
import pt.ist.stepaside.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private WifiP2pManager mManager;
	private Channel mChannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MLocationManager.getInstance().startLocationUpdates();
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
	}

	public void startRegistration(View v) {
		//  Create a string map containing information about your service.
		Map<String,String> record = new HashMap<String,String>();
		record.put("loc", Utils.locationToString(MLocationManager.getInstance().getBestLocation()));
		record.put("msd_id", ((int)( Math.random() * 1000)) + " ");

		// Service information.  Pass it an instance name, service type
		// _protocol._transportlayer , and the map containing
		// information other devices will want once they connect to this one.
		WifiP2pDnsSdServiceInfo serviceInfo =
				WifiP2pDnsSdServiceInfo.newInstance("StepAsideApp", "_presence._tcp", record);

		// Add the local service, sending the service info, network channel,
		// and listener that will be used to indicate success or failure of
		// the request.
		mManager.addLocalService(mChannel, serviceInfo, new ActionListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(getApplicationContext(), "Registo successo!", Toast.LENGTH_SHORT).show();
				// Command successful! Code isn't necessarily needed here,
				// Unless you want to update the UI or add logging statements.
			}

			@Override
			public void onFailure(int arg0) {
				Toast.makeText(getApplicationContext(), "Registo fail!", Toast.LENGTH_SHORT).show();
				// Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
			}
		});
	}


	final HashMap<String, String> buddies = new HashMap<String, String>();

	public void discoverService(View v) {
		DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
			@Override
			/* Callback includes:
			 * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
			 * record: TXT record dta as a map of key/value pairs.
			 * device: The device running the advertised service.
			 */

			public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
				Log.d("Letz", "DnsSdTxtRecord available -" + record.toString());
				buddies.put(device.deviceAddress, record.get("loc").toString());
				Toast.makeText(getApplicationContext(), record.get("loc").toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(), record.get("msd_id").toString(), Toast.LENGTH_SHORT).show();
			}
		};

		DnsSdServiceResponseListener servListener = new DnsSdServiceResponseListener() {
			@Override
			public void onDnsSdServiceAvailable(String instanceName, String registrationType,
					WifiP2pDevice resourceType) {

				// Update the device name with the human-friendly version from
				// the DnsTxtRecord, assuming one arrived.
				resourceType.deviceName = buddies
						.containsKey(resourceType.deviceAddress) ? buddies
								.get(resourceType.deviceAddress) : resourceType.deviceName;

								Log.d("Letz", "onBonjourServiceAvailable " + instanceName);
								Toast.makeText(getApplicationContext(), instanceName, Toast.LENGTH_SHORT).show();
			}
		};

		mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

		WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		mManager.addServiceRequest(mChannel,
				serviceRequest,
				new ActionListener() {
			@Override
			public void onSuccess() {
				Log.d("Letz", "service request success");
				Toast.makeText(getApplicationContext(), "service request success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				Log.d("Letz", "service request error");
				Toast.makeText(getApplicationContext(), "service request error", Toast.LENGTH_SHORT).show();
			}
		});

		mManager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d("Letz", "service discover success");
				Toast.makeText(getApplicationContext(), "service discover success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				Log.d("Letz", "service discover error");
				Toast.makeText(getApplicationContext(), "service discover error", Toast.LENGTH_SHORT).show();
			}
		});

	}

}
