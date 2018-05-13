package com.tekbeast.reached;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
//    DatabaseHelper mydb;
//    TextView textView;
DatabaseHelper mydb;
    public static ApiInterface apiInterface;

    ArrayAdapter<String> adapter;

    ArrayList<String> items;
    ListView listView;
    String user,pass;
    int num;
    String ms,ph,iddd,fph;
    int idd;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    mydb=new DatabaseHelper(getApplicationContext());


//                    textView.append("\n" +intent.getExtras().get("coordinates"));
                    //Toast.makeText(getApplicationContext(), intent.getExtras().get("id").toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), intent.getExtras().get("val").toString(),Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), intent.getExtras().get("lat").toString(),Toast.LENGTH_LONG).show();
                   // Toast.makeText(getApplicationContext(), intent.getExtras().get("long").toString(),Toast.LENGTH_LONG).show();
                    //-----
                    if(intent.getExtras().get("id").toString().equals("no match found")){}
                    else{
                    idd= Integer.parseInt(intent.getExtras().get("id").toString());
                    if(idd>0){
                        Cursor res=mydb.getData(idd);
//                while(res.moveToNext()){
                        ph=res.getString(1);
                        if(ph.startsWith("+"))
                        {
                            if(ph.length()==13)
                            {
                                String str_getMOBILE=ph.substring(3);
                                fph=str_getMOBILE;
                            }
                            else if(ph.length()==14)
                            {
                                String str_getMOBILE=ph.substring(4);
                                fph=str_getMOBILE;
                            }

                        }
                        else
                        {
                            fph=ph;
                        }
                        ms=res.getString(5)+" \n Reached \n"+res.getString(2);
                        //Toast.makeText(getApplicationContext(), fph+" "+ms, Toast.LENGTH_SHORT).show();

//                        requestSmsPermission();

                            Get();
                            //sendsms();


//            }
                    }
                    else {Toast.makeText(getApplicationContext(), "not working", Toast.LENGTH_SHORT).show();}

                }}
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

        DatabaseHelper databaseHelper =new DatabaseHelper(this);
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

        Cursor res=databaseHelper.get();
        if(res.getCount()==0){
            //Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
            items.clear();
            adapter.notifyDataSetChanged();
        }else{
//        StringBuffer buffer = new StringBuffer();
            if (items != null && items.size()>0)
                items.clear();
        while(res.moveToNext()){
//            buffer.append("id :"+res.getString(0)+"\n");
//            buffer.append("Number :"+res.getString(1)+"\n");
//            buffer.append("Place :"+res.getString(2)+"\n");
//            buffer.append("Message :"+res.getString(3)+"\n");
//            buffer.append("Status :"+res.getString(4)+"\n");
            items.add("ID:\t"+res.getString(0)+"."+"\n"+"Number:\t" + res.getString(1)+"\n"+ res.getString(2)+"\n"+"Message:\t"+res.getString(5)+"\n"+"lat:\t"+res.getDouble(3)+"\n"+"long:\t"+res.getDouble(4)+"\n"+"Status:\t"+res.getString(6));

        }

        adapter.notifyDataSetChanged();}

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void sendsms(){
        Call<User> call =MainActivity.apiInterface.performSend(user,pass,ph,ms);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("send")){
                    iddd= String.valueOf(idd);
                    Boolean r=mydb.upData(iddd);
                    if(r==true){
                    Toast.makeText(getApplicationContext(),"Message Send Successfully",Toast.LENGTH_LONG).show();


                        DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

                        Cursor res=databaseHelper.get();
                        if(res.getCount()==0){
                            items.clear();
                            adapter.notifyDataSetChanged();
                        }else{
                            if (items != null && items.size()>0)
                                items.clear();
                            while(res.moveToNext()){
                                items.add("ID:\t"+res.getString(0)+"."+"\n"+"Number:\t" + res.getString(1)+"\n"+ res.getString(2)+"\n"+"Message:\t"+res.getString(5)+"\n"+"lat:\t"+res.getDouble(3)+"\n"+"long:\t"+res.getDouble(4)+"\n"+"Status:\t"+res.getString(6));

                            }

                            adapter.notifyDataSetChanged();}
                    }

                }
                else if(response.body().getResponse().equals("nodata")){
                    Toast.makeText(getApplicationContext(),"Check your message and phone number",Toast.LENGTH_LONG).show();
                    showAlert();

                }
                else if(response.body().getResponse().equals("not")){
                    showNotification();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                showconnection();
            }
        });
    }
    private void showNotification() {

        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        Intent notificationIntent = new Intent(this,Main3Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable. notification_template_icon_bg)
                .setContentTitle("Set Way2Sms gateway")
                .setContentText("add username and password to send message")

                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
    }

    private void showAlert() {
        Notification mNotification;
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable. notification_template_icon_bg)
                .setContentTitle("Check your Message and Number")
                .setContentText("Sorry their might some problem in our server..!")
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);

    }

    private void showconnection() {

        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable. notification_template_icon_bg)
                .setContentTitle("Turn On Internet")
                .setContentText("Turn on Internet and GPS..!")
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        listView=(ListView)findViewById(R.id.listv);
        items=new ArrayList<String>();
        adapter=new ArrayAdapter(this, R.layout.item_layout,R.id.txt,items);
        listView.setAdapter(adapter);


        DatabaseHelper databaseHelper =new DatabaseHelper(this);
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

        Cursor res=databaseHelper.get();

//        textView=(TextView)findViewById(R.id.textView4);




        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
        }else{
//        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
//            buffer.append("id :"+res.getString(0)+"\n");
//            buffer.append("Number :"+res.getString(1)+"\n");
//            buffer.append("Place :"+res.getString(2)+"\n");
//            buffer.append("Message :"+res.getString(3)+"\n");
//            buffer.append("Status :"+res.getString(4)+"\n");
            items.add("ID:\t"+res.getString(0)+"."+"\n"+"Number:\t" + res.getString(1)+"\n"+ res.getString(2)+"\n"+"Message:\t"+res.getString(5)+"\n"+"lat:\t"+res.getDouble(3)+"\n"+"long:\t"+res.getDouble(4)+"\n"+"Status:\t"+res.getString(6));

        }
        adapter.notifyDataSetChanged();}
//        textView.setText(buffer.toString());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                //finish();
                //Snackbar.make(view, "Reached Request added Successfully", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(Main7Activity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Main7Activity.this, type+" "+play, Toast.LENGTH_SHORT).show();
                String separated = parent.getItemAtPosition(position).toString();
                int s=separated.indexOf(".");

                String val=separated.substring(3,s);

//                num=s.substring(s.indexOf(":") + 1, s.indexOf("N"));
//                num=separated.trim();

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID",val);
                intent.putExtras(bundle);
                startActivity(intent);
                //finish();
                //System.exit(0);


//               Toast.makeText(MainActivity.this, val, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void Get() {
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Name)) {
          user=sharedpreferences.getString(Name, "");
        }
        if (sharedpreferences.contains(Password)) {
            pass=sharedpreferences.getString(Password, "");

        }
        if(user==""||pass==""||user==null||pass==null){
            //Toast.makeText(getApplicationContext(),"Goto Settings to Set gateway for msg",Toast.LENGTH_SHORT).show();
            showNotification();
        }
        else {
            //Toast.makeText(getApplicationContext(),"user"+user+" pass"+pass,Toast.LENGTH_SHORT).show();
            sendsms();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent= new Intent(MainActivity.this,Main3Activity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_start) {

            runtime_permissions();


            return true;
        }
        if (id == R.id.action_stop) {

            Intent i =new Intent(getApplicationContext(),GPS_Service.class);
            stopService(i);
            Toast.makeText(getApplicationContext(),"Service Stopped",Toast.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        return true;
        }
        Intent i =new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);

            }else {
                Toast.makeText(getApplicationContext(),"Permission Needed",Toast.LENGTH_LONG).show();
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            // Handle the camera action
            Intent intent= new Intent(MainActivity.this,Main4Activity.class);
            startActivity(intent);
        }
         else if (id == R.id.nav_manage) {
            Intent intent= new Intent(MainActivity.this,Main3Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
