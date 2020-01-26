package com.maxgfr.prodr.model;


public class MatchUnit {
    private String match_name ;
    private String match_image ;

    public MatchUnit(String i, String j) {
        match_name = i;
        match_image = j;
    }

    public String getMatchName() {
        return match_name;
    }

    public void setMatchName(String matchName) {
        this.match_name = matchName;
    }

    public String getMatchImage() {
        return match_image;
    }

    public void setMatchImage(String matchImg) {
        this.match_image = matchImg;
    }

    @Override
    public String toString() {
        return "match name = " + this.match_name + "\nmatch img = " + this.match_image + "\n\n";
    }
}
