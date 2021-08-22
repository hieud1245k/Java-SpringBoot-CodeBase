package com.fcn.fanscoin.domain;

import com.fcn.fanscoin.enums.CountryCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import java.time.Instant;

@Entity
@Table(name = "fcn_accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account
        extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", length = 3)
    private CountryCode countryCode;

    @Email
    @Column(name = "google_email", unique = true)
    private String googleEmail;

    @Builder.Default
    @Column(name = "activated", nullable = false)
    private boolean activated = true;

    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reseted_at")
    private Instant resetedAt;

    @Builder.Default
    @Column(name = "confirmed_email", nullable = false)
    private boolean confirmedEmail = false;

    @Builder.Default
    @Column(name = "enable_online_status")
    private boolean enableOnlineStatus = true;

    @Builder.Default
    @Column(name = "online_status")
    private boolean onlineStatus = true;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Builder.Default
    private boolean deleted = Boolean.FALSE;
}
