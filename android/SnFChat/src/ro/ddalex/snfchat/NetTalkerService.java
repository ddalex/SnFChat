package ro.ddalex.snfchat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NetTalkerService extends Service {

	private static NetTalkerService sInstance = null;
	private String TAG = getClass().getSimpleName();
	
	private BlockingQueue<String> incomingMessageQueue = new LinkedBlockingQueue<String>();
	
	public BlockingQueue<String> getMessageQueue() { return incomingMessageQueue;} 
	
	private class NetListener extends Thread {
		
		private int c = 0;
		
		@Override
		public void run()
		{
			while(true) {
				incomingMessageQueue.offer("tata " + c);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c++;
			}
		}
	}
	
	static public NetTalkerService getInstance(){
		return sInstance;
	}
	
	private NetListener nl = null;
	
    @Override
    public void onCreate() {
    	super.onCreate();
    	sInstance = this;
    	if (nl == null)
    	{
    		nl = new NetListener();
    		nl.start();
    	}
    	Log.i(TAG, "Service created");
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void postMessage(String s)
	{
		Log.i(TAG, "Posting " + s);
		incomingMessageQueue.offer(s);
	}
	
	class NetReceiver{
		
	}
	
}
