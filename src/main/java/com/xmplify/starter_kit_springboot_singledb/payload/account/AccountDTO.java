package com.xmplify.starter_kit_springboot_singledb.payload.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AccountDTO {

    private String id;
    private String adminId;
    private String date;// credit or debit date (only date)
    private String type;// Credit / Debit
    private int amount;//
    private String reason;// reson for credit or debit

}
