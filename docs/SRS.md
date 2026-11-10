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
| Kullanılabilir stok | Available stock | Bir varyantın satın alınabilecek adedi. Hesaplanması FR-STOCK-02'de tanımlanır. |
| Ödeme simülatörü | Payment simulator | Gerçek para hareketi olmadan, kart numarasına göre başarılı veya başarısız sonuç dönen ödeme sağlayıcısı. |
| Satış sınırı | Purchase limit | Bir varyanttan tek bir siparişte alınabilecek en fazla adet. Satıcı belirler, FR-CATALOG-04'te tanımlanır. |
| Satıştaki ürün | Listed product | Listelemede görünen ve satın alınabilen ürün. Koşulları FR-CATALOG-08'de tanımlanır. |
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
| Yol haritası | [README.md](../README.md) (içerik Hafta 2'de eklenecek) |
| Teknik tasarım dokümanı (SDD) | [docs/SDD.md](SDD.md) (Hafta 2'de eklenecek) |
| Mimari karar kayıtları (ADR) | [docs/adr/](adr/) (Hafta 1'de eklenecek) |
| Mimari şema | [docs/mimari-sema.png](mimari-sema.png) |

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
| Kategori ağacını ve kategori özelliklerini yönetir | ✗ | ✗ | ✗ | ✓ |
| Kategorilerin KDV ve komisyon oranlarını belirler | ✗ | ✗ | ✗ | ✓ |
| Uygunsuz ürünü satıştan kaldırır | ✗ | ✗ | ✗ | ✓ |

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
| Siparişlerini görüntüler ve takip eder | ✗ | Kendi | Kendi | ✗ |
| Siparişini iptal eder (Faz 2) | ✗ | Kendi | Kendi | ✗ |
| İade talebi oluşturur (Faz 2) | ✗ | Kendi | Kendi | ✗ |
| Gelen alt siparişleri görüntüler | ✗ | ✗ | Kendi | ✗ |
| Bütün siparişleri ve alt siparişleri görüntüler | ✗ | ✗ | ✗ | ✓ |
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

Her gereksinim aşağıdaki kalıpla yazılır:

```markdown
#### FR-<MODÜL>-<NO> Kısa başlık

Sistem, <kim> için <ne> yapmalıdır.

- **Öncelik:** Çekirdek veya Ertelenebilir
- **Kabul kriterleri:**
  1. <Koşul ve beklenen sonuç>
  2. <Hata veya sınır durumu ve beklenen sonuç>
```

| Öncelik | Anlamı |
| --- | --- |
| Çekirdek | Faz 1'in tamamlanma kriteri için gereklidir. Ertelenmez. |
| Ertelenebilir | Süre yetmezse sonraki faza ertelenir. Ertelenen gereksinim bu dokümanda "(Faz 2'ye ertelendi)" olarak işaretlenir. |

Yazım kuralları:

- Her gereksinim tek bir şey ister. İki ayrı iş tek gereksinimde birleştirilmez.
- Gereksinim sistemin ne yapacağını söyler, nasıl yapacağını söylemez. Teknoloji ve araç adı geçmez.
- Kabul kriterleri gözlenebilir bir sonuç tanımlar. "Hızlı", "kolay", "kullanıcı dostu" gibi ölçülemeyen ifadeler kullanılmaz.
- Kabul kriterleri başarılı durumun yanında en az bir hata veya sınır durumunu da içerir. Hata durumu reddedilen bir girdiyi, sınır durumu ise kuralın uç bir değerde nasıl davrandığını gösterir.

### 3.1 identity – Kimlik

Bu bölüm kayıt, giriş ve hesap bilgilerini kapsar. Şifre sıfırlama ve e-posta doğrulama e-posta servisine bağlı olduğu için Faz 2'dedir.

#### FR-IDENTITY-01 Kayıt olma

Sistem, misafirin ad, soyad, e-posta adresi ve şifreyle hesap oluşturmasına izin vermelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Geçerli bilgilerle kayıt olan kullanıcının hesabı oluşturulur ve hesaba müşteri rolü verilir.
  2. Sistemde kayıtlı bir e-posta adresiyle kayıt olunmak istendiğinde kayıt reddedilir.
  3. Şifre kurallarına (bkz. NFR-SEC) uymayan şifreyle kayıt reddedilir ve kullanıcıya uymadığı kural gösterilir.

#### FR-IDENTITY-02 Giriş yapma

Sistem, kayıtlı kullanıcının e-posta adresi ve şifresiyle giriş yapmasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Doğru bilgilerle giriş yapan kullanıcı, rolüne uygun sayfalara erişir.
  2. Yanlış bilgilerle giriş reddedilir. Hata mesajı, e-posta adresinin mi şifrenin mi yanlış olduğunu belirtmez.

#### FR-IDENTITY-03 Çıkış yapma

Sistem, giriş yapmış kullanıcının oturumunu sonlandırmasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Çıkış yapan kullanıcı misafir olarak siteyi kullanmaya devam eder.
  2. Çıkıştan sonra giriş gerektiren bir sayfa açılmak istendiğinde kullanıcı giriş sayfasına yönlendirilir.

#### FR-IDENTITY-04 İki adımlı doğrulamayı yönetme

Sistem, kullanıcının hesabında iki adımlı doğrulamayı açıp kapatabilmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. İki adımlı doğrulamayı açan kullanıcı hesabına bir doğrulama uygulaması bağlar. Sonraki girişlerinde şifreden sonra uygulamanın ürettiği tek kullanımlık kod istenir.
  2. Yanlış veya süresi geçmiş kod girildiğinde giriş reddedilir.
  3. Müşteri iki adımlı doğrulamayı kapatabilir. Satıcı ve admin kapatamaz (bkz. FR-IDENTITY-05).

#### FR-IDENTITY-05 Satıcı ve admin için zorunlu iki adımlı doğrulama

Sistem, satıcı ve admin rolündeki kullanıcıların iki adımlı doğrulama kullanmasını zorunlu tutmalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. İki adımlı doğrulaması kapalı bir kullanıcı satıcı rolü aldığında, sonraki girişinde doğrulamayı kurmadan devam edemez.
  2. Admin hesabı, iki adımlı doğrulama kurulmadan kullanılamaz.

#### FR-IDENTITY-06 Profil bilgilerini yönetme

Sistem, kullanıcının kendi profil bilgilerini görüntüleyip düzenlemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Kullanıcı ad ve soyadını değiştirip kaydettiğinde yeni bilgiler profilinde görünür.
  2. Ad veya soyad boşsa ya da 50 karakterden uzunsa kayıt reddedilir ve hatalı alan gösterilir.
  3. E-posta adresi profil sayfasından değiştirilemez.
  4. Kullanıcı başka bir kullanıcının profilini görüntüleyemez ve düzenleyemez.

#### FR-IDENTITY-07 Adres yönetme

Sistem, müşteri ve satıcının teslimat adreslerini ekleyip düzenlemesini ve silmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Bir adres şu alanlardan oluşur: adres başlığı (örneğin "Ev"), alıcının adı ve soyadı, alıcının cep telefonu, il, ilçe ve açık adres. Hepsi zorunludur.
  2. Eksik alanla kaydedilmek istenen adres reddedilir ve eksik alan gösterilir.
  3. Cep telefonu Türkiye cep telefonu biçiminde değilse (5XX XXX XX XX) adres reddedilir.
  4. İl, 81 ilin listesinden seçilir.
  5. Kullanıcı en fazla 10 adres kaydedebilir. 11. adres reddedilir.
  6. Adresin düzenlenmesi veya silinmesi, o adrese daha önce verilmiş siparişlerin teslimat bilgisini değiştirmez (bkz. FR-ORDER-07).
  7. Kullanıcı başka bir kullanıcının adreslerine erişemez.

#### FR-IDENTITY-08 Varsayılan adres

Sistem, kullanıcının adreslerinden birini varsayılan olarak işaretlemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Kullanıcının ilk eklediği adres otomatik olarak varsayılan olur.
  2. Bir kullanıcının aynı anda yalnızca bir varsayılan adresi olur. Yeni bir adres varsayılan yapılınca öncekinin işareti kalkar.
  3. Varsayılan adres silinirse kalan en eski adres varsayılan olur.
  4. Ödeme adımında varsayılan adres önceden seçili gelir.

### 3.2 seller – Satıcı

Bu bölüm satıcı başvurusunu ve mağaza profilini kapsar. Başvurunun onaylanması ve reddedilmesi admin işlemi olduğu için 3.8'dedir. Satış raporları ve hakediş Faz 2'dedir.

#### FR-SELLER-01 Satıcı başvurusu yapma

Sistem, müşterinin satıcı olmak için başvuru yapmasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Başvuru mağaza adı, mağaza açıklaması ve iletişim telefonundan oluşur. Hepsi zorunludur.
  2. Mağaza adı 3–50, mağaza açıklaması en fazla 1000 karakterdir. Telefon biçimi FR-IDENTITY-07 ile aynıdır.
  3. Mağaza adı büyük-küçük harf farkı gözetmeden benzersizdir. Var olan bir mağazanın ya da bekleyen bir başvurunun adıyla yapılan başvuru reddedilir.
  4. Yeni başvuru "Beklemede" durumunda oluşur.
  5. Bekleyen başvurusu olan kullanıcı yeni başvuru yapamaz.
  6. Satıcı ve admin başvuru yapamaz.

#### FR-SELLER-02 Başvuru durumunu takip etme

Sistem, başvuru yapan kullanıcının başvurusunun durumunu görmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Kullanıcı başvurusunu "Beklemede", "Onaylandı" veya "Reddedildi" durumuyla görür.
  2. Reddedilen başvuruda admin'in yazdığı red gerekçesi gösterilir.
  3. Başvurusu reddedilen kullanıcı yeni başvuru yapabilir.
  4. Kullanıcı başka bir kullanıcının başvurusunu göremez.

#### FR-SELLER-03 Mağaza sayfasını görüntüleme

Sistem, herkesin bir satıcının mağaza sayfasını görüntülemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Mağaza sayfasında mağaza adı, mağaza açıklaması ve mağazanın satıştaki ürünleri gösterilir. Ürünler 3.3'teki listeleme kurallarıyla listelenir.
  2. Mağaza sayfası misafir dahil herkese açıktır.
  3. İletişim telefonu mağaza sayfasında gösterilmez.
  4. Var olmayan bir mağaza açılmak istendiğinde "bulunamadı" sayfası gösterilir.

#### FR-SELLER-04 Mağaza profilini düzenleme

Sistem, satıcının kendi mağazasının açıklamasını ve iletişim telefonunu düzenlemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Satıcının kaydettiği yeni açıklama mağaza sayfasında görünür. Açıklama ve telefon FR-SELLER-01'deki kurallara uymazsa kayıt reddedilir.
  2. Mağaza adı değiştirilemez.
  3. Satıcı başka bir satıcının mağaza profilini düzenleyemez.

### 3.3 catalog – Katalog

Bu bölüm kategori ağacını, ürün ve varyant yönetimini, ürün listeleme ve incelemeyi kapsar. Yazım hatasına toleranslı arama, otomatik tamamlama ve filtre panelindeki sonuç sayıları Faz 3'tedir.

#### FR-CATALOG-01 Kategori ağacını yönetme

Sistem, admin'in kategori ağacını oluşturup düzenlemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Admin kategori ekler, kategorinin adını değiştirir ve kategoriyi başka bir kategorinin altına taşır.
  2. Kategori ağacı en fazla 3 seviyedir (örneğin Elektronik > Telefon > Akıllı telefon).
  3. Aynı üst kategorinin altında, büyük-küçük harf farkı gözetmeden aynı adlı iki kategori olamaz.
  4. Ürün yalnızca alt kategorisi olmayan kategorilere eklenir. Ürünü bulunan bir kategoriye alt kategori eklenemez.
  5. İçinde ürün veya alt kategori bulunan kategori silinemez.
  6. Bir kategori kendi alt kategorilerinden birinin altına taşınamaz.

#### FR-CATALOG-02 Kategoriye özel özellikleri tanımlama

Sistem, admin'in her kategori için ürünlerde girilecek özellikleri tanımlamasını sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Bir özellik ad, tür ve zorunluluk bilgisinden oluşur. Türler: metin, sayı (isteğe bağlı birimle, örneğin "GB"), evet/hayır ve listeden seçim (örneğin Renk: Siyah, Beyaz).
  2. Listeden seçim türündeki bir özellik "varyant özelliği" olarak işaretlenebilir. Bir ürünün varyantları bu özelliklerin değerleriyle birbirinden ayrılır (örneğin Beden ve Renk).
  3. Bir kategoride tanımlanan özellik, o kategorinin bütün alt kategorilerinde de geçerlidir (örneğin Elektronik'teki "Marka", Akıllı telefon'da da istenir).
  4. Ürünü bulunan bir kategoriye zorunlu özellik doğrudan eklenemez. Özellik isteğe bağlı olarak eklenir, gerekirse FR-CATALOG-03'teki geçiş süresiyle zorunlu yapılır.
  5. Değer girilmiş ürünü olan bir özelliğin türü değiştirilemez.

#### FR-CATALOG-03 İsteğe bağlı özelliği zorunlu yapma

Sistem, admin'in isteğe bağlı bir özelliği, satıcılara geçiş süresi tanıyarak zorunlu yapmasını sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Admin isteğe bağlı bir özelliği istediği zaman zorunlu yapabilir. Bunu yaparken gün olarak bir geçiş süresi belirler. Süre 0 veya daha büyük bir tam sayıdır. 0 girilirse özelliği boş olan ürünler hemen satıştan kalkar.
  2. 7 günden kısa veya 60 günden uzun bir süre girildiğinde admin, satıştan kalkacak ürün sayısıyla birlikte uyarılır. Admin onaylarsa süre kaydedilir.
  3. Geçiş süresi başladıktan sonra eklenen veya düzenlenen ürünlerde özellik hemen zorunludur.
  4. Geçiş süresi boyunca özelliği boş olan ürünler satışta kalır. Satıcı panelinde bu ürünler, eksik özellik ve sürenin bitiş tarihiyle birlikte gösterilir.
  5. Süre dolduğunda özelliği hâlâ boş olan ürünler satıştan kalkar: listelemede görünmez ve satın alınamaz. Ürün silinmez.
  6. Satıcı eksik özelliği doldurduğunda ürün yeniden satışa açılır.
  7. Ürünün satıştan kalkması, daha önce verilmiş siparişleri etkilemez.

#### FR-CATALOG-04 Ürün ekleme ve düzenleme

Sistem, satıcının kendi mağazasına ürün eklemesini ve ürünlerini düzenlemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Ürün ad, açıklama, kategori ve kategorinin özelliklerinden oluşur. Ad 3–150 karakterdir. Açıklama en fazla 5000 karakterlik düz metindir.
  2. Ürün yalnızca alt kategorisi olmayan bir kategoriye eklenir (bkz. FR-CATALOG-01).
  3. Kategorinin zorunlu özellikleri doldurulmadan ürün kaydedilmez. Özellik değeri türüne uymazsa (örneğin sayı alanına metin) kayıt reddedilir.
  4. Ürünün kategorisi ürün eklendikten sonra değiştirilemez.
  5. Satıcı kendi ürününü satıştan kaldırabilir ve yeniden satışa açabilir.
  6. Satıcı başka bir satıcının ürününü düzenleyemez.
  7. Satıcı, ürünün her varyantından tek bir siparişte en fazla kaç adet alınabileceğini (satış sınırı) belirler. Satış sınırı 1 ile 100 arasında bir tam sayıdır, girilmezse 10 olur. Bu aralığın dışındaki değer reddedilir.

#### FR-CATALOG-05 Varyant yönetme

Sistem, satıcının kendi ürünlerine varyant eklemesini ve varyantları düzenlemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Bir varyant, kategorinin varyant özelliklerinin değerlerinden (örneğin Beden: M, Renk: Siyah) ve fiyattan oluşur. Stok 3.4'te tanımlanır.
  2. Kategoride varyant özelliği yoksa ürünün tek bir varyantı olur (örneğin bir kitap).
  3. Aynı ürünün iki varyantı aynı özellik değerlerine sahip olamaz.
  4. Fiyat Türk lirası cinsinden, KDV dahil, 0'dan büyük ve en fazla iki ondalık basamaklıdır.
  5. Ürün en az bir varyantı olmadan satışa açılamaz.
  6. Siparişi bulunan bir varyant silinemez, satıştan kaldırılır.
  7. Fiyat değişikliği önceden verilmiş siparişleri etkilemez (bkz. FR-ORDER-07).

#### FR-CATALOG-06 Ürün görsellerini yönetme

Sistem, satıcının kendi ürünlerine görsel yüklemesini, görselleri sıralamasını ve silmesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Görsel JPEG, PNG veya WebP biçiminde ve en fazla 5 MB'tır. Bir ürünün en fazla 8 görseli olur.
  2. Bu kurallara uymayan görsel reddedilir ve kullanıcıya sebebi gösterilir.
  3. Satıcı görsellerin sırasını belirler. İlk görsel ürün listelerinde gösterilir.
  4. Görseli olmayan ürünlerde varsayılan bir yer tutucu görsel gösterilir.

#### FR-CATALOG-07 Uygunsuz ürünü satıştan kaldırma

Sistem, admin'in uygunsuz bir ürünü gerekçe yazarak satıştan kaldırmasını sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Admin ürünü satıştan kaldırır, ürünün içeriğini düzenleyemez. Gerekçe zorunludur.
  2. Satıcı kaldırılan ürünü ve gerekçesini panelinde görür.
  3. Admin tarafından kaldırılan ürünü yalnızca admin yeniden satışa açabilir.
  4. Ürünün satıştan kalkması, önceden verilmiş siparişleri etkilemez.

#### FR-CATALOG-08 Ürünleri listeleme

Sistem, herkesin ürünleri kategoriye göre listelemesini ve sıralamasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Yalnızca satıştaki ürünler listelenir. Bir ürün şu koşulların hepsini sağlıyorsa satıştadır: satıcısı ürünü satışa açmıştır, admin kaldırmamıştır, zorunlu özellik eksikliği yüzünden satıştan kalkmamıştır ve satıştaki en az bir varyantı vardır.
  2. Bir kategori seçildiğinde o kategorinin ve bütün alt kategorilerinin ürünleri listelenir.
  3. Her üründe ilk görsel, ürün adı, mağaza adı ve satıştaki varyantların en düşük fiyatı gösterilir. Varyantların fiyatları farklıysa fiyat "…'den başlayan" ifadesiyle gösterilir.
  4. Satıştaki bütün varyantları "Tükendi" durumunda olan ürün (bkz. FR-STOCK-02) listede "Tükendi" etiketiyle gösterilir.
  5. Ürünler en yeniye (varsayılan), fiyata göre artan ve fiyata göre azalan sırayla sıralanabilir.
  6. Sayfa başına 24 ürün gösterilir.
  7. Seçili kategori, sıralama ve sayfa adres çubuğunda tutulur. Adres paylaşıldığında aynı liste açılır.

#### FR-CATALOG-09 Ürünleri filtreleme

Sistem, listelenen ürünlerin özelliklere, fiyata ve stok durumuna göre filtrelenmesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Liste, kategorinin listeden seçim ve evet/hayır türündeki özelliklerine göre filtrelenir.
  2. Aynı özellikte birden fazla değer seçildiğinde ürünün bunlardan birine uyması yeterlidir. Farklı özellikler seçildiğinde ürün hepsine uymalıdır (örneğin Marka: Samsung veya Apple, ve Renk: Siyah).
  3. Varyant özelliğine göre filtrelemede, ürünün satıştaki en az bir varyantı uyuyorsa ürün listelenir.
  4. Fiyat aralığı seçildiğinde, satıştaki en az bir varyantının fiyatı aralıktaysa ürün listelenir.
  5. "Sadece stokta olanlar" seçeneği tükenmiş ürünleri gizler.
  6. Filtrelere uyan ürün yoksa "Ürün bulunamadı" mesajı ve filtreleri temizleme seçeneği gösterilir.
  7. Seçili filtreler adres çubuğunda tutulur.

#### FR-CATALOG-10 Ürün arama

Sistem, herkesin ürünleri adlarına göre aramasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Arama metni en az 2 karakterdir ve ürün adında aranır.
  2. Arama büyük-küçük harf farkı gözetmez ve Türkçe harfleri doğru eşleştirir ("IŞIK" araması "ışık" geçen ürünü, "istanbul" araması "İstanbul" geçen ürünü bulur).
  3. Birden fazla kelime girildiğinde, bütün kelimeleri herhangi bir sırayla içeren ürünler bulunur ("siyah tişört" araması "Basic Tişört Siyah" ürününü bulur).
  4. Kelime ekleri ve yazım hataları dikkate alınmaz ("telefonlar" araması "Telefon" adlı ürünü bulmaz). Bu yetenek Faz 3'tedir.
  5. Sonuçlar FR-CATALOG-08'in kurallarıyla listelenir ve FR-CATALOG-09'un filtreleriyle daraltılabilir.
  6. Sonuç yoksa "… için sonuç bulunamadı" mesajı gösterilir.
  7. Arama metni adres çubuğunda tutulur.

#### FR-CATALOG-11 Ürün detayını inceleme

Sistem, herkesin bir ürünün detay sayfasını görüntülemesini ve varyant seçmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Detay sayfasında ürün adı, açıklaması, görselleri, kategori yolu (örneğin Elektronik > Telefon > Akıllı telefon), özellikleri ve mağaza adı gösterilir.
  2. Kullanıcı varyant özelliklerini seçtiğinde o varyantın fiyatı ve stok durumu ("Stokta" veya "Tükendi") gösterilir.
  3. Satışta olmayan varyantlar ve var olmayan özellik kombinasyonları seçilemez.
  4. Açıklama düz metin olarak, satır sonları korunarak gösterilir.
  5. Satıcının kaldırdığı veya zorunlu özellik eksikliği yüzünden satıştan kalkan ürünün sayfası açılır, "Bu ürün satışta değil" uyarısı gösterilir ve ürün sepete eklenemez.
  6. Admin'in kaldırdığı veya hiç var olmamış bir ürün açılmak istendiğinde "bulunamadı" sayfası gösterilir.
  7. Mağaza adı, mağaza sayfasına bağlantı verir (FR-SELLER-03 ertelenirse bağlantı olmaz).

#### FR-CATALOG-12 Kategori oranlarını belirleme

Sistem, admin'in her kategori için KDV oranını ve komisyon oranını belirlemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. KDV oranı ve komisyon oranı 0 ile 100 arasında, en fazla iki ondalık basamaklı bir yüzdedir. Bu aralığın dışındaki değer reddedilir.
  2. En üst seviyedeki kategoriler için iki oran da zorunludur. Oranı girilmeden en üst seviyede kategori kaydedilemez.
  3. Oranı girilmemiş bir alt kategori, oranı üst kategorisinden devralır. Alt kategoriye girilen oran, devralınan oranın yerine geçer. Elektronik için %20 KDV girilmiş, Akıllı telefon için KDV oranı girilmemişse Akıllı telefon %20 KDV oranını kullanır.
  4. Oranlardaki değişiklik yalnızca değişiklikten sonra oluşturulan siparişlere uygulanır (bkz. FR-ORDER-08 ve FR-ORDER-09).

### 3.4 stock – Stok

Bu bölüm varyant stoğunu ve sipariş sırasında stoğun ayrılmasını kapsar. Rezervasyonu sipariş başlatır (bkz. FR-ORDER-02), ödeme sonucu kesinleştirir veya serbest bırakır (bkz. 3.7). İade ile stoğun geri eklenmesi Faz 2'dedir.

#### FR-STOCK-01 Stok miktarını güncelleme

Sistem, satıcının kendi varyantlarının stok miktarını güncellemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Satıcı stok miktarını kesin bir sayı olarak girer (örneğin 50). Girilen sayı önceki miktarın yerine geçer.
  2. Stok miktarı 0 ile 99.999 arasında bir tam sayıdır. Negatif, ondalıklı veya bu aralığın dışındaki değer reddedilir.
  3. Varyant eklenirken stok miktarı girilmezse 0 olur.
  4. Stok miktarı, ödemesi beklenen siparişler için ayrılmış adedin altına düşürülemez. Böyle bir değer reddedilir ve hata mesajında ayrılmış adet gösterilir.
  5. Satıcı başka bir satıcının varyantının stok miktarını değiştiremez.

#### FR-STOCK-02 Stok durumunu belirleme

Sistem, her varyantın stok durumunu kullanılabilir stoğuna göre belirlemelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Kullanılabilir stok, stok miktarından ödemesi beklenen siparişler için ayrılmış adet çıkarılarak bulunur.
  2. Kullanılabilir stoğu 0'dan büyük olan varyant "Stokta", 0 olan varyant "Tükendi" durumundadır. FR-CATALOG-08, FR-CATALOG-09 ve FR-CATALOG-11 bu durumu kullanır.
  3. Müşteriye kullanılabilir stok adedi gösterilmez, yalnızca durum gösterilir.
  4. Stok miktarı 5 ve ayrılmış adedi 5 olan varyant, satıcının elinde ürün olsa da "Tükendi" durumundadır.

#### FR-STOCK-03 Stok rezervasyonu

Sistem, sipariş oluşturulduğunda siparişteki her varyant için stoğu belirli bir süre ayırmalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Sipariş oluşturulduğunda her kalemin adedi, ilgili varyantın kullanılabilir stoğundan ayrılır.
  2. Ayrılan adet, siparişin oluşturulduğu andan itibaren 15 dakika boyunca ayrılmış kalır. Süre uzatılamaz.
  3. Kalemlerden birinin adedi varyantın kullanılabilir stoğundan fazlaysa sipariş oluşturulmaz ve hiçbir kalem için stok ayrılmaz. Kullanıcıya stoğu yetmeyen ürün gösterilir, kullanılabilir adet gösterilmez.
  4. Kullanılabilir stoğu 2 olan bir varyanttan 3 adet içeren sipariş reddedilir. Reddedildikten sonra varyantın kullanılabilir stoğu 2 olarak kalır.

#### FR-STOCK-04 Rezervasyonun kesinleşmesi

Sistem, ödemesi başarılı olan siparişin ayrılmış stoğunu kesinleştirmelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Ödeme başarılı olduğunda her kalemin ayrılmış adedi, varyantın stok miktarından düşülür ve rezervasyon kapanır. Kullanılabilir stok değişmez.
  2. Stok miktarı 10 ve ayrılmış adedi 3 olan varyantta 3 adetlik sipariş ödenince stok miktarı 7, ayrılmış adet 0 olur. Kullanılabilir stok önce de sonra da 7'dir.
  3. Aynı siparişin başarılı ödeme sonucu ikinci kez işlenirse stok ikinci kez düşülmez.

#### FR-STOCK-05 Rezervasyonun serbest bırakılması

Sistem, süresi dolan rezervasyonun ayrılmış stoğunu serbest bırakmalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Süresi dolan rezervasyonun ayrılmış adedi, süre dolduktan sonra en geç 1 dakika içinde serbest bırakılır ve kullanılabilir stok o kadar artar. Sipariş iptal edilir (bkz. FR-ORDER-10).
  2. Başarısız ödeme rezervasyonu serbest bırakmaz. Müşteri süre dolana kadar aynı siparişi tekrar ödemeyi deneyebilir (bkz. 3.7).
  3. Süresi dolmuş bir siparişin ödemesi, stok henüz serbest bırakılmamış olsa bile alınmaz.
  4. Bir rezervasyon yalnızca bir kez serbest bırakılır. Serbest bırakılmış veya kesinleşmiş bir rezervasyon tekrar serbest bırakılmaz, stok iki kez geri eklenmez.

#### FR-STOCK-06 Fazla satışın önlenmesi

Sistem, aynı varyant için aynı anda gelen işlemlerde kullanılabilir stoktan fazlasının ayrılmasını engellemelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Kullanılabilir stok hiçbir koşulda 0'ın altına düşmez.
  2. Kullanılabilir stoğu 1 olan bir varyant için aynı anda gelen 2 siparişten yalnızca biri oluşturulur. Diğeri FR-STOCK-03'teki gibi reddedilir.
  3. Kullanılabilir stoğu 10 olan bir varyant için aynı anda gelen, 1'er adetlik 100 sipariş isteğinden tam olarak 10'u oluşturulur.
  4. Satıcının stok güncellemesi ile bir sipariş aynı anda gerçekleştiğinde de stok miktarı, ayrılmış adedin altına düşmez (bkz. FR-STOCK-01).

### 3.5 cart – Sepet

Bu bölüm sepete ekleme, sepetin düzenlenmesi, misafir sepeti ve giriş yapınca sepetlerin birleşmesini kapsar. Sepete ürün eklemek stok ayırmaz, stok sipariş oluşturulunca ayrılır (bkz. FR-STOCK-03). Sepette gösterilen fiyat ürünün güncel fiyatıdır, fiyat sipariş anında sabitlenir (bkz. FR-ORDER-07).

#### FR-CART-01 Sepete ekleme

Sistem, misafir, müşteri ve satıcının satıştaki bir ürünün varyantını sepete eklemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Yalnızca satıştaki ürünlerin (bkz. FR-CATALOG-08) "Stokta" durumundaki varyantları sepete eklenebilir.
  2. Sepette bulunan bir varyant tekrar eklenirse yeni kalem oluşmaz, mevcut kalemin adedi artar.
  3. Bir varyantın sepetteki adedi ürünün satış sınırını (bkz. FR-CATALOG-04) aşamaz. Satış sınırı 3 olan bir varyanttan sepette 3 adet varken 1 adet daha eklenmek istenirse istek reddedilir ve kullanıcıya satış sınırı gösterilir.
  4. Sepetteki adetle birlikte eklenmek istenen adet varyantın kullanılabilir stoğunu aşarsa istek reddedilir. Kullanıcıya stoğun yetersiz olduğu bildirilir, kullanılabilir adet gösterilmez.
  5. Satıcı kendi mağazasının ürünlerini sepete ekleyemez.
  6. Admin sepete ürün ekleyemez.

#### FR-CART-02 Sepeti görüntüleme

Sistem, misafir, müşteri ve satıcının kendi sepetini görüntülemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Her kalemde ürünün ilk görseli, ürün adı, varyantın özellik değerleri, birim fiyat, adet ve kalem tutarı (birim fiyat × adet) gösterilir.
  2. Kalemler mağazaya göre gruplanır ve her grubun başında mağaza adı gösterilir.
  3. Sepetin toplam tutarı, siparişe dahil edilebilecek kalemlerin tutarlarının toplamıdır. Satın alınamayan kalemler (bkz. FR-CART-06) toplama katılmaz.
  4. Fiyatlar varyantın güncel fiyatıdır. Sepete 100 TL iken eklenen bir varyantın fiyatı 120 TL'ye çıkarsa sepet 120 TL gösterir.
  5. Kullanıcı veya misafir başka bir sepeti görüntüleyemez.

#### FR-CART-03 Sepetteki kalemleri düzenleme

Sistem, misafir, müşteri ve satıcının sepetindeki kalemlerin adedini değiştirmesini ve kalemleri silmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Bir kalemin adedi 1 ile ürünün satış sınırı arasında bir tam sayı olabilir. Satış sınırı 3 olan bir varyantın adedi 4 yapılmak istenirse istek reddedilir.
  2. Adet 1'in altına düşürülemez. Kalem sepetten silinerek çıkarılır.
  3. Adet artırılırken kullanılabilir stok aşılırsa istek FR-CART-01'deki gibi reddedilir. Adet azaltılırken stok kontrolü yapılmaz.
  4. Satın alınamayan kalemler de (bkz. FR-CART-06) silinebilir ve adetleri azaltılabilir.

#### FR-CART-04 Misafir sepeti

Sistem, misafirin giriş yapmadan sepet kullanmasını ve sepetinin belirli bir süre saklanmasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Misafirin sepeti, aynı cihaz ve tarayıcıdan siteye döndüğünde korunur.
  2. Sepete ekleme, adet değiştirme ve kalem silme sepette değişiklik sayılır. Sepeti görüntülemek değişiklik sayılmaz.
  3. Son değişikliğinden bu yana 30 gün geçen misafir sepeti, en geç 1 gün içinde silinir. 29. gününde değiştirilen sepetin süresi yeniden başlar.
  4. Giriş yapmış kullanıcının sepeti süreyle silinmez.

#### FR-CART-05 Sepet birleştirme

Sistem, misafir giriş yaptığında misafir sepetini kullanıcının hesabındaki sepetle birleştirmelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Giriş yapıldığında misafir sepetindeki kalemler hesaptaki sepete eklenir ve misafir sepeti silinir. Kayıt olan kullanıcının misafir sepeti de aynı şekilde hesabına aktarılır.
  2. Aynı varyant iki sepette de varsa adetler toplanır. Toplam satış sınırını aşarsa adet satış sınırına indirilir ve kullanıcıya bildirilir. Satış sınırı 3 olan bir varyanttan misafir sepetinde 2, hesaptaki sepette 2 adet varsa birleşen sepette 3 adet olur.
  3. Birleştirmede stok ve satış durumu kontrol edilmez. Satın alınamayan kalemler FR-CART-06'daki gibi gösterilir.
  4. Giriş yapan kullanıcı satıcıysa, misafir sepetindeki kendi mağazasının ürünleri hesaptaki sepete eklenmez ve kullanıcıya bildirilir.
  5. Giriş yapan kullanıcı adminse misafir sepeti hesaba aktarılmaz.

#### FR-CART-06 Satın alınamayan kalemler

Sistem, sepetteki kalemlerin satın alınıp alınamayacağını güncel bilgilere göre belirlemeli ve satın alınamayan kalemleri işaretlemelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Kalemlerin durumu sepet her görüntülendiğinde güncel bilgilere göre belirlenir.
  2. Satın alınamayan kalem şu uyarılardan biriyle gösterilir: ürünü satışta olmayan veya varyantı satıştan kaldırılmış kalem "Satışta değil", varyantı "Tükendi" durumundaki kalem (bkz. FR-STOCK-02) "Tükendi", sepetteki adedi varyantın kullanılabilir stoğundan fazla olan kalem "Stok yetersiz". Uyarılar bu sırayla kontrol edilir ve kalemde yalnızca ilk uyan uyarı gösterilir. "Stok yetersiz" uyarısında kullanılabilir adet gösterilmez.
  3. Satın alınamayan kalemler sepetten kendiliğinden silinmez.
  4. Sipariş yalnızca satın alınabilir kalemlerle oluşturulur, satın alınamayan kalemler sepette kalır. Sepette satın alınabilir kalem yoksa sipariş adımına geçilemez.
  5. Kalemin koşulu düzelirse uyarı kalkar. "Tükendi" uyarılı bir kalem, satıcı stok ekledikten sonraki ilk görüntülemede uyarısız gösterilir.
  6. Satıcı satış sınırını düşürürse, sınırı aşan kalemin adedi sepet görüntülendiğinde yeni sınıra indirilir ve kullanıcıya bildirilir. Bu kalem satın alınabilir kalır.

#### FR-CART-07 Sipariş verilen kalemlerin sepetten çıkarılması

Sistem, ödemesi başarılı olan siparişteki kalemleri sepetten çıkarmalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Ödeme başarılı olduğunda siparişteki varyantların kalemleri, sepetteki adetleri ne olursa olsun, sepetten çıkarılır.
  2. Siparişe dahil edilmeyen satın alınamayan kalemler ve sipariş oluşturulduktan sonra sepete eklenen başka varyantlar sepette kalır.
  3. Ödeme başarısız olursa veya sipariş herhangi bir sebeple iptal edilirse sepet değişmez.
  4. Aynı siparişin başarılı ödeme sonucu ikinci kez işlenirse sepet ikinci kez değişmez.

### 3.6 order – Sipariş

Bu bölüm ödeme adımını, siparişin oluşturulmasını ve mağazalara göre bölünmesini, sipariş anındaki bilgilerin saklanmasını, sipariş durumlarını ve siparişlerin görüntülenmesini kapsar. Sipariş, ödeme başlarken oluşturulur ve stoğu ayırır (bkz. FR-STOCK-03). Ödemenin alınması ve sonucunun işlenmesi 3.7'dedir. Faz 1'de kargo ücreti alınmaz. Ödenmiş siparişin iptali, iade ve kargo takibi Faz 2'dedir.

#### FR-ORDER-01 Ödeme adımı

Sistem, müşteri ve satıcının sepetindeki satın alınabilir kalemlerin özetini görmesini ve teslimat adresini seçmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Ödeme adımına geçmek isteyen misafir giriş sayfasına yönlendirilir. Giriş yaptıktan sonra sepetler birleşir (bkz. FR-CART-05) ve kullanıcı sepet sayfasına döner.
  2. Özette yalnızca satın alınabilir kalemler (bkz. FR-CART-06) mağazaya göre gruplanarak gösterilir. Her grupta mağaza adı ve grubun tutarı, özetin sonunda siparişin KDV dahil toplam tutarı gösterilir.
  3. Müşteri kayıtlı adreslerinden birini teslimat adresi olarak seçer. Adres seçilmeden ödeme yapılamaz.
  4. Müşteri ödeme adımında yeni adres ekleyebilir. Eklenen adres FR-IDENTITY-07'deki kurallara uyar ve adres listesine kaydedilir. 10 kayıtlı adresi olan müşterinin yeni adresi reddedilir.
  5. Özetin görüntülenmesi sipariş oluşturmaz ve stok ayırmaz.

#### FR-ORDER-02 Sipariş oluşturma

Sistem, müşteri ödeme adımında kart bilgilerini girip ödemeyi başlattığında özetteki kalemlerden sipariş oluşturmalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Sipariş, müşteri kart bilgilerini girip "Öde"ye bastığında oluşturulur, stok ayrılır (bkz. FR-STOCK-03) ve ödeme hemen başlar (bkz. 3.7). Yeni siparişin durumu "Ödeme bekleniyor"dur.
  2. Her siparişe benzersiz bir sipariş numarası verilir. Müşteri, satıcı ve admin siparişi bu numarayla görür.
  3. Sipariş oluşturulmadan önce her kalemin fiyatı, satış durumu, stoğu (bkz. FR-CART-06) ve satış sınırı (bkz. FR-CATALOG-04) güncel bilgilere göre tekrar kontrol edilir. Bunlardan biri özette gösterilenden farklıysa sipariş oluşturulmaz, stok ayrılmaz ve müşteriye güncel bilgilerle yeni özet gösterilir.
  4. Özette 100 TL görünen bir varyantın fiyatı müşteri "Öde"ye basmadan 120 TL olursa sipariş oluşturulmaz ve yeni özette 120 TL gösterilir. Müşteri yeni özette "Öde"ye basarsa sipariş 120 TL ile oluşturulur.
  5. Yeni özette satın alınamaz hale gelen kalemler FR-CART-06'daki uyarıyla gösterilir ve siparişe dahil edilmez. Satın alınabilir kalem kalmazsa müşteri sepet sayfasına yönlendirilir.
  6. Satıcının kendi mağazasının ürününü içeren sipariş isteği reddedilir. Admin sipariş oluşturamaz.

#### FR-ORDER-03 Tekrarlanan sipariş isteğinin önlenmesi

Sistem, aynı ödeme adımından tekrar gönderilen sipariş isteği için yeni sipariş oluşturmamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Müşteri "Öde"ye iki kez bastığında veya istek bağlantı hatası yüzünden tekrar gönderildiğinde tek sipariş oluşturulur. Tekrar gelen isteğe ilk oluşturulan sipariş döner.
  2. Tekrar gelen istek stok ayırmaz. Kullanılabilir stoğu 5 olan bir varyanttan 1 adetlik sipariş isteği iki kez gelirse kullanılabilir stok 4 olur.
  3. Tekrar gelen istek, önceki ödenmemiş siparişi FR-ORDER-04'teki kurala göre iptal etmez. Tekrar kontrolü, önceki siparişin iptalinden önce yapılır.
  4. Ödeme adımı yeniden açıldığında gönderilen istek yeni bir istek sayılır.
  5. Sipariş oluşturulmadan reddedilen bir istekten sonra (örneğin FR-ORDER-02'deki fiyat değişikliğinde) gönderilen istek yeni bir istek sayılır.

#### FR-ORDER-04 Tek ödenmemiş sipariş

Sistem, bir müşterinin aynı anda en fazla bir "Ödeme bekleniyor" durumunda siparişi olmasını sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. "Ödeme bekleniyor" durumunda siparişi olan müşteri sepetten yeni sipariş oluşturursa önceki sipariş "Yeni sipariş oluşturuldu" sebebiyle iptal edilir ve rezervasyonu serbest bırakılır (bkz. FR-STOCK-05).
  2. Önceki siparişten serbest bırakılan stok yeni sipariş için kullanılabilir. Son adedi önceki siparişte ayrıldığı için kullanılabilir stoğu 0 olan bir varyant, yeni siparişte tekrar alınabilir.
  3. Yeni sipariş oluşturulamazsa önceki sipariş iptal edilmez ve rezervasyonu korunur.
  4. Müşteri yeni sipariş oluşturmak yerine önceki siparişi ödemeye devam edebilir. Bu durumda sipariş ve ödeme süresi değişmez (bkz. FR-ORDER-13 ve 3.7).
  5. Aynı müşteriden farklı sekmelerden aynı anda gelen iki yeni sipariş isteğinden sonra müşterinin en fazla bir "Ödeme bekleniyor" durumunda siparişi olur.

#### FR-ORDER-05 Sipariş sınırı

Sistem, müşterinin kısa sürede çok sayıda sipariş oluşturup ödemeden bırakarak stoğu sürekli ayırmasını engellemelidir.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Son 60 dakika içinde ödenmeden iptal edilmiş 5 siparişi olan müşterinin yeni sipariş isteği reddedilir. Müşteriye ne zaman tekrar sipariş verebileceği gösterilir.
  2. Yalnızca ödenmeden iptal edilen siparişler ("Ödeme süresi doldu" veya "Yeni sipariş oluşturuldu" sebebiyle) sayılır. Ödenen siparişler ve başarısız ödeme denemeleri sayılmaz.
  3. Son 60 dakika içinde ödenmeden iptal edilmiş 4 siparişi olan müşterinin yeni siparişi oluşturulur.
  4. Reddedilen istek, müşterinin "Ödeme bekleniyor" durumundaki siparişini iptal etmez.

#### FR-ORDER-06 Alt siparişlere bölme

Sistem, siparişi kalemlerin ait olduğu mağazalara göre alt siparişlere bölmelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Siparişteki her mağaza için bir alt sipariş oluşturulur. Her kalem kendi mağazasının alt siparişine aittir.
  2. A mağazasından 2, B mağazasından 1 varyant içeren sipariş 2 alt siparişe bölünür. Tek mağazadan verilen siparişin 1 alt siparişi olur.
  3. Alt siparişin tutarı kalemlerinin tutarlarının, siparişin toplam tutarı alt siparişlerin tutarlarının toplamıdır.
  4. Sipariş, alt siparişlerin sayısından bağımsız olarak tek ödemeyle ödenir (bkz. 3.7).

#### FR-ORDER-07 Sipariş anındaki bilgilerin saklanması

Sistem, sipariş anındaki ürün, fiyat ve teslimat adresi bilgilerini siparişte saklamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Her kalemde ürün adı, mağaza adı, varyantın özellik değerleri, birim fiyat, adet, kalem tutarı, KDV oranı ve KDV tutarı saklanır.
  2. Teslimat adresinin bütün alanları (bkz. FR-IDENTITY-07) siparişte saklanır.
  3. Sipariş verildikten sonra ürün, varyant veya adres bilgileri değişse ya da silinse de sipariş saklanan bilgileri gösterir. 100 TL'ye verilen siparişte, varyantın fiyatı 120 TL'ye çıktıktan sonra da 100 TL görünür.
  4. Satıştan kalkan bir ürünün önceki siparişleri görüntülenmeye devam eder.

#### FR-ORDER-08 KDV hesaplama

Sistem, her sipariş kaleminin KDV tutarını ürünün kategorisinin KDV oranına (bkz. FR-CATALOG-12) göre hesaplamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Fiyatlar KDV dahil olduğu için KDV tutarı kalem tutarının içinden hesaplanır. KDV oranı %20 olan 120 TL'lik kalemin KDV tutarı 20 TL'dir.
  2. KDV tutarı kuruşa yuvarlanır, yarım kuruş ve üstü yukarı yuvarlanır. KDV oranı %20 olan 99,99 TL'lik kalemin KDV tutarı 16,665 TL değil 16,67 TL'dir.
  3. Alt siparişin ve siparişin KDV tutarı, yuvarlanmış kalem KDV tutarlarının toplamıdır.
  4. KDV oranı sonradan değişirse önceki siparişlerin KDV tutarı değişmez.

#### FR-ORDER-09 Komisyon hesaplama

Sistem, her sipariş kalemi için platform komisyonunu ürünün kategorisinin komisyon oranına (bkz. FR-CATALOG-12) göre hesaplamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Komisyon, KDV dahil kalem tutarı üzerinden hesaplanır. Komisyon oranı %15 olan 120 TL'lik kalemin komisyonu 18 TL'dir.
  2. Komisyon tutarı FR-ORDER-08'deki kuralla kuruşa yuvarlanır.
  3. Komisyon oranı ve tutarı kalemde saklanır. Oran sonradan değişirse önceki siparişlerin komisyonu değişmez.
  4. Komisyon müşteriye gösterilmez.

#### FR-ORDER-10 Ödeme süresi dolan siparişin iptali

Sistem, ödeme süresi içinde ödenmeyen siparişi iptal etmelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Oluşturulmasından 15 dakika sonra hâlâ "Ödeme bekleniyor" durumunda olan sipariş, en geç 1 dakika içinde "İptal edildi" durumuna geçer ve rezervasyonu serbest bırakılır (bkz. FR-STOCK-05).
  2. 15 dakika dolmadan ödemesi başarılı olan sipariş iptal edilmez.
  3. İptal edilen siparişte iptal sebebi gösterilir: "Ödeme süresi doldu" veya "Yeni sipariş oluşturuldu" (bkz. FR-ORDER-04).
  4. Ödeme süresi dolmuş sipariş, henüz iptal edilmemiş olsa bile ödenemez (bkz. FR-STOCK-05).
  5. Sipariş iptal edildiğinde sepet değişmez (bkz. FR-CART-07).

#### FR-ORDER-11 Alt sipariş durumları

Sistem, alt siparişin durumunu yalnızca tanımlı geçişlerle değiştirmelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Alt sipariş şu durumlardan birindedir: Ödeme bekleniyor, Ödendi, Hazırlanıyor, Kargoda, Teslim edildi, İptal edildi.
  2. Geçerli geçişler ve bunları yapan:
     - Ödeme bekleniyor → Ödendi: ödeme başarılı olunca sistem (bkz. 3.7)
     - Ödeme bekleniyor → İptal edildi: sistem (bkz. FR-ORDER-04 ve FR-ORDER-10)
     - Ödendi → Hazırlanıyor, Hazırlanıyor → Kargoda, Kargoda → Teslim edildi: satıcı (bkz. FR-ORDER-15)
  3. Bu geçişlerin dışındaki her değişiklik reddedilir ve durum değişmez. "Ödendi" durumundaki alt sipariş doğrudan "Kargoda" yapılamaz.
  4. "Teslim edildi" ve "İptal edildi" son durumlardır. Bu durumlardan başka bir duruma geçilemez.
  5. Ödeme sonucu siparişin bütün alt siparişlerine birlikte uygulanır: alt siparişler birlikte "Ödendi" veya birlikte "İptal edildi" olur.

#### FR-ORDER-12 Sipariş durumu

Sistem, siparişin durumunu alt siparişlerinin durumlarından belirlemelidir.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Siparişin durumu ayrıca değiştirilmez, her zaman alt siparişlerinin durumlarından belirlenir.
  2. Bütün alt siparişler aynı durumdaysa sipariş de o durumdadır.
  3. Alt siparişler farklı durumlardaysa sipariş, en geride olan alt siparişin durumunu alır. Sıra: Ödendi, Hazırlanıyor, Kargoda, Teslim edildi. Alt siparişlerinden biri "Kargoda", diğeri "Hazırlanıyor" olan sipariş "Hazırlanıyor" durumundadır.
  4. Üç alt siparişinden ikisi "Teslim edildi" olan sipariş "Teslim edildi" durumunda değildir.

#### FR-ORDER-13 Siparişleri görüntüleme

Sistem, müşteri ve satıcının kendi verdiği siparişleri listelemesini ve sipariş detayını görüntülemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Siparişler en yeni en üstte olacak şekilde listelenir. Her siparişte sipariş numarası, sipariş tarihi, durum ve toplam tutar gösterilir. Sayfa başına 10 sipariş gösterilir.
  2. Detayda her alt sipariş mağaza adı ve durumuyla, kalemleri FR-ORDER-07'de saklanan bilgilerle gösterilir. Teslimat adresi, toplam tutar ve KDV tutarı da gösterilir.
  3. "Ödeme bekleniyor" durumundaki siparişte kalan ödeme süresi ve ödemeye devam etme seçeneği gösterilir (bkz. 3.7).
  4. İptal edilmiş siparişte iptal sebebi gösterilir.
  5. Kullanıcı başka bir kullanıcının siparişini görüntüleyemez.
  6. Satıcı burada yalnızca müşteri olarak verdiği siparişleri görür. Mağazasına gelen alt siparişler FR-ORDER-14'tedir.

#### FR-ORDER-14 Gelen alt siparişleri görüntüleme

Sistem, satıcının kendi mağazasına gelen alt siparişleri listelemesini ve detayını görüntülemesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Satıcı yalnızca ödenmiş alt siparişleri görür. "Ödeme bekleniyor" durumundaki ve ödenmeden iptal edilen alt siparişler gösterilmez.
  2. Alt siparişler en yeni en üstte listelenir ve duruma göre filtrelenebilir. Her alt siparişte sipariş numarası, sipariş tarihi, durum ve alt sipariş tutarı gösterilir. Sayfa başına 20 alt sipariş gösterilir.
  3. Detayda kalemler, teslimat adresi, alt sipariş tutarı, KDV tutarı ve komisyon tutarı gösterilir.
  4. Satıcı aynı siparişin başka mağazalara ait alt siparişlerini ve müşterinin e-posta adresini göremez.
  5. Satıcı başka bir mağazanın alt siparişini görüntüleyemez.

#### FR-ORDER-15 Alt sipariş durumunu güncelleme

Sistem, satıcının kendi alt siparişlerinin durumunu bir sonraki duruma ilerletmesini sağlamalıdır.

- **Öncelik:** Çekirdek
- **Kabul kriterleri:**
  1. Satıcı alt siparişi her seferinde bir adım ilerletir: Ödendi → Hazırlanıyor → Kargoda → Teslim edildi (bkz. FR-ORDER-11).
  2. Satıcı alt siparişi geri alamaz ve adım atlayamaz. Böyle bir istek reddedilir ve durum değişmez.
  3. "Hazırlanıyor" durumundaki alt sipariş için ikinci kez "Hazırlanıyor" isteği gelirse istek reddedilir ve durum değişmez.
  4. Satıcı başka bir mağazanın alt siparişinin durumunu değiştiremez.

#### FR-ORDER-16 Bütün siparişleri görüntüleme

Sistem, admin'in bütün siparişleri ve alt siparişleri görüntülemesini sağlamalıdır.

- **Öncelik:** Ertelenebilir
- **Kabul kriterleri:**
  1. Admin her durumdaki bütün siparişleri en yeni en üstte görür. Sipariş numarasıyla arama ve duruma göre filtreleme yapabilir.
  2. Detayda müşterinin adı ve e-posta adresi, alt siparişler, kalemler, teslimat adresi, KDV ve komisyon tutarları gösterilir.
  3. Admin siparişin ve alt siparişlerin durumunu değiştiremez.
  4. Var olmayan bir sipariş numarasıyla arama yapıldığında "Sipariş bulunamadı" mesajı gösterilir.

### 3.7 payment – Ödeme

_Henüz yazılmadı._

### 3.8 admin – Yönetim

_Henüz yazılmadı._

## 4. Fonksiyonel olmayan gereksinimler

Her gereksinim `NFR-<KATEGORİ>-<NO>` biçiminde bir kimlik taşır (örneğin `NFR-PERF-01`). Kimlik ve yazım kuralları bölüm 3 ile aynıdır.

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
