package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.exceptions.CardNotFoundException;
import com.bankingapp.bankingapp.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CardService {

    private CardRepository cardRepository;

    public Card createCard(String pin) {
        return cardRepository.save(Card.builder().PIN(pin).build());
    }

    public String changePIN(Long cardId, String newPin) {
        var card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card with the given id doesn't exists!")
        );

        card.setPIN(newPin);
        cardRepository.save(card);
        return "PIN changed successfuly!";
    }

}
