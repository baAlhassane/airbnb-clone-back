package com.alhas.airbnb.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.alhas.airbnb.user.repository",
        "com.alhas.airbnb.listing.repository",
        "com.alhas.airbnb.booking.repository"
})
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {


    
}
