package com.xmplify.starter_kit_springboot_singledb.payload.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AccountDetailDTO {
    private String remainingAmountBeforeYear;
    private List<AccountDTO> account;
}
