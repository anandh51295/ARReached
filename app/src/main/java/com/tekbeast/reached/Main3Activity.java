package com.tekbeast.reached;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    Button btn,cl,register;
    EditText usr,pwd;
    TextView info,hint;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        info=(TextView)findViewById(R.id.textView4);
        usr=(EditText)findViewById(R.id.usr);
        pwd=(EditText)findViewById(R.id.pwd);
        btn=(Button)findViewById(R.id.button4);
        register=(Button)findViewById(R.id.button5);
        hint=(TextView)findViewById(R.id.textView5);
        cl=(Button)findViewById(R.id.cl);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            usr.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Password)) {
            pwd.setText(sharedpreferences.getString(Password, ""));

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });


        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this,Main5Activity.class);
                startActivity(intent);
            }
        });
    }
    public void clear(){

        usr.setText("");
        pwd.setText("");

    }


    public void Save() {
        String n = usr.getText().toString();
        String e = pwd.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(Password, e);
        editor.commit();
    }
}
