# ADR-002: Kimlik yönetimi için Keycloak

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

Sistemde kayıt, giriş ve üç rol (müşteri, satıcı, admin) gerekiyor. Parola saklama, oturum ve token yönetimi hataya açık konular ve projenin asıl odağı değil.

## Seçenekler

1. **Kendi kimlik doğrulamam.** Kullanıcı tablosu, parola özeti ve JWT üretimi uygulamanın içinde. Ek servis gerekmez, ama güvenlik açısından en riskli ve en çok kod isteyen yol.
2. **Keycloak.** Açık kaynak, kendi sunucumda çalışan bir kimlik sağlayıcı. Standart OIDC akışlarını, giriş ve kayıt ekranlarını hazır verir.
3. **Bulut kimlik servisi.** Kurulumu en kolay olan, ama dış servise bağımlılık ve ücretsiz kullanım sınırları getirir.

## Karar

Keycloak.

- Frontend, Authorization Code + PKCE akışıyla giriş yapar.
- Backend bir OAuth2 Resource Server'dır. Token'ı Keycloak'ın açık anahtarlarıyla (JWKS) kendi içinde doğrular, her istekte Keycloak'a gitmez.
- Roller Keycloak'ta realm rolü olarak tutulur ve backend'de Spring yetkilerine çevrilir.
- Satıcı başvurusu onaylanınca rol, backend'den Keycloak Admin API ile atanır. Atama başarısız olursa onay da başarısız olur.
- Realm ayarları dosya olarak repoda tutulur ve ortam açılırken otomatik yüklenir.
- İki adımlı doğrulama ve e-posta doğrulama kapalıdır, çünkü demoyu deneyen kişiyi engeller.

## Sonuçlar

**Olumlu**

- Parola saklama, giriş ekranı ve token yenileme hazır gelir.
- Standart bir protokol kullanılır, kimlik sağlayıcı değişse backend'in doğrulama mantığı aynı kalır.
- Kimlik yönetimi uygulamadan ayrıdır, backend sadece token'a bakar.

**Olumsuz**

- Çalıştırılması gereken ek bir servis ve belirgin bir bellek yükü.
- Satıcı onayında iki sisteme yazılır (veritabanı ve Keycloak). İkisinin tutarlı kalması uygulamanın sorumluluğudur.
- Tarayıcı ve backend Keycloak'ı aynı adresle görmelidir, yoksa token'daki `iss` alanı uyuşmaz ve doğrulama başarısız olur.
