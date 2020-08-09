package com.xmplify.starter_kit_springboot_singledb.constants;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GlobalConstants {
    public static final Object UNDER_SCORE = "_";
    public static final String DOT = ".";
    public static DateTimeFormatter DATE_FORMAT;
    public static List<String> HOBBIES;

    public static final String STATUS_ACTIVE = "Active";
    public static final String REQUEST_SUCCESS = "Success";
    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_FAILED = "Failed";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_NORMAL = "NORMAL_USER";
    public static final String BASIC_DETAIL = "Basic_Detail";
    public static final String ADDRESS = "Address";
    public static final String EDUCATION = "EducationDTO";
    public static final String NEWS_EVENT = "news";
    public static final String PROFILE_EVENT = "profile";
    public static final String EDUCATION_EVENT = "education";
    public static final String ACHIEVEMENT_EVENT = "achievement";
    public static final String ACTIVITY_EVENT = "activity";
    public static final String UPLOAD_DIR = "/WEB-INF/";
    public static final String UPLOAD_NEWS_MEDIA_FULL_PATH = UPLOAD_DIR + "image/news/";
    public static final String UPLOAD_NEWS_MEDIA_URL_PATH = "/image/news/";
    public static final String IMAGE = "Image";
    public static final String BACK_SLASH = "/";
    public static final String UPLOAD_PROFILE_PICTURE_PATH = GlobalConstants.UPLOAD_DIR + GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH + GlobalConstants.PROFILE_EVENT + GlobalConstants.BACK_SLASH;

    public static final String NEWS_TYPE = "News";
    public static final String FILE_DOWNLOAD_HTTP_HEADER = "attachment; filename=\"%s\"";

    public static final String SYNC_STATUS = "1";
    public static final String CREDIT = "CREDIT";
    public static final String DEBIT = "DEBIT";

    public static final String MASTER_ADMIN = "MASTER_ADMIN";
    public static final String ANONYMUS = "ANONYMUS_USER";
    public static final String NEWS_ADMIN = "NEWS_ADMIN";
    public static final String ACTIVITY_ADMIN = "ACTIVITY_ADMIN";
    public static final String ALL_DATA = "ALL_DATA";
    public static final int DEFAULT_PAGE_SIZE = 10;


    //User Fields
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String SURNAME_FIELD = "surname";
    public static final String EMAIL_FIELD = "email";
    public static final String MOBILENO_FIELD = "mobileno";
    public static final String GENDER_FIELD = "gender";
    public static final String MARITAL_STATUS_FIELD = "maritalStatus";
    public static final String BLOOD_GROUP_FIELD = "bloodGroup";
    public static final String BLOOD_DONATE_FIELD = "bloodDonate";

    // setting type

    public static final String BLOOD_DONATE = "BLOOD_DONATE";
    public static final String NEWS_NOTIFICATION = "NEWS_NOTIFICATION";
    public static final String CONTACT_NUMBER_VISIBILITY = "CONTACT_NUMBER_VISIBILITY";
    public static final String PROFILE_PICTURE_VISIBLITY = "PROFILE_PICTURE_VISIBLITY";
    public static final String ADMIN_CAN_UPDATE = "ADMIN_CAN_UPDATE";
    public static final String SHOW_YOUR_DETAIL_TO_ALL_USER = "SHOW_YOUR_DETAIL_TO_ALL_USER";


    //HOBBIES
    static {
        HOBBIES = new ArrayList<>();
        HOBBIES.add("Reading");
        HOBBIES.add("Watching TV");
        HOBBIES.add("Family Time");
        HOBBIES.add("Going to Movies");
        HOBBIES.add("Fishing");
        HOBBIES.add("Computer");
        HOBBIES.add("Gardening");
        HOBBIES.add("Renting Movies");
        HOBBIES.add("Walking");
        HOBBIES.add("Exercise");
        HOBBIES.add("Listening to Music");
        HOBBIES.add("Entertaining");
        HOBBIES.add("Hunting");
        HOBBIES.add("Team Sports");
        HOBBIES.add("Shopping");
        HOBBIES.add("Traveling");
        HOBBIES.add("Sleeping");
        HOBBIES.add("Socializing");
        HOBBIES.add("Sewing");
        HOBBIES.add("Golf");
        HOBBIES.add("Church Activities");
        HOBBIES.add("Relaxing");
        HOBBIES.add("Playing Music");
        HOBBIES.add("Housework");
        HOBBIES.add("Crafts");
        HOBBIES.add("Watching Sports");
        HOBBIES.add("Bicycling");
        HOBBIES.add("Playing Cards");
        HOBBIES.add("Hiking");
        HOBBIES.add("Cooking");
        HOBBIES.add("Eating Out");
        HOBBIES.add("Dating Online");
        HOBBIES.add("Swimming");
        HOBBIES.add("Camping");
        HOBBIES.add("Skiing");
        HOBBIES.add("Working on Cars");
        HOBBIES.add("Writing");
        HOBBIES.add("Boating");
        HOBBIES.add("Motorcycling");
        HOBBIES.add("Animal Care");
        HOBBIES.add("Bowling");
        HOBBIES.add("Painting");
        HOBBIES.add("Running");
        HOBBIES.add("Dancing");
        HOBBIES.add("Horseback Riding");
        HOBBIES.add("Tennis");
        HOBBIES.add("Theater");
        HOBBIES.add("Billiards");
        HOBBIES.add("Beach");
        HOBBIES.add("Volunteer Work");

        DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }


}
