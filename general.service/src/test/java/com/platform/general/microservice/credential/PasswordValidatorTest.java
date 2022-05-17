package com.platform.general.microservice.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.general.microservice.web.credential.BasicPasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private BasicPasswordValidator target;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"  ", "","asdasdasd","GFGFGHFG","1233132","asdASDasas das"})
    void validateWhenInvalidPassword(String password){
        target = new BasicPasswordValidator();
        boolean result = target.isValid(password);
        Assertions.assertFalse(result);
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"asdQSASAed1", "asdd12ASD4sdffdff"})
    public void validateWhenValidPassword(String password){
        target = new BasicPasswordValidator();
        boolean result = target.isValid(password);
        Assertions.assertTrue(result);
    }

    @Test
    void asdasd(){
        try {
            DecodedJWT jwt = JWT.decode("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFYXFaYlBIRUEwNGM4UUdST0swSnVBUzJpaEVDTloydE1YU0p0RVdYOXRvIn0.eyJleHAiOjE2NTEwNjgzMDUsImlhdCI6MTY1MTA2NzcwNSwianRpIjoiZTFhNGMxODItMDE3ZC00MDM3LWI1NDItYjY4MWY5NjBiZDM3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDg2L3JlYWxtcy9taWNyb3NlcnZpY2UucGxhZm9ybSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmMjQxMWQ4NC0xOWE5LTRmMjQtODllMC02OGFhYjE0OTBlOTkiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtaWNyb3NlcnZpY2UucGxhZm9ybSIsInNlc3Npb25fc3RhdGUiOiIwMjQ0ZThlZi1jODk0LTQwYjctYjcxYS03NWVmNThkZGY1MzMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1pY3Jvc2VydmljZS5wbGFmb3JtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIwMjQ0ZThlZi1jODk0LTQwYjctYjcxYS03NWVmNThkZGY1MzMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJqYXZpZXIgc2FudG9zIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamF2aWVyIiwiZ2l2ZW5fbmFtZSI6ImphdmllciIsImZhbWlseV9uYW1lIjoic2FudG9zIiwiZW1haWwiOiJqYXZpZXIuZGF2aWQuc2FudG9zQGdtYWlsLmNvbSJ9.LTNm3btCXCJ8Rmr5BsNEAvxyuT7gcCYjaTt8E9rJQrAlJZNQCRSnMhBq5TNNQ6o9qXSSVTZ0e0g0TB6chZrrWEaNRmKWUUAHQy227HuA4g6dJu3s_rYmt4TDezFXdHOcvIAQKSegfyRZ5axSSQmmgN6U-SJDXX7-ByLYKmkgfMnQ59RBFXsS-GMxMJfmWHup20D2YLVHgrvV_knapMxhAKdvdNpaVmI87DCSZ_4n_bBGq4QGnHHAHwLMywspohf1pMLesLiDl5vHWmHRPM_AJ6e5eKqBeaWILr5xX34B5UkgJvtd5OL0SvU1NauVjSYnRkh68dqlH3hAH-q6qXGtgg");
            System.out.println(jwt);
        } catch (JWTDecodeException exception){
            //Invalid token
        }
    }

}
