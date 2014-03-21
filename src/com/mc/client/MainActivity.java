package com.mc.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity
{
	Socket socket;
	EditText editText;
	Button button;
	String IP;

	public void widget_init()
	{
		editText = (EditText)findViewById(R.id.input_IP);
		button = (Button)findViewById(R.id.shutBt);
	}
	
	public void getIP()
	{
		IP = editText.getText().toString();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		widget_init();
		
	}

	public void onClick_shutdown(View view)
	{
		
		getIP();
		Log.d("MC",IP);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					socket = new Socket(IP, 6666);
					Log.d("MC", "123");
				} catch (UnknownHostException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				try
				{
					socket.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
