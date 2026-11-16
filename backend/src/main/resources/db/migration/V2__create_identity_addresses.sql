-- Kullanıcı adresleri. user_id, Keycloak'taki kullanıcı kimliğidir (token'daki sub).
-- Kullanıcılar Keycloak'ta tutulduğu için bu sütunda yabancı anahtar yoktur.
CREATE TABLE identity.addresses (
                                    id             UUID PRIMARY KEY,
                                    user_id        UUID         NOT NULL,
                                    title          VARCHAR(50)  NOT NULL,
                                    recipient_name VARCHAR(100) NOT NULL,
                                    phone          VARCHAR(20)  NOT NULL,
                                    city           VARCHAR(50)  NOT NULL,
                                    district       VARCHAR(50)  NOT NULL,
                                    address_line   VARCHAR(255) NOT NULL,
                                    postal_code    VARCHAR(10),
                                    created_at     TIMESTAMPTZ  NOT NULL,
                                    updated_at     TIMESTAMPTZ  NOT NULL
);

-- Adresler her zaman kullanıcıya göre arandığı için user_id üzerinde indeks var.
CREATE INDEX idx_addresses_user_id ON identity.addresses (user_id);