package com.maxgfr.prodr.model;

public interface AsyncModify {
    void onSuccess(String msg);
    void onFailure(String msg);
}
