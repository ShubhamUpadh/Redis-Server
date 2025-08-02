package org.redis;

public class Response {
    private final Status status;
    private final String response;
    public Response(Status status, String response){
        this.status = status;
        this.response = response;
    }

    public boolean isFailed(){
        return (status == Status.GET_FAILED) || (status == Status.SET_FAILED);
    }
}
