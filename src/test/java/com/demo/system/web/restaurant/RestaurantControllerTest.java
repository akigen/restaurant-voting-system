package com.demo.system.web.restaurant;

import com.demo.system.mapper.RestaurantMapper;
import com.demo.system.repository.RestaurantRepository;
import com.demo.system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.demo.system.web.restaurant.RestaurantTestData.MAC_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.RESTAURANT_MATCHER;
import static com.demo.system.web.restaurant.RestaurantTestData.RESTAURANT_MATCHER_WITH_MENU;
import static com.demo.system.web.restaurant.RestaurantTestData.SHALYPIN_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.mac;
import static com.demo.system.web.restaurant.RestaurantTestData.wasabi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;
    @Autowired
    private RestaurantMapper mapper;

    @Test
    void getWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menu_today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_WITH_MENU.contentJson(mapper.toTo(wasabi), mapper.toTo(mac)));
    }

    @Test
    void getWithMenuByRestaurantForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MAC_ID + "/menu_today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER_WITH_MENU.contentJson(mapper.toTo(mac)));
    }

    @Test
    void getDisabled() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + SHALYPIN_ID + "/menu_today"))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllEnabled() throws Exception {
        repository.save(mac);
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(wasabi, mac));
    }
}
