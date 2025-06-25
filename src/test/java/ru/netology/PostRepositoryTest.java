package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import static org.junit.jupiter.api.Assertions.*;

public class PostRepositoryTest {
    private PostRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PostRepository();
    }

    @Test
    void testSaveNewPost() {
        // Проверяем создание нового поста с id = 0
        Post post = new Post(0, "Test content");
        Post savedPost = repository.save(post);

        // Ожидаем, что id будет автоматически присвоен (1)
        assertEquals(1, savedPost.getId());
        assertEquals("Test content", savedPost.getContent());
        // Проверяем, что пост добавлен в хранилище
        assertTrue(repository.getById(1).isPresent());
    }

    @Test
    void testSaveUpdateExistingPost() {
        // Сначала создаём пост
        Post post = new Post(0, "Original content");
        Post savedPost = repository.save(post);
        long postId = savedPost.getId();

        // Обновляем пост с новым содержимым
        Post updatedPost = new Post(postId, "Updated content");
        Post result = repository.save(updatedPost);

        // Проверяем, что пост обновлён
        assertEquals(postId, result.getId());
        assertEquals("Updated content", result.getContent());
        assertEquals("Updated content", repository.getById(postId).get().getContent());
    }

    @Test
    void testSaveNonExistentPostThrowsException() {
        // Пытаемся обновить пост с несуществующим id
        Post post = new Post(999, "Non-existent content");

        assertThrows(NotFoundException.class, () -> repository.save(post));
    }

    @Test
    void testRemoveById() {
        // Создаём пост
        Post post = new Post(0, "Content to remove");
        Post savedPost = repository.save(post);
        long postId = savedPost.getId();

        // Удаляем пост
        repository.removeById(postId);

        // Проверяем, что пост удалён
        assertFalse(repository.getById(postId).isPresent());
    }

    @Test
    void testRemoveByIdNonExistentThrowsException() {
        // Пытаемся удалить пост с несуществующим id
        assertThrows(NotFoundException.class, () -> repository.removeById(999));
    }
}