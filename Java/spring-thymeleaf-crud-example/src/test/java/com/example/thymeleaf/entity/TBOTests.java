package com.example.thymeleaf.entity;

import org.junit.jupiter.api.Test;

import com.example.thymeleaf.dto.CreateStudentDTO;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

class TBOTests {

    @Test
    void StudentDTOTestCorrect() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //CreateStudentDTO is the only class with constraints so it should be the one to be tested for input validation
        CreateStudentDTO dto = SetupStudentDTO("Andrew", "andrew@gmail.com", LocalDate.of(2000, 10, 2), "0000000",
                                        "Street", "5", "some compliment", "Joshua", "Boston", "Alaska");

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.size() == 0);
    }

    @Test
    void StudentDTOTestIncorrect() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //missing/null data
        CreateStudentDTO dto = SetupStudentDTO("", "", LocalDate.of(2000, 10, 2), "",
                                        "", "", "", "", "", "");

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.size() == 8);//-1 because complement can be empty

        //incorrect data examples
        CreateStudentDTO dto2 = SetupStudentDTO("({}*&79})", "({}*&79})@gmail.com", LocalDate.of(3024, 10, 2), "asdjygsdif",
                                        "({}*&79})", "adyfasghab", "({}*&79})<!/>", "({}*&79})", "({}*&79})", "({}*&79})");

        Set<ConstraintViolation<CreateStudentDTO>> violations2 = validator.validate(dto2);
        assertTrue(violations2.size() == 10);
    }

    @Test
    void StudentDTOTestInjection() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        String injectionStr = "<script>alert('XSS');</script>";

        CreateStudentDTO dto = SetupStudentDTO(injectionStr, injectionStr, LocalDate.of(2000, 10, 2), injectionStr,
                                            injectionStr, injectionStr, injectionStr, injectionStr, injectionStr, injectionStr);

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.size() == 9);
    }

    @Test
    void StudentDTOTestExtreme() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        String bigString = "x".repeat(10000);

        CreateStudentDTO dto = SetupStudentDTO(bigString, bigString, LocalDate.of(2000, 10, 2), bigString,
                                            bigString, bigString, bigString, bigString, bigString, bigString);

        Set<ConstraintViolation<CreateStudentDTO>> violations = validator.validate(dto);
        assertTrue(violations.size() == 9);
    }

    //helper functions

    CreateStudentDTO SetupStudentDTO(String name, String email, LocalDate birthday, String ZipCode, String street, String number, String complement, 
                                      String district, String city, String state) {

        CreateStudentDTO dto = new CreateStudentDTO();
        dto.setName(name);
        dto.setEmail(email);
        dto.setBirthday(birthday);
        dto.setZipCode(ZipCode);
        dto.setStreet(street);
        dto.setNumber(number);
        dto.setComplement(complement);
        dto.setDistrict(district);
        dto.setCity(city);
        dto.setState(state);

        return dto;
    }
}