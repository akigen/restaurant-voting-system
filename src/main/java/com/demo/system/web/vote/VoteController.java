package com.demo.system.web.vote;

import com.demo.system.model.Vote;
import com.demo.system.repository.VoteRepository;
import com.demo.system.web.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteRepository repository;

    @GetMapping
    public List<Vote> getOwn() {
        int authId = SecurityUtil.authId();
        log.info("getOwn for userId={}", authId);
        return repository.getByUserId(authId);
    }

    @GetMapping("/by-date")
    public ResponseEntity<Vote> getOwnByDate(@Nullable @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int authId = SecurityUtil.authId();
        if (date == null) date = LocalDate.now();
        log.info("getOwnByDate for userId={} and {}", authId, date);
        return ResponseEntity.of(repository.getByUserIdAndDate(authId, date));
    }
}