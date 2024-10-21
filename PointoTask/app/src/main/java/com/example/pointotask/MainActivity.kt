//package com.example.pointotask
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.text.method.LinkMovementMethod
//import android.util.Log
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.pointotask.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var bluetoothManager: BluetoothManager
//    private lateinit var bluetoothAdapter: BluetoothAdapter
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var receiver: BluetoothReceiver
//    private lateinit var receiver2: Discoverability
//    private val REQUEST_ACCESS_COARSE_LOCATION = 1001
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
//        receiver = BluetoothReceiver()
//        receiver2 = Discoverability()
//
//        // Register the Bluetooth state receiver
//        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
//        registerReceiver(receiver, intentFilter)
//
//        checkPermissions()
//        enableDisableDiscoverability()
//
//        binding.btGetPairedDevices.setOnClickListener {
//            getPairedDevices()
//        }
//
//        binding.btDiscoverDevices.setOnClickListener {
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                when(ContextCompat.checkSelfPermission(
//                    baseContext,Manifest.permission.ACCESS_COARSE_LOCATION
//                )){
//                    PackageManager.PERMISSION_DENIED -> AlertDialog.Builder(this)
//                        .setTitle("Runtime Permission")
//                        .setMessage("Give Permission")
//                        .setNeutralButton("Okay",DialogInterface.OnClickListener{dialod,which ->
//                            if(ContextCompat.checkSelfPermission(baseContext,Manifest.permission.ACCESS_COARSE_LOCATION) !=
//                                PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_ACCESS_COARSE_LOCATION)
//                            }
//                        }).show()
//                        .findViewById<TextView>(R.id.message)!!.movementMethod = LinkMovementMethod.getInstance()
//
//                    PackageManager.PERMISSION_GRANTED -> {
//                        Log.d("dicoverableDevices", "Permission Granted")
//                    }
//                }
//                discoverDevices()
//            }
//        }
//
//
//        // Handle Bluetooth toggle button click
//        binding.btOnOff.setOnClickListener {
//            if (!bluetoothAdapter.isEnabled) {
//                // Request Bluetooth to be enabled (shows system dialog)
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//
//                    return@setOnClickListener
//                }
//                startActivityForResult(enableBtIntent, 102)
//            } else {
//                // Disable Bluetooth without the dialog
//                bluetoothAdapter.disable()
//            }
//        }
//
//        // Discovery button logic (you can implement this)
//
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun discoverDevices() {
//        val filter = IntentFilter()
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
//        filter.addAction(BluetoothDevice.ACTION_FOUND)
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        registerReceiver(discoverDeviceReceiver,filter)
//        bluetoothAdapter.startDiscovery()
//    }
//
//    @SuppressLint("MissingPermission")
//    private val discoverDeviceReceiver = object  : BroadcastReceiver() {
//        var deviceCounter = 0
//        override fun onReceive(p0: Context?, p1: Intent?) {
//            var action = ""
//            if(intent != null) {
//                val action = intent.action
//            }
//            if(BluetoothAdapter.ACTION_STATE_CHANGED == action){
//                Log.d("discoverDevice1","Action State Changes")
//            }
//            when(action) {
//                BluetoothAdapter.ACTION_STATE_CHANGED ->{
//                    Log.d("discoverDevice1","STATE CHANGES")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED ->{
//                    Log.d("discoverDevice2","DISCOVERY STARTED")
//                }
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    Log.d("discoverDevice3","Discovery FInished")
//                }
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                    if(device != null) {
//                        Log.d("discoverDevice4","${device.name} ${device.address}")
//                        deviceCounter++
//                        when(deviceCounter){
//                            1 -> {binding.device1.text = device.name}
//                            2 -> {binding.device2.text = device.name}
//                            3 -> {binding.device3.text = device.name
//                            binding.device3.setOnClickListener {
//                                device.createBond()
//                            }}
//                        }
//                    }
//                    if (device != null) {
//                        when(device.bondState) {
//                            BluetoothDevice.BOND_NONE -> {
//                                Log.d("Buetooth Bond Status", "${device.name} bond nonw")
//                            }
//                            BluetoothDevice.BOND_BONDING -> {
//                                Log.d("Buetooth Bond Status", "${device.name} bond bonding")
//                            }
//                            BluetoothDevice.BOND_BONDED -> {
//                                Log.d("Buetooth Bond Status", "${device.name} bond bonded")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun getPairedDevices() {
//        val arr = bluetoothAdapter.bondedDevices
//        Log.d("bondedDevices", arr.size.toString())  // total devices
//        Log.d("bondedDevices",arr.toString())
//        for(device in arr) {
//            Log.d("bondedDevices", device.name+"  "+device.address+"  "+device.uuids) // it shows device name and address
//        }
//    }
//
//    private fun enableDisableDiscoverability() {
//        when{
//            ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADVERTISE ) == PackageManager.PERMISSION_GRANTED -> {
//
//            }
//            shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_ADVERTISE) -> {
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_ADVERTISE),101)
//            }
//        }
//        binding.btnDiscover.setOnClickListener {
//            val discoverIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//            discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,20)
//            startActivity(discoverIntent)
//
//            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
//            registerReceiver(receiver2,intentFilter)
//        }
//    }
//
//    private fun checkPermissions() {
//        val bluetoothPermissions = arrayOf(
//            android.Manifest.permission.BLUETOOTH_CONNECT,
//            android.Manifest.permission.BLUETOOTH_SCAN
//        )
//        when {
//            ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED -> {
//                // Permissions already granted, proceed with Bluetooth actions
//            }
//            else -> {
//                // Request necessary permissions
//                ActivityCompat.requestPermissions(this, bluetoothPermissions, 101)
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // Permission granted, proceed with Bluetooth actions
//        } else {
//            // Handle permission denial case
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//        unregisterReceiver(receiver2)
//    }
//}

package com.example.pointotask

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pointotask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: BluetoothReceiver
    private lateinit var receiver2: Discoverability

    private var pairedDevicesList = mutableListOf<BluetoothDevice>() // Store paired devices here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        receiver = BluetoothReceiver()
        receiver2 = Discoverability()

        // Register the Bluetooth state receiver
        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(receiver, intentFilter)

        checkPermissions()
        enableDisableDiscoverability()

        // Handle button clicks
        binding.btGetPairedDevices.setOnClickListener {
            getPairedDevices()
        }

        binding.map.setOnClickListener {
            val intent = Intent(this, CurrLocation::class.java)
            startActivity(intent)
        }

        binding.btOnOff.setOnClickListener {
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                startActivityForResult(enableBtIntent, 102)
            } else {
                bluetoothAdapter.disable()
            }
        }

        // Setup paired device click listeners
        setupDeviceClickListeners()
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e("Bluetooth", "Bluetooth not available or not enabled.")
            return
        }

        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            pairedDevicesList.clear() // Clear previous list
            pairedDevicesList.addAll(pairedDevices) // Store paired devices

            var deviceCounter = 1
            for (device in pairedDevices) {
                when (deviceCounter) {
                    1 -> binding.device1.text = device.name
                    2 -> binding.device2.text = device.name
                    3 -> binding.device3.text = device.name
                }
                deviceCounter++
            }
        } else {
            Log.d("Bluetooth", "No paired devices found.")
        }
    }

    // Set up click listeners for paired devices
    private fun setupDeviceClickListeners() {
        binding.device1.setOnClickListener { showDeviceDetails(0) }
        binding.device2.setOnClickListener { showDeviceDetails(1) }
        binding.device3.setOnClickListener { showDeviceDetails(2) }
    }

    @SuppressLint("MissingPermission")
    private fun showDeviceDetails(index: Int) {
        if (index < pairedDevicesList.size) {
            val device = pairedDevicesList[index]
            val deviceDetails = "Name: ${device.name}\nMAC: ${device.address}"

            // Create an AlertDialog to show basic details
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Device Details")
            builder.setMessage(deviceDetails)

            // Add a "Connect" button to show services and characteristics
            builder.setPositiveButton("Connect") { _, _ ->
                connectToDevice(device)
            }

            builder.setNegativeButton("Close", null)
            builder.show()
        } else {
            Toast.makeText(this, "No device available", Toast.LENGTH_SHORT).show()
        }
    }

    // Connect to the Bluetooth device and fetch services/characteristics
    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice) {
        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt?.discoverServices()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                val services = gatt?.services
                val serviceDetails = services?.joinToString("\n") { service ->
                    "Service UUID: ${service.uuid}\n" +
                            service.characteristics.joinToString("\n") { characteristic ->
                                "Characteristic UUID: ${characteristic.uuid}"
                            }
                } ?: "No services found"

                runOnUiThread {
                    // Show service and characteristic details in a dialog
                    showServiceDetailsDialog(device, serviceDetails)
                }
            }
        }

        device.connectGatt(this, false, gattCallback)
    }

    // Show dialog with service and characteristic details
    @SuppressLint("MissingPermission")
    private fun showServiceDetailsDialog(device: BluetoothDevice, serviceDetails: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Device Services: ${device.name}")
        builder.setMessage(serviceDetails)
        builder.setPositiveButton("OK", null)
        builder.show()
    }

    private fun enableDisableDiscoverability() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
            // Discoverable logic here
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE), 101
            )
        }

        binding.btnDiscover.setOnClickListener {
            val discoverIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
            startActivity(discoverIntent)

            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
            registerReceiver(receiver2, intentFilter)
        }
    }

    private fun checkPermissions() {
        val bluetoothPermissions = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            // Permissions already granted
        } else {
            ActivityCompat.requestPermissions(this, bluetoothPermissions, 101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permissions granted, proceed with Bluetooth actions
        } else {
            Log.e("Bluetooth", "Permissions denied.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        unregisterReceiver(receiver2)
    }
}

