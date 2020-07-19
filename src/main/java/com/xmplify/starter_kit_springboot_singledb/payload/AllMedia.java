package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMedia {

    private String id;

    private String mediaType;

    private String relatedType;

    private String relatedId;

    private String storePath;

    private String createdAt;

    private int idDeleted = 0;
}
