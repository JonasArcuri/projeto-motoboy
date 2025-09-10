package com.example.projeto_motoboy.repository;

import com.example.projeto_motoboy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.estabelecimento LEFT JOIN FETCH u.motoboy WHERE u.id = :id")
    Optional<User> findByIdWithDetails(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.estabelecimento LEFT JOIN FETCH u.motoboy")
    List<User> findAllWithDetails();

    List<User> findByTipo(String tipo);

    boolean existsByEmail(String email);
}