package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.transaction.Transactional;
import java.util.Locale;

@AllArgsConstructor
@Service
public class UserAccountService {

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
    private final ResourceBundleMessageSource resourceBundleMessageSource;
    private final LocaleChangeInterceptor localeChangeInterceptor;

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    @Transactional
    public String addCashToUser(Long userId, Double cash) {

        var user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + userId + " dosen't exists in db")
        );

        user.setAmountOfMoney(user.getAmountOfMoney() + cash);
        var userAfterOperation = userRepository.save(user);

        //logger.info(messageSource.getMessage("successfulOperation", ));

        return resourceBundleMessageSource.getMessage("successfulOperation", null, Locale.GERMAN)+
                userAfterOperation.getAmountOfMoney();

    }

    @Transactional
    public String takeCashFromAccount(long userId, double cash) {

        var user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + userId + " dosen't exists in db")
        );

        if (user.getAmountOfMoney() < cash)
            throw new NotEnoughMoneyException("Not enough money to take so much cash!");

        user.setAmountOfMoney(user.getAmountOfMoney() - cash);
        var userAfterOperation = userRepository.save(user);

        return "Operation successful! Now you have: "
                + userAfterOperation.getAmountOfMoney();

    }

}
