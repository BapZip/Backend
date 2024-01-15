package com.example.BapZip.domain;

import com.example.BapZip.domain.common.BaseEntity;
import com.example.BapZip.domain.enums.InOrOut;
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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 50)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'IN'")
    InOrOut inOrOut;

    @Column(length = 50)
    String businessHours;

    @Column(length = 50)
    String closedDay;

    @Column(length = 50)
    String position;

    @Column(length = 50)
    String waitingAverage;


}
