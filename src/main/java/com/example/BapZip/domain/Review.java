package com.example.BapZip.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.example.BapZip.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {
    /**
     * id
     * store_id
     * price
     * score
     * content
     * paymentTime
     * reply
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    @JsonIgnore
    private Store store;

    private Integer price;

    private Integer score;

    @Column(nullable = false, length = 700)
    private String content;

    private LocalDate paymentTime;

    private String menuName;

    @Column(nullable = true, length = 300)
    private String reply;


}
