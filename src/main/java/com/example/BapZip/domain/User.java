package com.example.BapZip.domain;

import com.example.BapZip.domain.common.BaseEntity;
import com.example.BapZip.domain.enums.AdminStatus;
import com.example.BapZip.domain.enums.Term;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 20)
    private String userId;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    @JsonIgnore
    private School school;


    @Column(nullable = true, length = 20)
    private String major;

    @Column(nullable = true, length = 20)
    private String email;

    @Column(nullable = true, length = 20)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'CHECKED'")
    private AdminStatus admin;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'CHECKED'")
    private Term term1;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'CHECKED'")
    private Term term2;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'CHECKED'")
    private Term term3;

    @Column(nullable = false)
    private String imageUrl;

}
