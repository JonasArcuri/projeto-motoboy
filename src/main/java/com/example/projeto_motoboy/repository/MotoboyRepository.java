package com.example.projeto_motoboy.repository;

import com.example.projeto_motoboy.model.Motoboy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MotoboyRepository extends JpaRepository<Motoboy, Long> {
    Optional<Motoboy> findByUserId(Long userId);
    boolean existsByCpf(String cpf);
    Optional<Motoboy> findByCpf(String cpf);
    boolean existsByPlacaMoto(String placaMoto);
}