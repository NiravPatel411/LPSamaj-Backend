package com.xmplify.starter_kit_springboot_singledb.service.impl;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Media;
import com.xmplify.starter_kit_springboot_singledb.payload.AddEditMedia;
import com.xmplify.starter_kit_springboot_singledb.repository.ActivityRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Transactional
    public boolean addUpdateActivity(Activity activity, AddEditMedia[] activityMedia, HttpServletRequest context) throws IOException {
        Activity savedActivity = activityRepository.save(activity);
        for(AddEditMedia editMedia : activityMedia){
            Media media = new Media();

            MultipartFile file = editMedia.getMedia();
            byte[] bytes = file.getBytes();
            ServletContext servletContext = context.getServletContext();
            String fullPath = context.getRealPath(
                    GlobalConstants.UPLOAD_IMAGE +
                            GlobalConstants.ACTIVITY_MEDIA_TYPE + GlobalConstants.BACK_SLASH +
                            GlobalConstants.ACTIVITY_MEDIA_TYPE + GlobalConstants.BACK_SLASH);
            Path path = Paths.get(fullPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String random = RandomStringUtils.random(5, true, true);
            String storePath = savedActivity.getId() + "_" + random + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1];
            Path filePath = Paths.get(path + GlobalConstants.BACK_SLASH + storePath);
            Files.write(filePath, bytes);

            media.setMediaType(editMedia.getMediaType());
            media.setRelatedId(savedActivity.getId());
            media.setRelatedType(GlobalConstants.ACTIVITY_MEDIA_TYPE);
            media.setStorePath(storePath);
            mediaRepository.save(media);
        }
        return true;
    }

}
