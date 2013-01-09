package ro.ddalex.snfchat;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	ArrayList<String> listItems=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView listView =  (ListView)findViewById(R.id.list);
	    adapter=new ArrayAdapter<String>(this,
	               android.R.layout.simple_list_item_1,
	               listItems);
	    listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	public void send_message(View view)
	{
		// for now, just post up
		listItems.add("dada:test " + count);
		count ++;
		adapter.notifyDataSetChanged();
	}
}
