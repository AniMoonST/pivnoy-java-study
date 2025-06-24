package ttv.poltoraha.pivka.service;

import jakarta.persistence.EntityNotFoundException;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;

public interface ReviewService {
    public void createReview(ReviewRequestDto requestDto);

    public default void deleteReview(Integer reviewId) {
        // Логика удаления отзыва
        // Если отзыв не найден, выбросьте исключение
        throw new EntityNotFoundException("Review not found with id: " + reviewId);
    }

    public default void updateReview(Integer reviewId, ReviewRequestDto dto) {
        // Логика обновления отзыва
        // Если отзыв не найден, выбросьте исключение
        throw new EntityNotFoundException("Review not found with id: " + reviewId);
    }
}
