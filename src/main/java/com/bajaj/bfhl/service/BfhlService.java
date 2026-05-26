package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;

/**
 * Service interface for BFHL business logic.
 */
public interface BfhlService {

    BfhlResponse processData(BfhlRequest request);
}
