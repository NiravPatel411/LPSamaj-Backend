-- Country
  insert into country (name, id) values ('India', '1');
  insert into country (name, id) values ('Pakistan', '2');

-- states
  insert into state (country_id, name, id) values ('1', 'Gujarat', '1');
  insert into state (country_id, name, id) values ('1', 'Rajasthan', '2');

-- Districts
  insert into district (name, state_id, id) values ('Gandhinagar', '1', '1');
   insert into district (name, state_id, id) values ('Ahmedabad', '1', '2');

-- Village
  insert into village (district_id, name, id) values ('1', 'Shiyavada', '1');

-- roles
 INSERT INTO `36lpsamaj`.`roles` (`id`, `is_deleted`, `status`, `display_name`, `name`) VALUES ('1', '0', 'Active', 'Normal', 'Normal');
 INSERT INTO `36lpsamaj`.`admin_role` (`id`, `is_deleted`, `status`, `display_name`, `name`) VALUES ('1', '0', 'Active', 'NEWS_ADMIN', 'NEWS_ADMIN');

-- User
  INSERT INTO `36lpsamaj`.`users` (`id`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `last_name`, `maritual_status`, `mobileno`,`password`) VALUES ('1', '0', 'Active', '1994-04-11 00:00:00', 'admin@mail.com', 'admin', 'male', 'admin', 'single', '9999999999','$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e');
  insert into user_roles (user_id, role_id) values ('1', '1');

--Admin
  insert into admin (admin_role_id, name, person_id, id) values (1, 'admin', 1, 1);

-- News
  insert into news_type (type, id) values ('Image', 1);