package com.example.prodr.activity;

import androidx.appcompat.app.AppCompatActivity;
import model.AsyncCreate;
import model.AsyncFbApi;
import model.AsyncLogin;
import model.FacebookApi;
import model.FirebaseService;
import model.LoginType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.prodr.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String BASIC_INFO = "basic_info";
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(BASIC_INFO, EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onSuccessConnection(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSuccessConnection (final LoginResult loginResult) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("access_token", loginResult.getAccessToken().getToken());
        editor.apply();
        final FacebookApi fbApi = FacebookApi.getInstance();
        fbApi.getInfo(loginResult.getAccessToken(), new AsyncFbApi() {
            @Override
            public void onSuccess(GraphResponse resp) throws JSONException {
                final String id = resp.getJSONObject().getString("id");
                final String name = resp.getJSONObject().getString("name");
                final String email = resp.getJSONObject().getString("email");
                final FirebaseService firebaseService = FirebaseService.getInstance();
                firebaseService.findUser(id, new AsyncLogin() {
                    @Override
                    public void onSuccess(String msg, LoginType type) {
                        if(type == LoginType.CREATE) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", id);
                            data.put("name", name);
                            data.put("email", email);
                            firebaseService.createUser(id, data, new AsyncCreate() {
                                @Override
                                public void onSuccess(String msg) {
                                    System.out.println("Account created");
                                    Intent intent = new Intent(getApplicationContext(), InfosActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(String msg) {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (type == LoginType.RECONNECT) {
                            Intent intent = new Intent(getApplicationContext(), InfosActivity.class);
                            startActivity(intent);
                            System.out.println("Reconnection");
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
