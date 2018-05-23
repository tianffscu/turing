package com.scu.turing.service.repository;

import com.scu.turing.entity.UserRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRateRepository extends JpaRepository<UserRate, Long> {

    UserRate findByUserId(long userId);

}
