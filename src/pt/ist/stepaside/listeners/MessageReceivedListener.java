package pt.ist.stepaside.listeners;

import pt.ist.stepaside.models.Message;

/**
 *
 * Interface to receive messages from WifiDirectControlUnit
 *
 * @author letz
 *
 */
public interface MessageReceivedListener {

	/**
	 * This method is called when a message arrives
	 * @param response
	 */
	public void onMessageReceived(Message response);

}
