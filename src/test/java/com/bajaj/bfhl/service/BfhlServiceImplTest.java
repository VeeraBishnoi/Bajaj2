package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }

    @Test
    @DisplayName("Standard mixed input - numbers, alphabets, special chars")
    void testMixedInput() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        // 1 is odd, 334 is even, 4 is even
        assertEquals(List.of("1"), res.getOddNumbers());
        assertEquals(List.of("334", "4"), res.getEvenNumbers());
        assertEquals(List.of("A", "R"), res.getAlphabets());
        assertEquals(List.of("$"), res.getSpecialCharacters());
        assertEquals("339", res.getSum());  // 1+334+4 = 339
    }

    @Test
    @DisplayName("Empty data array")
    void testEmptyArray() {
        BfhlRequest req = new BfhlRequest(Collections.emptyList());
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertTrue(res.getOddNumbers().isEmpty());
        assertTrue(res.getEvenNumbers().isEmpty());
        assertTrue(res.getAlphabets().isEmpty());
        assertTrue(res.getSpecialCharacters().isEmpty());
        assertEquals("0", res.getSum());
        assertEquals("", res.getConcatString());
    }

    @Test
    @DisplayName("Null data field defaults to empty")
    void testNullData() {
        BfhlRequest req = new BfhlRequest(null);
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertEquals("0", res.getSum());
    }

    @Test
    @DisplayName("Only numbers")
    void testOnlyNumbers() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("2", "3", "10", "7"));
        BfhlResponse res = service.processData(req);

        assertEquals(List.of("3", "7"), res.getOddNumbers());
        assertEquals(List.of("2", "10"), res.getEvenNumbers());
        assertTrue(res.getAlphabets().isEmpty());
        assertTrue(res.getSpecialCharacters().isEmpty());
        assertEquals("22", res.getSum());
    }

    @Test
    @DisplayName("Only alphabets - concat_string logic")
    void testConcatString() {
        // Input: ["A","ABCD","DOE"]
        // alphabets (uppercased): ["A","ABCD","DOE"]
        // combined: "AABCDDOE"
        // reversed: "EODDCBAA"
        // alt-caps:  "EoDdCbAa"
        BfhlRequest req = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse res = service.processData(req);

        assertEquals("EoDdCbAa", res.getConcatString());
        assertEquals(List.of("A", "ABCD", "DOE"), res.getAlphabets());
        assertEquals("0", res.getSum());
    }

    @Test
    @DisplayName("Only special characters")
    void testOnlySpecialChars() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("$", "@", "#", "!"));
        BfhlResponse res = service.processData(req);

        assertTrue(res.getOddNumbers().isEmpty());
        assertTrue(res.getEvenNumbers().isEmpty());
        assertTrue(res.getAlphabets().isEmpty());
        assertEquals(4, res.getSpecialCharacters().size());
        assertEquals("0", res.getSum());
    }

    @Test
    @DisplayName("Alphabets are uppercased in response")
    void testAlphabetsUppercased() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("abc", "xyz", "Hello"));
        BfhlResponse res = service.processData(req);

        assertEquals(List.of("ABC", "XYZ", "HELLO"), res.getAlphabets());
    }

    @Test
    @DisplayName("Numbers remain as strings in response")
    void testNumbersRemainStrings() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("5", "10", "15"));
        BfhlResponse res = service.processData(req);

        // All values in odd/even lists must be strings
        for (String s : res.getOddNumbers()) assertNotNull(s);
        for (String s : res.getEvenNumbers()) assertNotNull(s);
        assertEquals("30", res.getSum());
    }

    @Test
    @DisplayName("Mixed alphanumeric strings treated as special characters")
    void testMixedAlphanumericAsSpecial() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a1", "2b", "abc123"));
        BfhlResponse res = service.processData(req);

        // "a1", "2b", "abc123" are neither purely numeric nor purely alphabetic
        assertEquals(3, res.getSpecialCharacters().size());
        assertTrue(res.getAlphabets().isEmpty());
        assertTrue(res.getOddNumbers().isEmpty());
    }

    @Test
    @DisplayName("user_id, email, roll_number are always present")
    void testStaticFields() {
        BfhlRequest req = new BfhlRequest(Collections.emptyList());
        BfhlResponse res = service.processData(req);

        assertNotNull(res.getUserId());
        assertTrue(res.getUserId().startsWith("veera_bishnoi_"));
        assertEquals("veerabishnoi231058@acropolis.in", res.getEmail());
        assertEquals("0827AL231144", res.getRollNumber());
    }

    @Test
    @DisplayName("Zero is even")
    void testZeroIsEven() {
        BfhlRequest req = new BfhlRequest(List.of("0"));
        BfhlResponse res = service.processData(req);

        assertEquals(List.of("0"), res.getEvenNumbers());
        assertTrue(res.getOddNumbers().isEmpty());
        assertEquals("0", res.getSum());
    }
}
