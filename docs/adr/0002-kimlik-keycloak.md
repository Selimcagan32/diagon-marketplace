# ADR-002: Kimlik için Keycloak

**Durum:** Kabul edildi (16 Kasım 2026)

## Bağlam
Diagon'da misafir, müşteri, satıcı ve admin var. Kayıt, giriş, şifre saklama, rol yönetimi ve isteğe bağlı 2FA gerekiyor. Bunları backend içinde yazmak hem uzun sürer hem de güvenlik hatası riski taşır.

## Karar
Kimlik yönetimi backend'den ayrı bir servis olan Keycloak'a verilir. Realm ayarları (roller, kayıt, 2FA, client'lar) `keycloak/` klasöründe dosya olarak tutulur ve açılışta yüklenir. Backend kullanıcı tablosu tutmaz; Keycloak'ın imzaladığı JWT'yi doğrular, rolleri `realm_access.roles` alanından okur ve kullanıcı kimliği olarak `sub` alanını kullanır.

## Neden
- Şifre saklama, 2FA ve OIDC akışı hazır ve denenmiş gelir.
- Backend sadece token doğrular, bu yüzden stateless kalır ve birden fazla kopya çalışabilir.
- Kullanıcı bilgisi tek yerde durur, iki sistemi senkron tutmak gerekmez.

## Sonuçlar
- Kullanıcıya ait kayıtlar `user_id` (Keycloak `sub`) ile saklanır; bu sütunda yabancı anahtar yoktur.
- Kullanıcı kimliği her zaman token'dan alınır; başkasının kaydı için 404 döner.
- Satıcı rolü admin onayından sonra backend tarafından Keycloak'ta atanır.
- Keycloak ek bir servis olarak çalıştırılmalı ve izlenmelidir.