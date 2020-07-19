package com.xmplify.starter_kit_springboot_singledb.mapper;

import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ActivityMapper {

    @Autowired
    AdminRepository adminRepository;

    public Activity convertDTOToActivity(AddActivityRequest request) {
        Optional<Admin> admin = adminRepository.findById(request.getAdminId());
        Activity activity = new Activity(request.getId(),
                request.getDescription(),
                request.getExtraData(),
                request.getAim(),
                request.getDateTime(),
                request.getConclusion(),
                request.getPlace(),
                admin.isPresent() ? admin.get() : null
        );
        activity.setCreatedBy(admin.isPresent() ? admin.get() : null);
        activity.setUpdatedBy(admin.isPresent() ? admin.get() : null);
        return activity;
    }

}
