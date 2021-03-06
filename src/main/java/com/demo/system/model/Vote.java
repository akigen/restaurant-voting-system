package com.demo.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "actual_date"}, name = "uk_vote")})
@Getter
@Setter
@NoArgsConstructor
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonIgnore
    private User user;

    @Column(name = "actual_date", nullable = false)
    @NotNull
    private LocalDate actualDate;

    @Column(name = "actual_time", nullable = false)
    @NotNull
    private LocalTime actualTime;

    @Column(name = "restaurant_id")
    private int restaurantId;

    public Vote(Integer id, @NotNull User user, @NotNull LocalDate actualDate, @NotNull LocalTime actualTime, int restaurantId) {
        super(id);
        this.user = user;
        this.actualDate = actualDate;
        this.actualTime = actualTime;
        this.restaurantId = restaurantId;
    }
}
