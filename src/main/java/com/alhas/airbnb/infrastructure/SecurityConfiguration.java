package com.alhas.airbnb.infrastructure;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configuration(final HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        requestAttributeHandler.setCsrfRequestAttributeName(null);
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "api/tenant-listing/get-all-by-category").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/tenant-listing/get-one").permitAll()
                        .requestMatchers(HttpMethod.GET,"assets/*").permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(csrfToken -> csrfToken.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(requestAttributeHandler))
               // .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("http://localhost:4200/", true)) // Ajout de la redirection vers Angular après la connexion
                .oauth2ResourceServer(auth2ResourceServer -> auth2ResourceServer.jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://dev-lbu4c820m2nza8vh.us.auth0.com/.well-known/jwks.json").build();
    }



    @Bean
    public GrantedAuthoritiesMapper    userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            authorities.forEach(grantedAuthority -> {
                if (grantedAuthority instanceof OidcUserAuthority oidcUserAuthority) {
                    grantedAuthorities.addAll(SecurityUtils
                            .extractAuthorityFromClaims(oidcUserAuthority.
                                    getUserInfo().getClaims()));
                }
            });
            return grantedAuthorities;
        };
    }

}
