package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.domain.Account;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.exceptions.CardNotFoundException;
import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Locale;

@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final PropertiesLanguageConnector propertiesLanguageConnector;
    private final UserRepository userRepository;

    public Account createAccountToUser(Long userId, Long cardId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with the given ID not found!")
        );
        var card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card with the given ID not found!")
        );

        var account = new Account(null, 0.0, user, card);
        account.getUser().getAccounts().add(account);
        account.getCard().setAccount(account);
        accountRepository.save(account);
        return account;
    }


    @Transactional
    public String addCashToAccount(Long accountId, Double cash, Locale... locale) {

        var account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("The Account with the given ID doesn't exists!")
        );

        account.setAmountOfMoney(account.getAmountOfMoney() + cash);
        var accountAfterOperation = accountRepository.save(account);

        var msg = locale == null || locale.length == 0 ?
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaymentOperation", Locale.US) :
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaymentOperation", locale[0]);
        logger.info("Account with id: " + accountAfterOperation.getId() + " added " + cash + " to account");

        return  msg + " " + accountAfterOperation.getAmountOfMoney();

    }


    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public String takeCashFromAccount(Long accountId, Double cash, Locale... locale) {

        var account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("The Account with the given ID doesn't exists!")
        );

        account.setAmountOfMoney(account.getAmountOfMoney() - cash);
        var accountAfterOperation = accountRepository.save(account);

        var msg = locale == null || locale.length == 0 ?
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", Locale.US) :
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", locale[0]);

        logger.info("Account with id: " + accountAfterOperation.getId() + " withdrew " + cash + " from account");

        return msg + " " + accountAfterOperation.getAmountOfMoney();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String transferMoney(MoneyTransferRequest transferRequest) throws InterruptedException {
        /*User sender = userRepository.findById(transferRequest.getSenderId()).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + transferRequest.getSenderId() + " doesn't exists in db")
        );

        User receiver = userRepository.findById(transferRequest.getReceiverId()).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + transferRequest.getReceiverId() + " doesn't exists in db")
        );

        if (sender.getUserAccount().getAmountOfMoney() < transferRequest.getAmount()) throw new NotEnoughMoneyException("Not enough money");
        Thread.sleep(6000);

        sender.getUserAccount().setAmountOfMoney(sender.getUserAccount().getAmountOfMoney() - transferRequest.getAmount());
        receiver.getUserAccount().setAmountOfMoney(receiver.getUserAccount().getAmountOfMoney() + transferRequest.getAmount());

        logger.info("User with id: " + sender.getId() + " sends " + transferRequest.getAmount() + " from account " +
                "to user with id: " + receiver.getId());*/

        return "Money was transferred successfully.";
    }

}
