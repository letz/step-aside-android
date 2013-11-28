package pt.ist.stepaside;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;
import android.widget.Toast;

public class WifiDirectControlUnit {

	public static final String SERVICE_NAME = "StepAsideApp";

	private static WifiDirectControlUnit instance;
	private Context mContext;
	private WifiP2pManager mManager;
	private Channel mChannel;

	public static WifiDirectControlUnit getInstance(){
		if(instance == null)
			instance = new WifiDirectControlUnit(StepAsideApp.getContext());
		return instance;
	}

	private WifiDirectControlUnit(Context context){
		mContext = context;
		mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(mContext, mContext.getMainLooper(), null);
	}

	public void startRegistration(int id, String location) {
		//  Create a string map containing information about your service.
		Map<String,String> record = new HashMap<String,String>();
		record.put("loc", location);
		record.put("msd_id", id + "");

		// Service information.  Pass it an instance name, service type
		// _protocol._transportlayer , and the map containing
		// information other devices will want once they connect to this one.
		WifiP2pDnsSdServiceInfo serviceInfo =
				WifiP2pDnsSdServiceInfo.newInstance(SERVICE_NAME, "_presence._tcp", record);

		// Add the local service, sending the service info, network channel,
		// and listener that will be used to indicate success or failure of
		// the request.
		mManager.addLocalService(mChannel, serviceInfo, new ActionListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(mContext, "Registo successo!", Toast.LENGTH_SHORT).show();
				// Command successful! Code isn't necessarily needed here,
				// Unless you want to update the UI or add logging statements.
			}

			@Override
			public void onFailure(int arg0) {
				Toast.makeText(mContext, "Registo fail!", Toast.LENGTH_SHORT).show();
				// Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
			}
		});
	}


	final HashMap<String, String> buddies = new HashMap<String, String>();

	public void discoverService() {
		DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
			@Override
			/* Callback includes:
			 * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
			 * record: TXT record dta as a map of key/value pairs.
			 * device: The device running the advertised service.
			 */

			public void onDnsSdTxtRecordAvailable(String fullDomain, Map<String,String> record, WifiP2pDevice device) {
				Log.d("Letz", "DnsSdTxtRecord available -" + record.toString());
				buddies.put(device.deviceAddress, record.get("loc").toString());
				Toast.makeText(mContext, record.get("loc").toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(mContext, record.get("msd_id").toString(), Toast.LENGTH_SHORT).show();
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
								Toast.makeText(mContext, instanceName, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, "service request success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				Log.d("Letz", "service request error");
				Toast.makeText(mContext, "service request error", Toast.LENGTH_SHORT).show();
			}
		});

		mManager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d("Letz", "service discover success");
				Toast.makeText(mContext, "service discover success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				Log.d("Letz", "service discover error");
				Toast.makeText(mContext, "service discover error", Toast.LENGTH_SHORT).show();
			}
		});

	}


}
