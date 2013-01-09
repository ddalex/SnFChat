package ro.ddalex.snfchat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	ArrayList<String> listItems=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	int count = 0;
	
	private NetTalkerService service;
	
	//private  MainActivity getMAThis() { return this; }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent i = new Intent(this, NetTalkerService.class);
		startService(i);
	
		// get a timer to get the service and then pull updates
		final Timer t = new Timer();
		t.schedule(
		new TimerTask() {
			@Override
			public void run() {
				while ((service = NetTalkerService.getInstance()) == null)
				{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				
				while (! service.getMessageQueue().isEmpty() )
				{
					String s = service.getMessageQueue().poll();
					if (s != null)
						displayNetMessage(s);
				}
			}
		}, 10, 10);
		
		
		ListView listView =  (ListView)findViewById(R.id.list);
	    adapter=new ArrayAdapter<String>(this,
	               android.R.layout.simple_list_item_1,
	               listItems);
	    listView.setAdapter(adapter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();		
		Intent i = new Intent(this, NetTalkerService.class);
		stopService(i);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void displayNetMessage(String message)
	{
		listItems.add("test " + message);		
		adapter.notifyDataSetChanged();
	}
	
	public void sendNetMessage(View view)
	{		
		if (service == null)
			return;
		
		EditText mEdit = (EditText) findViewById(R.id.editText1);
		String text = mEdit.getText().toString();
		if (text.length() > 0) {
			service.postMessage(text);
			mEdit.setText("");
		}
	}
}
