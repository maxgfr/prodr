package com.example.prodr.activity;

import androidx.appcompat.app.AppCompatActivity;
import model.DataApi;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prodr.R;

public class ProfilActivity extends AppCompatActivity {

    private TextView mOutputText;
    private Button mCallApiButton;
    private DataApi myApi;

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        mCallApiButton = (Button) findViewById(R.id.button);
        mOutputText = (TextView) findViewById(R.id.textview);
        myApi = new DataApi(this);
    }

    public void onSynchronize(View view) {
        mCallApiButton.setEnabled(false);
        mOutputText.setText("");
        myApi.getResultsFromApi();
        mCallApiButton.setEnabled(true);
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText("This app requires Google Play Services. Please install Google Play Services on your device and relaunch this app.");
                } else {
                    myApi.getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    System.out.println("account name "+accountName);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        myApi.setNameAccount(accountName);
                        myApi.getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    myApi.getResultsFromApi();
                }
                break;
        }
    }
}
