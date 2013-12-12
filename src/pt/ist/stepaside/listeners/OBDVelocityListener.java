package pt.ist.stepaside.listeners;

/**
 *
 * Interface to mock velocity received from OBD car interface
 *
 * @author letz diogo rosa
 *
 */
public interface OBDVelocityListener {
	
	public void onReceivedVelocity(long velocity);
	
	

}
