package com.example.projeto_motoboy.repository;

import com.example.projeto_motoboy.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByEstabelecimentoId(Long estabelecimentoId);
}
