# ADR-004: Stokta kötümser kilitleme

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

Aynı varyantı aynı anda birden çok kullanıcı satın almaya çalışabilir. Stok hiçbir koşulda eksiye düşmemeli ve son ürün iki kişiye satılmamalı. En zorlu senaryo flaş indirim: stoğu 50 olan bir ürüne aynı anda 1000 istek.

## Seçenekler

1. **İyimser kilitleme.** Stok satırında bir sürüm sütunu tutulur. Güncelleme sırasında sürüm değişmişse işlem başarısız olur ve yeniden denenir. Çakışma azken hızlıdır, çakışma çokken isteklerin büyük kısmı başarısız olup tekrar dener.
2. **Kötümser kilitleme.** Stok satırı okunurken kilitlenir (`SELECT ... FOR UPDATE`). Diğer istekler kilidin bırakılmasını bekler.
3. **Tek koşullu güncelleme.** Stok, "yeterliyse düş" koşulunu içeren tek bir `UPDATE` ile azaltılır. Hızlı ve basittir, ama stok kontrolü ile rezervasyon kaydının yazılması ayrı adımlara bölünür.

## Karar

Kötümser kilitleme.

- Rezervasyon sırasında stok satırı kilitlenir, miktar kontrol edilir, stok düşülür ve rezervasyon kaydı yazılır.
- Birden çok varyant içeren siparişte kilitler her zaman varyant kimliği sırasıyla alınır.
- Kilidin tutulduğu transaction kısa tutulur, içinde dış servis çağrısı yapılmaz.

Flaş indirimde çakışma kural olduğu için iyimser yöntem sürekli yeniden deneme üretirdi. Kötümser yöntemde istekler sıraya girer ve her biri güncel stoğu görür.

## Sonuçlar

**Olumlu**

- Stok eksiye düşmez, yeniden deneme mantığı gerekmez.
- Akış okunması ve açıklanması kolay tek bir sıradan oluşur: kilitle, kontrol et, düş, kaydet.

**Olumsuz**

- Aynı varyanta gelen istekler sırayla işlenir, yoğun yükte bekleme süresi artar.
- Kilit sırası kuralına uyulmazsa iki sipariş birbirini kilitleyebilir (deadlock).
- Transaction uzarsa kilit de uzar, bu yüzden transaction'ın içeriği dikkatle sınırlanmalıdır.

**Doğrulama**

- Son ürünü aynı anda isteyen iki kullanıcıyı simüle eden eşzamanlılık testi (Faz 1).
- k6 ile flaş indirim yük testi: 1000 kullanıcı, 50 stok, tam 50 satış (Faz 4).
