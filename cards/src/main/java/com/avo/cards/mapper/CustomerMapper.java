package com.avo.cards.mapper;

import com.avo.cards.dto.CustomerDetailsDto;
import com.avo.cards.dto.CustomerDto;

public class CustomerMapper {

    public static CustomerDetailsDto mapToCustomerDetailsDto(
            CustomerDto customerDto, CustomerDetailsDto customerDetailsDto){
        customerDetailsDto.setName(customerDto.getName());
        customerDetailsDto.setEmail(customerDto.getEmail());
        customerDetailsDto.setMobileNumber(customerDto.getMobileNumber());
        customerDetailsDto.setAccountsDto(customerDto.getAccountsDto());
        return customerDetailsDto;
    }

}
