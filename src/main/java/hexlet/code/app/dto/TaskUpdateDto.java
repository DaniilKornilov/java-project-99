package hexlet.code.app.dto;

public record TaskUpdateDto(Integer index,
                            Long assigneeId,
                            String title,
                            String content,
                            String status) {

}
