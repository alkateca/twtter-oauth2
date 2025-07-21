package com.jose.twttr_security.repository;

import com.jose.twttr_security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolerRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
