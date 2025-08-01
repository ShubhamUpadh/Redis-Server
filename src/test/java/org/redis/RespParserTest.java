package org.redis;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RespParserTest {


    @Test
    public void testSimpleString() {
        String input = "+OK\r\n";
        Object result = RespParser.deserialize(input);
        assertEquals("OK", result);
    }

    @Test
    public void testNullBulkString() {
        String input = "$-1\r\n";
        Object result = RespParser.deserialize(input);
        assertNull(result);
    }

    @Test
    public void testArrayWithPing() {
        String input = "*1\r\n$4\r\nping\r\n";
        Object result = RespParser.deserialize(input);
        assertArrayEquals(new Object[]{"ping"}, (Object[]) result);
    }

    @Test
    public void testCommandEcho() {
        String input = "*2\r\n$4\r\necho\r\n$11\r\nhello world\r\n";
        Object[] expected = new Object[]{"echo", "hello world"};
        assertArrayEquals(expected, (Object[]) RespParser.deserialize(input));
    }

    @Test
    public void serializeNull(){
        String expected = "$-1\r\n";
        assertEquals(expected, RespParser.serialize(null));
    }

    @Test
    public void serializeString(){
        assertEquals("$5\r\nhello\r\n", RespParser.serialize("hello"));
    }
}
