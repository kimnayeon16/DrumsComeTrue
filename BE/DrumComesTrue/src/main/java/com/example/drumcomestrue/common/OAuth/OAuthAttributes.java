package com.example.drumcomestrue.common.OAuth;

import java.util.Map;
import java.util.UUID;

import com.example.drumcomestrue.common.OAuth.userInfo.GoogleOAuth2UserInfo;
import com.example.drumcomestrue.common.OAuth.userInfo.KakaoOAuth2UserInfo;
import com.example.drumcomestrue.common.OAuth.userInfo.OAuth2UserInfo;
import com.example.drumcomestrue.db.entity.Role;
import com.example.drumcomestrue.db.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

	private final String nameAttributeKey;
	private final OAuth2UserInfo oAuth2UserInfo;

	public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
		if (socialType == SocialType.KAKAO) {
			return ofKakao(userNameAttributeName, attributes);
		}
		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
			.build();
	}

	public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
			.build();
	}

	public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {

		return User.builder()
			.socialType(socialType)
			.socialId(oauth2UserInfo.getId())
			.userId(UUID.randomUUID().toString())
			.userName(oauth2UserInfo.getNickname())
			.role(Role.GUEST)
			.build();
	}

}
