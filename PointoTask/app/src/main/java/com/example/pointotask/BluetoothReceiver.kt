package com.example.pointotask

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_ON -> {
                    Log.d("BluetoothState", "Bluetooth is On")
                }
                BluetoothAdapter.STATE_OFF -> {
                    Log.d("BluetoothState", "Bluetooth is Off")
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                    Log.d("BluetoothState", "Bluetooth is Turning Off")
                }
                BluetoothAdapter.STATE_TURNING_ON -> {
                    Log.d("BluetoothState", "Bluetooth is Turning On")
                }
            }
        }
    }
}
