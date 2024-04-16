CREATE TABLE `ROLES` (`id` integer PRIMARY KEY AUTO_INCREMENT,
                      `name` varchar(255) NOT NULL);

CREATE TABLE `USERS` (`id` integer PRIMARY KEY AUTO_INCREMENT,
                      `email` varchar(255) NOT NULL,
                      `name` varchar(255) NOT NULL,
                      `password` varchar(255) NOT NULL,
                      `created_at` timestamp,
                      `created_by` varchar(255),
                      `updated_at` timestamp,
                      `updated_by` varchar(255));

CREATE TABLE `USERS_ROLES` (`user_id` integer NOT NULL,
                            `role_id` integer NOT NULL,
                            PRIMARY KEY (`user_id`,`role_id`));

CREATE TABLE `TOPIC` (`id` integer PRIMARY KEY AUTO_INCREMENT,
                      `title` varchar(255) NOT NULL,
                      `description` varchar(2000),
                      `created_at` timestamp,
                      `created_by` varchar(255),
                      `updated_at` timestamp,
                      `updated_by` varchar(255));

CREATE TABLE `USERS_TOPIC` (`user_id` integer NOT NULL,
                            `topic_id` integer NOT NULL,
                            PRIMARY KEY (`user_id`,`topic_id`));

CREATE TABLE `ARTICLE` (`id` integer PRIMARY KEY AUTO_INCREMENT,
                        `title` varchar(255) NOT NULL,
                        `description` varchar(2000),
                        `topic_id` integer NOT NULL,
                        `created_at` timestamp,
                        `created_by` varchar(255),
                        `updated_at` timestamp,
                        `updated_by` varchar(255));

CREATE TABLE `COMMENT` (`id` integer PRIMARY KEY AUTO_INCREMENT,
                        `description` varchar(2000),
                        `article_id` integer NOT NULL,
                        `created_at` timestamp,
                        `created_by` varchar(255),
                        `updated_at` timestamp,
                        `updated_by` varchar(255));

CREATE UNIQUE INDEX `USERS_EMAIL_INDEX` ON `USERS` (`email`);
CREATE UNIQUE INDEX `USERS_NAME_INDEX` ON `USERS` (`name`);
CREATE UNIQUE INDEX `ROLES_INDEX` ON `ROLES` (`name`);

ALTER TABLE `USERS_ROLES` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `USERS_ROLES` ADD FOREIGN KEY (`role_id`) REFERENCES `ROLES` (`id`);
ALTER TABLE `ARTICLE` ADD FOREIGN KEY (`topic_id`) REFERENCES `TOPIC` (`id`);
ALTER TABLE `COMMENT` ADD FOREIGN KEY (`article_id`) REFERENCES `ARTICLE` (`id`);