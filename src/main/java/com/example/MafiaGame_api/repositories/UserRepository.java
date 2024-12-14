package com.example.MafiaGame_api.repositories;


import com.example.MafiaGame_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDeletedFalse();

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);
    @Query("SELECT u.email FROM User u WHERE u.deleted = false AND u.role <> 'customer'")
    List<String> findEmailsByRoleNotCustomer();
}
