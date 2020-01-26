package com.maxgfr.prodr.activity;


public class MatchUnit {
    private String match ;

    public String getMatch() {
        return match;
    }

    public void setMatch(String matchName) {
        this.match = matchName;
    }

    @Override
    public String toString() {
        return "MatchUnit{" +
                "match='" + match + '\'' + '}';
    }
}
