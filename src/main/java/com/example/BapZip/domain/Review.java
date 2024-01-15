package com.example.BapZip.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review {
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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;

    private Integer price;

    private Integer score;

    @Column(nullable = false, length = 700)
    private String content;

    private LocalDate paymentTime;

    @Column(nullable = true, length = 300)
    private String reply;






}
