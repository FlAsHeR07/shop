package com.equipment.shop;

import com.equipment.shop.exceptions.RegistrationException;
import com.equipment.shop.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class ShopApplicationTests {

    @Test
    void contextLoads() {
    }

    /*@Test
    void testValidation() {
        User user = new User(1, "username", "password", "test.account.diploma@gmail.com", "Середа Владислав");
        try {
            if(!user.getUsername().matches("^[a-zA-Z][a-zA-Z0-9_]*$") || user.getUsername().length() < 8)
                throw new RegistrationException("Username має починатись з літери a–z A–Z, може містити лише букви a–z A–Z 0–9 без пробілів, всього не менше 8 символів!");
            if(user.getPassword().matches("[^a-zA-Z\\d]") || user.getPassword().length() < 8)
                throw new RegistrationException("Пароль може містити лише букви a–z A–Z 0–9 без пробілів та не менше 8 символів!");
            if(!user.getEmail().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$"))
                throw new RegistrationException("Email не дійсний!");
            if(!user.getFullName().matches("^[а-яА-ЯєЄіІїЇёЁ\\-]+(\\s[а-яА-ЯєЄіІїЇёЁ]+){1,2}$|^[a-zA-Z]+(\\s[a-zA-Z]+){1,2}$"))
                throw new RegistrationException("ПІБ не валідне!");
        } catch (RegistrationException e) {
            //throw new RuntimeException(e);
            Assertions.fail(e);
        }
    }*/

}
