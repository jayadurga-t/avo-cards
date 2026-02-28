package com.avo.cards.client;

import com.avo.cards.dto.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountsFallback implements AccountsFeignClient {

    @Override
    public ResponseEntity<CustomerDto> fetchAccountDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
