package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BfhlService containing all business logic.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final String FULL_NAME   = "veera_bishnoi";
    private static final String EMAIL       = "veerabishnoi231058@acropolis.in";
    private static final String ROLL_NUMBER = "0827AL231144";

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData() == null ? new ArrayList<>() : request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long numericSum               = 0;

        for (String item : data) {
            if (item == null || item.isEmpty()) {
                // treat empty/null as special character
                specialChars.add(item == null ? "" : item);
                continue;
            }

            if (isNumeric(item)) {
                long val = Long.parseLong(item);
                numericSum += val;
                if (val % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabetic(item)) {
                alphabets.add(item.toUpperCase());
            } else {
                specialChars.add(item);
            }
        }

        BfhlResponse response = new BfhlResponse();
        response.setSuccess(true);
        response.setUserId(buildUserId());
        response.setEmail(EMAIL);
        response.setRollNumber(ROLL_NUMBER);
        response.setOddNumbers(oddNumbers);
        response.setEvenNumbers(evenNumbers);
        response.setAlphabets(alphabets);
        response.setSpecialCharacters(specialChars);
        response.setSum(String.valueOf(numericSum));
        response.setConcatString(buildConcatString(alphabets));

        return response;
    }

    // --- Private helpers ---

    /**
     * Builds user_id as full_name_ddmmyyyy using today's date.
     */
    private String buildUserId() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return FULL_NAME + "_" + date;
    }

    /**
     * Returns true if the entire string represents an integer (handles negatives too).
     */
    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        int start = s.charAt(0) == '-' ? 1 : 0;
        if (start == s.length()) return false;
        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the string is a letter (a-z / A-Z).
     */
    private boolean isAlphabetic(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the concat_string:
     * 1. Collect all characters from the already-uppercased alphabet entries.
     * 2. Reverse the combined character sequence.
     * 3. Apply alternating caps starting with uppercase (index 0 → upper, 1 → lower, …).
     *
     * Example: alphabets = ["A","ABCD","DOE"]
     *   combined  → "AABCDDOE"
     *   reversed  → "EODDCBAA"
     *   alt-caps  → "EoDdCbAa"
     */
    private String buildConcatString(List<String> uppercasedAlphabets) {
        // Collect all characters
        StringBuilder combined = new StringBuilder();
        for (String word : uppercasedAlphabets) {
            combined.append(word);
        }

        // Reverse
        String reversed = combined.reverse().toString();

        // Alternating caps
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            result.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }

        return result.toString();
    }
}
