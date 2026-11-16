package com.selimcagan.diagon.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Kullanıcının teslimat adresi. Her adres tek bir kullanıcıya (userId) aittir.
 */
@Entity
@Table(name = "addresses", schema = "identity")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Adresin sahibi; Keycloak'taki kullanıcı kimliği. Sonradan değişmez. */
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** JPA'nın veritabanından okurken kullandığı boş constructor. */
    protected Address() {
    }

    public Address(UUID userId, String title, String recipientName, String phone,
                   String city, String district, String addressLine, String postalCode) {
        this.userId = userId;
        apply(title, recipientName, phone, city, district, addressLine, postalCode);
    }

    /** Adresin bilgilerini değiştirir; sahibi (userId) değişmez. */
    public void update(String title, String recipientName, String phone,
                       String city, String district, String addressLine, String postalCode) {
        apply(title, recipientName, phone, city, district, addressLine, postalCode);
    }

    private void apply(String title, String recipientName, String phone,
                       String city, String district, String addressLine, String postalCode) {
        this.title = title;
        this.recipientName = recipientName;
        this.phone = phone;
        this.city = city;
        this.district = district;
        this.addressLine = addressLine;
        this.postalCode = postalCode;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
