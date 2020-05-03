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
     insert into village (district_id, name, id) values ('1', 'Bhojana muvada', '2');

 -- roles
  INSERT INTO `36lpsamaj`.`roles` (`id`, `is_deleted`, `status`, `display_name`, `name`,`created_at`) VALUES ('1', '0', 'Active', 'Normal', 'Normal','9999-12-31 23:59:59');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('2', 'MASTER ADMIN', 'MASTER_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('3', 'NEWS ADMIN', 'NEWS_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('4', 'VILLAGE ADMIN', 'VILLAGE_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('5', 'NEWS ADMIN', 'NEWS_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('6', 'ACTIVITY ADMIN', 'ACTIVITY_ADMIN');

-- Degree
  INSERT INTO `36lpsamaj`.`degree` (`id`, `is_proof_nedded`, `name`) VALUES ('1', 0, 'BE');
  INSERT INTO `36lpsamaj`.`degree` (`id`, `is_proof_nedded`, `name`) VALUES ('2', 0, 'B.ED');
 -- User
   INSERT INTO `36lpsamaj`.`users`
   (`id`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `last_name`, `marital_status`, `mobileno`,`password`,`created_at`) VALUES
   ('1', '0', 'Active', '1994-04-11', 'nnmgpatel@mail.com', 'Nirav', 'male', 'Patel', 'Married', '9409324429','$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e','9999-12-31 23:59:59');

   INSERT INTO `36lpsamaj`.`users`
   (`id`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `last_name`, `marital_status`, `mobileno`,`password`,`created_at`) VALUES
   ('2', '0', 'Active', '1994-04-11', 'chiragpatel106@mail.com', 'Chirag', 'male', 'Patel', 'Married', '9924463385','$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e','9999-12-31 23:59:59');

   INSERT INTO `36lpsamaj`.`users`
   (`id`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `last_name`, `marital_status`, `mobileno`,`password`,`created_at`) VALUES
   ('3', '0', 'Active', '1994-04-11', 'radhapatel6798@mail.com', 'Radhi', 'female', 'Patel', 'Married', '7046357447','$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e','9999-12-31 23:59:59');
/*
   insert into user_roles (user_id, role_id) values ('1', '1');
   insert into user_roles (user_id, role_id) values ('2', '1');
*/
 --AdminP
   insert into admin (admin_role_id, name, person_id, id) values (2, 'Nirav', 1, 1);
   insert into admin (admin_role_id, name, person_id, id) values (2, 'Chirag', 2, 2);
   insert into admin (admin_role_id, name, person_id, id) values (3, 'Radhi', 3, 3);

 -- News
   insert into news_type (type, id,priority_number) values ('Image', 1,10);
 insert into news_type (type, id,priority_number) values ('Video', 2,20);