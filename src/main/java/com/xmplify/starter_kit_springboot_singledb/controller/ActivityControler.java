package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.ActivityRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
public class ActivityControler {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MediaRepository mediaRepository;


    @GetMapping("/")
    public ResponseEntity<?> getAllActivity() {

        List<Activity> activityList = activityRepository.findAll();

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", activityList), HttpStatus.OK);

    }

    @PostMapping("/addEditActivity")
    public ResponseEntity<?> addAccountData(@ModelAttribute AddActivityRequest request) {

        Optional<Admin> admin = adminRepository.findById(request.getAdminId());

        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Your are not authorized to add the data", null), HttpStatus.BAD_REQUEST);
        }

        if (request.getId() != null && !request.getId().isEmpty()) {
            //Edit
            Activity activity = new Activity();
            activity.setId(request.getId());
            activity.setAim(request.getAim());
            activity.setAdminId(admin.get());
            activity.setDescription(request.getDescription());
            activity.setConclusion(request.getConclusion());
            activity.setDateTime(request.getDateTime());
            activity.setExtraData(request.getExtraData() != null ? request.getExtraData() : "");
            activity.setCreatedBy(admin.get());
            activity.setUpdatedBy(admin.get());

            if (request.getDeletedMediaIds() != null && !(request.getDeletedMediaIds().isEmpty())) {
                String[] strings = request.getDeletedMediaIds().split(",");
                for (int i = 0; i < strings.length; i++) {
                    mediaRepository.deleteById(strings[i]);
                }
            }

            Object obj = activityRepository.save(activity);
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Activity udate sucessfully", ""), HttpStatus.OK);

        } else {
            //add

            Activity activity = new Activity();
            activity.setAim(request.getAim());
            activity.setDescription(request.getDescription());
            activity.setConclusion(request.getConclusion());
            activity.setDateTime(request.getDateTime());
            activity.setAdminId(admin.get());
            activity.setExtraData(request.getExtraData() != null ? request.getExtraData() : "");
            activity.setCreatedBy(admin.get());
            activity.setUpdatedBy(admin.get());

            Object obj = activityRepository.save(activity);
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Activity added sucessfully", ""), HttpStatus.OK);
        }
    }
}
