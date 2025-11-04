CREATE TABLE task_manager.tasks
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    index       INT,
    description VARCHAR(255),
    status_id   BIGINT       NOT NULL,
    assignee_id BIGINT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE task_manager.tasks
    ADD CONSTRAINT FK_TASK_STATUS FOREIGN KEY (status_id) REFERENCES task_manager.task_statuses (id);

ALTER TABLE task_manager.tasks
    ADD CONSTRAINT FK_TASK_USER FOREIGN KEY (assignee_id) REFERENCES task_manager.users (id);
