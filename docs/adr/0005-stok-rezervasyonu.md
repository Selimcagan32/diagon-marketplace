# ADR-005: Stok rezervasyonu ve transaction sınırı

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

İki soru birlikte cevaplanmalı:

1. Stok ne zaman düşülür? Müşteri ödeme ekranındayken ürün başkasına satılırsa ödediği ürünü alamaz.
2. Sipariş ve stok ayrı modüller. Sipariş oluşturuldu ama stok ayrılamadıysa, ya da tersi olduysa veri tutarsız kalır.

## Seçenekler

Stoğun düşülme zamanı için:

1. **Sepete eklenince.** Müşteri için en güvenli olan, ama terk edilen sepetler stoğu boş yere tutar.
2. **Sipariş oluşturulunca rezerve et, ödemeyle kesinleştir.** Stok sadece satın alma niyeti netleşince ayrılır.
3. **Ödeme başarılı olunca.** Stok hiç boşa tutulmaz, ama ödeme alındıktan sonra stoğun bittiği anlaşılabilir.

Modüller arası tutarlılık için:

1. **Tek veritabanı transaction'ı.** Sipariş ve rezervasyon birlikte yazılır ya da hiçbiri yazılmaz.
2. **Saga.** Her modül kendi transaction'ını yapar, hata olursa önceki adımlar telafi edilir.

## Karar

Stok sipariş anında rezerve edilir ve bu işlem siparişle aynı transaction içinde yapılır.

- Sipariş oluşturulurken her kalem için bir rezervasyon kaydı yazılır ve stok düşülür.
- Rezervasyon 15 dakika geçerlidir. Ödeme başarılı olursa rezervasyon kesinleşir.
- 15 dakikada ödenmeyen siparişi zamanlanmış bir görev iptal eder ve stoğu serbest bırakır.
- Serbest bırakma sadece "rezerve" durumundaki kayıtta çalışır. Böylece aynı stok iki kez geri eklenemez.
- Sipariş ve stok modülleri aynı veritabanında olduğu için tek transaction kullanılır. Sipariş modülü stok modülünü yine sadece dış arayüzü üzerinden çağırır.
- Ödeme çağrısı bu transaction'ın dışında yapılır.

## Sonuçlar

**Olumlu**

- Ödeme sırasında ürün müşteri için ayrılmıştır.
- Yarım kalmış sipariş oluşmaz, telafi mantığı yazmak gerekmez.

**Olumsuz**

- Terk edilen siparişler stoğu 15 dakikaya kadar tutar.
- Modüller ileride ayrı servislere bölünürse tek transaction kullanılamaz. O durumda saga ve outbox desenine geçmek gerekir (bkz. ADR-007).
- Uygulama birden fazla kopya çalışırken zamanlanmış görevin tek kopyada çalışması sağlanmalıdır (Faz 2'de dağıtık kilit ile).
