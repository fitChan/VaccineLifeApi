package com.vaccinelife.vaccinelifeapi.repository;

import com.vaccinelife.vaccinelifeapi.model.Medical;
import com.vaccinelife.vaccinelifeapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

@EnableJpaAuditing
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);


    boolean existsByUsername(String username); //DB에 유저 아이디 존재여부 판별
    boolean existsByNickname(String nickname); //DB에  닉네임 존재여부 판별

    List<User> findAllByAfterEffectContaining(String effect);
}
