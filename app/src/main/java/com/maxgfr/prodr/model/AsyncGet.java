package com.maxgfr.prodr.model;

import java.util.Map;

public interface AsyncGet {
    void onSuccess(String id, Map<String, Object> data);
    void onFailure(String msg);
}
