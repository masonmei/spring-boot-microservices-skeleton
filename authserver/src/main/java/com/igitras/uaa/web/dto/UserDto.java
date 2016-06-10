package com.igitras.uaa.web.dto;

import static com.igitras.common.utils.Constants.Constrains.LOGIN_REGEX;

import com.google.common.base.MoreObjects;
import com.igitras.uaa.domain.entity.Authority;
import com.igitras.uaa.domain.entity.User;
import org.hibernate.validator.constraints.Email;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author mason
 */
public class UserDto {
    @NotNull
    @Pattern(regexp = LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

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

    public UserDto() {
    }

    public UserDto(User user) {
        // @formatter:off
        this(user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActivated(),
                user.getLangKey(),
                user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toSet()));
        // @formatter:on
    }

    public UserDto(String login, String firstName, String lastName, String email, boolean activated, String langKey,
            Set<String> authorities) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("login", login)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .add("activated", activated)
                .add("langKey", langKey)
                .add("authorities", authorities)
                .toString();
    }
}
