package com.selimcagan.diagon.identity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adres tablosuna erişim. Her sorgu kullanıcı kimliğini de içerir,
 * böylece bir kullanıcı başkasının adresine ulaşamaz.
 */
public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findAllByUserIdOrderByCreatedAtAsc(UUID userId);

    Optional<Address> findByIdAndUserId(UUID id, UUID userId);

    long countByUserId(UUID userId);
}
