package com.avo.cards.client;

import com.avo.cards.dto.CustomerDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("accounts")
public interface AccountsFeignClient {

    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber);

}
