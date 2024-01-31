package com.example.BapZip.domain;

import com.example.BapZip.domain.common.BaseEntity;
import com.example.BapZip.domain.enums.CongestionLevel;
import com.example.BapZip.domain.enums.VisitStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Congestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLevel;

    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus;

    private Integer occupancyCount;

    private Integer waitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

}
