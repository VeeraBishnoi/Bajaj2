package com.bajaj.bfhl.controller;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    @Test
    @DisplayName("GET /health returns 200 with status UP")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("POST /bfhl returns 200 with valid response structure")
    void testPostBfhl() throws Exception {
        BfhlResponse mockResponse = buildMockResponse();
        when(bfhlService.processData(any(BfhlRequest.class))).thenReturn(mockResponse);

        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").exists())
                .andExpect(jsonPath("$.email").value("veerabishnoi231058@acropolis.in"))
                .andExpect(jsonPath("$.roll_number").value("0827AL231144"))
                .andExpect(jsonPath("$.odd_numbers").isArray())
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.alphabets").isArray())
                .andExpect(jsonPath("$.special_characters").isArray())
                .andExpect(jsonPath("$.sum").exists())
                .andExpect(jsonPath("$.concat_string").exists());
    }

    @Test
    @DisplayName("POST /bfhl with null data returns 400")
    void testPostBfhlNullData() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\": null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl with malformed JSON returns 400")
    void testPostBfhlMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bfhl with empty array returns 200")
    void testPostBfhlEmptyArray() throws Exception {
        BfhlResponse mockResponse = buildMockResponse();
        mockResponse.setOddNumbers(List.of());
        mockResponse.setEvenNumbers(List.of());
        mockResponse.setAlphabets(List.of());
        mockResponse.setSpecialCharacters(List.of());
        mockResponse.setSum("0");
        mockResponse.setConcatString("");

        when(bfhlService.processData(any(BfhlRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.sum").value("0"));
    }

    // --- Helper ---

    private BfhlResponse buildMockResponse() {
        BfhlResponse r = new BfhlResponse();
        r.setSuccess(true);
        r.setUserId("veera_bishnoi_26052026");
        r.setEmail("veerabishnoi231058@acropolis.in");
        r.setRollNumber("0827AL231144");
        r.setOddNumbers(List.of("1"));
        r.setEvenNumbers(List.of("334", "4"));
        r.setAlphabets(List.of("A", "R"));
        r.setSpecialCharacters(List.of("$"));
        r.setSum("339");
        r.setConcatString("Ra");
        return r;
    }
}
