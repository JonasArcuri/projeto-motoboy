package com.example.projeto_motoboy.service;

import com.example.projeto_motoboy.dto.VagaDTO;
import com.example.projeto_motoboy.model.Estabelecimento;
import com.example.projeto_motoboy.model.Vaga;
import com.example.projeto_motoboy.repository.EstabelecimentoRepository;
import com.example.projeto_motoboy.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    public Vaga criarVaga(VagaDTO vagaDTO) {
        Long userId = vagaDTO.getEstabelecimentoId(); // aqui na verdade é o ID do usuário logado
        Estabelecimento estabelecimento = estabelecimentoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado para userId: " + userId));

        Vaga vaga = new Vaga();
        vaga.setDescricao(vagaDTO.getDescricao());
        vaga.setEndereco(vagaDTO.getEndereco());
        vaga.setValor(vagaDTO.getValor());
        vaga.setTaxas(vagaDTO.getTaxas());
        vaga.setHorario(vagaDTO.getHorario());
        vaga.setZona(vagaDTO.getZona());
        vaga.setTipo(vagaDTO.getTipo());
        vaga.setEstabelecimento(estabelecimento);

        return vagaRepository.save(vaga);
    }

    public List<Vaga> listarTodas() {
        return vagaRepository.findAll();
    }

    public List<Vaga> listarPorEstabelecimento(Long userId) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        return vagaRepository.findByEstabelecimentoId(estabelecimento.getId());
    }
}
