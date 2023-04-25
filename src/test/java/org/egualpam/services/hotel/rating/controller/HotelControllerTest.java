package org.egualpam.services.hotel.rating.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class HotelControllerTest {

    @MockBean private HotelService hotelService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryIsAcceptedSuccessfully() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    void hotelsMatchingQueryAreReturnedSuccessfully() throws Exception {
        when(hotelService.findHotelsMatchingQuery(any(HotelQuery.class)))
                .thenReturn(List.of(new RatedHotel()));

        this.mockMvc
                .perform(
                        post("/api/hotel/query")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)));
    }
}
