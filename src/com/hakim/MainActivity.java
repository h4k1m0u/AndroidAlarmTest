package com.hakim;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private PendingIntent pendingIntent = null;
	private AlarmManager alarmManager = null;
	public static int DELAY = 5000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// attach click listener
		findViewById(R.id.button).setOnClickListener(this);
		
		// init alarm manager & receiver
		registerReceiver(receiver, new IntentFilter("com.hakim.alarm"));
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.hakim.alarm"), 0);
		alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
            case R.id.button:
            	// set alarm
            	alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + DELAY, pendingIntent);
                break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// disable alarm & unregister receiver
		alarmManager.cancel(pendingIntent);
		unregisterReceiver(receiver);
		
		super.onDestroy();
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// called on alarm tick
			Toast.makeText(context, "Alarm ringing", Toast.LENGTH_LONG).show();
			
			// vibrate device
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
		}
		
	};
}
