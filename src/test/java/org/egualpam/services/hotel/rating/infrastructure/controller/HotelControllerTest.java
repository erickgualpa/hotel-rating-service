package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @MockBean
    private FindHotelsByRatingAverage findHotelsByRatingAverage;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() throws Exception {
        String request = """
                    {
                        "location": "%s",
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        randomAlphabetic(5),
                        100,
                        150
                );

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void badRequestIsReturnedWhenPriceRangeFilterIsInvalid() throws Exception {
        String request = """
                    {
                        "priceRange": {
                            "begin": %d,
                            "end": %d
                        }
                    }
                """.formatted
                (
                        500,
                        50
                );

        this.mockMvc
                .perform(
                        post("/v1/hotels/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                ).andExpect(status().isBadRequest());
    }
}
