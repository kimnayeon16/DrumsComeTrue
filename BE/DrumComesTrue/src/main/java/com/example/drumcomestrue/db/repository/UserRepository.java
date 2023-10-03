package com.example.drumcomestrue.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.drumcomestrue.common.OAuth.SocialType;
import com.example.drumcomestrue.common.OAuth.userInfo.OAuth2UserInfo;
import com.example.drumcomestrue.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(String userId);

	Optional<User> findByRefreshToken(String refreshToken);
	boolean existsByUserId(String userId);

	Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

	Optional<User> findByUserPk(long userValue);
}
