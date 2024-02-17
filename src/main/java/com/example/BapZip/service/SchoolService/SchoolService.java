package com.example.BapZip.service.SchoolService;

import com.example.BapZip.web.dto.SchoolResponseDTO;

import java.util.List;

public interface SchoolService {
    List<SchoolResponseDTO.SearchSchool> searchSchool(String schoolName);

    List<SchoolResponseDTO.SearchSchool> getSchoolList(Long regionId);

    List<SchoolResponseDTO.getSchoolRegion> getSchoolRegion();

    List<SchoolResponseDTO.getSchoolMajor> getSchoolMajor(Long schoolId, String majorName);

    SchoolResponseDTO.getSchoolLogo getSchoolLogo(Long schoolId);

    List<SchoolResponseDTO.getSchoolLogo> getAllSchool();
}
