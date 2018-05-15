package com.tekbeast.reached;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class Main8Activity extends AppCompatActivity {

    Switch aSwitch;
    TextView textView11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        aSwitch=(Switch)findViewById(R.id.switch1);
        textView11=(TextView)findViewById(R.id.textView8);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        aSwitch.setChecked(sharedPreferences.getBoolean("toggleButton", false));

        aSwitch.setOnClickListener(new Switch.OnClickListener() {

            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggleButton", aSwitch.isChecked());
                editor.commit();
            }
        });
    }
}
