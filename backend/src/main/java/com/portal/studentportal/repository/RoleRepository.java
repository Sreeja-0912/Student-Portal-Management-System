package com.portal.studentportal.repository;

import com.portal.studentportal.entity.Role;
import com.portal.studentportal.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
