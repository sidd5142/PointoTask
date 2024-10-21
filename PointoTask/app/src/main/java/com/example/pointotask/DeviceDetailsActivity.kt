//package com.example.pointotask
//
//import android.Manifest
//import android.bluetooth.BluetoothGatt
//import android.bluetooth.BluetoothGattCallback
//import android.bluetooth.BluetoothGattCharacteristic
//import android.bluetooth.BluetoothGattService
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothProfile
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//
//class DeviceDetailsActivity : AppCompatActivity() {
//
//    private lateinit var bluetoothGatt: BluetoothGatt
//    private lateinit var device: BluetoothDevice
//    private lateinit var characteristicsTextView: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_device_details)
//
//        characteristicsTextView = findViewById(R.id.characteristics_text)
//
//        device = intent.getParcelableExtra<BluetoothDevice>(MainActivity.EXTRA_DEVICE) ?: return
//        connectToDevice(device)
//    }
//
//    private fun connectToDevice(device: BluetoothDevice) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Request permissions if not granted
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
//                REQUEST_CODE_BLUETOOTH
//            )
//            return
//        }
//        bluetoothGatt = device.connectGatt(this, false, gattCallback)
//    }
//
//    private val gattCallback = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                Log.d("BLE Connection", "Connected to GATT server.")
//                if (ActivityCompat.checkSelfPermission(
//                        this@DeviceDetailsActivity,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    return
//                }
//                gatt.discoverServices()
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                Log.d("BLE Connection", "Disconnected from GATT server.")
//                finish() // Close activity if disconnected
//            }
//        }
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                val services = gatt.services
//                val characteristics = services.flatMap { it.characteristics }
//                displayCharacteristics(characteristics)
//            }
//        }
//    }
//
//    private fun displayCharacteristics(characteristics: List<BluetoothGattCharacteristic>) {
//        val charList = StringBuilder("Characteristics:\n")
//        characteristics.forEach { characteristic ->
//            charList.append("UUID: ${characteristic.uuid}\n")
//        }
//        characteristicsTextView.text = charList.toString()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        bluetoothGatt.disconnect()
//        bluetoothGatt.close()
//    }
//
//    companion object {
//        private const val REQUEST_CODE_BLUETOOTH = 101
//    }
//}
