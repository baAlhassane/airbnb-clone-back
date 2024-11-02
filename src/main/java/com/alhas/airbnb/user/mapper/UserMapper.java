package com.alhas.airbnb.user.mapper;

import com.alhas.airbnb.infrastructure.SecurityUtils;
import com.alhas.airbnb.user.application.dto.ReadUserDTO;
import com.alhas.airbnb.user.domain.Authority;
import com.alhas.airbnb.user.domain.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring" )
public interface UserMapper {
    /**
     * readUserDTOToUser(User user) : Cette méthode définit une
     * transformation de l'objet User vers l'objet ReadUserDTO.
     * MapStruct génère automatiquement l'implémentation de cette méthode,
     * ce qui permet de simplifier la conversion entre les types d'objets.
     * */

    ReadUserDTO readUserDTOToUser(User user);

    default String mapAuthoritiesToString(Authority authority) {
        return authority.getName();

    }



}
