package com.calenaur.pandemic.api.store;

import com.calenaur.pandemic.api.net.response.ErrorCode;

public interface PromiseHandler<T> {

    PromiseHandler<Void> EMPTY_HANDLER = new PromiseHandler<Void>() {
        @Override
        public void onDone(Void object) {

        }

        @Override
        public void onError(ErrorCode errorCode) {

        }
    };

    void onDone(T object);

    void onError(ErrorCode errorCode);

}
