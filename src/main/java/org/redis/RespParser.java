package org.redis;

import java.util.List;

public class RespParser {
    public static Object deserialize(String input){
        RespReader reader = new RespReader(input);
        return parseResp(reader);
    }
    public static String serialize(Object input){
        return serialisedString(input);
    }

    private static String serialisedString(Object input){
        switch (input) {
            case null -> {
                return "$-1\r\n";
            }
            case Integer i -> {
                return ":" + input + "\r\n";
            }
            case String s -> {
                return serializeBulkString(s);
            }
            case Object[] objects -> {
                return serializeBulkArray(objects);
            }
            case RespError respError -> {
                return "-" + respError.getMessage() + "\r\n";
            }
            default -> {
                throw new RuntimeException("Not a valid input string");
            }
        }
    }

    private static String serializeBulkString(String input){
        return "$" + input.length() + "\r\n" + input + "\r\n";
    }

    private static String serializeBulkArray(Object[] input){
        StringBuilder bulkArr = new StringBuilder("*" + input.length + "\r\n");
        for (Object o : input) {
            bulkArr.append(serialisedString(o));
        }
        return bulkArr.toString();
    }

    private static Object parseResp(RespReader reader){
        char prefix = reader.peek();
        return switch (prefix) {
            case '+' -> parseSimpleString(reader);
            case '-' -> parseError(reader);
            case ':' -> parseInteger(reader);
            case '*' -> parseArray(reader);
            case '$' -> parseBulkString(reader);
            default -> throw new RuntimeException("Unidentified data type");
        };

    }

    private static Object parseError(RespReader reader) {
        reader.readBytes(1);
        return reader.readLine();
    }

    private static Object parseSimpleString(RespReader reader){
        reader.readBytes(1);
        return reader.readLine();
    }

    private static Object parseInteger(RespReader reader){
        reader.readBytes(1);
        return Integer.parseInt(reader.readLine());
    }

    private static Object parseBulkString(RespReader reader){
        reader.readBytes(1);
        int length = Integer.parseInt(reader.readLine());
        if (length == -1) return null;
        String result = reader.readBytes(length);
        reader.readBytes(2);
        return result;
    }

    private static Object parseArray(RespReader reader){
        reader.readBytes(1);
        int count = Integer.parseInt(reader.readLine());
        if (count == -1) return null;
        Object[] result = new Object[count];
        for (int i = 0; i < count; i++){
            result[i] = parseResp(reader);
        }
        return result;
    }

}

class RespReader{
    private final String input;
    private int pos;

    public RespReader(String input){
        this.input = input;
        pos = 0;
    }

    public String readLine(){
        int end = input.indexOf("\r\n",pos);
        if (end == -1) throw new RuntimeException("Missing CRLF");
        String line = input.substring(pos, end);
        pos = end + 2;
        return line;
    }

    public String readBytes(int length){
        System.out.println("This is pos " + pos + " and this is length " + length);
        String result = input.substring(pos,pos + length);
        pos += length;
        return result;
    }

    public char peek(){
        return input.charAt(pos);
    }

    public boolean hasNext(){
        return pos < input.length();
    }

}

class RespError{
    private String message;
    public RespError(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}

