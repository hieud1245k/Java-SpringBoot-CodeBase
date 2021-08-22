package com.fcn.fanscoin.repository;

import com.fcn.fanscoin.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
        extends JpaRepository<Role, Long> {
}
