package com.example.BapZip.domain;

import com.example.BapZip.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    Store store;

    @ColumnDefault("0")
    Integer h1;

    @ColumnDefault("0")
    Integer h2;

    @ColumnDefault("0")
    Integer h3;

    @ColumnDefault("0")
    Integer h4;

    @ColumnDefault("0")
    Integer h5;

    @ColumnDefault("0")
    Integer h6;

    @ColumnDefault("0")
    Integer h7;

    @ColumnDefault("0")
    Integer h8;




}
