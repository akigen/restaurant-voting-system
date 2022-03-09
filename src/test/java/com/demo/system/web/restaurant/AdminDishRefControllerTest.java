package com.demo.system.web.restaurant;

import com.demo.system.model.DishRef;
import com.demo.system.repository.DishRefRepository;
import com.demo.system.util.JsonUtil;
import com.demo.system.util.validation.AdminRestaurantsUtil;
import com.demo.system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.demo.system.web.restaurant.RestaurantTestData.DISH_REF_MATCHER;
import static com.demo.system.web.restaurant.RestaurantTestData.MAC_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.WASABI_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.getNewDish;
import static com.demo.system.web.restaurant.RestaurantTestData.getUpdatedDish;
import static com.demo.system.web.restaurant.RestaurantTestData.mac_chb;
import static com.demo.system.web.restaurant.RestaurantTestData.mac_chm20;
import static com.demo.system.web.restaurant.RestaurantTestData.mac_fof;
import static com.demo.system.web.restaurant.RestaurantTestData.wasabi_rsh;
import static com.demo.system.web.user.UserTestData.ADMIN_MAIL;
import static com.demo.system.web.user.UserTestData.R_ADMIN_MAIL;
import static com.demo.system.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishRefControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantsUtil.REST_URL + '/';

    @Autowired
    private DishRefRepository dishRefRepository;

    private String getUrl(int restaurantId) {
        return REST_URL + restaurantId + "/dish-ref/";
    }

    private String getUrl(int restaurantId, int id) {
        return getUrl(restaurantId) + id;
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(getUrl(MAC_ID, mac_fof.id())))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_REF_MATCHER.contentJson(mac_fof));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(getUrl(MAC_ID, mac_fof.id())))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void getNotBelong() throws Exception {
        perform(MockMvcRequestBuilders.get(getUrl(WASABI_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(getUrl(MAC_ID, wasabi_rsh.id())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(getUrl(MAC_ID, mac_fof.id())))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRefRepository.findById(mac_fof.id()).isPresent());
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void update() throws Exception {
        DishRef updated = getUpdatedDish();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(getUrl(MAC_ID, mac_fof.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_REF_MATCHER.assertMatch(dishRefRepository.getById(mac_fof.id()), getUpdatedDish());
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void createWithLocation() throws Exception {
        DishRef newDishRef = getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(getUrl(MAC_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishRef)))
                .andExpect(status().isCreated());

        DishRef created = DISH_REF_MATCHER.readFromJson(action);
        int newId = created.id();
        newDishRef.setId(newId);
        DISH_REF_MATCHER.assertMatch(created, newDishRef);
        DISH_REF_MATCHER.assertMatch(dishRefRepository.getById(newId), newDishRef);
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(getUrl(MAC_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_REF_MATCHER.contentJson(mac_fof, mac_chm20, mac_chb));
    }

    @Test
    @WithUserDetails(value = R_ADMIN_MAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(getUrl(MAC_ID, mac_fof.id()))
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(dishRefRepository.getById(mac_fof.id()).isEnabled());
    }
}