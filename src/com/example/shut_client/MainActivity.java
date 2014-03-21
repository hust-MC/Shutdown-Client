package com.example.shut_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

public class MainActivity extends Activity
{
	Socket socket = null;
	EditText editText;
	Button button;

	public void widget_init()
	{
		editText = (EditText) findViewById(R.id.input_IP);
		button = (Button) findViewById(R.id.shutBt);
	}

	public void saveIP(String IP)
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("IP", IP);
		editor.commit();
	}

	public String getIP()
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",
				Activity.MODE_PRIVATE);
		return sp.getString("IP", null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		widget_init();
		getIP();
		editText.setText(getIP());
	}

	public void onClick_shutdown(View view)
	{

		final String IP = editText.getText().toString();
		Log.d("MC", IP);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					socket = new Socket();
					socket.connect(new InetSocketAddress(IP, 6666), 3000);
					Log.d("MC", "success");
					saveIP(IP);
				} catch (Exception e)
				{
					if (socket != null)
					{
						try
						{
							socket.close();
						} catch (IOException e1)
						{
						}
						socket = null;
					}
					Log.v("Socket", e.getMessage());
				}
				Message message = Message.obtain();
				message.obj = socket;
				handler.sendMessage(message);
			}
		}).start();
	}

	public void onClick_clear(View view)
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear().commit();
		editText.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj == null)
			{
				Toast.makeText(getApplicationContext(), "¡¨Ω” ß∞‹",
						Toast.LENGTH_LONG).show();
			}
			else if (msg.obj instanceof Socket)
			{
			}
		}
	};
}
