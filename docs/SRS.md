# Diagon — Yazılım Gereksinim Spesifikasyonu (SRS)

| Alan | Değer |
| --- | --- |
| Proje | Diagon – Çok satıcılı e-ticaret pazar yeri |
| Doküman sürümü | 0.1 |
| Durum | Taslak |
| Kapsadığı faz | Faz 1 |
| Yazar | Selim Çağan |

## 1. Giriş

### 1.1 Amaç

Bu doküman, Diagon pazar yerinin ne yapması gerektiğini tanımlar. Sistemin nasıl yapılacağı (mimari, teknoloji, veri modeli) teknik tasarım dokümanında (`docs/SDD.md`) anlatılır.

Doküman şu amaçlarla kullanılır:

- Faz 1'de geliştirilecek özelliklerin sınırını çizmek.
- Her gereksinime bir kimlik vererek testlerin, ADR'lerin ve pull request'lerin ona başvurmasını sağlamak.
- Bir özelliğin bitip bitmediğine ölçülebilir kabul kriterleriyle karar vermek.

Hedef okuyucular: projenin geliştiricisi, kodu ve dokümanları inceleyen kişiler (örneğin mülakatçılar) ve ileride projeye katılacak geliştiriciler.

### 1.2 Kapsam

Diagon, birden fazla satıcının ürünlerini tek bir sitede sattığı genel bir e-ticaret pazar yeridir. Müşteri farklı satıcıların ürünlerini tek sepette toplar ve tek seferde öder. Sistem siparişi satıcılara göre ayırır ve her satıcı kendi payını hazırlar.

**Bu sürümün kapsamı (Faz 1):**

- Misafir ürünleri arar, listeler, inceler ve sepete ekler.
- Kullanıcı kayıt olur ve giriş yapar. İki adımlı doğrulama (2FA) desteklenir. Misafirken oluşturduğu sepet, giriş yapınca hesabındaki sepetle birleşir.
- Müşteri sipariş verir, simüle ödemeyle öder ve siparişlerini takip eder.
- Kullanıcı satıcı olmak için başvurur, admin başvuruyu onaylar veya reddeder.
- Satıcı ürün, varyant ve stok yönetir, kendi alt siparişlerini görür ve durumlarını günceller.
- Admin kullanıcıları ve kategorileri yönetir.

**Sonraki sürümlerde eklenecekler:**

- Faz 2: Gerçek bir ödeme sağlayıcısının test ortamıyla ödeme, iptal ve iade, kargo takibi, yorum ve puan, favoriler, kupon, e-posta bildirimleri, KVKK işlemleri.
- Faz 3: Yazım hatasına toleranslı ürün araması, otomatik tamamlama, filtre panelinde sonuç sayıları.
- Faz 4: Ürün önerileri, yorumların otomatik denetlenmesi, fotoğrafla ürün arama.

**Proje kapsamı dışında (planlanan hiçbir fazda yok):**

- Gerçek para hareketi. Bütün ödemeler simülatörle veya ödeme sağlayıcısının test ortamıyla yapılır.
- Gerçek e-arşiv fatura entegrasyonu.
- LDAP gibi kurumsal kullanıcı dizinleri.
- Mobil uygulama.
- Gerçek kargo şirketi entegrasyonu. Kargo durumları sistem içinde simüle edilir.
- Dijital ürünler (e-kitap, hediye kartı gibi). İleride eklenebilir.

### 1.3 Tanımlar ve kısaltmalar

Kullanıcı rolleri (misafir, müşteri, satıcı, admin) bölüm 2.2'de tanımlanır.

**Terimler**

| Terim | İngilizce karşılığı | Tanım |
| --- | --- | --- |
| Alt sipariş | Sub-order | Ana siparişin tek bir satıcıya ait kısmı. Hazırlama, kargo ve iade alt sipariş üzerinden yürür. |
| Ana sipariş | Order | Müşterinin tek seferde verip tek ödemeyle ödediği sipariş. Bir veya daha fazla alt siparişten oluşur. |
| Hakediş (Faz 2) | Payout | Satış tutarından komisyon ve iadeler düşüldükten sonra satıcıya ödenecek tutar. |
| Kabul kriteri | Acceptance criterion | Bir gereksinimin karşılandığını gösteren, test edilebilir koşul. |
| Komisyon | Commission | Platformun her satıştan satıcıdan aldığı pay. |
| Ödeme simülatörü | Payment simulator | Gerçek para hareketi olmadan, kart numarasına göre başarılı veya başarısız sonuç dönen ödeme sağlayıcısı. |
| Sepet birleştirme | Cart merge | Misafirken oluşturulan sepetin, giriş yapıldığında kullanıcının hesabındaki sepetle tek sepette toplanması. |
| Sipariş kalemi | Order item | Siparişteki tek bir varyant ve adedi. Sipariş anındaki fiyatı ve ürün bilgisini saklar. |
| Stok rezervasyonu | Stock reservation | Sipariş verildiğinde stoğun belirli bir süre ayrılması. Ödeme gelirse kesinleşir, süre dolarsa serbest bırakılır. |
| Varyant | Variant | Bir ürünün beden veya renk gibi seçeneklerle ayrılan hali. Stok ve fiyat varyant düzeyinde tutulur. |

**Kısaltmalar**

| Kısaltma | Açılımı |
| --- | --- |
| 2FA | İki adımlı doğrulama (two-factor authentication) |
| ADR | Mimari karar kaydı (architecture decision record) |
| FR | Fonksiyonel gereksinim (functional requirement) |
| KDV | Katma değer vergisi |
| KVKK | Kişisel Verilerin Korunması Kanunu |
| NFR | Fonksiyonel olmayan gereksinim (non-functional requirement) |
| SDD | Teknik tasarım dokümanı (software design document) |
| SRS | Yazılım gereksinim spesifikasyonu (software requirements specification) |

### 1.4 Referanslar

**Proje dokümanları**

| Doküman | Konum |
| --- | --- |
| Yol haritası | [README.md](../README.md) |
| Teknik tasarım dokümanı (SDD) | [docs/SDD.md](SDD.md) |
| Mimari karar kayıtları (ADR) | [docs/adr/](adr/) |
| Mimari şema | [docs/architecture.drawio](architecture.drawio) |

**Standartlar ve yasal düzenlemeler**

| Kaynak | Kullanıldığı yer |
| --- | --- |
| ISO/IEC/IEEE 29148:2018 – Gereksinim mühendisliği | Bu dokümanın yapısı (sadeleştirilmiş olarak) |
| 6698 sayılı Kişisel Verilerin Korunması Kanunu (KVKK) | Kişisel veri işleme, rıza, veri silme ve dışa aktarma (Faz 2) |
| 6502 sayılı Tüketicinin Korunması Hakkında Kanun ve Mesafeli Sözleşmeler Yönetmeliği | Ön bilgilendirme, mesafeli satış sözleşmesi, 14 günlük cayma hakkı (Faz 2) |

## 2. Genel tanım

### 2.1 Ürün perspektifi

Diagon yeni ve bağımsız bir sistemdir, mevcut bir sistemin parçası veya devamı değildir.

Kullanıcılar sisteme web tarayıcısıyla erişir. Müşteri, satıcı ve admin işlemleri aynı web sitesinden yapılır, her kullanıcı rolüne uygun sayfaları görür.

Sistemin bağlantılı olduğu dış sistemler:

| Dış sistem | Sistemle ilişkisi | Faz |
| --- | --- | --- |
| Ödeme sağlayıcısı | Ödemeyi alır ve sonucunu Diagon'a bildirir. Kart bilgileri Diagon'da saklanmaz. Faz 1'de dış sağlayıcı yerine ödeme simülatörü kullanılır. | Faz 2 (Faz 1'de simülatör) |
| E-posta servisi | Sipariş onayı, kargo bilgisi ve satıcı başvuru sonucu gibi bildirimleri kullanıcılara iletir. | Faz 2 |

### 2.2 Kullanıcı rolleri

| Rol | Kimdir | Nasıl edinilir |
| --- | --- | --- |
| Misafir | Giriş yapmadan siteyi kullanan herkes. | Giriş yapmamış olmak yeterlidir. Misafir bir hesap değildir. |
| Müşteri | Kayıt olmuş kullanıcı. | Kayıt olunca otomatik olarak verilir. |
| Satıcı | Mağazası olan ve ürün satan kullanıcı. | Müşteri satıcı başvurusu yapar, admin onaylar. |
| Admin | Platformu yöneten kişi. | Kayıt olarak edinilemez. Hesap elle oluşturulur. |

Rol kuralları:

- Bir kullanıcının birden fazla rolü olabilir. Satıcı aynı zamanda müşteridir ve alışveriş yapmaya devam eder.
- Satıcı kendi ürünlerini satın alamaz ve kendi ürünlerine yorum yapamaz (Faz 2).
- Başvurusu reddedilen kullanıcı müşteri olarak kalır.
- Admin alışveriş yapmaz ve satıcı olamaz.
- Misafirin sepeti geçicidir. Giriş yapınca hesabındaki sepetle birleşir.

### 2.3 Rol-yetki matrisi

Tablolar her kullanıcı türünün yapabileceği her şeyi gösterir. Roller biriktiği için satıcı sütunu, müşteri yetkilerini de içerir.

| Değer | Anlamı |
| --- | --- |
| ✓ | Yapabilir |
| Kendi | Sadece kendisine ait kayıtlar için yapabilir |
| ✗ | Yapamaz |

(Faz 2) etiketi taşıyan satırlar bu sürümde geliştirilmez, sonraki sürümlerin yetkilerini önceden göstermek için yer alır.

#### identity – Kimlik

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Profilini görüntüler ve düzenler | ✗ | Kendi | Kendi | Kendi |
| Adreslerini yönetir | ✗ | Kendi | Kendi | ✗ |

#### seller – Satıcı

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Satıcı başvurusu yapar | ✗ | ✓ | ✗ | ✗ |
| Mağaza profilini görüntüler | ✓ | ✓ | ✓ | ✓ |
| Mağaza profilini düzenler | ✗ | ✗ | Kendi | ✗ |
| Satış raporlarını görüntüler (Faz 2) | ✗ | ✗ | Kendi | ✓ |
| Hakedişini görüntüler (Faz 2) | ✗ | ✗ | Kendi | ✓ |

#### catalog – Katalog

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Ürünleri arar, listeler ve inceler | ✓ | ✓ | ✓ | ✓ |
| Ürün, varyant ve görsel ekler, düzenler | ✗ | ✗ | Kendi | ✗ |
| Kategori ağacını yönetir | ✗ | ✗ | ✗ | ✓ |

#### stock – Stok

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Varyant stok miktarını günceller | ✗ | ✗ | Kendi | ✗ |

#### cart – Sepet

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Sepetini görüntüler ve düzenler | Kendi | Kendi | Kendi¹ | ✗ |

#### order – Sipariş

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Sipariş verir | ✗ | ✓ | ✓¹ | ✗ |
| Siparişlerini görüntüler ve takip eder | ✗ | Kendi | Kendi | ✓ |
| Siparişini iptal eder (Faz 2) | ✗ | Kendi | Kendi | ✗ |
| İade talebi oluşturur (Faz 2) | ✗ | Kendi | Kendi | ✗ |
| Gelen alt siparişleri görüntüler | ✗ | ✗ | Kendi | ✓ |
| Alt sipariş durumunu günceller | ✗ | ✗ | Kendi | ✗ |
| İade talebini onaylar veya reddeder (Faz 2) | ✗ | ✗ | Kendi | ✗ |

#### payment – Ödeme

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Siparişinin ödemesini yapar | ✗ | Kendi | Kendi | ✗ |

#### admin – Yönetim

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Satıcı başvurularını görüntüler, onaylar veya reddeder | ✗ | ✗ | ✗ | ✓ |
| Kullanıcıları görüntüler ve yönetir | ✗ | ✗ | ✗ | ✓ |

#### Faz 2 alanları

Bu işlemlerin hangi modüle ait olacağına Faz 2'de ADR ile karar verilecek.

| İşlem | Misafir | Müşteri | Satıcı | Admin |
| --- | --- | --- | --- | --- |
| Ürüne yorum yapar ve puan verir (Faz 2) | ✗ | ✓² | ✓¹ ² | ✗ |
| Favorilerini yönetir (Faz 2) | ✗ | Kendi | Kendi | ✗ |

**Notlar**

¹ Satıcı kendi ürünlerini sepete ekleyemez, satın alamaz ve kendi ürünlerine yorum yapamaz.

² Sadece satın aldığı ürünlere.

### 2.4 Kısıtlar

| Kısıt | Açıklama | Kaynağı |
| --- | --- | --- |
| Teknoloji | Backend Java 21 ve Spring Boot ile, frontend React ile geliştirilir. | Projenin öğrenme hedefi |
| Geliştirme ekibi | Proje tek bir geliştirici tarafından geliştirilir. | Proje koşulu |
| Süre | Faz 1 yaklaşık 8 haftada tamamlanır. | Proje koşulu |
| Bütçe | Faz 1'in canlı demosu tek bir küçük sunucuda çalışır. | Maliyet |
| Pazar | Sistem Türkiye'deki kullanıcılar için tasarlanır. Para birimi Türk lirasıdır. | İş alanı |
| Yasal | Sistem KVKK ve tüketici mevzuatına uyar (bkz. 1.4). | Yasal düzenleme |
| Dil | Arayüz Türkçe ve İngilizcedir. İngilizce desteği gerekirse Faz 2'ye ertelenir. | Proje koşulu |

### 2.5 Varsayımlar ve bağımlılıklar

**Varsayımlar**

| Varsayım | Yanlış çıkarsa etkisi |
| --- | --- |
| Kullanıcıların güncel bir web tarayıcısı ve internet bağlantısı vardır. | Eski tarayıcı desteği ve çevrimdışı çalışma gerekir. |
| Her kullanıcının bir e-posta adresi vardır. | Telefon numarasıyla kayıt gibi başka bir kayıt yolu gerekir. |
| Satıcılar fiyat, stok ve ürün bilgilerini doğru girer. Sistem içeriğin doğruluğunu denetlemez. | Ürün içeriği için bir denetim süreci gerekir. |
| Faz 1'de demo amaçlı, düşük trafik beklenir. | Tek sunucu yetmez, ölçekleme Faz 3'ten öne çekilir. |
| Kullanılan açık kaynak bileşenler ücretsiz lisansla kullanılabilir ve bakımları sürer. | Bileşenin yerine başka bir bileşen seçilir. |

**Bağımlılıklar**

Sistem, 2.1'de listelenen dış sistemlere bağımlıdır. Bu sistemlerin test ortamlarının erişilebilir ve ücretsiz olduğu varsayılır.

## 3. Fonksiyonel gereksinimler

Her gereksinim `FR-<MODÜL>-<NO>` biçiminde bir kimlik taşır (örneğin `FR-CART-03`). Modül adları koddaki paket adlarıyla aynıdır. Bir gereksinim silinirse kimliği başka bir gereksinime verilmez, gereksinim "(kaldırıldı)" olarak işaretlenir.

### 3.1 identity – Kimlik

_Henüz yazılmadı._

### 3.2 seller – Satıcı

_Henüz yazılmadı._

### 3.3 catalog – Katalog

_Henüz yazılmadı._

### 3.4 stock – Stok

_Henüz yazılmadı._

### 3.5 cart – Sepet

_Henüz yazılmadı._

### 3.6 order – Sipariş

_Henüz yazılmadı._

### 3.7 payment – Ödeme

_Henüz yazılmadı._

### 3.8 admin – Yönetim

_Henüz yazılmadı._

## 4. Fonksiyonel olmayan gereksinimler

Her gereksinim `NFR-<KATEGORİ>-<NO>` biçiminde bir kimlik taşır (örneğin `NFR-PERF-01`). Kimlik kuralları bölüm 3 ile aynıdır.

### 4.1 PERF – Performans

_Henüz yazılmadı._

### 4.2 SEC – Güvenlik

_Henüz yazılmadı._

### 4.3 A11Y – Erişilebilirlik

_Henüz yazılmadı._

## 5. Sürüm geçmişi

| Sürüm | Tarih | Değişiklik |
| --- | --- | --- |
| 0.1 | 2026-11-08 | İlk taslak: giriş, genel tanım ve rol-yetki matrisi. |
