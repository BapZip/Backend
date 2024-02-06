package com.example.BapZip.domain.mapping;

import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "user_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "review_id"})
)
public class UserReview extends BaseEntity {
    /**
     * ReviewLike - 좋아요 mapping Table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id") // <- 조인하는 대상 지정
    private Review review; // <- 이거는 단순히 객체임
}
