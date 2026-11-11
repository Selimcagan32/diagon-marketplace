# Diagon

[English](README.en.md)

Java 21 ve Spring Boot ile geliştirilen çok satıcılı bir e-ticaret pazar yeri. Müşteri farklı satıcıların ürünlerini tek sepette toplar ve tek seferde öder, sistem siparişi satıcılara göre ayırır.

Bu bir portfolyo ve öğrenme projesidir. Gerçek ödeme alınmaz, gerçek ürün satılmaz.

## Durum

Proje geliştirme aşamasında, şu an Faz 0'da (tasarım ve iskelet). Canlı demo, ekran görüntüleri ve kurulum adımları Faz 1 ilerledikçe eklenecek.

## Amaç

Bir backend projesinde sık karşılaşılan konuları tek bir uygulamada, gerekçeleriyle birlikte uygulamak:

- İş alanını modüllere bölmek ve modül sınırlarını korumak
- Eşzamanlı isteklerde stok tutarlılığı
- Çift ödeme koruması
- Önbellek ve geçici veri yönetimi
- Olay tabanlı iletişim
- Konteyner ile dağıtım ve ölçekleme

## Faz 1 kapsamı

- **Misafir:** ürün arama, listeleme, filtreleme, sepete ekleme
- **Müşteri:** kayıt ve giriş, misafir sepetinin hesap sepetiyle birleşmesi, sipariş, simüle ödeme, sipariş takibi ve iptal
- **Satıcı:** satıcı başvurusu, ürün, varyant ve stok yönetimi, gelen alt siparişlerin durumunu güncelleme
- **Admin:** satıcı başvurularını onaylama, kategori yönetimi

## Mimari

![Mimari şema](docs/architecture.png)

Şema hedef mimariyi gösterir. Bileşenler yol haritasındaki fazlarla birlikte eklenir.

- Sistem bir modüler monolittir: tek bir Spring Boot uygulaması, içinde birbirinden ayrılmış iş modülleri.
- Her modülün kendi PostgreSQL şeması vardır. Modüller birbirine dış arayüzleri üzerinden ve kimlik (ID) ile başvurur.
- Kimlik doğrulama Keycloak'tadır. Backend, token'ları Keycloak'ın açık anahtarlarıyla doğrular.
- Sepet ve önbellek Redis'tedir.
- Sipariş olayları outbox deseniyle Kafka'ya aktarılır, bildirim modülü bu olayları dinler.

## Teknolojiler

| Katman | Teknoloji | Faz |
| --- | --- | --- |
| Backend | Java 21, Spring Boot 4.1 | Faz 0–1 |
| Veri | PostgreSQL, Flyway, Hibernate / JPA | Faz 0 |
| Kimlik | Keycloak (OIDC, PKCE) | Faz 0–1 |
| Sepet ve önbellek | Redis | Faz 1 |
| Frontend | React, Vite | Faz 1 |
| Dağıtım | Docker, Docker Compose, GitHub Actions | Faz 0–1 |
| Orkestrasyon | Kubernetes (kind), Kustomize | Faz 2 |
| Mesajlaşma | Apache Kafka | Faz 3 |
| İzleme ve yük testi | Prometheus, Grafana, k6 | Faz 4 |
| Görsel arama | Python, FastAPI, pgvector | Faz 5 |
| Test | JUnit 5, Mockito, Testcontainers, Spring Modulith | Faz 0'dan itibaren |

## Roller ve yetkiler

| Rol | Kimdir | Nasıl edinilir |
| --- | --- | --- |
| Misafir | Giriş yapmadan siteyi kullanan herkes | Giriş yapmamış olmak yeterlidir |
| Müşteri | Kayıt olmuş kullanıcı | Kayıt olunca otomatik verilir |
| Satıcı | Mağazası olan ve ürün satan kullanıcı | Müşteri başvurur, admin onaylar |
| Admin | Platformu yöneten kişi | Kayıtla edinilemez, hesap elle oluşturulur |

Rol kuralları:

- Bir kullanıcının birden fazla rolü olabilir. Satıcı aynı zamanda müşteridir ve alışveriş yapabilir.
- Satıcı kendi ürünlerini sepete ekleyemez ve satın alamaz.
- Başvurusu reddedilen kullanıcı müşteri olarak kalır.
- Admin alışveriş yapmaz ve satıcı olamaz.

Yetki tablosunda ✓ yapabilir, ✗ yapamaz, "Kendi" sadece kendisine ait kayıtlar için yapabilir anlamına gelir.

| Modül | İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- | --- |
| Kimlik | Profilini görüntüler ve düzenler | ✗ | Kendi | Kendi | Kendi |
| Kimlik | Adreslerini yönetir | ✗ | Kendi | Kendi | ✗ |
| Satıcı | Satıcı başvurusu yapar | ✗ | ✓ | ✗ | ✗ |
| Satıcı | Mağaza profilini görüntüler | ✓ | ✓ | ✓ | ✓ |
| Satıcı | Mağaza profilini düzenler | ✗ | ✗ | Kendi | ✗ |
| Katalog | Ürünleri arar, listeler ve inceler | ✓ | ✓ | ✓ | ✓ |
| Katalog | Ürün, varyant ve görsel ekler, düzenler | ✗ | ✗ | Kendi | ✗ |
| Katalog | Kategori ağacını yönetir | ✗ | ✗ | ✗ | ✓ |
| Stok | Varyant stok miktarını günceller | ✗ | ✗ | Kendi | ✗ |
| Sepet | Sepetini görüntüler ve düzenler | Kendi | Kendi | Kendi | ✗ |
| Sipariş | Sipariş verir | ✗ | ✓ | ✓ | ✗ |
| Sipariş | Siparişlerini görüntüler ve takip eder | ✗ | Kendi | Kendi | ✗ |
| Sipariş | Siparişini iptal eder | ✗ | Kendi | Kendi | ✗ |
| Sipariş | Gelen alt siparişleri görüntüler | ✗ | ✗ | Kendi | ✗ |
| Sipariş | Alt sipariş durumunu günceller | ✗ | ✗ | Kendi | ✗ |
| Ödeme | Siparişinin ödemesini yapar | ✗ | Kendi | Kendi | ✗ |
| Yönetim | Satıcı başvurularını onaylar veya reddeder | ✗ | ✗ | ✗ | ✓ |

## Tasarım kararları

Önemli kararlar kısa karar kayıtları (ADR) olarak tutulur. Her kayıt bağlamı, değerlendirilen seçenekleri, kararı ve sonuçlarını içerir.

| No | Karar |
| --- | --- |
| [ADR-001](docs/adr/0001-moduler-monolit.md) | Modüler monolit mimari |
| [ADR-002](docs/adr/0002-keycloak.md) | Kimlik yönetimi için Keycloak |
| [ADR-003](docs/adr/0003-kategori-ozellikleri-jsonb.md) | Kategoriye özel özellikler için JSONB |
| [ADR-004](docs/adr/0004-stokta-kotumser-kilitleme.md) | Stokta kötümser kilitleme |
| [ADR-005](docs/adr/0005-stok-rezervasyonu.md) | Stok rezervasyonu ve transaction sınırı |
| [ADR-006](docs/adr/0006-sepet-redis.md) | Sepetin Redis'te tutulması |
| [ADR-007](docs/adr/0007-kafka-ve-outbox.md) | Kafka ve outbox deseni |

## Yol haritası

- [ ] **Faz 0 – Tasarım ve iskelet:** veri modeli, karar kayıtları, proje iskeleti, CI
- [ ] **Faz 1 – Çekirdek alışveriş akışı:** kimlik, katalog, stok, sepet, sipariş, ödeme simülatörü, canlı demo
- [ ] **Faz 2 – Kubernetes ve CD:** yerel kümede çalıştırma, otomatik ölçekleme, onaylı dağıtım
- [ ] **Faz 3 – Kafka ve bildirim:** sipariş olayları, outbox deseni, e-posta bildirimi
- [ ] **Faz 4 – İzleme ve yük testi:** metrikler, Grafana paneli, flaş indirim yük testi
- [ ] **Faz 5 – Fotoğrafla arama:** görsel benzerliğine göre ürün arama
