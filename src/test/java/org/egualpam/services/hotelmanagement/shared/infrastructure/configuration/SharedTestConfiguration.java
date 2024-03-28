package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.ReviewTestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class SharedTestConfiguration {

    @Bean
    public HotelTestRepository hotelTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new HotelTestRepository(namedParameterJdbcTemplate);
    }

    @Bean
    public ReviewTestRepository reviewTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new ReviewTestRepository(namedParameterJdbcTemplate);
    }

    @Bean
    public EventStoreTestRepository eventStoreTestRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new EventStoreTestRepository(namedParameterJdbcTemplate);
    }
}