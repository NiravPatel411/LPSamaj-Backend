package com.xmplify.starter_kit_springboot_singledb.service.impl;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.Media;
import com.xmplify.starter_kit_springboot_singledb.model.News;
import com.xmplify.starter_kit_springboot_singledb.payload.AllMedia;
import com.xmplify.starter_kit_springboot_singledb.payload.AllNews;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NewsService {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    AdminRepository adminRepository;


    @Transactional
    public void deleteNews(News news, HttpServletRequest request) {
        List<Media> media = mediaRepository.findAllByRelatedId(news.getId());
        ServletContext context = request.getServletContext();
        String fullPath = context.getRealPath(
                GlobalConstants.UPLOAD_IMAGE +
                        news.getNewsType().getType() + GlobalConstants.BACK_SLASH +
                        GlobalConstants.NEWS_MEDIA_TYPE + GlobalConstants.BACK_SLASH);
        for(Media med : media) {
            File file = new File(fullPath+med.getStorePath());
            file.delete();
        }
        newsRepository.delete(news);
    }

    public List<AllNews> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return  getAllNewsFromNews(newsList);
    }

    private List<AllNews> getAllNewsFromNews(List<News> newsList) {
        List<AllNews> allNewsList = new ArrayList<>();
        for(News news : newsList){
            AllNews allNews = new AllNews(news.getId(),
                    news.getNewsType().getId(),
                    news.getNewsType().getType(),
                    news.getTitle(),
                    news.getDescription(),
                    news.getAdminId().getId(),
                    news.getAdminId().getPerson().getFirstName(),
                    news.getAdminId().getPerson().getLastName(),
                    news.getAdminId().getPerson().getSurname(),
                    news.getExtraData(),
                    news.getCreatedAt() != null ? news.getCreatedAt().toString() : "",
                    news.getUpdatedAt() != null ? news.getUpdatedAt().toString() : "",null);
            getAllMediaByNews(allNews);
            allNewsList.add(allNews);
        }
        return allNewsList;
    }

    private void getAllMediaByNews(AllNews allNews) {
        List<Media> mediaList = mediaRepository.findAllByRelatedId(allNews.getId());
        List<AllMedia> allMediaList = new ArrayList<>();
        for(Media media : mediaList){
            String fullPath = GlobalConstants.BACK_SLASH +
                    allNews.getNewsTypeName() + GlobalConstants.BACK_SLASH +
                    GlobalConstants.NEWS_MEDIA_TYPE + GlobalConstants.BACK_SLASH;

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
        allNews.setAllMedia(allMediaList);
    }

    public List<AllNews> getAllNewsByType(String newsTypeId) {
        List<News> newsList = newsRepository.findByNewsTypeId(newsTypeId);
        return  getAllNewsFromNews(newsList);
    }

    public List<AllNews> getAllNewsByTypeAndAdmin(String newsTypeId,String role, String currentUserId) {
        String adminId = null;
        List<Admin> admins = adminRepository.isExistsAdminByPerson(currentUserId);
        if(Objects.nonNull(admins) && admins.stream().anyMatch(a -> a.getAdminRole().getName().equalsIgnoreCase(role))){
            for(Admin ad : admins){
                if(ad.getAdminRole().getName().equalsIgnoreCase(role)){
                   adminId = ad.getId();
                   break;
                }
            }
        }
        List<News> newsList = newsRepository.findByNewsTypeId(newsTypeId,adminId);
        return getAllNewsFromNews(newsList);
    }
}
