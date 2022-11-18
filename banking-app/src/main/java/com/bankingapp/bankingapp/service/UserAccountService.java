package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class UserAccountService {

    private final UserRepository userRepository;

    @Transactional
    public String addCashToUser(Long userId, Double cash) {

        var user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + userId + " dosen't exists in db")
        );

        user.setAmountOfMoney(user.getAmountOfMoney() + cash);
        userRepository.save(user);

        return "Operation successful! Cash was added successfuly! Now you have: " + user.getAmountOfMoney();

    }

    public String takeCashFromAccount(long userId, double cash) {
        return null;
    }

}
