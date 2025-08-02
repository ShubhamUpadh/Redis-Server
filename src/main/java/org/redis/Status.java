package org.redis;

public enum Status{
    SET_SUCCESSFUL(100),    // Set successful
    SET_FAILED(150),
    GET_SUCCESSFUL(200),    // Get Succesful
    GET_FAILED(250);

    private final int code;

    Status(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
