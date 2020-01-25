package com.maxgfr.prodr.model;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class YoutubeUser {

    private String accountMail;
    private String possibleUserName;
    private String thumbnailUrl;
    private List<String> listChannelIdUpload;
    private List<YoutubeVideo> listUpload;
    private String nbSubscriber;

    public YoutubeUser() {
        accountMail="";
        possibleUserName="";
        nbSubscriber="";
        listChannelIdUpload = new ArrayList<>();
        listUpload = new ArrayList<>();
    }

    public String getPossibleUserName() {
        return possibleUserName;
    }

    public String getAccountMail() {
        return accountMail;
    }

    public List<String> getListChannelIdUpload() {
        return listChannelIdUpload;
    }

    public List<YoutubeVideo> getListUpload() {

        return listUpload;
    }

    public String getNbSubscriber() {
        return nbSubscriber;
    }

    public void setNbSubscriber(String nbSubscriber) {
        this.nbSubscriber = nbSubscriber;
    }

    public void setListUpload(List<YoutubeVideo> listUpload) {
        this.listUpload = listUpload;
    }

    public void setAccountMail(String accountMail) {
        this.accountMail = accountMail;
        //Set the possible username
        int index = accountMail.indexOf("@");
        this.possibleUserName = accountMail.substring(0, index);
        System.out.println("POSSIBLE USERNAME is : "+this.possibleUserName);
    }

    public void setListChannelIdUpload(List<String> listChannelIdUpload) {
        this.listChannelIdUpload = listChannelIdUpload;
    }


    public void addInformation (BigInteger viewCount, String listChannelIdUpload) {
        this.listChannelIdUpload.add(listChannelIdUpload);
    }

    public void addVideoContent(List<YoutubeVideo> videoId) {
        this.listUpload.addAll(videoId);
    }

    @Override
    public String toString() {
        String str = "\n";
        int i = 1;
        for(YoutubeVideo yv : listUpload) {
            str += "VIDEO ID num."+i+" "+yv.getVideoId()+"\n";
            i++;
        }
        return "YoutubeUser{" +
                "accountMail='" + accountMail + '\'' +
                ", possibleUserName='" + possibleUserName + '\'' +
                ", nbSubscriber='" + nbSubscriber + '\'' +
                ", listChannelIdUpload=" + listChannelIdUpload +
                ", listUpload=" + str +
                '}';
    }

    public void addUpload(String uploads) {
        this.listChannelIdUpload.add(uploads);
    }

    public int getNbVideo() {
        return this.listUpload.size();
    }

    public void addThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public void saveData() {
        Map<String, Object> data = new HashMap<>();
        data.put("accountMail", this.accountMail);
        data.put("possibleUserName", this.possibleUserName);
        data.put("thumbnailUrl", this.thumbnailUrl);
        data.put("nbSubscriber", this.nbSubscriber);
        data.put("listChannelIdUpload", this.listChannelIdUpload);
        data.put("listUpload", this.listUpload);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String id = pref.getString("id", "");
        final FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.modifyData(id, "users", data, new AsyncModify() {
            @Override
            public void onSuccess(String msg) {
                System.out.println("Data added");
                Toast.makeText(getApplicationContext(), "Data added to database", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String msg) {
                System.out.println(msg);
            }
        });

    }
}