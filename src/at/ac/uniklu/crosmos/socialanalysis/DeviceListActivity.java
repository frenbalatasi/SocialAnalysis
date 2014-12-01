package at.ac.uniklu.crosmos.socialanalysis;

import at.ac.uniklu.crosmos.socialanalysis.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.widget.ListView;
import android.widget.Toast;


public class DeviceListActivity extends Activity {
	private ListView mListView;
	private DeviceListAdapter mAdapter;
	private ArrayList<BluetoothDevice> mDeviceList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_paired_devices);
		
		mDeviceList		= getIntent().getExtras().getParcelableArrayList("device.list");
		
		mListView		= (ListView) findViewById(R.id.lv_paired);
		
		mAdapter		= new DeviceListAdapter(this);
		
		mAdapter.setData(mDeviceList);
		mAdapter.setListener(new DeviceListAdapter.OnPairButtonClickListener() {			
			@Override
			public void onPairButtonClick(int position) {
				BluetoothDevice device = mDeviceList.get(position);
				
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					//unpairDevice(device);
				} else {
					//showToast("Pairing...");
					
					//pairDevice(device);
				}
			}
		});
		
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}
}