package com.shopeasy.shopeasy.repository;

import com.shopeasy.shopeasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String eameil);

    boolean existsByPublicId(String publicId);

    Optional<Void> deleteByPublicId(String publicId);

    Optional<User> findByEmail(String email);

    Optional<User> findByPublicId(String publicId);
}
