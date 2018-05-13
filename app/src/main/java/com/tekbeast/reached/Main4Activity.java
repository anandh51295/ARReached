package com.tekbeast.reached;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main4Activity extends AppCompatActivity {
    ArrayAdapter<String> adapters;

    ArrayList<String> itemss;
    ListView listViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        listViews=(ListView)findViewById(R.id.listv12);
        itemss=new ArrayList<String>();
        adapters=new ArrayAdapter(this, R.layout.iitem_layout,R.id.txt12,itemss);
        listViews.setAdapter(adapters);
        DatabaseHelper databaseHelper =new DatabaseHelper(this);
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

        Cursor res=databaseHelper.getin();

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
                itemss.add("ID:\t"+res.getString(0)+"."+"\n"+"Number:\t" + res.getString(1)+"\n"+ res.getString(2)+"\n"+"Message:\t"+res.getString(5)+"\n"+"lat:\t"+res.getDouble(3)+"\n"+"long:\t"+res.getDouble(4)+"\n"+"Status:\t"+res.getString(6));

            }
            adapters.notifyDataSetChanged();}

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab12);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main4Activity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                        DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

                        Cursor res=databaseHelper.deleteallData();
                        if(res.getCount()==0){

//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

                            Cursor res1=databaseHelper.getin();
                            if(res1.getCount()==0){
                                //Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
                                itemss.clear();
                                adapters.notifyDataSetChanged();
                            }
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                //finish();
                //Snackbar.make(view, "Reached Request added Successfully", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseHelper databaseHelper =new DatabaseHelper(this);
//        SQLiteDatabase sqLiteDatabase= databaseHelper.getReadableDatabase();

        Cursor res=databaseHelper.getin();
        if(res.getCount()==0){
            //Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show();
            itemss.clear();
            adapters.notifyDataSetChanged();
        }else{
//        StringBuffer buffer = new StringBuffer();
            if (itemss != null && itemss.size()>0)
                itemss.clear();
            while(res.moveToNext()){
//            buffer.append("id :"+res.getString(0)+"\n");
//            buffer.append("Number :"+res.getString(1)+"\n");
//            buffer.append("Place :"+res.getString(2)+"\n");
//            buffer.append("Message :"+res.getString(3)+"\n");
//            buffer.append("Status :"+res.getString(4)+"\n");
                itemss.add("ID:\t"+res.getString(0)+"."+"\n"+"Number:\t" + res.getString(1)+"\n"+ res.getString(2)+"\n"+"Message:\t"+res.getString(5)+"\n"+"lat:\t"+res.getDouble(3)+"\n"+"long:\t"+res.getDouble(4)+"\n"+"Status:\t"+res.getString(6));

            }

            adapters.notifyDataSetChanged();}
    }
}
