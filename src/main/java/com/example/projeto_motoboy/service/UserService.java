// ========== USER SERVICE ATUALIZADO ==========
package com.example.projeto_motoboy.service;

import com.example.projeto_motoboy.dto.*;
import com.example.projeto_motoboy.model.*;
import com.example.projeto_motoboy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private MotoboyRepository motoboyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========== CRIAR USUÁRIO ==========
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        validateUserCreation(userCreateDTO);

        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword())); // Encodado
        user.setEndereco(userCreateDTO.getEndereco());
        user.setBairro(userCreateDTO.getBairro());
        user.setCidade(userCreateDTO.getCidade());
        user.setUf(userCreateDTO.getUf());
        user.setCep(userCreateDTO.getCep());
        user.setTipo(userCreateDTO.getTipo());

        user = userRepository.save(user);

        if ("Estabelecimento".equals(userCreateDTO.getTipo())) {
            createEstabelecimento(user, userCreateDTO);
        } else if ("Motoboy".equals(userCreateDTO.getTipo())) {
            createMotoboy(user, userCreateDTO);
        }

        return convertToResponseDTO(userRepository.findByIdWithDetails(user.getId()).orElse(user));
    }

    // ========== BUSCAR USUÁRIO POR ID ==========
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return convertToResponseDTO(user);
    }

    // ========== BUSCAR TODOS OS USUÁRIOS ==========
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllWithDetails().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ========== ATUALIZAR USUÁRIO ==========
    public UserResponseDTO updateUser(Long id, UserCreateDTO userUpdateDTO) {
        User user = userRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getTipo().equals(userUpdateDTO.getTipo())) {
            throw new RuntimeException("Não é possível alterar o tipo do usuário");
        }

        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword())); // encode ao atualizar
        }

        user.setEndereco(userUpdateDTO.getEndereco());
        user.setBairro(userUpdateDTO.getBairro());
        user.setCidade(userUpdateDTO.getCidade());
        user.setUf(userUpdateDTO.getUf());
        user.setCep(userUpdateDTO.getCep());

        if ("Estabelecimento".equals(user.getTipo())) {
            updateEstabelecimento(user, userUpdateDTO);
        } else if ("Motoboy".equals(user.getTipo())) {
            updateMotoboy(user, userUpdateDTO);
        }

        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }

    // ========== DELETAR USUÁRIO ==========
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    // ========== BUSCAR POR EMAIL ==========
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user = userRepository.findByIdWithDetails(user.getId()).orElse(user);

        return convertToResponseDTO(user);
    }

    // ========== VALIDAÇÕES PRIVADAS ==========
    private void validateUserCreation(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        if ("Estabelecimento".equals(dto.getTipo())) {
            validateEstabelecimentoFields(dto);
        } else if ("Motoboy".equals(dto.getTipo())) {
            validateMotoboyFields(dto);
        } else {
            throw new RuntimeException("Tipo de usuário inválido");
        }
    }

    private void validateEstabelecimentoFields(UserCreateDTO dto) {
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new RuntimeException("CNPJ é obrigatório para estabelecimentos");
        }
        if (dto.getNomeFantasia() == null || dto.getNomeFantasia().trim().isEmpty()) {
            throw new RuntimeException("Nome fantasia é obrigatório para estabelecimentos");
        }
        if (dto.getTelefone() == null || dto.getTelefone().trim().isEmpty()) {
            throw new RuntimeException("Telefone é obrigatório para estabelecimentos");
        }
        if (estabelecimentoRepository.existsByCnpj(dto.getCnpj())) {
            throw new RuntimeException("CNPJ já está em uso");
        }
    }

    private void validateMotoboyFields(UserCreateDTO dto) {
        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            throw new RuntimeException("CPF é obrigatório para motoboys");
        }
        if (dto.getCnh() == null || dto.getCnh().trim().isEmpty()) {
            throw new RuntimeException("CNH é obrigatória para motoboys");
        }
        if (dto.getPlacaMoto() == null || dto.getPlacaMoto().trim().isEmpty()) {
            throw new RuntimeException("Placa da moto é obrigatória para motoboys");
        }
        if (dto.getModeloMoto() == null || dto.getModeloMoto().trim().isEmpty()) {
            throw new RuntimeException("Modelo da moto é obrigatório para motoboys");
        }
        if (dto.getTelefoneContato() == null || dto.getTelefoneContato().trim().isEmpty()) {
            throw new RuntimeException("Telefone de contato é obrigatório para motoboys");
        }
        if (motoboyRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já está em uso");
        }
        if (motoboyRepository.existsByPlacaMoto(dto.getPlacaMoto())) {
            throw new RuntimeException("Placa da moto já está em uso");
        }
    }

    // ========== CRIAR RELACIONADOS ==========
    private void createEstabelecimento(User user, UserCreateDTO dto) {
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setUser(user);
        estabelecimento.setCnpj(dto.getCnpj());
        estabelecimento.setNomeFantasia(dto.getNomeFantasia());
        estabelecimento.setCategoria(dto.getCategoria());
        estabelecimento.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        estabelecimento.setTelefone(dto.getTelefone());
        estabelecimento.setDescricao(dto.getDescricao());

        estabelecimentoRepository.save(estabelecimento);
        user.setEstabelecimento(estabelecimento);
    }

    private void createMotoboy(User user, UserCreateDTO dto) {
        Motoboy motoboy = new Motoboy();
        motoboy.setUser(user);
        motoboy.setCpf(dto.getCpf());
        motoboy.setCnh(dto.getCnh());
        motoboy.setPlacaMoto(dto.getPlacaMoto());
        motoboy.setModeloMoto(dto.getModeloMoto());
        motoboy.setAnoMoto(dto.getAnoMoto());
        motoboy.setExperiencia(dto.getExperiencia());
        motoboy.setTelefoneContato(dto.getTelefoneContato());

        motoboyRepository.save(motoboy);
        user.setMotoboy(motoboy);
    }

    // ========== ATUALIZAR RELACIONADOS ==========
    private void updateEstabelecimento(User user, UserCreateDTO dto) {
        Estabelecimento estabelecimento = user.getEstabelecimento();
        if (estabelecimento == null) {
            createEstabelecimento(user, dto);
            return;
        }

        estabelecimento.setCnpj(dto.getCnpj());
        estabelecimento.setNomeFantasia(dto.getNomeFantasia());
        estabelecimento.setCategoria(dto.getCategoria());
        estabelecimento.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        estabelecimento.setTelefone(dto.getTelefone());
        estabelecimento.setDescricao(dto.getDescricao());

        estabelecimentoRepository.save(estabelecimento);
    }

    private void updateMotoboy(User user, UserCreateDTO dto) {
        Motoboy motoboy = user.getMotoboy();
        if (motoboy == null) {
            createMotoboy(user, dto);
            return;
        }

        motoboy.setCpf(dto.getCpf());
        motoboy.setCnh(dto.getCnh());
        motoboy.setPlacaMoto(dto.getPlacaMoto());
        motoboy.setModeloMoto(dto.getModeloMoto());
        motoboy.setAnoMoto(dto.getAnoMoto());
        motoboy.setExperiencia(dto.getExperiencia());
        motoboy.setTelefoneContato(dto.getTelefoneContato());

        motoboyRepository.save(motoboy);
    }

    // ========== CONVERSÃO ==========
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEndereco(user.getEndereco());
        dto.setBairro(user.getBairro());
        dto.setCidade(user.getCidade());
        dto.setUf(user.getUf());
        dto.setCep(user.getCep());
        dto.setTipo(user.getTipo());

        if (user.getEstabelecimento() != null) {
            EstabelecimentoDTO estabDTO = new EstabelecimentoDTO();
            estabDTO.setId(user.getEstabelecimento().getId());
            estabDTO.setCnpj(user.getEstabelecimento().getCnpj());
            estabDTO.setNomeFantasia(user.getEstabelecimento().getNomeFantasia());
            estabDTO.setCategoria(user.getEstabelecimento().getCategoria());
            estabDTO.setHorarioFuncionamento(user.getEstabelecimento().getHorarioFuncionamento());
            estabDTO.setTelefone(user.getEstabelecimento().getTelefone());
            estabDTO.setDescricao(user.getEstabelecimento().getDescricao());
            dto.setEstabelecimento(estabDTO);
        }

        if (user.getMotoboy() != null) {
            MotoboyDTO motoboyDTO = new MotoboyDTO();
            motoboyDTO.setId(user.getMotoboy().getId());
            motoboyDTO.setCpf(user.getMotoboy().getCpf());
            motoboyDTO.setCnh(user.getMotoboy().getCnh());
            motoboyDTO.setPlacaMoto(user.getMotoboy().getPlacaMoto());
            motoboyDTO.setModeloMoto(user.getMotoboy().getModeloMoto());
            motoboyDTO.setAnoMoto(user.getMotoboy().getAnoMoto());
            motoboyDTO.setExperiencia(user.getMotoboy().getExperiencia());
            motoboyDTO.setTelefoneContato(user.getMotoboy().getTelefoneContato());
            dto.setMotoboy(motoboyDTO);
        }

        return dto;
    }
}
