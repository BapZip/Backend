package com.example.BapZip.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId")
    private Store store;
}
