package org.redis;

public enum RequestValidationCode {
    INVALID_METHOD(100),    // Neither set nor get
    INVALID_NUMBER_OF_PARAMETERS(101),
    VALID(200);

    private final int code;

    RequestValidationCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
