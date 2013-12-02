package pt.ist.stepaside;

import java.util.HashMap;
import java.util.Map;

import pt.ist.stepaside.listeners.MessageReceivedListener;
import pt.ist.stepaside.models.Message;
import android.content.Context;
import android.location.Location;
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

/**
 * This class have the responsibility to receive and send messages thru wifi-direct services announcement
 *
 * @author letz, diogo
 *
 */
public class WifiDirectControlUnit {

	public static final String TAG = WifiDirectControlUnit.class.getName();

	public static final String SERVICE_NAME = "StepAsideApp";

	private Context mContext;
	private WifiP2pManager mManager;
	private Channel mChannel;
	private MessageReceivedListener mListener;


	public void setMessageListener(MessageReceivedListener listener){
		mListener = listener;
	}

	public WifiDirectControlUnit(Context context){
		mContext = context;
		mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(mContext, mContext.getMainLooper(), null);
	}

	public void sendMessage(Message message) {
		//  Create a string map containing information about your service.
		Map<String,String> record = new HashMap<String,String>();
		record.put("loc", message.getStringCoordinates());
		record.put("msd_id", message.getId()+"");

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
	public void receiveMessages() {
		DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
			@Override

			public void onDnsSdTxtRecordAvailable(String fullDomain, Map<String,String> record, WifiP2pDevice device) {

				Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
				buddies.put(device.deviceAddress, record.get("loc").toString());
				Toast.makeText(mContext, record.get("loc").toString(), Toast.LENGTH_SHORT).show();
				Toast.makeText(mContext, record.get("msd_id").toString(), Toast.LENGTH_SHORT).show();
				String [] locToParse = record.get("loc").toString().split(" ");
				Location l = new Location("dummy");
				l.setLatitude(Double.parseDouble(locToParse[0]));
				l.setLongitude(Double.parseDouble(locToParse[1]));
				Message m = new Message(Integer.parseInt(record.get("msd_id").toString()), l);
				mListener.onMessageReceived(m);
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

								Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
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
				Log.d(TAG, "service request success");
				Toast.makeText(mContext, "service request success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				Log.d(TAG, "service request error");
				Toast.makeText(mContext, "service request error", Toast.LENGTH_SHORT).show();
			}
		});

		mManager.discoverServices(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "service discover success");
				Toast.makeText(mContext, "service discover success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int code) {
				String s = "";
				switch (code) {
				case WifiP2pManager.P2P_UNSUPPORTED:
					s = "Wi-Fi P2P isn't supported on the device running the app.";
					break;
				case WifiP2pManager.BUSY:
					s = "The system is to busy to process the request.";
					break;
				case WifiP2pManager.ERROR:
					s = "The operation failed due to an internal error.";
					break;
				default:
					break;
				}
				Log.d(TAG, s);
				Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
			}
		});

	}

}
