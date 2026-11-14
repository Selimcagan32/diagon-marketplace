# ADR-001: Modüler monolit

**Durum:** Kabul edildi (12 Kasım 2026)

## Bağlam
Diagon, tek geliştiricinin 3 haftada bitireceği bir pazar yeri. Kimlik, satıcı, katalog, stok, sepet, sipariş ve ödeme gibi ayrı iş alanları var.

## Karar
Backend tek bir Spring Boot uygulaması olarak yazılır ve 7 modüle ayrılır. Her modülün kendi paketi ve kendi veritabanı şeması vardır. Bir modül başka modülün tablosuna erişmez, onun dış arayüzünü çağırır.

## Neden
- Mikroservis tek geliştirici için fazla yük getirir: ayrı deploy, ağ hataları, dağıtık transaction.
- Düz monolitte modüller birbirine karışır, sonradan ayırmak zorlaşır.

## Sonuçlar
- Sipariş ve stok rezervasyonu tek transaction'da yapılabilir.
- Bir modül ileride ayrı servise çıkarılabilir.
- Modül sınırlarını derleyici korumaz, kurala uyarak korunur.