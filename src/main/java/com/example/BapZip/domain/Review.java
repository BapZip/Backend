package com.example.BapZip.domain;


import com.example.BapZip.domain.mapping.UserReview;
import com.example.BapZip.domain.mapping.UserStore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.example.BapZip.domain.common.BaseEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
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

    @ElementCollection
    private List<String> hashtags;

    @Column(nullable = false, length = 700)
    private String content;

    private LocalDate paymentTime;

    private String menuName;

    // 좋아요 한 리뷰 조회때 = new ArrayList<>();; 추가함
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)  // 리뷰 삭제할 때, 리뷰 이미지도 지워져야 하므로 수정함
    private List<ReviewImage> images=new ArrayList<>();;



    @Column(nullable = true, length = 300)
    private String reply;


    // 좋아요 한 리뷰 조회 (위해 더해봄)
    @OneToMany(mappedBy = "review")
    private List<UserReview> reviewLikedList=new ArrayList<>();



}
