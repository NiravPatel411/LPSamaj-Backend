package com.xmplify.starter_kit_springboot_singledb.service;

import java.util.List;

import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonAllDetails;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

	 public List<User> findAll();

    User save(AddPersonDTO addPersonDTO, HttpServletRequest request);

    PersonAllDetails getPersonAllServiceByPersonId(String id);
}
