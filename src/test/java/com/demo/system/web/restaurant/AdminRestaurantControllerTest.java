package com.demo.system.web.restaurant;

import com.demo.system.model.Restaurant;
import com.demo.system.repository.RestaurantRepository;
import com.demo.system.repository.VoteRepository;
import com.demo.system.util.JsonUtil;
import com.demo.system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.demo.system.web.restaurant.RestaurantTestData.MAC_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.RESTAURANT_MATCHER;
import static com.demo.system.web.restaurant.RestaurantTestData.getNew;
import static com.demo.system.web.restaurant.RestaurantTestData.getUpdated;
import static com.demo.system.web.restaurant.RestaurantTestData.mac;
import static com.demo.system.web.restaurant.RestaurantTestData.shalypin;
import static com.demo.system.web.restaurant.RestaurantTestData.wasabi;
import static com.demo.system.web.user.UserTestData.ADMIN_MAIL;
import static com.demo.system.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MAC_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(mac));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteWithoutVote() throws Exception {
        voteRepository.deleteByRestaurantId(MAC_ID);
        perform(MockMvcRequestBuilders.delete(REST_URL + MAC_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(MAC_ID).isPresent());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + MAC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(MAC_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(wasabi, mac, shalypin));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + MAC_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(restaurantRepository.getById(MAC_ID).isEnabled());
    }
}
