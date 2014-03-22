package com.mc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity
{
	Socket socket = null;
	EditText editText;
	Button button;
	LinearLayout alertdialog;

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
		alertdialog = (LinearLayout) getLayoutInflater().inflate(
				R.layout.alertdialog, null);
		final AlertDialog alert = new AlertDialog.Builder(this)
				.setView(alertdialog).setTitle("PowerOff")
				.setMessage("����Ŭ�����ӵ���...").show();
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							socket = new Socket();
							socket.connect(new InetSocketAddress(IP, 6666),
									3000);
							alert.dismiss();
							Log.d("MC", "123");
						}
						catch (Exception e)
						{
							alert.dismiss();
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
						Message message = Message.obtain();
						message.obj = socket;
						handler.sendMessage(message);
					}
				}).start();
			}
		}, 2000);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		if (item.getItemId() == 1)
		{
			new AlertDialog.Builder(this).setTitle("����")
					.setMessage("�汾: Զ�̹ػ�(V1.2)")
					.setNegativeButton("ȷ��", null).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "����");
		return super.onCreateOptionsMenu(menu);
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj == null)
			{
				Toast.makeText(getApplicationContext(), "����ʧ��",
						Toast.LENGTH_SHORT).show();
			}
			else if (msg.obj instanceof Socket)
			{
				Toast.makeText(MainActivity.this, "�ػ��ɹ���", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
}
