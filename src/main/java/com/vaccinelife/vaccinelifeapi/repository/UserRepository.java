package com.vaccinelife.vaccinelifeapi.repository;

import com.vaccinelife.vaccinelifeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);

//    Optional<User> findById(Long id);

    User getById(Long id);
    boolean existsByUsername(String username); //DB에 유저 아이디 존재여부 판별
    boolean existsByNickname(String nickname); //DB에  닉네임 존재여부 판별

//    List<User> findAllByAfterEffectContaining(String effect);
}
