//package com.example.pointotask
//
//import android.Manifest
//import android.bluetooth.BluetoothDevice
//import android.content.pm.PackageManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.app.ActivityCompat
//import androidx.recyclerview.widget.RecyclerView
//
//class DeviceAdapter(
//    private val devices: List<BluetoothDevice>,
//    private val itemClickListener: (BluetoothDevice) -> Unit
//) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
//
//    inner class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val deviceName: TextView = view.findViewById(R.id.device_name)
//        private val deviceAddress: TextView = view.findViewById(R.id.device_address)
//
//        fun bind(device: BluetoothDevice) {
//            // Use itemView.context to get the context
//            val context = itemView.context
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // Handle the case where permission is not granted (optional)
//                deviceName.text = "Permission Denied"
//                deviceAddress.text = ""
//                return
//            }
//            deviceName.text = device.name ?: "Unknown Device"
//            deviceAddress.text = device.address
//            itemView.setOnClickListener { itemClickListener(device) }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
//        return DeviceViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
//        holder.bind(devices[position])
//    }
//
//    override fun getItemCount(): Int = devices.size
//}
