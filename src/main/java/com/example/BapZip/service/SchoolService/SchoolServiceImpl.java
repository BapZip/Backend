package com.example.BapZip.service.SchoolService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.Major;
import com.example.BapZip.domain.Region;
import com.example.BapZip.domain.School;
import com.example.BapZip.repository.MajorRepository;
import com.example.BapZip.repository.RegionRepository;
import com.example.BapZip.repository.SchoolRepository;
import com.example.BapZip.web.dto.SchoolResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements  SchoolService{
    private final SchoolRepository schoolRepository;
    private final RegionRepository regionRepository;
    private final MajorRepository majorRepository;
    @Override
    public List<SchoolResponseDTO.SearchSchool> searchSchool(String schoolName) {
        List<School> schoolList = schoolRepository.findByNameContains(schoolName);
        return convertToSearchSchoolDTOList(schoolList);
    }

    @Override
    public List<SchoolResponseDTO.SearchSchool> getSchoolList(Long regionId) {
        Region region = regionRepository.findById(regionId).orElseThrow(()->new GeneralException(ErrorStatus.RIGION_NOT_EXIST));
        List<School> schoolList = schoolRepository.findByRegion(region);
        return convertToSearchSchoolDTOList(schoolList);
    }

    @Override
    public List<SchoolResponseDTO.getSchoolRegion> getSchoolRegion() {
        List<Region> regionList = regionRepository.findAll();
        return convertToSchoolRegionDTOList(regionList);
    }

    @Override
    public List<SchoolResponseDTO.getSchoolMajor> getSchoolMajor(Long schoolId, String majorName) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()->new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        List<Major> majorList = majorRepository.findBySchoolAndNameContains(school,majorName);
        return convertToMajorDTOList(majorList);

    }

    @Override
    public SchoolResponseDTO.getSchoolLogo getSchoolLogo(Long schoolId) {
        School school = schoolRepository.findById(schoolId).orElseThrow(()->new GeneralException(ErrorStatus.SCHOOL_NOT_EXIST));
        return SchoolResponseDTO.getSchoolLogo.builder().name(school.getName()).logo(school.getLogo()).id(school.getId()).build();
    }

    @Override
    public List<SchoolResponseDTO.getSchoolLogo> getAllSchool() {
        List<School> schoolList =schoolRepository.findAll();
        List<SchoolResponseDTO.getSchoolLogo> results=new ArrayList<>();
        for(School school:schoolList){
            results.add(SchoolResponseDTO.getSchoolLogo.builder().
                    id(school.getId()).name(school.getName()).logo(school.getLogo()).build());
        }
        return results;
    }

    private List<SchoolResponseDTO.getSchoolMajor> convertToMajorDTOList(List<Major> majorList) {
        List<SchoolResponseDTO.getSchoolMajor> dtoList = new ArrayList<>();
        for (Major major : majorList) {
            dtoList.add(
                    SchoolResponseDTO.getSchoolMajor.builder().id(major.getId()).name(major.getName()).build()
            );
        }
        return dtoList;
    }

    private List<SchoolResponseDTO.SearchSchool> convertToSearchSchoolDTOList(List<School> schoolList) {
        List<SchoolResponseDTO.SearchSchool> dtoList = new ArrayList<>();
        for (School school : schoolList) {
            dtoList.add(
                SchoolResponseDTO.SearchSchool.builder().id(school.getId()).name(school.getName()).build()
        );
        }
        return dtoList;
    }

    private List<SchoolResponseDTO.getSchoolRegion> convertToSchoolRegionDTOList(List<Region> regionList){
        List<SchoolResponseDTO.getSchoolRegion> dtoList = new ArrayList<>();
        for(Region region : regionList){
            dtoList.add(SchoolResponseDTO.getSchoolRegion.builder().id(region.getId()).name(region.getName()).build());
        }
        return dtoList;
    }
}
