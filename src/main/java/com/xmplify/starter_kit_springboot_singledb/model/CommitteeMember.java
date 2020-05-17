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

    @OneToOne
    private CommitteeType committeeType;

    private String personId;

    private String designation; // free text maximum 20 character

}
