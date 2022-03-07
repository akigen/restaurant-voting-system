package com.demo.system.web.vote;

import com.demo.system.model.Vote;
import com.demo.system.repository.VoteRepository;
import com.demo.system.service.VoteService;
import com.demo.system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.demo.system.web.restaurant.RestaurantTestData.WASABI_ID;
import static com.demo.system.web.user.UserTestData.ADMIN_ID;
import static com.demo.system.web.user.UserTestData.ADMIN_MAIL;
import static com.demo.system.web.user.UserTestData.USER_ID;
import static com.demo.system.web.user.UserTestData.USER_MAIL;
import static com.demo.system.web.vote.VoteTestData.VOTE_MATCHER;
import static com.demo.system.web.vote.VoteTestData.vote_1;
import static com.demo.system.web.vote.VoteTestData.vote_2;
import static com.demo.system.web.vote.VoteTestData.vote_3;
import static com.demo.system.web.vote.VoteTestData.vote_4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getOwn() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote_1, vote_2, vote_3));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getOwnForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-date"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote_1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getOwnByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-date")
                .param("date", "2021-06-05"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote_4));
    }


    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void voteToday() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", Integer.toString(WASABI_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        LocalDate date = created.getActualDate();
        VOTE_MATCHER.assertMatch(created, voteRepository.getByUserIdAndDate(ADMIN_ID, date).get());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void revoteTodayBeforeDeadline() throws Exception {
        VoteService.setDeadline(LocalTime.MAX);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", Integer.toString(WASABI_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(voteRepository.getByUserIdAndDate(USER_ID, LocalDate.now()).get().getRestaurantId(), WASABI_ID);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void revoteTodayAfterDeadline() throws Exception {
        VoteService.setDeadline(LocalTime.MIN);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .param("restaurantId", Integer.toString(WASABI_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteTodayBeforeDeadline() throws Exception {
        VoteService.setDeadline(LocalTime.MAX);
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("restaurantId", Integer.toString(WASABI_ID)))
                .andExpect(status().isNoContent());
        assertFalse(voteRepository.getByUserIdAndDate(USER_ID, LocalDate.now()).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteTodayAfterDeadline() throws Exception {
        VoteService.setDeadline(LocalTime.MIN);
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .param("restaurantId", Integer.toString(WASABI_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}