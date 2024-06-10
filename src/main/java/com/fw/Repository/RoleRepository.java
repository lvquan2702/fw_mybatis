package com.fw.Repository;

import com.fw.model.ERole;
import com.fw.model.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(ERole name);
}
