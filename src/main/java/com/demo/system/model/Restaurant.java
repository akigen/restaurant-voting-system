package com.demo.system.model;

import com.demo.system.util.validation.NoHtml;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "address"}, name = "uk_restaurant")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Column(name = "address")
    @Size(max = 1024)
    @NoHtml
    @Nullable
    private String address;

    public Restaurant(Integer id, String name, @Nullable String address) {
        super(id, name);
        this.address = address;
    }
}
