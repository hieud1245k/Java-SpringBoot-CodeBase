package com.fcn.fanscoin.repository;

import com.fcn.fanscoin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {
}
