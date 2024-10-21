package com.example.pointotask

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pointotask.databinding.ActivityCurrLocationBinding
import com.example.pointotask.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrLocation : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityCurrLocationBinding // View binding reference
    private val PERMISSION_REQUEST_CODE = 101
    private val PERMISSION_SEND_SMS = 102

    private var latitude: String = ""
    private var longitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityCurrLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Handle button click
        binding.sendLocationButton.setOnClickListener {
            // Check for Location Permissions
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                getLocationAndSendSMS()
            }
        }
    }

    // Get Location and send SMS
    @SuppressLint("SetTextI18n")
    private fun getLocationAndSendSMS() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()

                Toast.makeText(this, "Location: Lat: $latitude, Lon: $longitude", Toast.LENGTH_LONG).show()

                binding.link.text = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"


                // Log location in Logcat
                Log.d("CurrLocation", "Location: Lat: $latitude, Lon: $longitude")

                // Send notification with location
                sendNotificationWithLocation(latitude, longitude)

                sendSMSWithLocation()
            } else {
                Log.e("MainActivity", "Failed to get location")
            }
        }
    }

    // Send SMS with location
    private fun sendSMSWithLocation() {
        val message = "I am at https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
        val phoneNumber = "9569673877" // Set the phone number

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSION_SEND_SMS)
        } else {
            val smsManager = SmsManager.getDefault()
            val sentPI = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"),
                PendingIntent.FLAG_IMMUTABLE)
            val deliveredPI = PendingIntent.getBroadcast(this, 0, Intent("SMS_DELIVERED"),
                PendingIntent.FLAG_IMMUTABLE)
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
            Log.d("MainActivity", "SMS Sent: $message")
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotificationWithLocation(lat: String, lon: String) {
        val channelId = "location_channel"
        val channelName = "Location Notification"
        val notificationId = 1

        // Create a notification channel for Android 8.0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for location updates"
            }

            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("Current Location")
            .setContentText("Lat: $lat, Lon: $lon")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismiss the notification when clicked
            .build()

        // Log for debugging
        Log.d("CurrLocation", "Attempting to send notification with location Lat: $lat, Lon: $lon")

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
            Log.d("CurrLocation", "Notification sent successfully")
        }
    }


    // Handle permission results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocationAndSendSMS()
                } else {
                    Log.e("MainActivity", "Location Permission Denied")
                }
            }
            PERMISSION_SEND_SMS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendSMSWithLocation()
                } else {
                    Log.e("MainActivity", "SMS Permission Denied")
                }
            }
        }
    }
}
