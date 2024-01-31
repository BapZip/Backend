package com.example.BapZip.web.dto;

import lombok.*;

public class SchoolResponseDTO {
    // api 변경 가능성이 있어서 필드는 다 똑같지만 따로 만들어둠
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchSchool {
        Long id;
        String name;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSchoolRegion {
        Long id;
        String name;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getSchoolMajor {
        Long id;
        String name;
    }
}
