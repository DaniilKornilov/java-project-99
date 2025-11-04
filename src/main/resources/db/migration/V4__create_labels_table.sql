CREATE TABLE task_manager.labels
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(1000) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task_manager.task_labels
(
    task_id  BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    PRIMARY KEY (task_id, label_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (label_id) REFERENCES labels(id) ON DELETE CASCADE
);

INSERT INTO task_manager.labels(name)
VALUES ('feature'),
       ('bug');
