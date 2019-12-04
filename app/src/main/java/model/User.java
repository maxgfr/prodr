package model;

import java.util.Map;

public class User {

    private String id;
    private Map<String, Object> data;

    public User() {

    }

    public User(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }
}
