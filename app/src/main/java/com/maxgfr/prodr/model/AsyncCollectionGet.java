package com.maxgfr.prodr.model;

import java.util.Map;

public interface AsyncCollectionGet {
    void onSuccess(Map<String, Map<String, Object>> res);
    void onFailure(String msg);
}
