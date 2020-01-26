package com.maxgfr.prodr.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.AsyncModify;
import com.maxgfr.prodr.model.FirebaseService;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class InfosActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText description;
    RadioButton isArtist;
    RadioButton isBeatmaker;
    Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);
        firstName = findViewById(R.id.editText);
        lastName = findViewById(R.id.editText2);
        description = findViewById(R.id.editText3);
        isArtist = findViewById(R.id.radioButton);
        isBeatmaker = findViewById(R.id.radioButton2);
        validate = findViewById(R.id.button);
    }

    public void onValidate(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

        String id = pref.getString("id", "");

        String firstname = firstName.getText().toString();
        String lastname = lastName.getText().toString();
        String des = description.getText().toString();

        boolean isCheckedArtist = isArtist.isChecked();
        boolean isCheckedBeatmaker = isBeatmaker.isChecked();
        if( (isCheckedArtist && isCheckedBeatmaker) || (!isCheckedArtist && !isCheckedBeatmaker) || firstname.compareTo("") == 0  || lastname.compareTo("") == 0  || des.compareTo("") == 0 ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Complete each field and choice between artist and beatmark")
                    .setTitle("Be careful bro");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("firstname", firstname);
        data.put("lastname", lastname);
        data.put("description", des);
        if(isCheckedBeatmaker) {
            data.put("artist", false);
            editor.putBoolean("artist", false);
        } else {
            data.put("artist", true);
            editor.putBoolean("artist", false);
        }
        editor.putString("firstname", firstname);
        editor.putString("lastname", lastname);
        editor.putString("description", des);
        final FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.modifyData(id, "users", data, new AsyncModify() {
            @Override
            public void onSuccess(String msg) {
                System.out.println("Data added");
                editor.putBoolean("inscription_done", true);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
