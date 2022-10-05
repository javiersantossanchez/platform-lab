package com.platform.general.microservice.web.credential.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;


class BasicPasswordValidatorTest {

    private BasicPasswordValidator target = new BasicPasswordValidator();

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"  ", "asasdasdasD ","asdasdasd","GFGFGHFG","1233132","asdASDasas das","as@asdasdasD","as-asdasdasD","as1sD"})
    void validateWhenInvalidPassword(String password){
        boolean result = target.isValid(password);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdQSASAed1", "asdd12ASD4sdffdff","asasdasdasD"})
    public void validateWhenValidPassword(String password){
        boolean result = target.isValid(password);
        Assertions.assertTrue(result);
    }
}
