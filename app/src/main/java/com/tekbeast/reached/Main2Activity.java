package com.tekbeast.reached;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import javax.xml.datatype.Duration;

public class Main2Activity extends AppCompatActivity {
    DatabaseHelper mydb;
    TextView getplace,getcontact,lt,lg;
    EditText editText;
    Button button,delete,update;
    ImageButton imageButton,imageButton1;
    int PLACE_PICKER_REQUEST=0;
    String place;
    String number;
    String msg;
    double ll;
    double lgg;
    int id;
//    int RQS_PICK_CONTACT =1;
public static final int REQUEST_READ_CONTACTS = 79;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mydb=new DatabaseHelper(this);
        editText=(EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        imageButton=(ImageButton)findViewById(R.id.imageButton2);
        imageButton1=(ImageButton)findViewById(R.id.imageButton);
        getplace=(TextView)findViewById(R.id.textView2);
        getcontact=(TextView)findViewById(R.id.textView3);
        delete=(Button)findViewById(R.id.button3);
        update=(Button)findViewById(R.id.button2);
        lt=(TextView)findViewById(R.id.lat);
        lg=(TextView)findViewById(R.id.lg);
        try{
            Bundle bundle = getIntent().getExtras();
            id= Integer.parseInt(bundle.getString("ID").trim());
//            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            if(id!=0){
                Cursor res=mydb.getData(id);
//                while(res.moveToNext()){
                    getcontact.setText(res.getString(1));
                    getplace.setText(res.getString(2));
                    editText.setText(res.getString(5));
                    button.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    update.setVisibility(View.VISIBLE);
//            }
            }
            else {Toast.makeText(getApplicationContext(), "not working", Toast.LENGTH_SHORT).show();}
        }
        catch (NullPointerException e){e.printStackTrace();}
        if(id==0){
            button.setVisibility(View.VISIBLE);
            delete.setVisibility(View.INVISIBLE);
            update.setVisibility(View.INVISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer isDeleted=mydb.deleteData(String.valueOf(id));
                if(isDeleted>0){
                    id=0;
                    Intent intent =new Intent(Main2Activity.this,MainActivity.class);

                    startActivity(intent);
                    finish();}
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n;
                String p;
                String m;
                double l;
                double g;
                n=getcontact.getText().toString();
                p=getplace.getText().toString();
                l=Double.parseDouble(lt.getText().toString());
                g=Double.parseDouble(lg.getText().toString());
                m=editText.getText().toString();

                boolean isUpdated= mydb.updateData(String.valueOf(id),n,p,l,g,m);
                if(isUpdated==true){
                    id=0;
//                        Toast.makeText(getApplicationContext(),"Added To List",Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(Main2Activity.this,MainActivity.class);

                    startActivity(intent);
                    finish();
                }

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getApplicationContext());
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }
            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    requestLocationPermission();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place=getplace.getText().toString();
                ll=Double.parseDouble(lt.getText().toString());
                lgg=Double.parseDouble(lg.getText().toString());
                number=getcontact.getText().toString();
                msg=editText.getText().toString();
                String status="active";
                int le=msg.length();
                if(place!=""&&number!=""&&msg!=""&&le>0){

                   boolean isInserted= mydb.insertData(number,place,ll,lgg,msg,status);
                    if(isInserted==true){
//                        Toast.makeText(getApplicationContext(),"Added To List",Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(Main2Activity.this,MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
//                    Toast.makeText(getApplicationContext(),place+" "+number+" "+msg,Toast.LENGTH_LONG).show();
                }else {Toast.makeText(getApplicationContext(),"Fill all the Fields",Toast.LENGTH_LONG).show();}
            }
        });

    }


    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode) {
            case 0:

                if (requestCode == PLACE_PICKER_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(data, this);
                        String address = String.format("Place: %s", place.getAddress());
                        String latitude = String.format("%.7f",place.getLatLng().latitude);
                        String longitude = String.format("%.7f",place.getLatLng().longitude);
                        lt.setText(latitude);
                        lg.setText(longitude);
                        getplace.setText(address);
                    }
                }
                break;
            case REQUEST_READ_CONTACTS:
                if(requestCode == REQUEST_READ_CONTACTS){
                    if(resultCode == RESULT_OK){
                        Uri contactData = data.getData();
                        Cursor cursor =  managedQuery(contactData, null, null, null, null);
                        cursor.moveToFirst();

                        String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        //contactName.setText(name);
                        getcontact.setText(number);
                        //contactEmail.setText(email);
                    }
                }

                break;

        }

    }

    protected void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContacts();

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }


    public void getContacts() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, REQUEST_READ_CONTACTS);
    }

}
