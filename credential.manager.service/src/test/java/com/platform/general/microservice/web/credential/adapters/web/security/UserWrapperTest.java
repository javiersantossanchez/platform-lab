package com.platform.general.microservice.web.credential.adapters.web.security;

import com.platform.general.microservice.web.credential.exceptions.InvalidUserInformationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.UUID;


class UserWrapperTest {

    Principal principal = Mockito.mock(Principal.class);

    @Test
    void getIdWithPrincipalAsNull(){
        UserWrapper target = new UserWrapper(null);
        Assertions.assertThrows(InvalidUserInformationException.class,target::getId);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"invalid-uuid"})
    void getIdWhenGetNameReturnsInvalidUUID(String id){
        Mockito.doReturn(id).when(principal).getName();
        UserWrapper target = new UserWrapper(principal);
        Assertions.assertThrows(InvalidUserInformationException.class, target::getId);
    }

    @Test
    void getIdWhenOk(){
        UUID id = UUID.randomUUID();
        Mockito.doReturn(id.toString()).when(principal).getName();
        UserWrapper target = new UserWrapper(principal);
        Assertions.assertEquals(id,target.getId());
    }

}
