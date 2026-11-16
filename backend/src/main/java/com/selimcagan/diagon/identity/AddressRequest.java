package com.selimcagan.diagon.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Adres ekleme ve güncelleme isteği. Kullanıcı kimliği burada yoktur; token'dan alınır.
 */
public record AddressRequest(
        @NotBlank(message = "Adres başlığı boş olamaz")
        @Size(max = 50, message = "Adres başlığı en fazla 50 karakter olabilir")
        String title,

        @NotBlank(message = "Alıcı adı boş olamaz")
        @Size(max = 100, message = "Alıcı adı en fazla 100 karakter olabilir")
        String recipientName,

        @NotBlank(message = "Telefon boş olamaz")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Telefon numarası geçersiz")
        String phone,

        @NotBlank(message = "İl boş olamaz")
        @Size(max = 50, message = "İl en fazla 50 karakter olabilir")
        String city,

        @NotBlank(message = "İlçe boş olamaz")
        @Size(max = 50, message = "İlçe en fazla 50 karakter olabilir")
        String district,

        @NotBlank(message = "Açık adres boş olamaz")
        @Size(max = 255, message = "Açık adres en fazla 255 karakter olabilir")
        String addressLine,

        @Size(max = 10, message = "Posta kodu en fazla 10 karakter olabilir")
        String postalCode
) {
}