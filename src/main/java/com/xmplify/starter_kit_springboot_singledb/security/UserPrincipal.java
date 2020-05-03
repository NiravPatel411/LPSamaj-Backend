package com.xmplify.starter_kit_springboot_singledb.security;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.payload.AuthAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.model.User;

public class UserPrincipal implements UserDetails {
    private String id;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;
    
    @JsonIgnore
    private String mobileno;

    @JsonIgnore
    private String role;

    @JsonIgnore
    private AuthAdmin authAdmin;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id, String firstName, String lastName, String email, String password,String mobileno, Collection<? extends GrantedAuthority> authorities,AuthAdmin authAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
        this.authorities = authorities;
        this.authAdmin = authAdmin;
    }

    public static UserPrincipal create(User user, AuthAdmin authAdmin) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getMobileno(),
                authorities,
                authAdmin
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}