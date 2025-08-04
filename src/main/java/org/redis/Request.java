package org.redis;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Request {
    private String type;
    private String key;
    private String value;
    private final RequestValidationCode requestValidationCode;

    public Request(String type, String key) {
        this.type = type;
        this.key = key;
        this.requestValidationCode = RequestValidationCode.VALID;
    }

    public Request(String type, String key, String value){
        this.type = type;
        this.key = key;
        this.value = value;
        this.requestValidationCode = RequestValidationCode.VALID;
    }

    public Request(RequestValidationCode requestValidationCode){
        this.requestValidationCode = requestValidationCode;
    }


    public static Optional<Request> fromInput(List<String> input){
        if ((input.size() > 3 || input.size() < 2)
                || (Objects.equals(input.getFirst(),"set") && input.size() != 3)
                || (Objects.equals(input.getFirst(),"get") && input.size() != 2) ){
            return Optional.of(new Request(RequestValidationCode.INVALID_NUMBER_OF_PARAMETERS));
        }
        else if(!(Objects.equals(input.getFirst().toLowerCase(),"set")
                || Objects.equals(input.getFirst().toLowerCase(),"get"))){
            return Optional.of(new Request(RequestValidationCode.INVALID_METHOD));
        }


        if (input.getFirst().equalsIgnoreCase("set")) {
            return Optional.of(new Request(input.getFirst(), input.get(1), input.getLast()));
        }
        else{
            return Optional.of(new Request(input.getFirst(), input.get(1)));
        }
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

    public boolean isValid(){
        return requestValidationCode == RequestValidationCode.VALID;
    }

    public RequestValidationCode getRequestValidationCode(){
        return requestValidationCode;
    }
}
