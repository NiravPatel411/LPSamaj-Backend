package com.xmplify.starter_kit_springboot_singledb.service.impl;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Media;
import com.xmplify.starter_kit_springboot_singledb.payload.AddEditMedia;
import com.xmplify.starter_kit_springboot_singledb.payload.AllActivity;
import com.xmplify.starter_kit_springboot_singledb.payload.AllMedia;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.ActivityRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    MediaRepository mediaRepository;

    @Transactional
    public boolean addUpdateActivity(Activity activity, AddActivityRequest activityRequest, HttpServletRequest context) throws IOException {
        Activity savedActivity = activityRepository.save(activity);
        if(Objects.nonNull(activityRequest) && Objects.nonNull(activityRequest.getActivityMedia())) {
            for (AddEditMedia editMedia : activityRequest.getActivityMedia()) {
                Media media = new Media();

                MultipartFile file = editMedia.getMedia();
                byte[] bytes = file.getBytes();
                ServletContext servletContext = context.getServletContext();
                String fullPath = servletContext.getRealPath(
                        GlobalConstants.UPLOAD_IMAGE +
                                GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH +
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

        }

        if(Objects.nonNull(activityRequest) && Objects.nonNull(activityRequest.getDeletedMediaIds())){
            ServletContext servletContext = context.getServletContext();
            String fullPath1 = servletContext.getRealPath(
                    GlobalConstants.UPLOAD_IMAGE +
                            GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH +
                            GlobalConstants.ACTIVITY_MEDIA_TYPE + GlobalConstants.BACK_SLASH);
            for(String mediaId : activityRequest.getDeletedMediaIds()) {
                Optional<Media> media = mediaRepository.findById(mediaId);
                if(media.isPresent()){
                    File file = new File(fullPath1+media.get().getStorePath());
                    file.delete();
                }
                mediaRepository.delete(media.get());
            }
        }
        return true;
    }

    public List<AllActivity> getAllActivityDTOWithMedia(List<Activity> content) {
        List<AllActivity> allActivityList = new ArrayList<>();
        for(Activity activity : content){
            AllActivity allActivity = new AllActivity(activity.getId(),
                    activity.getAim(),
                    activity.getDescription(),
                    activity.getDateTime(),
                    activity.getConclusion(),
                    activity.getPlace(),
                    activity.getAdminId().getId(),
                    activity.getAdminId().getPerson().getFirstName(),
                    activity.getAdminId().getPerson().getLastName(),
                    activity.getAdminId().getPerson().getLastName(),
                    activity.getExtraData(),
                    activity.getCreatedAt().toString(),
                    activity.getUpdatedAt().toString(),
                    null
                    );
            getAllMediaByActivity(allActivity);
            allActivityList.add(allActivity);
        }
        return allActivityList;
    }

    private void getAllMediaByActivity(AllActivity allActivity) {
        List<Media> mediaList = mediaRepository.findAllByRelatedId(allActivity.getId());
        List<AllMedia> allMediaList = new ArrayList<>();
        for(Media media : mediaList){
            String fullPath = GlobalConstants.BACK_SLASH +
                    GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH +
                    GlobalConstants.ACTIVITY_MEDIA_TYPE + GlobalConstants.BACK_SLASH;

            String storePath = media.getStorePath();
            AllMedia allMedia = new AllMedia(
                    media.getId(),
                    media.getMediaType(),
                    media.getRelatedType(),
                    media.getRelatedId(),
                    ServletUriComponentsBuilder.fromCurrentContextPath().path(fullPath + storePath).toUriString(),
                    media.getCreatedAt() != null ? media.getCreatedAt().toString() : "",
                    0
            );
            allMediaList.add(allMedia);
        }
        allActivity.setAllMedia(allMediaList);
    }
}
