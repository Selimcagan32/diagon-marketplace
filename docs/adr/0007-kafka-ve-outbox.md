# ADR-007: Kafka ve outbox deseni

- **Durum:** Kabul edildi, Faz 3'te uygulanacak
- **Tarih:** 2026-11-11

## Bağlam

Sipariş oluşturulduğunda, ödendiğinde ya da iptal edildiğinde müşteriye e-posta gönderilecek. Bu iş siparişin kendisini yavaşlatmamalı ve e-posta gönderilemedi diye sipariş başarısız olmamalı.

İkinci sorun çift yazma: sipariş veritabanına yazılıp mesaj gönderilemezse olay kaybolur, mesaj gönderilip veritabanı işlemi geri alınırsa var olmayan bir sipariş için bildirim gider.

## Seçenekler

1. **Sipariş servisinin içinde doğrudan çağrı.** En basit olan, ama bildirim hatası siparişi etkiler ve sipariş süresi uzar.
2. **Uygulama içi olaylar.** Spring'in olay mekanizmasıyla bildirim ayrı bir dinleyicide çalışır. Ek altyapı gerekmez, ama uygulama o anda kapanırsa olay kaybolur.
3. **Kafka'ya doğrudan yayın.** Olay kalıcı olur, ama çift yazma sorunu çözülmez.
4. **Outbox ve Kafka.** Olay önce siparişle aynı transaction içinde bir tabloya yazılır, sonra ayrı bir süreç Kafka'ya aktarır.

## Karar

Outbox deseni ve Kafka.

- Sipariş modülü olayı, siparişle aynı transaction içinde outbox tablosuna yazar.
- Bir aktarıcı, outbox'taki olayları sırayla Kafka'ya gönderir ve gönderildi olarak işaretler.
- Bildirim modülü olayları Kafka'dan dinler ve e-postayı gönderir.
- Bir olay birden fazla kez teslim edilebilir. Tüketici işlediği olay kimliklerini tutar ve tekrar gelen olayı atlar.
- Birkaç denemeden sonra işlenemeyen olay ayrı bir konuya (dead letter topic) alınır.
- Mesajlaşma bir ayarla kapatılabilir. Kapalıyken olay yayınlanmaz ve uygulama Kafka olmadan çalışır.

Bu ölçekte bir uygulama için ikinci seçenek de yeterli olurdu. Kafka, olay tabanlı iletişimi ve outbox desenini gerçek bir mesajlaşma sistemiyle öğrenmek ve göstermek için seçildi.

## Sonuçlar

**Olumlu**

- Sipariş, bildirimden bağımsızdır. Bildirim tarafı kapalıyken verilen siparişlerin e-postası sonradan gönderilir.
- Veritabanına yazılan her olay eninde sonunda yayınlanır, olay kaybolmaz.
- Yeni bir dinleyici eklemek sipariş modülünü değiştirmeyi gerektirmez.

**Olumsuz**

- Çalıştırılması gereken ek bir altyapı ve belirgin bir bellek yükü. Bu yüzden küçük canlı sunucuda mesajlaşma kapalıdır.
- Bildirim gecikmeli gelir, sipariş anında değil kısa süre sonra gönderilir.
- Aynı olay iki kez gelebildiği için tüketicinin idempotent olması zorunludur.
