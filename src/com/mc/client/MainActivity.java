package com.mc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		widget_init();
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
					Log.d("MC", "123");
				}
				catch (Exception e)
				{
					if (socket != null)
					{
						try
						{
							socket.close();
						}
						catch (IOException e1)
						{
						}
						socket = null;
					}
					Log.v("Socket", e.getMessage());
				}
//				finally
//				{
//					try
//					{
//						socket.close();
//					}
//					catch (IOException e)
//					{
//						e.printStackTrace();
//					}
//				}
				Message message = Message.obtain();
				message.obj = socket;
				handler.sendMessage(message);
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
