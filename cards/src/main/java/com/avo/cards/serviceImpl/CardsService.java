package com.avo.cards.serviceImpl;

import com.avo.cards.constants.CardsConstants;
import com.avo.cards.dto.CardsDto;
import com.avo.cards.entity.Cards;
import com.avo.cards.exception.CardAlreadyExistsException;
import com.avo.cards.exception.ResourceNotFoundException;
import com.avo.cards.mapper.CardsMapper;
import com.avo.cards.repository.CardsRepository;
import com.avo.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class CardsService implements ICardsService {

    private CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> cards = cardsRepository.findByMobileNumber(mobileNumber);

        if (cards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }

        cardsRepository.save(createNewCard(mobileNumber));
    }

    public Cards createNewCard(String mobileNumber){

        Cards newCard = new Cards();
        newCard.setMobileNumber(mobileNumber);
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;

    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {

        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                new Supplier<ResourceNotFoundException>() {
                    @Override
                    public ResourceNotFoundException get() {
                        return new ResourceNotFoundException("Card", "mobileNumber", mobileNumber);
                    }
                }
        );
        return null;
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {

        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                new Supplier<ResourceNotFoundException>() {
                    @Override
                    public ResourceNotFoundException get() {
                        return new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber());
                    }
                }
        );

        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {

        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                new Supplier<ResourceNotFoundException>() {
                    @Override
                    public ResourceNotFoundException get() {
                        return new ResourceNotFoundException("Cards", "mobileNumber", mobileNumber);
                    }
                }
        );

        cardsRepository.findById(cards.getCardId());
        return true;
    }
}
