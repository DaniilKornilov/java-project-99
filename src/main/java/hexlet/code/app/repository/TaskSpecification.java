package hexlet.code.app.repository;

import hexlet.code.app.entity.Label;
import hexlet.code.app.entity.Task;
import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskSpecification {
    public static Specification<Task> titleContains(String substring) {
        return (root, query, cb) ->
                substring == null ? null
                        : cb.like(cb.lower(root.get("name")), "%" + substring.toLowerCase() + "%");
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? null
                        : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<Task> hasStatus(String slug) {
        return (root, query, cb) ->
                slug == null ? null
                        : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    public static Specification<Task> hasLabel(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return null;
            }
            Join<Task, Label> labels = root.join("labels");
            return cb.equal(labels.get("id"), labelId);
        };
    }
}
