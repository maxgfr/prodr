package model;

public interface AsyncFirebase {
    void onSuccess(String response);
    void onFailure(String response);
}
