package com.tekbeast.reached;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Virus on 12-03-2018.
 */

public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double lat2 = location.getLatitude();
                double lng2 = location.getLongitude();

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

                Cursor res = databaseHelper.get();

//        textView=(TextView)findViewById(R.id.textView4);


                if (res.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
                }
//        StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
//            buffer.append("id :"+res.getString(0)+"\n");
//            buffer.append("Number :"+res.getString(1)+"\n");
//            buffer.append("Place :"+res.getString(2)+"\n");
//            buffer.append("Message :"+res.getString(3)+"\n");
//            buffer.append("Status :"+res.getString(4)+"\n");
                    Double lat1 = res.getDouble(3);
                    Double lng1 = res.getDouble(4);
                    if (distance(lat1, lng1, lat2, lng2) < 0.1) { // if distance < 0.1 miles we take locations as equal
                        //do what you want to do...
                        Intent i = new Intent("location_update");
                        i.putExtra("id", res.getString(0));
                        i.putExtra("val", lat1+"// "+lng1+"// "+lat2+"// "+lng2);
                        sendBroadcast(i);
                        //Toast.makeText(getApplicationContext(),"match found",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),lat1+"// "+lng1+"// "+lat2+"// "+lng2,Toast.LENGTH_LONG).show();
                        Intent i = new Intent("location_update");
                        i.putExtra("id", "no match found");
                        i.putExtra("val", lat1+"// "+lng1+"// "+lat2+"// "+lng2);
                        sendBroadcast(i);
                    }
                }
                // lat1 and lng1 are the values of a previously stored location

            }

            private double distance(double lat1, double lng1, double lat2, double lng2) {

                double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

                double dLat = Math.toRadians(lat2 - lat1);
                double dLng = Math.toRadians(lng2 - lng1);

                double sindLat = Math.sin(dLat / 2);
                double sindLng = Math.sin(dLng / 2);

                double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                double dist = earthRadius * c;

                return dist; // output distance, in MILES
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
