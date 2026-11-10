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
  6. Adresin düzenlenmesi veya silinmesi, o adrese daha önce verilmiş siparişlerin teslimat bilgisini değiştirmez (bkz. 3.6).
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
  7. Fiyat değişikliği önceden verilmiş siparişleri etkilemez (bkz. 3.6).

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

### 3.4 stock – Stok

Bu bölüm varyant stoğunu ve sipariş sırasında stoğun ayrılmasını kapsar. Rezervasyonu sipariş başlatır (bkz. 3.6), ödeme sonucu kesinleştirir veya serbest bırakır (bkz. 3.7). İade ile stoğun geri eklenmesi Faz 2'dedir.

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
  1. Süresi dolan rezervasyonun ayrılmış adedi, süre dolduktan sonra en geç 1 dakika içinde serbest bırakılır ve kullanılabilir stok o kadar artar. Sipariş iptal edilir (bkz. 3.6).
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

_Henüz yazılmadı._

### 3.6 order – Sipariş

_Henüz yazılmadı._

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
