package com.selimcagan.diagon.identity;

import java.util.UUID;

/**
 * API'nin döndürdüğü adres bilgisi.
 */
public record AddressResponse(
        UUID id,
        String title,
        String recipientName,
        String phone,
        String city,
        String district,
        String addressLine,
        String postalCode
) {

    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getTitle(),
                address.getRecipientName(),
                address.getPhone(),
                address.getCity(),
                address.getDistrict(),
                address.getAddressLine(),
                address.getPostalCode());
    }
}
