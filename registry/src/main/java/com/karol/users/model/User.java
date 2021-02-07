package com.karol.users.model;


import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
@RequiredArgsConstructor
public class User {

    private @Id
    @GeneratedValue
    Long id;
    private final String username;
    private final Date dateOfBirth;
    private final String country;

    private final Boolean testUser;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public Boolean getTestUser() {
        return testUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(dateOfBirth, user.dateOfBirth) &&
                Objects.equals(country, user.country) &&
                Objects.equals(testUser, user.testUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, dateOfBirth, country, testUser);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", country='" + country + '\'' +
                ", testUser=" + testUser +
                '}';
    }
}
