package com.example.gitryeokoffice.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 대한 Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByGithubId(String githubId);

    boolean existsByNickname(String nickname);

    boolean existsByGithubId(String githubId);
}
