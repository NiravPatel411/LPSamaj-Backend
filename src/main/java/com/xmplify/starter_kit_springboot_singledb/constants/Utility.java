package com.xmplify.starter_kit_springboot_singledb.constants;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Utility {

    PasswordEncoder encoder;
    public static PasswordEncoder passwordEncoder;

    @Autowired
    public Utility(PasswordEncoder passwordEncoder) {
        this.encoder = passwordEncoder;
        Utility.passwordEncoder = passwordEncoder;
    }
}
