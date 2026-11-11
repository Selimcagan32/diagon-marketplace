# ADR-003: Kategoriye özel özellikler için JSONB

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

Genel bir pazar yerinde her kategorinin kendine özgü özellikleri var: telefonda ekran boyutu ve depolama, tişörtte kumaş türü. Bu özellikler kategoriden kategoriye değişiyor ve ürünler bu özelliklere göre filtrelenebilmeli.

## Seçenekler

1. **Her özellik için ayrı sütun.** En basit sorgular, ama her yeni özellik bir migration demek ve tablo çoğu boş kalan sütunlarla dolar.
2. **EAV (ayrı özellik tabloları).** Özellik tanımı, ürün ve değer ayrı tablolarda tutulur. Esnek ve ilişkisel, ama her okuma ve filtre birden çok JOIN gerektirir.
3. **PostgreSQL JSONB.** Ürünün özellik değerleri tek bir JSONB sütununda tutulur.

## Karar

JSONB.

- Kategori, kendi özellik tanımlarını tutar: özelliğin adı, türü ve zorunlu olup olmadığı.
- Ürün, özellik değerlerini bir JSONB sütununda saklar.
- Değerler ürün kaydedilirken kategorinin tanımlarına göre uygulamada doğrulanır.
- Filtreleme PostgreSQL'in JSONB operatörleriyle yapılır, gerekirse GIN indeksi eklenir.

## Sonuçlar

**Olumlu**

- Tek tablo, az JOIN, daha az kod.
- Yeni bir kategori özelliği eklemek migration gerektirmez.
- Ürün okunurken özellikleri tek sorguyla gelir.

**Olumsuz**

- Veritabanı özelliklerin türünü ve zorunluluğunu denetlemez, bu sorumluluk uygulamadadır.
- JSONB sütununun JPA ile eşlenmesi ek ayar ister.
- Birden çok özelliğe göre filtreleme sorgularını yazmak ilişkisel modele göre daha zahmetlidir.
