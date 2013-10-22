package com.notpongwars.core.bluetooth;

import com.notslip.notpongwars.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothManager {

	public enum SelectedOptionItem{
		NULL,
		SCAN,
		DISCOVER
	}

	private SelectedOptionItem selectedOptionItem = SelectedOptionItem.NULL;
	public SelectedOptionItem getSelectedOptionItem(){return selectedOptionItem;}

	//for Toast
	private final Activity mActivity;

	// Debugging
	private static final String TAG = "BluetoothManager";
	private static final boolean D = true;

	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;

	/*
	// Layout Views
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;

	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	 */

	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;

	// Member object for the chat services
	private BluetoothService mService = null;

	private Handler mGameHandler;

	private Handler mHandler;
	
	public BluetoothManager(Activity activity, Handler handler) {

		if(D) Log.e(TAG, "+++ BLUETOOTH MANAGER +++");

		mActivity = activity;
		//mGameHandler = handler;
		
		mHandler = handler;
		
		/*
		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);



		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);
		 */

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			
			/*
			// Send a failure message back to the Activity
	        Message msg = mHandler.obtainMessage(BluetoothManager.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(BluetoothManager.TOAST, "Bluetooth is not available.");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
			*/
			mActivity.finish();
			return;
		}


	}


	public void onStart() {

		if(D) Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mService == null) setupManager();
		}
	}


	public synchronized void onResume() {

		if(D) Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (mService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mService.start();
			}
		}
	}

	private void setupManager() {
		Log.d(TAG, "setupManager()");

		/*
		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		mConversationView = (ListView) findViewById(R.id.in);
		mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});
		 */
		// Initialize the BluetoothService to perform bluetooth connections
		mService = new BluetoothService(mActivity, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}


	public synchronized void onPause() {

		if(D) Log.e(TAG, "- ON PAUSE -");
	}


	public void onStop() {

		if(D) Log.e(TAG, "-- ON STOP --");
	}

	public void onDestroy() {
		// Stop the Bluetooth chat services
		if (mService != null) mService.stop();
		if(D) Log.e(TAG, "--- ON DESTROY ---");
	}

	public void ensureDiscoverable() {
		if(D) Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() !=
				BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			mActivity.startActivity(discoverableIntent);
		}
	}


	/**
	 * Used to create server and connect to another device.
	 */
	public void createServer(){
		if(D) Log.d(TAG, "create server and connect");
		// Launch the DeviceListActivity to see devices and do scan
		Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
		mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}


	/**
	 * Sends a message.
	 * @param message  A string of text to send.
	 */
	public void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			//Toast.makeText(mActivity, R.string.not_connected, Toast.LENGTH_SHORT).show();
			 
			// Send a failure message back to the Activity
	        Message msg = mHandler.obtainMessage(BluetoothManager.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(BluetoothManager.TOAST, "You are not connected to a device");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
			
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send = message.getBytes();
			mService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			//mOutEditText.setText(mOutStringBuffer);
		}
	}

	/*
	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener =
			new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// If the action is a key-up event on the return key, send the message
			if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if(D) Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	 */

	/*
	// The Handler that gets information back from the BluetoothService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mGameHandler.sendMessage(Message.obtain(msg));
		}
	};
	*/

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

				// Attempt to connect to the device
				mService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupManager();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				//Toast.makeText(mActivity, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				
				 // Send a failure message back to the Activity
		        Message msg = mHandler.obtainMessage(BluetoothManager.MESSAGE_TOAST);
		        Bundle bundle = new Bundle();
		        bundle.putString(BluetoothManager.TOAST, "Bluetooth was not enabled. Closing Multiplayer.");
		        msg.setData(bundle);
		        mHandler.sendMessage(msg);
				
				mActivity.finish();
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = mActivity.getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	public void scanDevices(){
		// Launch the DeviceListActivity to see devices and do scan
		Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
		mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	public void discoverDevices(){
		// Ensure this device is discoverable by others
		ensureDiscoverable();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			scanDevices();
			selectedOptionItem = SelectedOptionItem.SCAN;
			return true;
		case R.id.discoverable:
			discoverDevices();
			selectedOptionItem = SelectedOptionItem.DISCOVER;
			return true;
		}
		return false;
	}


}
