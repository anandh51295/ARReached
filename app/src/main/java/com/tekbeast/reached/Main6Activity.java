package com.tekbeast.reached;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Main6Activity extends AppCompatActivity {

    ListView ltt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        ltt=(ListView)findViewById(R.id.listView14);




        ltt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) ltt.getItemAtPosition(i);

                if(item.equals("Select Gateway")){
                    //Toast.makeText(getApplicationContext(),"You selected : Gateway" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main6Activity.this,Main7Activity.class);
                    startActivity(intent);

                }
                if(item.equals("Configure way2sms")){

                    Intent intent = new Intent(Main6Activity.this,Main3Activity.class);
                    startActivity(intent);

                }

                if(item.equals("Notification")){
                    //Toast.makeText(getApplicationContext(),"You selected : Notification ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main6Activity.this,Main8Activity.class);
                    startActivity(intent);

                } if(item.equals("About")){
                    //Toast.makeText(getApplicationContext(),"You selected : About " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main6Activity.this,Main9Activity.class);
                    startActivity(intent);

                }

            }
        });
    }
}
