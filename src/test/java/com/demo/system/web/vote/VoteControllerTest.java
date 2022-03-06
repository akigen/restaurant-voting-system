package com.demo.system.web.vote;

import com.demo.system.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.demo.system.web.user.UserTestData.ADMIN_MAIL;
import static com.demo.system.web.user.UserTestData.USER_MAIL;
import static com.demo.system.web.vote.VoteTestData.VOTE_MATCHER;
import static com.demo.system.web.vote.VoteTestData.vote_1;
import static com.demo.system.web.vote.VoteTestData.vote_2;
import static com.demo.system.web.vote.VoteTestData.vote_3;
import static com.demo.system.web.vote.VoteTestData.vote_4;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

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
}