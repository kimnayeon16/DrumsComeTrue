package com.example.drumcomestrue.common.OAuth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.example.drumcomestrue.db.entity.Role;

import lombok.Getter;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String userId;
    private final Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                             String nameAttributeKey, String username, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.userId = username;
        this.role = role;
    }

    // public static CustomOAuth2User of(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
    //                                   String nameAttributeKey, String username, Role role) {
    //     return new CustomOAuth2User(authorities, attributes, nameAttributeKey, username, role);
    // }
}