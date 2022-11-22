package com.platform.general.microservice.web.credential.adapters.web;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/***
 * Rest API controller for all end-points related with basic-credential (credential with user and password)
 *
 * @author  Javier Santos - javier.david.santos@gmail.com
 */
@RestController
@RequestMapping("/web-credentials")
public class CredentialApi {


    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialApi.class);

    @Autowired
    WebCredentialService webCredential;

    @GetMapping("/{credentialId}")
    ResponseEntity<WebCredential> get(@PathVariable(value = "credentialId",required = true) UUID credentialId, Principal principal,@AuthenticationPrincipal Jwt jwt) {
        WebCredential credential = webCredential.findCredential(credentialId);
        LOGGER.debug("The user search credential {}",credential);
        return ResponseEntity.ok(credential);
    }

    @PostMapping("")
    ResponseEntity<WebCredential> create(@RequestBody WebCredentialParam newCredential, Principal principal,@AuthenticationPrincipal Jwt jwt) {// principal and jwt are the paremeters required to review the user or authentication information
        WebCredential response = webCredential.createNewWebCredential(newCredential.getPassword(),newCredential.getUserName(),newCredential.getCredentialName());
        LOGGER.debug("New credential was created by the user, Credential: {}",response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{credentialId}")
    void deleteCredential(@PathVariable(value = "credentialId",required = true) UUID credentialId) {
        webCredential.deleteByID(credentialId);
    }
}
