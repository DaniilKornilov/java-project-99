package hexlet.code.dto;

public record TaskFilter(String titleCont,
                         Long assigneeId,
                         String status,
                         Long labelId) {
}
