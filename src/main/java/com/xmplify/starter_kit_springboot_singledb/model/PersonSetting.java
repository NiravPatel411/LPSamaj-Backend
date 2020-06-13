package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private User person;

    private Integer adminCanUpdate;
    private Integer profilePictureVisiblity;
    private Integer contactNumberVisibility;
    private Integer bloodDonate;
    private Integer newsNotification;

}
