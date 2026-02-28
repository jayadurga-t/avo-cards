package com.avo.cards.serviceImpl;

import com.avo.cards.client.AccountsFeignClient;
import com.avo.cards.client.LoansFeignClient;
import com.avo.cards.dto.CardsDto;
import com.avo.cards.dto.CustomerDetailsDto;
import com.avo.cards.dto.CustomerDto;
import com.avo.cards.dto.LoansDto;
import com.avo.cards.entity.Cards;
import com.avo.cards.exception.ResourceNotFoundException;
import com.avo.cards.mapper.CardsMapper;
import com.avo.cards.mapper.CustomerMapper;
import com.avo.cards.repository.CardsRepository;
import com.avo.cards.service.ICardsService;
import com.avo.cards.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountsFeignClient accountsFeignClient;
    private LoansFeignClient loansFeignClient;
    private CardsRepository cardsRepository;

    private ICardsService iCardsService;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String correlationId, String mobileNumber) {

        ResponseEntity<CustomerDto> customerDtoResponseEntity =
                accountsFeignClient.fetchAccountDetails(correlationId, mobileNumber);
        CustomerDetailsDto customerDetailsDto = new CustomerDetailsDto();

        if (customerDtoResponseEntity!=null && customerDtoResponseEntity.getBody()!=null){
            customerDetailsDto = CustomerMapper
                    .mapToCustomerDetailsDto(customerDtoResponseEntity.getBody(), customerDetailsDto);
        }

        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                new Supplier<ResourceNotFoundException>() {
                    @Override
                    public ResourceNotFoundException get() {
                        return new ResourceNotFoundException("Card", "mobileNumber", mobileNumber);
                    }
                }
        );
        CardsDto cardsDto = CardsMapper.mapToCardsDto(cards, new CardsDto());
        customerDetailsDto.setCardsDto(cardsDto);

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoan(correlationId, mobileNumber);

        if (loansDtoResponseEntity!=null && loansDtoResponseEntity.getBody()!=null){
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

//        CustomerDetailsDto customerDetailsDto = CustomerMapper
//                .mapToCustomerDetailsDto(accountsFeignClient.fetchAccountDetails(correlationId, mobileNumber).getBody(), new CustomerDetailsDto());
//        customerDetailsDto.setCardsDto(iCardsService.fetchCard(mobileNumber));
//        customerDetailsDto.setLoansDto(loansFeignClient.fetchLoan(correlationId,mobileNumber).getBody());

        return customerDetailsDto;
    }
}
