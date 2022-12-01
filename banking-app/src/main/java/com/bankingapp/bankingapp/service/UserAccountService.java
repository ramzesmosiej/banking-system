package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@AllArgsConstructor
@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
    private final PropertiesLanguageConnector propertiesLanguageConnector;


    @Transactional
    public String addCashToUser(Long userId, Double cash, Locale... locale) {

        var user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + userId + " dosen't exists in db")
        );

        user.setAmountOfMoney(user.getAmountOfMoney() + cash);
        var userAfterOperation = userRepository.save(user);

        var msg = locale == null || locale.length == 0 ?
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaymentOperation", Locale.US) :
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaymentOperation", locale[0]);
        logger.info("User with id: " + user.getId() + " adds " + cash + " to account");

        return  msg + " " + userAfterOperation.getAmountOfMoney();

    }


    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public String takeCashFromAccount(Long userId, Double cash, Locale... locale) {

        var user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + userId + " dosen't exists in db")
        );

        if (user.getAmountOfMoney() < cash)
            throw new NotEnoughMoneyException("Not enough money to take so much cash!");
        user.setAmountOfMoney(user.getAmountOfMoney() - cash);
        var userAfterOperation = userRepository.save(user);

        var msg = locale == null || locale.length == 0 ?
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", Locale.US) :
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", locale[0]);

        logger.info("User with id: " + user.getId() + " withdraws " + cash + " from account");

        return msg + " " + userAfterOperation.getAmountOfMoney();

    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String transferMoney(MoneyTransferRequest transferRequest) throws InterruptedException {
        User sender = userRepository.findById(transferRequest.getSenderId()).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + transferRequest.getSenderId() + " doesn't exists in db")
        );

        User receiver = userRepository.findById(transferRequest.getReceiverId()).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + transferRequest.getReceiverId() + " doesn't exists in db")
        );

        if (sender.getAmountOfMoney() < transferRequest.getAmount()) throw new NotEnoughMoneyException("Not enough money");
        Thread.sleep(6000);

        sender.setAmountOfMoney(sender.getAmountOfMoney() - transferRequest.getAmount());
        receiver.setAmountOfMoney(receiver.getAmountOfMoney() + transferRequest.getAmount());

        logger.info("User with id: " + sender.getId() + " sends " + transferRequest.getAmount() + " from account " +
                "to user with id: " + receiver.getId());

        return "Money was transferred successfully.";
    }

}
