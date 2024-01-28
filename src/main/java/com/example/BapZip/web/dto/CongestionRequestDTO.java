package com.example.BapZip.web.dto;

import com.example.BapZip.domain.User;
import com.example.BapZip.domain.enums.CongestionLevel;
import com.example.BapZip.domain.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

public class CongestionRequestDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class registerCongestion{

        private String congestionLevel; // 혼잡 정동

        private String visitStatus; // 방문객 or 비방문객

        private Integer occupancyCount; // 대략적인 인원

        private Integer waitTime; // 대기 시간

    }
}
