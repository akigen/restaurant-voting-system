package com.demo.system.web.vote;

import com.demo.system.model.Vote;
import com.demo.system.web.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.demo.system.web.restaurant.RestaurantTestData.MAC_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.SHALYPIN_ID;
import static com.demo.system.web.restaurant.RestaurantTestData.WASABI_ID;
import static com.demo.system.web.user.UserTestData.admin;
import static com.demo.system.web.user.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final Vote vote_1 = new Vote(1, user, LocalDate.now(), LocalTime.parse("12:30:00"), MAC_ID);
    public static final Vote vote_2 = new Vote(2, user, LocalDate.parse("2021-06-05"), LocalTime.parse("09:15:00"), MAC_ID);
    public static final Vote vote_3 = new Vote(3, user, LocalDate.parse("2021-06-04"), LocalTime.parse("15:55:00"), WASABI_ID);
    public static final Vote vote_4 = new Vote(4, admin, LocalDate.parse("2021-06-05"), LocalTime.parse("08:15:00"), SHALYPIN_ID);
    public static final Vote vote_5 = new Vote(5, admin, LocalDate.parse("2021-06-04"), LocalTime.parse("12:55:00"), WASABI_ID);
}
