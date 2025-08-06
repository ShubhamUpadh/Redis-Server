package org.redis;

public class Response {
    private final String response;
    private final boolean failed;

    public Response(String response, boolean failed) {
        this.response = response;
        this.failed = failed;
    }

    public static Response success(String response) {
        return new Response(response, false);
    }

    public static Response failure(String errorMessage) {
        return new Response(errorMessage, true);
    }

    public String getResponse() {
        return response;
    }

    public boolean isFailed() {
        return failed;
    }
}
