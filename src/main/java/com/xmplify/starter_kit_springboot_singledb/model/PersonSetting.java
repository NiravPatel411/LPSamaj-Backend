package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PersonSetting")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PersonSetting {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String personSettingId;
    private Integer adminCanUpdate;
    private Integer profilePictureVisiblity;
    private Integer contactNumberVisibility;
    private Integer bloodDonate;
    private Integer newsNotification;

    public static PersonSetting getDefaultSetting(){
        return  new PersonSetting(null,1,1,1,1,1);
    }
}
