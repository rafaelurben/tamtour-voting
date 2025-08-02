/* Source: https://docs.spring.io/spring-security/reference/servlet/appendix/database-schema.html#dbschema-oauth2-client */

CREATE TABLE IF NOT EXISTS oauth2_authorized_client
(
    client_registration_id  VARCHAR(100)  NOT NULL,
    principal_name          VARCHAR(200)  NOT NULL,
    access_token_type       VARCHAR(100)  NOT NULL,
    access_token_value      VARCHAR(4000) NOT NULL,
    access_token_issued_at  TIMESTAMP     NOT NULL,
    access_token_expires_at TIMESTAMP     NOT NULL,
    access_token_scopes     VARCHAR(1000),
    refresh_token_value     VARCHAR(4000),
    refresh_token_issued_at TIMESTAMP,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (client_registration_id, principal_name)
);
