package com.demo.system.service;

import com.demo.system.error.DataConflictException;
import com.demo.system.model.User;
import com.demo.system.model.Vote;
import com.demo.system.repository.RestaurantRepository;
import com.demo.system.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private  final VoteRepository repository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    static LocalTime deadline = LocalTime.of(11, 0);

    @Transactional
    public Vote createToday(User user, int restaurantId) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Vote> dbVote = repository.getByUserIdAndDate(user.id(), now.toLocalDate());
        dbVote.ifPresent(v -> {
            throw new DataConflictException("Already voted today");
        });
        restaurantRepository.checkAvailable(restaurantId);
        Vote vote = new Vote(null, user, now.toLocalDate(), now.toLocalTime(), restaurantId);
        return repository.save(vote);
    }

    @Transactional
    public void updateToday(User user, int restaurantId, boolean deleteVote) {
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalTime().isAfter(deadline)) {
            throw new DataConflictException("Deadline for change vote has passed");
        }
        Optional<Vote> dbVote = repository.getByUserIdAndDate(user.id(), now.toLocalDate());
        Vote vote = dbVote.orElseThrow(() -> new DataConflictException("Have not voted today"));
        if (deleteVote) {
            repository.delete(vote.id());
        } else {
            restaurantRepository.checkAvailable(restaurantId);
            vote.setActualTime(now.toLocalTime());
            vote.setRestaurantId(restaurantId);
        }
    }


}
