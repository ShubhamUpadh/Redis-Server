package org.redis;

import java.util.HashMap;

public class DispatcherLayer {
    private HashMap<String, String> map;

    public DispatcherLayer(){
        map = new HashMap<>();
    }

    public Response handleMessage(Request input){
        if (input.isGet()){
            return handleGet(input);
        }
        else{
            return handleSet(input);
        }
    }

    private Response handleGet(Request input) {
        if (!map.containsKey(input.getKey())) {
            return new Response(Status.GET_FAILED, "");
        }
        try{
            String response = map.get(input.getKey());
            return new Response(Status.GET_SUCCESSFUL, response);
        }
        catch (Exception e){
            return new Response(Status.GET_FAILED, "");
        }

    }

    private Response handleSet(Request input){

        try{
            map.put(input.getKey(), input.getValue());
            return new Response(Status.SET_SUCCESSFUL, "");
        }
        catch (Exception e){
            return new Response(Status.SET_FAILED, "");
        }
    }

}
