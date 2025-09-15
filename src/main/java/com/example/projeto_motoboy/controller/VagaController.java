package com.example.projeto_motoboy.controller;

import com.example.projeto_motoboy.dto.VagaDTO;
import com.example.projeto_motoboy.model.Vaga;
import com.example.projeto_motoboy.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@CrossOrigin(origins = "http://localhost:5173")
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @PostMapping
    public Vaga criarVaga(@RequestBody VagaDTO vagaDTO) {
        return vagaService.criarVaga(vagaDTO);
    }

    @GetMapping
    public List<Vaga> listarTodas() {
        return vagaService.listarTodas();
    }

    @GetMapping("/estabelecimento/{id}")
    public List<Vaga> listarPorEstabelecimento(@PathVariable Long id) {
        return vagaService.listarPorEstabelecimento(id);
    }
}
