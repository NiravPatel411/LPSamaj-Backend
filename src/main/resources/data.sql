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
   insert into village (district_id, name, id,short_form) values ('1', 'Shiyavada', '1','SHYVD');
     insert into village (district_id, name, id,short_form) values ('1', 'Bhojana muvada', '2','BHJNMVD');

 -- roles
  INSERT INTO `36lpsamaj`.`roles` (`id`, `is_deleted`, `status`, `display_name`, `name`,`created_at`) VALUES ('1', '0', 'Active', 'Normal', 'Normal','9999-12-31 23:59:59');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('2', 'MASTER ADMIN', 'MASTER_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('3', 'NEWS ADMIN', 'NEWS_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('4', 'VILLAGE ADMIN', 'VILLAGE_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('5', 'NEWS ADMIN', 'NEWS_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('6', 'ACTIVITY ADMIN', 'ACTIVITY_ADMIN');
  INSERT INTO `36lpsamaj`.`admin_role` (`id`, `display_name`, `name`) VALUES ('7', 'NORMAL USER', 'NORMAL_USER');

-- Degree
  INSERT INTO `36lpsamaj`.`degree` (`id`, `is_proof_nedded`, `name`) VALUES ('1', 0, 'BE');
  INSERT INTO `36lpsamaj`.`degree` (`id`, `is_proof_nedded`, `name`) VALUES ('2', 0, 'B.ED');

  --Settings

  INSERT INTO `36lpsamaj`.`person_setting` (`person_setting_id`, `admin_can_update`, `blood_donate`, `contact_number_visibility`, `news_notification`, `profile_picture_visiblity`) VALUES ('1', '1', '1', '1', '1', '1');
  INSERT INTO `36lpsamaj`.`person_setting` (`person_setting_id`, `admin_can_update`, `blood_donate`, `contact_number_visibility`, `news_notification`, `profile_picture_visiblity`) VALUES ('2', '1', '1', '1', '1', '1');
  INSERT INTO `36lpsamaj`.`person_setting` (`person_setting_id`, `admin_can_update`, `blood_donate`, `contact_number_visibility`, `news_notification`, `profile_picture_visiblity`) VALUES ('3', '1', '1', '1', '1', '1');
  INSERT INTO `36lpsamaj`.`person_setting` (`person_setting_id`, `admin_can_update`, `blood_donate`, `contact_number_visibility`, `news_notification`, `profile_picture_visiblity`) VALUES ('4', '1', '1', '1', '1', '1');
 -- User

  INSERT INTO `36lpsamaj`.`users` (`id`, `created_at`, `is_deleted`, `status`, `birth_date`, `email`, `family_code`, `first_name`, `gender`, `hobby`, `last_name`, `marital_status`, `mobileno`, `password`, `profile_pic`, `surname`, `user_name`, `village_id`, `person_setting_id`)
  VALUES ('1', '9999-12-31 23:59:59', '0', 'Active', '1994-04-11', 'nnmgpatel@mail.com', 'SHYVD', 'Nirav', 'male', 'Watching TV', 'Patel', 'Married', '9409324429', '$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e', 'null', 'Patel', 'User#Nirav', '1', '1');

  insert into admin (admin_role_id, name, person_id, id) values (2, 'Nirav', 1, 1);
  UPDATE `36lpsamaj`.`users` SET `created_by_id` = '1' WHERE (`id` = '1');

  INSERT INTO `36lpsamaj`.`users` (`id`, `created_at`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `hobby`, `last_name`, `marital_status`, `mobileno`, `password`, `user_name`, `village_id`, `created_by_id`, `person_setting_id`)
   VALUES ('2', '9999-12-31 23:59:59', '0', 'Active', '1994-04-11', 'chiragpatel106@mail.com', 'Chirag', 'male', 'Gardening', 'Patel', 'Married', '9924463385', '$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e', 'User#Chirag', '1', '1', '2');

  INSERT INTO `36lpsamaj`.`users` (`id`, `created_at`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `hobby`, `last_name`, `marital_status`, `mobileno`, `password`, `user_name`, `village_id`, `created_by_id`, `person_setting_id`)
   VALUES ('3', '9999-12-31 23:59:59', '0', 'Active', '1994-04-11', 'radhapatel6798@mail.com', 'Radhi', 'female', 'Traveling', 'Patel', 'Married', '7046357447', '$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e', 'User#Radhi', '1', '1', '3');

  INSERT INTO `36lpsamaj`.`users` (`id`, `created_at`, `is_deleted`, `status`, `birth_date`, `email`, `first_name`, `gender`, `last_name`, `marital_status`, `mobileno`, `password`, `surname`, `user_name`, `village_id`, `created_by_id`, `person_setting_id`)
   VALUES ('4', '9999-12-31 23:59:59', '0', 'Active', '1994-04-11', 'abc@mail.com', 'Ramesh', 'male', 'Patel', 'Married', '9924463386', '$2a$10$s1Fz9XJFbpJQTeTCjRdlE.oIzrvePZ5yihqTJaKuwNGC1X4HJ6b5e', 'Patel', 'User#Ramesh', '1', '1', '4');
/*
   insert into user_roles (user_id, role_id) values ('1', '1');
   insert into user_roles (user_id, role_id) values ('2', '1');
*/
 --AdminP
   insert into admin (admin_role_id, name, person_id, id) values (2, 'Chirag', 2, 2);
   insert into admin (admin_role_id, name, person_id, id) values (3, 'Radhi', 3, 3);

 -- News
 insert into news_type (type, id,priority_number) values ('Image', 1,10);
 insert into news_type (type, id,priority_number) values ('Video', 2,20);

--Observation
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('1', '1', '2');
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('2', '1', '3');
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('3', '2', '3');
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('4', '3', '1');
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('5', '3', '2');
INSERT INTO `36lpsamaj`.`observation` (`id`, `observer_id`, `observing_id`) VALUES ('6', '3', '4');