package com.fcn.fanscoin.domain;

import com.fcn.fanscoin.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import java.util.Date;

@Entity
@Table(name = "fcn_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User
        extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "user_no", length = 25)
    private String userNo;

    @Column(unique = true, length = 50)
    private String nickname;

    @Column(name = "birth_day", columnDefinition = "DATE")
    @Basic
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Lob
    @Column(columnDefinition = "text")
    private String bio;

    private String avatar;

    @Column(name = "avatar_thumbnail")
    private String avatarThumbnail;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "number_of_subscribers")
    @Builder.Default
    private Long numberOfSubscribers = 0L;

    @Column(name = "number_of_subscribings")
    @Builder.Default
    private Long numberOfSubscribings = 0L;

    @Builder.Default
    private boolean deleted = Boolean.FALSE;

    @Transient
    private String email;
}
