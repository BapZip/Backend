package com.example.BapZip.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CongestionLevel {
    SPARSE(10), MODERATE(20), CROWDED(40);
    private Integer value;

}
