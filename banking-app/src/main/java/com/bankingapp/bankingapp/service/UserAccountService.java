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
    public String addCashToUser(Long user_id, Double cash) {

        var user = userRepository.findById(user_id).orElseThrow(() ->
                new UserNotFoundException("User with the id: " + user_id + " dosen't exists in db")
        );

        user.setAmountOfMoney(user.getAmountOfMoney() + cash);
        userRepository.save(user);

        return "Operation successful! Cash was added successfuly! Now you have: " + user.getAmountOfMoney();

    }

}
