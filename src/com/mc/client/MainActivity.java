package com.mc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.shut_client.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
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
	LinearLayout alertDialog;

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
		alertDialog = (LinearLayout) getLayoutInflater().inflate(
				R.layout.alertdialog, null);
		final AlertDialog alert = new AlertDialog.Builder(this)
				.setView(alertDialog).setTitle("PowerOff")
				.setMessage("正在努力连接电脑...").show();
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
							saveIP(IP);
							alert.dismiss();
							Log.d("MC", "123");
						} catch (Exception e)
						{
							alert.dismiss();
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
		}, 2000);
	}

	public void onClick_clear(View view)
	{
		SharedPreferences sp = getSharedPreferences("RememberIP",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear().commit();
		editText.setText("");
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj == null)
			{
				Toast.makeText(getApplicationContext(), "连接失败",
						Toast.LENGTH_LONG).show();
			}
			else if (msg.obj instanceof Socket)
			{
				Toast.makeText(MainActivity.this, "远程关机成功", Toast.LENGTH_SHORT)
						.show();
				Log.d("MC","success");
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 1)
		{
			new AlertDialog.Builder(this).setTitle("关于")
					.setMessage("版本: 远程关机(V1.4)").setNegativeButton("确定", null)
					.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "关于");
		return super.onCreateOptionsMenu(menu);
	}
}
