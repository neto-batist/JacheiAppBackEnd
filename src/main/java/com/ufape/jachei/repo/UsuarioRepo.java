package com.ufape.jachei.repo;

import com.ufape.jachei.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}