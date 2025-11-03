CREATE TABLE task_manager.task_statuses
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    slug       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO task_manager.task_statuses(name, slug)
VALUES ('Draft', 'draft'),
       ('In Review', 'to_review'),
       ('To Be Fixed', 'to_be_fixed'),
       ('To Publish', 'to_publish'),
       ('Published', 'published');
