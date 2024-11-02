package com.alhas.airbnb.infrastructure;

import com.alhas.airbnb.user.domain.Authority;
import com.alhas.airbnb.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SecurityUtils {
    public static final String ROLE_TENANT = "ROLE_TENANT";
    public static final String ROLE_LANDLORD = "ROLE_LANDLORD";
   // public static final String CLAIMS_NAMESPACE = "https://www.alhas.com/roles";
    public static final String CLAIMS_NAMESPACE = "https://alhas.com/roles";

    public static User mapOauth2AttributesToUser(Map<String, Object> attributes) {
      User user = new User();
      String sub =String.valueOf(attributes.get("sub"));

      String username = null ;

      if(attributes.get("preferred_username")!=null){
          username = ((String)  attributes.get("preferred_username")).toLowerCase();
      }

        if(attributes.get("given_name")!=null){
            user.setFirstName(((String)  attributes.get("given_name")).toLowerCase());
        }
        else if (attributes.get("nickname")!=null){
            user.setFirstName(((String)  attributes.get("nickname")).toLowerCase());
        }

        if(attributes.get("family_name")!=null){
            user.setLastName(((String)  attributes.get("family_name")));
        }

        if(attributes.get("email")!=null){
            user.setEmail(((String)  attributes.get("email")).toLowerCase());
        }
        else if (sub.contains("|") && (username != null && username.contains("@"))) {
            user.setEmail(username);
        }
        else {
            user.setEmail(sub);
        }

        if(attributes.get("picture")!=null){
            user.setImageUrl(((String)  attributes.get("picture")));
        }


        if(attributes.get(CLAIMS_NAMESPACE)!=null){
            List<String> auithoritiesRaw= (List<String>) attributes.get(CLAIMS_NAMESPACE);
            Set<Authority> authorities = auithoritiesRaw.stream()
                    .map(authority-> {
                        Authority authority1 = new Authority();
                        authority1.setName(authority);
                        return authority1;
                    }).collect(Collectors.toSet());

            user.setAuthorities(authorities);
        }


        return user;
    }

    public static List<SimpleGrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {

        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));

    }

    private static Collection<String> getRolesFromClaims(Map<String, Object> claims) {
        return (List<String>) claims.get(CLAIMS_NAMESPACE);
    }

    public static List<SimpleGrantedAuthority>  mapRolesToGrantedAuthorities(Collection<String > roles) {
            return roles.stream()
                    .filter(role-> role.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
    }


public static boolean hasCurrentUserAuthorites(String ...authorities) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication !=null && getAuthorities(authentication)
            .anyMatch(authority-> Arrays.asList(authorities).contains(authority)));
}

    private static Stream<String> getAuthorities(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication instanceof JwtAuthenticationToken jwtAuthenticationToken ?
                extractAuthorityFromClaims(jwtAuthenticationToken.getToken().getClaims()) : authentication.getAuthorities();

        return  authorities.stream().map(GrantedAuthority::getAuthority);
    }


}
