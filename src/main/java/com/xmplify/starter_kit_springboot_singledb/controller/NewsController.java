package com.xmplify.starter_kit_springboot_singledb.controller;


import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.Media;
import com.xmplify.starter_kit_springboot_singledb.model.News;
import com.xmplify.starter_kit_springboot_singledb.model.NewsType;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.MediaRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.NewsRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.NewsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    NewsTypeRepository newsTypeRepository;

    @Autowired
    MediaRepository mediaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        List<AllNews> allNewsList = new ArrayList<>();
        for (News news : newsList) {
            AllNews allNews = new AllNews();
            allNews.setId(news.getId());
            allNews.setAdminId(news.getAdminId().getId());
            allNews.setDescription(news.getDescription());
            allNews.setNewsTypeId(news.getNewsType().getId());
            allNews.setNewsTypeName(news.getNewsType().getType());
            allNews.setTitle(news.getTitle());
            allNews.setAdminFirstName(news.getAdminId().getPerson().getFirstName());
            allNews.setAdminLastName(news.getAdminId().getPerson().getLastName());
            allNews.setAdminSurname(news.getAdminId().getPerson().getSurname());

            allNews.setCreatedAt(news.getCreatedAt() != null ? news.getCreatedAt().toString() : null);
            allNews.setUpdatedAt(news.getUpdatedAt() != null ? news.getUpdatedAt().toString() : null);
            List<Media> mediaList = mediaRepository.findAllByRelatedId(news.getId());
            if (mediaList != null) {
                List<AllMedia> allMedia = new ArrayList<>();

                for (Media media : mediaList) {
                    AllMedia allMediaObj = new AllMedia();
                    allMediaObj.setId(media.getId());
                    allMediaObj.setMediaType(media.getMediaType());
                    allMediaObj.setRelatedId(media.getRelatedId());
                    allMediaObj.setStorePath(media.getStorePath());
                    allMediaObj.setCreatedAt(media.getCreatedAt());
                    allMediaObj.setIdDeleted(media.getIdDeleted());
                    allMediaObj.setRelatedType(media.getRelatedType());
                    allMedia.add(allMediaObj);
                }
                allNews.setAllMedia(allMedia);
            }
            allNewsList.add(allNews);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allNewsList), HttpStatus.OK);
    }

    @GetMapping("/getAllNewsType")
    public ResponseEntity<?> getAllNewsType() {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", newsTypeRepository.findAllByOrOrderByPriority_number()), HttpStatus.OK);
    }

    @PostMapping("/addNewsType")
    public ResponseEntity<?> AddNewsType(@RequestBody AddNewsType addNewsType) {
        NewsType newsType = new NewsType();
        newsType.setType(addNewsType.getName());
        newsType.setPriority_number(addNewsType.getPriority_number());

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", newsTypeRepository.save(newsType)), HttpStatus.OK);
    }

    @PostMapping("/updateNewsType")
    public ResponseEntity<?> AddNewsType(@RequestBody UpdateNewsType updateNewsType) {
        Optional<NewsType> newsType = newsTypeRepository.findById(updateNewsType.getId());
        if (!newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "News id not found", null), HttpStatus.OK);
        }
        NewsType newsTypeNew = new NewsType();
        newsTypeNew.setType(updateNewsType.getName());
        newsTypeNew.setId(updateNewsType.getId());

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", newsTypeRepository.save(newsTypeNew)), HttpStatus.OK);
    }

    @DeleteMapping("/deleteNewsType/{typeId}")
    public ResponseEntity<?> deleteNewsType(@PathVariable String typeId) {
        Optional<NewsType> newsType = newsTypeRepository.findById(typeId);
        if (!newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "News id not found", null), HttpStatus.OK);
        }
        newsTypeRepository.delete(newsType.get());
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, typeId + " Successfully deleted", null), HttpStatus.OK);
    }

    @GetMapping("/getNewsType/{typeId}")
    public ResponseEntity<?> getNewsTypeById(@PathVariable String typeId) {
        Optional<NewsType> newsType = newsTypeRepository.findById(typeId);
        if (newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "News id not found", null), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", newsRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNews(@ModelAttribute AddNewsRequest newsRequest, BindingResult result, HttpServletRequest request) {
        Media media = new Media();
        Map<String, Object> retObject = new HashMap<>();
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (Object object : result.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    System.out.println(fieldError.getCode());
                    errors.add(fieldError.getDefaultMessage());
                }
                if (object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;

                    System.out.println(objectError.getCode());
                    errors.add(objectError.getDefaultMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }
        Optional<Admin> admin = adminRepository.findById(newsRequest.getAdminId());
        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found admin by adminId", null), HttpStatus.OK);
        }
        Optional<NewsType> newsType = newsTypeRepository.findById(newsRequest.getNewsTypeId());
        if (!newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found News Type by newsTypeId", null), HttpStatus.OK);
        }
        News news = new News();
        AllNews ret = new AllNews();
        news.setAdminId(admin.get());
        news.setDescription(newsRequest.getDescription());
        news.setNewsType(newsType.get());
        news.setDescription(newsRequest.getDescription());
        news.setTitle(newsRequest.getTitle());

        News newsResult = newsRepository.save(news);

        ret.setAdminId(newsRequest.getAdminId());
        ret.setDescription(newsRequest.getDescription());
        ret.setNewsTypeId(newsType.get().getId());
        ret.setNewsTypeName(newsType.get().getType());
        ret.setTitle(newsResult.getTitle());
        ret.setId(newsResult.getId());
        ret.setAdminSurname(newsResult.getAdminId().getPerson().getSurname());
        ret.setAdminLastName(newsResult.getAdminId().getPerson().getLastName());
        ret.setAdminFirstName(newsResult.getAdminId().getPerson().getFirstName());
        ret.setCreatedAt(newsResult.getCreatedAt() != null ? newsResult.getCreatedAt().toString() : null);
        ret.setUpdatedAt(newsResult.getUpdatedAt() != null ? newsResult.getUpdatedAt().toString() : null);
        retObject.put("NewsDetail", newsResult);
        List<AllMedia> allMedia = new ArrayList<>();
        if (newsRequest.getNewsMedia() != null) {
            for (int i = 0; i < newsRequest.getNewsMedia().length; i++) {
                // for(AddNewsMedia addNewsMedia : newsRequest.getNewsMedia()){
                AllMedia retMedia = new AllMedia();
                AddNewsMedia addNewsMedia = newsRequest.getNewsMedia()[i];
                MultipartFile file = addNewsMedia.getMedia();

                if (file.isEmpty()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "File can not be empty", null),
                            HttpStatus.BAD_REQUEST);
                }
                try {
                    byte[] bytes = file.getBytes();
                    ServletContext context = request.getServletContext();
                    String fullPath = context.getRealPath(GlobalConstants.UPLOAD_NEWS_MEDIA_FULL_PATH);
                    Path path = Paths.get(fullPath);
                    if (!Files.exists(path)) {
                        Files.createDirectories(path);
                    }
                    Path filePath = Paths.get(path + GlobalConstants.BACK_SLASH + newsResult.getId() + "_" + i + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1]);
                    Files.write(filePath, bytes);

                    media.setMediaType(addNewsMedia.getMediaType());
                    media.setRelatedId(newsResult.getId());
                    media.setRelatedType(GlobalConstants.NEWS_TYPE);

                    //ServletContext context = request.getServletContext();
                    media.setStorePath(ServletUriComponentsBuilder.fromCurrentContextPath().path(GlobalConstants.UPLOAD_NEWS_MEDIA_URL_PATH + newsResult.getId() + "_" + i + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1]).toUriString());


                    Media mediaDb = mediaRepository.save(media);
                    retMedia.setRelatedType(mediaDb.getRelatedType());
                    retMedia.setIdDeleted(mediaDb.getIdDeleted());
                    retMedia.setId(mediaDb.getId());
                    retMedia.setCreatedAt(mediaDb.getCreatedAt());
                    retMedia.setStorePath(mediaDb.getStorePath());
                    retMedia.setMediaType(mediaDb.getMediaType());
                    allMedia.add(retMedia);
                    // byte[] bytes1 = file.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "IO Exception", e.toString()), HttpStatus.OK);
                }
            }
        }
        ret.setAllMedia(allMedia);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", ret), HttpStatus.OK);
        //return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", newsRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateNews(@ModelAttribute UpdateNewsRequest updateNewsRequest, BindingResult result, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        Map<String, Object> retObject = new HashMap<>();
        if (result.hasErrors()) {
            for (Object object : result.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    System.out.println(fieldError.getCode());
                    errors.add(fieldError.getDefaultMessage());
                }
                if (object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;

                    System.out.println(objectError.getCode());
                    errors.add(objectError.getDefaultMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }
        Optional<Admin> admin = adminRepository.findById(updateNewsRequest.getAdminId());
        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found admin by adminId", null), HttpStatus.OK);
        }
        Optional<NewsType> newsType = newsTypeRepository.findById(updateNewsRequest.getNewsTypeId());
        if (!newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found News Type by newsTypeId", null), HttpStatus.OK);
        }

        Optional<News> news = newsRepository.findById(updateNewsRequest.getNewsId());
        List<AllNews> allNewsList = new ArrayList<>();
        AllNews ret = new AllNews();
        if (news.isPresent()) {
            News oldNews = news.get();
            News newNews = new News();
            newNews.setId(oldNews.getId());
            newNews.setDescription(updateNewsRequest.getDescription());
            newNews.setNewsType(newsType.get());
            newNews.setAdminId(admin.get());
            newNews.setTitle(updateNewsRequest.getTitle());
            News newsResult = newsRepository.save(newNews);
            ret.setAdminId(admin.get().getId());
            ret.setDescription(newsResult.getDescription());
            ret.setNewsTypeId(newsType.get().getId());
            ret.setNewsTypeName(newsType.get().getType());
            ret.setTitle(newsResult.getTitle());
            ret.setId(newsResult.getId());
            ret.setAdminSurname(newsResult.getAdminId().getPerson().getSurname());
            ret.setAdminLastName(newsResult.getAdminId().getPerson().getLastName());
            ret.setAdminFirstName(newsResult.getAdminId().getPerson().getFirstName());
            ret.setCreatedAt(newsResult.getCreatedAt() != null ? newsResult.getCreatedAt().toString() : null);
            ret.setUpdatedAt(newsResult.getUpdatedAt() != null ? newsResult.getUpdatedAt().toString() : null);
        }
        List<AllMedia> allMedia = new ArrayList<>();

        for (int i = 0; i < updateNewsRequest.getNewsMedia().length; i++) {
            UpdateNewsMedia updateNewsMedia = updateNewsRequest.getNewsMedia()[i];
            AllMedia retMedia = new AllMedia();
            Media media = new Media();
            if (Objects.nonNull(updateNewsMedia.getMediaId())) {
                Optional<Media> oldMedia = mediaRepository.findById(updateNewsMedia.getMediaId());
                if (oldMedia.isPresent()) {
                    media.setId(oldMedia.get().getId());
                    File file = new File(GlobalConstants.UPLOAD_NEWS_MEDIA_FULL_PATH + news.get().getId() + "_" + i + "." + updateNewsMedia.getMedia().getOriginalFilename().split("\\.(?=[^\\.]+$)")[1]);
                    file.delete();
                }
            }


            MultipartFile file = updateNewsMedia.getMedia();

            if (file.isEmpty()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "File can not be empty", null),
                        HttpStatus.BAD_REQUEST);
            }
            try {
                byte[] bytes = file.getBytes();

                Path path = Paths.get(GlobalConstants.UPLOAD_NEWS_MEDIA_FULL_PATH);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                Path filePath = Paths.get(GlobalConstants.UPLOAD_NEWS_MEDIA_FULL_PATH + news.get().getId() + "_" + i + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1]);
                Files.write(filePath, bytes);

                media.setMediaType(updateNewsMedia.getMediaType());
                media.setRelatedId(news.get().getId());
                media.setMediaType(updateNewsMedia.getMediaType());
                media.setRelatedType(GlobalConstants.NEWS_TYPE);

                ServletContext context = request.getServletContext();
                media.setStorePath(ServletUriComponentsBuilder.fromCurrentContextPath().path(GlobalConstants.UPLOAD_NEWS_MEDIA_URL_PATH + news.get().getId() + "_" + i + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1]).toUriString());
                Media mediaDb = mediaRepository.save(media);
                retMedia.setRelatedType(mediaDb.getRelatedType());
                retMedia.setIdDeleted(mediaDb.getIdDeleted());
                retMedia.setId(mediaDb.getId());
                retMedia.setCreatedAt(mediaDb.getCreatedAt());
                retMedia.setStorePath(mediaDb.getStorePath());
                retMedia.setMediaType(mediaDb.getMediaType());
                allMedia.add(retMedia);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "IO Exception", e.toString()), HttpStatus.OK);
            }
        }
        ret.setAllMedia(allMedia);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", ret), HttpStatus.OK);
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<?> getMediaByUrl(@PathVariable String fileName, HttpServletRequest request) {
        Path filePath = Paths.get(GlobalConstants.UPLOAD_NEWS_MEDIA_FULL_PATH + fileName);
        String contentType = null;
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format(GlobalConstants.FILE_DOWNLOAD_HTTP_HEADER, resource.getFilename()))
                .body(resource);
    }

    @GetMapping("/byNewsTypeId/{newsTypeId}")
    public ResponseEntity<?> getAllNewsByNewsTypeId(@PathVariable String newsTypeId) {
        List<AllNews> allNewsList = new ArrayList<>();
        Optional<NewsType> newsType = newsTypeRepository.findById(newsTypeId);
        if (!newsType.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Invalid id. News type not found", null), HttpStatus.OK);
        }

        List<News> newsList = newsRepository.findByNewsTypeId(newsType.get().getId());
        if (newsList == null) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allNewsList), HttpStatus.OK);
        }

        for (News news : newsList) {
            AllNews allNews = new AllNews();
            allNews.setId(news.getId());
            allNews.setAdminId(news.getAdminId().getId());
            allNews.setDescription(news.getDescription());
            allNews.setNewsTypeId(news.getNewsType().getId());
            allNews.setNewsTypeName(news.getNewsType().getType());
            allNews.setTitle(news.getTitle());
            allNews.setAdminFirstName(news.getAdminId().getPerson().getFirstName());
            allNews.setAdminLastName(news.getAdminId().getPerson().getLastName());
            allNews.setAdminSurname(news.getAdminId().getPerson().getSurname());

            allNews.setCreatedAt(news.getCreatedAt() != null ? news.getCreatedAt().toString() : null);
            allNews.setUpdatedAt(news.getUpdatedAt() != null ? news.getUpdatedAt().toString() : null);
            List<Media> mediaList = mediaRepository.findAllByRelatedId(news.getId());
            if (mediaList != null) {
                List<AllMedia> allMedia = new ArrayList<>();

                for (Media media : mediaList) {
                    AllMedia allMediaObj = new AllMedia();
                    allMediaObj.setId(media.getId());
                    allMediaObj.setMediaType(media.getMediaType());
                    allMediaObj.setRelatedId(media.getRelatedId());
                    allMediaObj.setStorePath(media.getStorePath());
                    allMediaObj.setCreatedAt(media.getCreatedAt());
                    allMediaObj.setIdDeleted(media.getIdDeleted());
                    allMediaObj.setRelatedType(media.getRelatedType());
                    allMedia.add(allMediaObj);
                }
                allNews.setAllMedia(allMedia);
            }
            allNewsList.add(allNews);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allNewsList), HttpStatus.OK);
    }

    @GetMapping("/byAdminId/{adminId}")
    public ResponseEntity<?> getAllNewsByAdminId(@PathVariable String adminId) {
        List<AllNews> allNewsList = new ArrayList<>();
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Invalid id. Admin not found", null), HttpStatus.OK);
        }

        List<News> newsList = newsRepository.getByAdminId(adminId);
        if (newsList == null) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "SUCCESS", allNewsList), HttpStatus.OK);
        }
        for (News news : newsList) {
            AllNews allNews = new AllNews();
            allNews.setId(news.getId());
            allNews.setAdminId(news.getAdminId().getId());
            allNews.setDescription(news.getDescription());
            allNews.setNewsTypeId(news.getNewsType().getId());
            allNews.setNewsTypeName(news.getNewsType().getType());
            allNews.setTitle(news.getTitle());
            allNews.setAdminFirstName(news.getAdminId().getPerson().getFirstName());
            allNews.setAdminLastName(news.getAdminId().getPerson().getLastName());
            allNews.setAdminSurname(news.getAdminId().getPerson().getSurname());

            allNews.setCreatedAt(news.getCreatedAt() != null ? news.getCreatedAt().toString() : null);
            allNews.setUpdatedAt(news.getUpdatedAt() != null ? news.getUpdatedAt().toString() : null);
            List<Media> mediaList = mediaRepository.findAllByRelatedId(news.getId());
            if (mediaList != null) {
                List<AllMedia> allMedia = new ArrayList<>();

                for (Media media : mediaList) {
                    AllMedia allMediaObj = new AllMedia();
                    allMediaObj.setId(media.getId());
                    allMediaObj.setMediaType(media.getMediaType());
                    allMediaObj.setRelatedId(media.getRelatedId());
                    allMediaObj.setStorePath(media.getStorePath());
                    allMediaObj.setCreatedAt(media.getCreatedAt());
                    allMediaObj.setIdDeleted(media.getIdDeleted());
                    allMediaObj.setRelatedType(media.getRelatedType());
                    allMedia.add(allMediaObj);
                }
                allNews.setAllMedia(allMedia);
            }
            allNewsList.add(allNews);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", allNewsList), HttpStatus.OK);

    }

}
