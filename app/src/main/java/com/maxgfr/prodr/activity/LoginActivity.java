package com.maxgfr.prodr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.AsyncCreate;
import com.maxgfr.prodr.model.AsyncFbApi;
import com.maxgfr.prodr.model.AsyncGet;
import com.maxgfr.prodr.model.AsyncLogin;
import com.maxgfr.prodr.model.FacebookApi;
import com.maxgfr.prodr.model.FirebaseService;
import com.maxgfr.prodr.model.LoginType;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

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
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("id", id);
                editor.apply();
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
                            System.out.println("Reconnection");
                            firebaseService.getData(id, "users", new AsyncGet() {
                                @Override
                                public void onSuccess(String id, Map<String, Object> object) {
                                    System.out.println("Account created");
                                    try {
                                        String firstname = object.get("firstname").toString();
                                        String lastname = object.get("lastname").toString();
                                        String description = object.get("description").toString();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putBoolean("inscription_done", true);
                                        editor.putString("firstname", firstname);
                                        editor.putString("lastname", lastname);
                                        editor.putString("description", description);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), SwipeActivity.class);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(String msg) {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
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
