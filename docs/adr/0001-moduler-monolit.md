# ADR-001: Modüler monolit mimari

- **Durum:** Kabul edildi
- **Tarih:** 2026-11-11

## Bağlam

Diagon tek geliştiricinin yazdığı bir pazar yeri. İş alanı birbirinden ayrı konulara bölünüyor: kimlik, satıcı, katalog, stok, sepet, sipariş, ödeme, yönetim, bildirim. İlk hedef yaklaşık 8 haftada canlıda çalışan bir alışveriş akışı.

Kodun büyüdükçe dağılmaması için konuların sınırları net olmalı. Öte yandan tek kişinin birden fazla servisi işletmesi ağır bir yük.

## Seçenekler

1. **Klasik katmanlı monolit.** Paketler katmana göre ayrılır (controller, service, repository). Başlaması en kolay olan bu, ama her sınıf her sınıfa erişebilir ve konular zamanla birbirine karışır.
2. **Modüler monolit.** Tek uygulama, ama her iş konusu kendi paketinde ve dışarıya sadece bir arayüz açıyor.
3. **Mikroservisler.** Her konu ayrı servis ve ayrı veritabanı. Bağımsız ölçeklenir, ama dağıtık transaction, servisler arası iletişim ve çok sayıda dağıtım gerektirir.

## Karar

Modüler monolit.

- Tek bir Spring Boot uygulaması, her iş modülü ayrı bir paket.
- Her modül dışarıya bir arayüz (örneğin `StockApi`) ve DTO'lar açar. Entity, repository ve iç servisler modülün `internal` alt paketinde kalır.
- Her modülün kendi PostgreSQL şeması vardır. Modüller arasında yabancı anahtar ve JPA ilişkisi kurulmaz, başka modülün kaydına kimlik (ID) ile başvurulur.
- Modül sınırları Spring Modulith testiyle CI'da doğrulanır.

## Sonuçlar

**Olumlu**

- Tek dağıtım ve tek veritabanı, işletmesi kolay.
- Modüller aynı veritabanında olduğu için gerektiğinde tek transaction kullanılabilir (bkz. ADR-005).
- Sınırlar testle korunur, yanlış bağımlılık CI'da yakalanır.
- İleride bir modül ayrı servise taşınmak istenirse sınırı zaten bellidir.

**Olumsuz**

- Modüller arası JOIN yapılamaz. Başka modülün verisi gerektiğinde arayüz üzerinden toplu çağrı yapılır.
- Modüller arası veri bütünlüğünü veritabanı değil uygulama sağlar.
- Modüller bağımsız ölçeklenemez, uygulama bir bütün olarak çoğaltılır.
