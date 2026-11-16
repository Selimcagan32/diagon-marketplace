package com.selimcagan.diagon.identity;

import com.selimcagan.diagon.common.exception.BusinessException;
import com.selimcagan.diagon.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Adres iş kuralları. Her metot kullanıcı kimliğini alır ve
 * sadece o kullanıcının adresleri üzerinde çalışır.
 */
@Service
public class AddressService {

    static final int MAX_ADDRESSES_PER_USER = 10;

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(UUID userId) {
        return addressRepository.findAllByUserIdOrderByCreatedAtAsc(userId).stream()
                .map(AddressResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse get(UUID userId, UUID addressId) {
        return AddressResponse.from(findOwned(userId, addressId));
    }

    @Transactional
    public AddressResponse create(UUID userId, AddressRequest request) {
        if (addressRepository.countByUserId(userId) >= MAX_ADDRESSES_PER_USER) {
            throw new BusinessException("ADDRESS_LIMIT_REACHED",
                    "En fazla " + MAX_ADDRESSES_PER_USER + " adres kaydedebilirsiniz");
        }
        Address address = new Address(userId, request.title(), request.recipientName(), request.phone(),
                request.city(), request.district(), request.addressLine(), request.postalCode());
        return AddressResponse.from(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse update(UUID userId, UUID addressId, AddressRequest request) {
        Address address = findOwned(userId, addressId);
        address.update(request.title(), request.recipientName(), request.phone(),
                request.city(), request.district(), request.addressLine(), request.postalCode());
        return AddressResponse.from(address);
    }

    @Transactional
    public void delete(UUID userId, UUID addressId) {
        addressRepository.delete(findOwned(userId, addressId));
    }

    /**
     * Sahiplik kontrolü: adres sadece sahibiyle birlikte aranır.
     * Başkasının adresi "bulunamadı" olarak döner; böyle bir adresin var olduğu bile belli olmaz.
     */
    private Address findOwned(UUID userId, UUID addressId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("Adres bulunamadı"));
    }
}