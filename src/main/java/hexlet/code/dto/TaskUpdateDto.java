package hexlet.code.dto;

import java.util.Set;

public record TaskUpdateDto(Integer index,
                            Long assigneeId,
                            String title,
                            String content,
                            String status,
                            Set<Long> taskLabelIds) {

}
