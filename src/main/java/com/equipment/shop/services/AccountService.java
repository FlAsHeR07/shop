package com.equipment.shop.services;

import com.equipment.shop.dao.UserRepository;
import com.equipment.shop.dto.UserDTO;
import com.equipment.shop.exceptions.AuthenticationException;
import com.equipment.shop.exceptions.RegistrationException;
import com.equipment.shop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    //Regex patterns
    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]*$";
    private static final String PASSWORD_PATTERN = "[^a-zA-Z\\d]";
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$";
    private static final String FULLNAME_PATTERN = "^[а-яА-ЯєЄіІїЇёЁ]+(\\s[а-яА-ЯєЄіІїЇёЁ]+){1,2}$|^[a-zA-Z]+(\\s[a-zA-Z]+){1,2}$";
    private static final String PHONE_PATTERN = "^\\+380\\d{9}$";

    //Warnings messages
    private static final String USERNAME_TAKEN = "Username вже використовується!";
    private static final String EMAIL_TAKEN = "Email вже використовується!";
    private static final String PHONE_TAKEN = "Номер телефону вже пов'язаний!";
    private static final String INVALID_USERNAME = "Username має починатись з літери a–z A–Z, може містити лише букви a–z A–Z 0–9 без пробілів, всього не менше 8 символів!";
    private static final String INVALID_PASSWORD = "Пароль може містити лише букви a–z A–Z 0–9 без пробілів та не менше 8 символів!";
    private static final String INVALID_EMAIL = "Email не дійсний!";
    private static final String INVALID_FULLNAME = "ПІБ не валідне!";
    private static final String INVALID_PHONE = "Номер телефону не валідний!";


    private final UserRepository userRepository;

    @Autowired
    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userRepository.saveAndFlush(user);
    }

    public void trySignUp(UserDTO userDTO) throws RegistrationException {
        checkForDuplicateUser(userDTO);
        validateUser(userDTO);
        createUser(userDTO);
    }

    public void tryLogIn(UserDTO userDTO) throws AuthenticationException {
        User user = userRepository.findFirstByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (user == null)
            throw new AuthenticationException("Username відсутній або password не коректний!");
    }

    public User getUser(UserDTO userDTO) {
        return userRepository.findFirstByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
    }

    private void validateUser(UserDTO userDTO) throws RegistrationException {
        if (!userDTO.getUsername().matches(USERNAME_PATTERN) || userDTO.getUsername().length() < 8)
            throw new RegistrationException(INVALID_USERNAME);
        if (userDTO.getPassword().matches(PASSWORD_PATTERN) || userDTO.getPassword().length() < 8)
            throw new RegistrationException(INVALID_PASSWORD);
        if (!userDTO.getEmail().matches(EMAIL_PATTERN))
            throw new RegistrationException(INVALID_EMAIL);
        if (!userDTO.getFullName().matches(FULLNAME_PATTERN))
            throw new RegistrationException(INVALID_FULLNAME);
        if (!userDTO.getPhoneNumber().matches(PHONE_PATTERN))
            throw new RegistrationException(INVALID_PHONE);
    }

    private void checkForDuplicateUser(UserDTO userDTO) throws RegistrationException {
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(userDTO.getUsername()))
                throw new RegistrationException(USERNAME_TAKEN);
            if (user.getEmail().equals(userDTO.getEmail()))
                throw new RegistrationException(EMAIL_TAKEN);
            if (user.getPhoneNumber().equals(userDTO.getPhoneNumber()))
                throw new RegistrationException(PHONE_TAKEN);
        }
    }

}
