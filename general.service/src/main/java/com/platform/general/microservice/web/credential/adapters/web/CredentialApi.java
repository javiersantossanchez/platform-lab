package com.platform.general.microservice.web.credential.adapters.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/credentials")
public class CredentialApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialApi.class);

    @Autowired
    WebCredentialService webCredential;

    @GetMapping("")
    List<WebCredential> getAll(HttpServletRequest request) {
        request.getHeaderNames().asIterator().forEachRemaining(item -> LOGGER.info("Headers: "+item));
/**        try {
            DecodedJWT jwt = JWT.decode("");
        } catch (JWTDecodeException exception){
            //Invalid token
        }
 */
        LOGGER.info("Getting all web credentials");
        return webCredential.findAll();
    }

    @PostMapping("")
    void createNewCredential() {
        webCredential.createNewWebCredential();
    }

    @DeleteMapping("")
    void deleteAllWebCredential() {
        webCredential.deleteAll();
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
