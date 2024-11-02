package com.alhas.airbnb.user.application.dto;

import com.alhas.airbnb.infrastructure.SecurityUtils;
import com.alhas.airbnb.user.domain.Authority;
import com.alhas.airbnb.user.domain.User;
import com.alhas.airbnb.user.mapper.UserMapper;
import com.alhas.airbnb.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final String UPDATED_AT_KEY = "updatedAt";
    private final UserMapper userMapper;

    Set<Authority> authoritiesService=new HashSet<>();

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public ReadUserDTO getAuthenticatedUserFromSecurityContext(){

        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext()
                .getAuthentication().
                getPrincipal();
        User user = SecurityUtils.mapOauth2AttributesToUser(principal.getAttributes());
        return getByEmail(user.getEmail()).orElseThrow();

    }

    @Transactional(readOnly = true)
    public Optional<ReadUserDTO> getByEmail(String email) {
       Optional<User> oneByEmail= userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::readUserDTOToUser);

    }

public void syncWithIdp(OAuth2User oAuth2User, boolean forceSync) {
        Map<String,Object> attributes = oAuth2User.getAttributes();
    //public static final String CLAIMS_NAMESPACE = "https://alhas.com/roles"
    System.out.println(" role1  "+ attributes.get(SecurityUtils.CLAIMS_NAMESPACE));
//        oAuth2User.getAttributes().forEach((key, value) -> {})
   User user= SecurityUtils.mapOauth2AttributesToUser(attributes);
    System.out.println(" user in  syncWithIdp(OAuth2Use  "+ user);
    this.authoritiesService=user.getAuthorities();

    Optional<User> existingUser= userRepository.findOneByEmail(user.getEmail());
    if(existingUser.isPresent()){
        if(attributes.get(UPDATED_AT_KEY) != null){
           Instant lastModifiedDate= existingUser.orElseThrow().getLastModifiedDate();
           Instant idModifiedDate;
           if(attributes.get(UPDATED_AT_KEY) instanceof Instant instant){
               idModifiedDate= instant;
           }
           else{
               idModifiedDate=Instant.ofEpochSecond( (Integer) attributes.get(UPDATED_AT_KEY));
           }
           if(idModifiedDate.isAfter(lastModifiedDate) || forceSync){
               updateUser(user);
           }
        }
    }else {
//        UUID uuid= UUID.randomUUID();
//        user.setPublicId(uuid);
       System.out.println(" role2 "+ user.getAuthorities());
        userRepository.saveAndFlush(user);
    }
}

    private void updateUser(User user) {
        Optional<User> userToUpdateOpt= userRepository.findOneByEmail(user.getEmail());
        if(userToUpdateOpt.isPresent()){
           User userToUpdate= userToUpdateOpt.get();
           userToUpdate.setEmail(user.getEmail());
           userToUpdate.setFirstName(user.getFirstName());
           userToUpdate.setLastName(user.getLastName());
           userToUpdate.setImageUrl(user.getImageUrl());
           userToUpdate.setAuthorities(user.getAuthorities());
           //UUID uuid= UUID.randomUUID();
           //userToUpdate.setPublicId(uuid);
           userRepository.saveAndFlush(userToUpdate);

        }
    }

    public Optional<ReadUserDTO> getByPublicId(UUID publicID) {
        Optional<User> user= userRepository.findOneByPublicId(publicID);
        return user.map(userMapper::readUserDTOToUser);

    }

    public Set<Authority> getUserRoles() {
//        System.out.println(" role1  "+ attributes.get(SecurityUtils.CLAIMS_NAMESPACE));
         return this.authoritiesService;
    }



}

