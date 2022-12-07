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

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public String takeCashFromAccount(Long accountId, Double cash, Locale... locale) {

        var account = accountRepository.findById(accountId).orElseThrow(
                () -> new IllegalArgumentException("The Account with the given ID doesn't exists!")
        );

        if (account.getAmountOfMoney() < cash) throw new NotEnoughMoneyException("Not enough money");
        account.setAmountOfMoney(account.getAmountOfMoney() - cash);
        var accountAfterOperation = accountRepository.save(account);

        var msg = locale == null || locale.length == 0 ?
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", Locale.US) :
                propertiesLanguageConnector.getMessageOnLanguage("successfulPaycheckOperation", locale[0]);

        logger.info("Account with id: " + accountAfterOperation.getId() + " withdrew " + cash + " from account");

        return msg + " " + accountAfterOperation.getAmountOfMoney();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String transferMoney(
            Long senderId,
            Long receiverId,
            Double amount
    ) throws InterruptedException {

        var sender = accountRepository.findById(senderId).orElseThrow(() ->
                new IllegalArgumentException("Account with the id: " + senderId + " doesn't exists in db")
        );

        var receiver = accountRepository.findById(receiverId).orElseThrow(() ->
                new IllegalArgumentException("Account with the id: " + receiverId + " doesn't exists in db")
        );

        if (sender.getAmountOfMoney() < amount) throw new NotEnoughMoneyException("Not enough money");
        Thread.sleep(6000);

        sender.setAmountOfMoney(sender.getAmountOfMoney() - amount);
        receiver.setAmountOfMoney(receiver.getAmountOfMoney() + amount);

        logger.info("Account with id: " + sender.getId() + " sends " + amount + " from account " +
                "to account with id: " + receiver.getId());

        return "Money was transferred successfully.";
    }

}
