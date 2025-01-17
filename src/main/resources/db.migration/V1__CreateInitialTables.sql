-- Table: `users`
CREATE TABLE IF NOT EXISTS`users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `created_at` DATETIME(6) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) DEFAULT NULL,
    `password` VARCHAR(255) DEFAULT NULL,
    `profile_picture` VARCHAR(255) DEFAULT NULL,
    `provider` ENUM('GOOGLE', 'LOCAL') NOT NULL,
    `provider_id` VARCHAR(255) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    `role` ENUM('CUSTOMER', 'EVENT_MANAGER', 'VENDOR') NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE (`email`)
    );

-- Table: `event`
CREATE TABLE IF NOT EXISTS `event` (
    `event_id` BIGINT NOT NULL AUTO_INCREMENT,
    `date` DATETIME(6) NOT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `location` VARCHAR(255) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `creator_id` BIGINT NOT NULL,
    PRIMARY KEY (`event_id`),
    CONSTRAINT `FK_event_creator_users`
    FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`)
    );

-- Table: `event_attendants`
CREATE TABLE IF NOT EXISTS `event_attendants` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `event_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_event_attendants_event`
    FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`),
    CONSTRAINT `FK_event_attendants_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
    );

-- Table: `invoice`
CREATE TABLE IF NOT EXISTS `invoice` (
    `invoice_id` BIGINT NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(255) DEFAULT NULL,
    `due_date` DATETIME(6) DEFAULT NULL,
    `issue_date` DATETIME(6) NOT NULL,
    `event_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(38,2) NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`invoice_id`),
    CONSTRAINT `FK_invoice_event`
    FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`),
    CONSTRAINT `FK_invoice_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
    );

-- Table: `service`
CREATE TABLE IF NOT EXISTS `service` (
    `service_id` BIGINT NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(255) DEFAULT NULL,
    `name` VARCHAR(255) NOT NULL,
    `duration` DECIMAL(38,2) NOT NULL,
    `price` DECIMAL(38,2) NOT NULL,
    `event_id` BIGINT NOT NULL,
    `invoice_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`service_id`),
    CONSTRAINT `FK_service_event`
    FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`),
    CONSTRAINT `FK_service_invoice`
    FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`invoice_id`)
    );