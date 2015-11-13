package com.emmaprojects.creativehub.web.rest.dto;

import com.emmaprojects.creativehub.domain.Authority;
import com.emmaprojects.creativehub.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    private List<String> roles;

    private Set<ExternalAccountDTO> externalAccounts = new HashSet<>();



    public UserDTO() {
    }

    public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles, Set<ExternalAccountDTO> externalAccounts) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
        if (externalAccounts != null) {
            this.externalAccounts.addAll(externalAccounts);
        }
    }

    public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles) {
        this(login, password, firstName, lastName, email, langKey, roles, null);
    }

    public UserDTO(String login, String firstName, String lastName, String email, ExternalAccountDTO externalAccount) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        if (externalAccount != null) {
            this.externalAccounts.add(externalAccount);
        }
    }

    public UserDTO(User user) {
        this.firstName = user.getFirstName();
        this.activated = user.getActivated();
        this.authorities = user.getAuthorities().stream().map(a-> a.getName()).collect(Collectors.toSet());
        this.email = user.getEmail();
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public Set<ExternalAccountDTO> getExternalAccounts() {
        return externalAccounts;
    }

    public void setExternalAccounts(Set<ExternalAccountDTO> externalAccounts) {
        this.externalAccounts = externalAccounts;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }

}
