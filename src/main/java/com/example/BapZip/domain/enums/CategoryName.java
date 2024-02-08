package com.example.BapZip.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryName {
    KOREA("한식"), CHINA("중식"), WESTERN("양식"), CAFE("카페"), JAPAN("일식");
    private String name;
}
