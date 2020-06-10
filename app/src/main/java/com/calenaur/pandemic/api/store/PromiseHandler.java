package com.calenaur.pandemic.api.store;

import com.calenaur.pandemic.api.net.response.ErrorCode;

public interface PromiseHandler<T> {

    void onDone(T object);

    void onError(ErrorCode errorCode);

}
