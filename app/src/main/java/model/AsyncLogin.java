package model;

public interface AsyncLogin {
    void onSuccess(String msg, LoginType type);
    void onFailure(String msg);
}
