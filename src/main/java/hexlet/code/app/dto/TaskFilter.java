package hexlet.code.app.dto;

public record TaskFilter(String titleCont,
                         Long assigneeId,
                         String status,
                         Long labelId) {
}
