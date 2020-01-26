package com.maxgfr.prodr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.maxgfr.prodr.R;
import com.maxgfr.prodr.model.DataApi;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {

    private TextView mail;
    private TextView firstname;
    private TextView lastname;
    private TextView description;
    private CircleImageView uri_profile;
    private Button mCallApiButton;
    private DataApi myApi;

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final String PREF_ACCOUNT_NAME = "accountName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mCallApiButton = (Button) findViewById(R.id.button);
        mail = (TextView) findViewById(R.id.mail);
        firstname = (TextView) findViewById(R.id.firstname);
        lastname = (TextView) findViewById(R.id.lastname);
        description = (TextView) findViewById(R.id.description);
        uri_profile = (CircleImageView) findViewById(R.id.uri_profile);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String email_pref = pref.getString("email", "");
        String firstname_pref = pref.getString("firstname", "");
        String description_pref = pref.getString("description", "");
        String lastname_pref = pref.getString("lastname", "");
        String thumbnailUrl_pref = pref.getString("thumbnailUrl", "https://i.stack.imgur.com/l60Hf.png");

        Set<String> listVideo = pref.getStringSet("all_video_id", null);
        List<String> arrayListVideo = new ArrayList<String>();
        arrayListVideo.addAll(listVideo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayListVideo);
        ListView listView = (ListView) findViewById(R.id.lview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String id = arrayListVideo.get(position);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v="+id); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mail.setText(email_pref);
        firstname.setText(firstname_pref);
        lastname.setText(lastname_pref);
        description.setText(description_pref);
        Picasso.get().load(thumbnailUrl_pref)
                .placeholder(R.drawable.man).error(R.drawable.man)
                .into(uri_profile);
        try {
            myApi = new DataApi(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
    }

}