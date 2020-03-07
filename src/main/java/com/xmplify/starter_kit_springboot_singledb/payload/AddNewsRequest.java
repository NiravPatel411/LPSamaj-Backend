package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.NewsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewsRequest {

    @NotNull(message = "newsTypeId can not be null or empty")
    private String newsTypeId;

    @NotNull(message = "title can not be null or empty")
    private String title;

    @NotNull(message = "description can not be null or empty")
    private String description;

    @NotNull(message = "adminId can not be null or empty")
    private String adminId;

    private AddNewsMedia[] newsMedia;

}
