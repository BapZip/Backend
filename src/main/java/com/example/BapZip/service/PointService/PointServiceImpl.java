package com.example.BapZip.service.PointService;

import com.example.BapZip.domain.Point;
import com.example.BapZip.repository.PointRepository;
import com.example.BapZip.web.dto.PointResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{
    private final PointRepository pointRepository;

    @Override
    public List<PointResponseDTO.PointHistoryDTO> getPointHistory(Long userId) {
        List<Point> points = pointRepository.findByUserId(userId);

        return points.stream()
                .map(this::mapToPointDTO)
                .collect(Collectors.toList());
    }

    // point 객체를 기반으로 DTO 생성
    private PointResponseDTO.PointHistoryDTO mapToPointDTO(Point point){
        return PointResponseDTO.PointHistoryDTO.builder()
                .pointId(point.getId())
                .point(point.getPoint())
                .classification(point.getClassification())
                .note(point.getNote())
                .date(point.getCreatedAt().toLocalDate().toString())
                .build();
    }

    @Override
    public int getAllPoints(Long userId) {
        List<Point> points = pointRepository.findByUserId(userId);

        int totalPoints = 0;
        for (Point point : points) {
            totalPoints += point.getPoint();
        }

        return totalPoints;
    }
}
