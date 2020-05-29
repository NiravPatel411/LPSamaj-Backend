package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "committee_member")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommitteeMember extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "committee_type_Id")
    private CommitteeType committeeType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    private String designation;
}
