package com.alhas.airbnb.user.application.dto;

import com.alhas.airbnb.infrastructure.SecurityUtils;
import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.FieldsFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Response;
import com.auth0.net.TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class Auth0Service {

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    @Value("${okta.oauth2.issuer}")
    private String domain;

    @Value("${application.auth0.role-landlord-id}")
    private String roleLandlordId;



    public void addLandlordRoleToUser( ReadUserDTO readUserDTO) {
        if(readUserDTO.authorities
                .stream()
                .noneMatch(role-> role.equals(SecurityUtils.ROLE_LANDLORD))){
            try {
                String accessToken=this.getAccessToken();
                assignRoleById(accessToken, readUserDTO.getEmail(), readUserDTO.getPublicId(), roleLandlordId);
            } catch (Auth0Exception e) {
                throw new UserException(String.format(" not possible to assign %s to %s",roleLandlordId,readUserDTO.getPublicId()));

            }
        }


    }




    private void assignRoleById(String accessToken, String email, UUID publicId, String roleIdToAdd) throws Auth0Exception {
        ManagementAPI managementAPI=ManagementAPI.newBuilder(domain,accessToken).build();

        Response<List<User>> authOUserByEamil = managementAPI.users().listByEmail(email,new FieldsFilter()).execute();
        User user= authOUserByEamil.getBody()
                .stream()
                .findFirst()
                .orElseThrow(()-> new UserException(String.format("Can't not find user with publicId %s", publicId)));
        managementAPI.roles()
                .assignUsers(roleIdToAdd,List.of(user.getId())).execute();

    }

   private String getAccessToken() throws Auth0Exception {
       AuthAPI  authAPI=AuthAPI.newBuilder(domain,clientId,clientSecret).build() ;//new AuthAPI(clientId, clientSecret, domain);
       TokenRequest tokenRequest=authAPI.requestToken(domain+"api/v2/");
       TokenHolder tokenHolder=tokenRequest.execute().getBody();
       return tokenHolder.getAccessToken();

   }


}
