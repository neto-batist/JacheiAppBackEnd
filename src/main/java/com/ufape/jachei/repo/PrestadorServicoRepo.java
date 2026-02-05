package com.ufape.jachei.repo;

import com.ufape.jachei.models.PrestadorServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestadorServicoRepo extends JpaRepository<PrestadorServico, Long> {

    // Fórmula de Haversine para buscar prestadores num raio de X km
    @Query(value = "SELECT *, " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(latitude)))) AS distance " +
            "FROM prestadorservicos " +
            "HAVING distance < :distanceKm " +
            "ORDER BY distance ASC",
            nativeQuery = true)
    List<PrestadorServico> findNearbyPrestadors(
            @Param("userLat") double userLat,
            @Param("userLng") double userLng,
            @Param("distanceKm") double distanceKm);

    Optional<PrestadorServico> findByFirebaseUid(String firebaseUid);
}