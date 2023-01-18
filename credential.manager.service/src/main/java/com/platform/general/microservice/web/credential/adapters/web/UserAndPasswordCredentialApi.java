package com.platform.general.microservice.web.credential.adapters.web;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import com.platform.general.microservice.web.credential.adapters.web.security.UserWrapper;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialUserCases;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/***
 * Rest API controller for all end-points related with basic-credential (credential with user and password)
 *
 * @author  Javier Santos - javier.david.santos@gmail.com
 */
@RestController
@RequestMapping("/"+ UserAndPasswordCredentialApi.BASE_URL)
public class UserAndPasswordCredentialApi {

    static final String BASE_URL = "user-and-password-credentials";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAndPasswordCredentialApi.class);


    private WebCredentialUserCases webCredentialUserCases;


    @Autowired
    public UserAndPasswordCredentialApi(WebCredentialUserCases webCredentialUserCases) {
        this.webCredentialUserCases = webCredentialUserCases;
    }

    @GetMapping("/{credentialId}")
    WebCredential get(@PathVariable(value = "credentialId",required = true) UUID credentialId, Principal principal,@AuthenticationPrincipal Jwt jwt) {
        UserWrapper userWrapper = new UserWrapper(principal);
        WebCredential credential = webCredentialUserCases.findCredentialById(credentialId,userWrapper.getId());
        LOGGER.debug("The user search credential {}",credential);
        return credential;
    }

    @GetMapping("")
    ResponseEntity<List<WebCredential>> getCredentialListByUser(@RequestParam(value="page-number",required = true) int pageNumber, @RequestParam(value="page-size",required = true) int pageSize,  Principal principal,@AuthenticationPrincipal Jwt jwt) {
        UserWrapper userWrapper = new UserWrapper(principal);
        PagingContext pagingContext = PagingContext.builder().pageSize(pageSize).pageNumber(pageNumber).build();
        List<WebCredential> credentialList = webCredentialUserCases.findByUserId(userWrapper.getId(),pagingContext);
        return ResponseEntity.ok(credentialList);
    }

    @PostMapping("")
    ResponseEntity<WebCredential> create(@RequestBody WebCredentialParam newCredential, Principal principal,@AuthenticationPrincipal Jwt jwt) {// principal and jwt are the paremeters required to review the user or authentication information
        UserWrapper userWrapper = new UserWrapper(principal);
        WebCredential response = webCredentialUserCases.createNewWebCredential(newCredential.getPassword(),newCredential.getUserName(),newCredential.getCredentialName(),userWrapper.getId());
        LOGGER.debug("New credential was created by the user, Credential: {}",response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{credentialId}")
    void deleteCredential(@PathVariable(value = "credentialId",required = true) UUID credentialId) {
        webCredentialUserCases.deleteByID(credentialId);
    }
}
