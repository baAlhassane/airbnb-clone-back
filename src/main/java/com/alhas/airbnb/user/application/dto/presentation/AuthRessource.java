package com.alhas.airbnb.user.application.dto.presentation;

import com.alhas.airbnb.infrastructure.SecurityUtils;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.application.dto.UserService;
import com.alhas.airbnb.user.domain.User;
import com.alhas.airbnb.user.mapper.UserMapper;
import com.alhas.airbnb.user.repository.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthRessource {

    private final UserService userService;
    private final ClientRegistration clientRegistration;

    private static final Logger logger = LoggerFactory.getLogger(AuthRessource.class);

    public AuthRessource(UserService userService, ClientRegistrationRepository clientRegistration) {
        this.userService = userService;
        this.clientRegistration = clientRegistration.findByRegistrationId("okta");
        if (this.clientRegistration != null) {
            logger.info("Client Registration ID: {}", this.clientRegistration.getRegistrationId());
        } else {
            logger.error("No client registration found for the provided ID.");
        }
    }

//    @GetMapping
//    public String TestApiOauth(){
//        return "test localhost:8080/api/auth";
//
//    }

    @GetMapping("/get-authenticated-user")
    public ResponseEntity<ReadUserDTO> getAuthenticationUse(
            @AuthenticationPrincipal OAuth2User user,
            @RequestParam(required = false) boolean forceRecync) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            userService.syncWithIdp(user,forceRecync);

           ReadUserDTO connectedUser= userService.getAuthenticatedUserFromSecurityContext();
           Set<String> roles =userService.getUserRoles().stream().
                   map(authority -> authority.getName()).collect(Collectors.toSet());
              connectedUser.setAuthorities(roles);
            return new ResponseEntity<>(connectedUser, HttpStatus.OK);

        }

    }

    @PostMapping("/logout")
  public ResponseEntity<Map<String,String>> logout(HttpServletRequest servletRequest){
        String issuerUri = clientRegistration.getProviderDetails().getIssuerUri();
        String origineUri = servletRequest.getHeader(HttpHeaders.ORIGIN);
         Object [] params={issuerUri,clientRegistration.getClientId(),origineUri};
         String logoutUrl= MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}",params);
         //"{0}b2/logout?client_id={1}&returnTo{2}"

         servletRequest.getSession().invalidate();
         return ResponseEntity.ok().body(Map.of("logoutUrl",logoutUrl));
  }
}
