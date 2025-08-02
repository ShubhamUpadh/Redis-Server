package org.redis;

import java.util.List;
import java.util.Objects;

public class Request {
    private String type;
    private String key;
    private String value;

    public Request(List<String> input){
//        if (!(Objects.equals(input.getFirst().toLowerCase(),"set")
//                || Objects.equals(input.getFirst().toLowerCase(),"get"))){
//        how do I return error message from here?
//        }
        this.type = input.getFirst().toLowerCase();
        this.key = input.get(1);
        this.value = (input.size() == 3) ? input.getLast() : null;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isGet() {
        return Objects.equals(type,"get");
    }
}
