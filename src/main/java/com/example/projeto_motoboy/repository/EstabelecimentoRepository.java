package com.example.projeto_motoboy.repository;

import com.example.projeto_motoboy.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {
    Optional<Estabelecimento> findByUserId(Long userId);
    boolean existsByCnpj(String cnpj);
    Optional<Estabelecimento> findByCnpj(String cnpj);
}