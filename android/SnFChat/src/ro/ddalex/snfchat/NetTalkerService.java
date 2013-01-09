package ro.ddalex.snfchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NetTalkerService extends Service {

	private static NetTalkerService sInstance = null;
	private String TAG = getClass().getSimpleName();
	
	private class NetListener extends Thread {
		
	}
	
	static public NetTalkerService getInstance(){
		return sInstance;
	}
	
	private NetListener nl;
	
    @Override
    public void onCreate() {
    	super.onCreate();
    	sInstance = this;
    	
    	nl.start();
    	Log.i(TAG, "Service created");
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// Specific code here
	private MainActivity mA = null;
	public void setActivity(MainActivity a)
	{
		mA = a;
	}
	
	public void postMessage(String s)
	{
		if (mA == null)
			Log.e(TAG, "mA not available");
		Log.i(TAG, "Posting " + s);
		mA.displayNetMessage(s);
	}
	
	class NetReceiver{
		
	}
	
}
