package org.redis;

import java.util.List;
import java.util.Objects;

public class Request {
    private final String type;
    private final String key;
    private final String value
    private final RequestValidationCode requestValidationCode;

    public Request(List<String> input){
        if ((input.size() > 3 || input.size() < 2)
                || (Objects.equals(input.getFirst(),"set") && input.size() != 2)
                || (Objects.equals(input.getFirst(),"get") && input.size() != 3) ){
            requestValidationCode = RequestValidationCode.INVALID_NUMBER_OF_PARAMETERS;
        }
        else if(!Objects.equals(input.getFirst().toLowerCase(),"set")
                || !Objects.equals(input.getFirst().toLowerCase(),"get")){
            requestValidationCode = RequestValidationCode.INVALID_METHOD;
        }
        else requestValidationCode = RequestValidationCode.VALID;

        if (requestValidationCode != RequestValidationCode.VALID){
            throw new RuntimeException("Unsupported Method or Arguments");
        }

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
