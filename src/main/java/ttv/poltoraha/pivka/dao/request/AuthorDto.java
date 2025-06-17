package ttv.poltoraha.pivka.dao.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private String fullName;
    private Double avgRating; // Если нужно, можно оставить, иначе можно убрать
}