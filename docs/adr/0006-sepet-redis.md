# ADR-006: Sepetin Redis'te tutulması

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

Misafir, giriş yapmadan sepete ürün ekleyebilmeli ve giriş yapınca bu sepet hesabındaki sepetle birleşmeli. Sepet sık değişen ve geçici bir veridir. Terk edilen misafir sepetlerinin bir şekilde temizlenmesi gerekir.

## Seçenekler

1. **PostgreSQL tabloları.** Kalıcıdır ve sorgulanabilir. Süresi dolan misafir sepetlerini silen ayrı bir görev gerektirir.
2. **Tamamı Redis'te.** Misafir ve üye sepetleri aynı yapıda tutulur, süresi dolan sepet kendiliğinden silinir.
3. **Misafir Redis'te, üye PostgreSQL'de.** Üye sepeti kalıcı olur, ama iki ayrı uygulama ve aralarında dönüşüm yazmak gerekir.

## Karar

Sepetin tamamı Redis'te tutulur.

- Her sepet bir hash'tir: alan varyant kimliği, değer adet.
- Misafir sepeti 7 gün, üye sepeti 30 gün saklanır. Süre her değişiklikte yenilenir.
- Sepette fiyat saklanmaz. Sepet gösterilirken fiyat ve stok bilgisi katalog ve stok modüllerinden güncel alınır.
- Giriş yapılınca misafir sepeti üye sepetiyle birleştirilir ve misafir sepeti silinir.
- Sipariş oluşturulurken fiyat ve stok veritabanından yeniden doğrulanır.

## Sonuçlar

**Olumlu**

- Temizleme görevi gerekmez, süre dolunca sepet kendiliğinden silinir.
- Tek uygulama, misafir ve üye sepeti için aynı kod.
- Sepet uygulamanın belleğinde durmadığı için uygulama birden fazla kopya çalışabilir.

**Olumsuz**

- Redis'teki veri kaybolursa sepetler de kaybolur. Sepet geçici veri olduğu için bu kabul edilir, sipariş ve ödeme verisi veritabanındadır.
- Sepetler üzerinde SQL ile rapor alınamaz.
- Redis, alışveriş akışının çalışması için zorunlu bir bileşen haline gelir.
