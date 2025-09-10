package com.example.projeto_motoboy.controller;

import com.example.projeto_motoboy.dto.*;
import com.example.projeto_motoboy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*") // Em produção, especificar os domínios permitidos
public class UserController {

    @Autowired
    private UserService userService;

    // ========== CRIAR USUÁRIO ==========
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            UserResponseDTO createdUser = userService.createUser(userCreateDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== BUSCAR TODOS OS USUÁRIOS ==========
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // ========== BUSCAR USUÁRIO POR ID ==========
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== ATUALIZAR USUÁRIO ==========
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDTO userUpdateDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== DELETAR USUÁRIO ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(new SuccessResponse("Usuário deletado com sucesso"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== BUSCAR USUÁRIO POR EMAIL ==========
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            UserResponseDTO user = userService.findByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== BUSCAR USUÁRIOS POR TIPO ==========
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getUsersByTipo(@PathVariable String tipo) {
        try {
            if (!tipo.equals("Estabelecimento") && !tipo.equals("Motoboy")) {
                return new ResponseEntity<>(new ErrorResponse("Tipo inválido. Use 'Estabelecimento' ou 'Motoboy'"), HttpStatus.BAD_REQUEST);
            }
            List<UserResponseDTO> users = userService.getAllUsers().stream()
                    .filter(user -> tipo.equals(user.getTipo()))
                    .toList();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== LOGIN SIMPLES (para teste) ==========
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            UserResponseDTO user = userService.findByEmail(loginDTO.getEmail());
            // Em produção: verificar senha com hash
            // if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse("Credenciais inválidas"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Erro interno do servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ========== CLASSES DE RESPOSTA ==========
    public static class ErrorResponse {
        private String message;
        private long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }

    public static class SuccessResponse {
        private String message;
        private long timestamp;

        public SuccessResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}