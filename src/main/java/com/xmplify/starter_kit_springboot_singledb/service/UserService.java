package com.xmplify.starter_kit_springboot_singledb.service;

import java.util.List;

import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonAllDetails;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;

public interface UserService {

	 public List<User> findAll();

    User save(AddPersonDTO addPersonDTO);

    PersonAllDetails getPersonAllServiceByPersonId(String id);
}
