package com.xmplify.starter_kit_springboot_singledb.service;

import com.google.common.base.Strings;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationListDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EducationService {

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    FileService fileService;

    @Transactional
    public void addEducation(List<PersonEducation> personEducationList) {
        educationRepository.saveAll(personEducationList);
    }

    public ResponseEntity<?> convertPersonEducation(@ModelAttribute EducationListDTO educationListDTO, List<PersonEducation> personEducationList, HttpServletRequest request) {
        for(EducationDTO educationDTO :educationListDTO.getEducation()) {
            Optional<PersonEducation> oldPersonEducation = Optional.empty();
            if(Objects.nonNull(educationDTO.getId())){
                oldPersonEducation = educationRepository.findById(educationDTO.getId());
            }
            MultipartFile multipartFile = educationDTO.getProofPhoto();
            if (Objects.nonNull(multipartFile)) {
                if (oldPersonEducation.isPresent() && !Strings.isNullOrEmpty(oldPersonEducation.get().getProofPhoto())) {
                    if (!fileService.deleteFile(oldPersonEducation.get().getProofPhoto(), GlobalConstants.IMAGE, GlobalConstants.EDUCATION_EVENT, request.getServletContext())) {
                        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Problem to delete old Image. see logs", null), HttpStatus.OK);
                    }
                    educationDTO.setProofUrl(null);
                }
                String fileStorePath = fileService.uploadFile(multipartFile, request.getServletContext(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT);
                if (Objects.isNull(fileStorePath)) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Problem to store Image see more logs", null), HttpStatus.OK);
                }
                educationDTO.setProofUrl(fileStorePath);
            } else if(oldPersonEducation.isPresent()){
                educationDTO.setProofUrl(oldPersonEducation.get().getProofPhoto());
            }

            personEducationList.add(PersonEducation.create(educationDTO));
        }
        return null;
    }
}
