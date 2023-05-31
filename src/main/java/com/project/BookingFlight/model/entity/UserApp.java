package com.project.BookingFlight.model.entity;

import com.project.BookingFlight.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "user")
@DynamicUpdate
public class UserApp implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "password",nullable = false,unique = true)
    private String password;

    @Column(name ="firstname" ,nullable = false)
    private String firstName;

    @Column(name ="lastname" ,nullable = false)
    private String lastName;

    @Column(name ="email" ,nullable = false,unique = true)
    private String email;

    @Column(name ="phonenumber" ,nullable = false)
    private String phoneNumber;

    @Column(name ="address" ,nullable = false)
    private String address;

    @Column(name ="role" ,nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.name()));
            return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
