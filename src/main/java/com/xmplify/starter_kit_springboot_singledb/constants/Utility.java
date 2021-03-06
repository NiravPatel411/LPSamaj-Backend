package com.xmplify.starter_kit_springboot_singledb.constants;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import com.xmplify.starter_kit_springboot_singledb.repository.ObservationRepository;
import com.xmplify.starter_kit_springboot_singledb.service.FileService;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Utility {

    PasswordEncoder encoder;
    public static PasswordEncoder passwordEncoder;
    public static FileService fileService;
    public static ObservationRepository ObservationRepositoy;

    @Autowired
    public Utility(PasswordEncoder passwordEncoder,FileService fileService,ObservationRepository ObservationRepositoy ) {
        this.encoder = passwordEncoder;
        Utility.passwordEncoder = passwordEncoder;
        Utility.fileService = fileService;
        Utility.ObservationRepositoy = ObservationRepositoy;
    }
}
