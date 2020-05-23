package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.ActivityMapper;
import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.Media;
import com.xmplify.starter_kit_springboot_singledb.payload.AllActivity;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.ActivityRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import com.xmplify.starter_kit_springboot_singledb.security.SecurityUtils;
import com.xmplify.starter_kit_springboot_singledb.service.impl.ActivityService;
import com.xmplify.starter_kit_springboot_singledb.service.impl.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    Validators validators;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ActivityService activityService;


    @GetMapping("/")
    public ResponseEntity<?> getAllActivity(@PageableDefault(page = 0,size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        if (GlobalConstants.MASTER_ADMIN.equalsIgnoreCase(SecurityUtils.getCurrentUserRole()) || GlobalConstants.ROLE_NORMAL.equalsIgnoreCase(SecurityUtils.getCurrentUserRole())) {
            Page<Activity> activityList = activityRepository.findAll(pageable);
            List<AllActivity> allActivityList = activityService.getAllActivityDTOWithMedia(activityList.getContent());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allActivityList), HttpStatus.OK);
        } else if (GlobalConstants.ACTIVITY_ADMIN.equalsIgnoreCase(SecurityUtils.getCurrentUserRole())){
            Page<Activity> activityList = activityRepository.findAllByAdminIdIdOrderByUpdatedAtDesc(SecurityUtils.getCurrentUserId(),pageable);
            List<AllActivity> allActivityList = activityService.getAllActivityDTOWithMedia(activityList.getContent());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allActivityList), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), true, "Can not access", null), HttpStatus.OK);
        }
    }

    @PostMapping("/addEditActivity")
    public ResponseEntity<?> addActivityData(@ModelAttribute AddActivityRequest request, HttpServletRequest context) throws IOException {
        List<String> dtoMessage = validators.validateAddActivityRequestDTO(request);
        if(!dtoMessage.isEmpty()){
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, dtoMessage.toString(), null),
                    HttpStatus.BAD_REQUEST);
        }
        Activity activity = activityMapper.convertDTOToActivity(request);
        if (activity.getId() != null && !activity.getId().isEmpty()) {
            List<String> messages = validators.validateUpdateActivityRequst(activity);
            if(!messages.isEmpty()){
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, messages.toString(), null),
                        HttpStatus.BAD_REQUEST);
            }
            boolean result = activityService.addUpdateActivity(activity,request.getActivityMedia(),context);
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Activity updated sucessfully", ""), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddActivityRequst(activity);
            if(!messages.isEmpty()){
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, messages.toString(), null),
                        HttpStatus.BAD_REQUEST);
            }
            boolean result = activityService.addUpdateActivity(activity,request.getActivityMedia(),context);
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Activity added sucessfully", ""), HttpStatus.OK);
        }
    }

}
