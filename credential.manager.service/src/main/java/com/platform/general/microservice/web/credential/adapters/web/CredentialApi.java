package com.platform.general.microservice.web.credential.adapters.web;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/web-credentials")
public class CredentialApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialApi.class);

    @Autowired
    WebCredentialService webCredential;

    @GetMapping("")
    ResponseEntity<List<WebCredential>> getAll(HttpServletRequest request) {
        List<WebCredential> credentialList = webCredential.findAll();
        LOGGER.debug("The user searching all the credentials {}",credentialList);
        return ResponseEntity.ok(credentialList);
    }

    @GetMapping("/{credentialId}")
    ResponseEntity<WebCredential> get(@PathVariable(value = "credentialId",required = true) UUID credentialId) {
        WebCredential credential = webCredential.findCredential(credentialId);
        LOGGER.debug("The user search credential {}",credential);
        return ResponseEntity.ok(credential);
    }

    @PostMapping("")
    ResponseEntity<WebCredential> create(@RequestBody WebCredentialParam newCredential) {
        WebCredential response = webCredential.createNewWebCredential(newCredential.getPassword(),newCredential.getUserName(),newCredential.getWebSite());
        LOGGER.debug("New credential was created by the user, Credential: {}",response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{credentialId}")
    void deleteCredential(@PathVariable(value = "credentialId",required = true) UUID credentialId) {
        webCredential.deleteByID(credentialId);
    }

    /**
    @GetMapping("/web-site/{webSite}")
    List<WebCredential> getByWebSite(@PathVariable(value = "webSite",required = true) String webSite) {
        WebCredentialFetcher fetcher = new WebCredentialFetcher();
        return fetcher.fetchByWebSite(webSite);
    }

    @GetMapping("/{credentialId}")
    List<WebCredential> getById(@PathVariable("credentialId") UUID credentialId) {
        WebCredentialFetcher fetcher = new WebCredentialFetcher();
        return fetcher.fetchById(credentialId);
    }


    */
}
