package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final List<Post> posts = new CopyOnWriteArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public List<Post> all() {
    return new CopyOnWriteArrayList<>(posts);
  }

  public Optional<Post> getById(long id) {
    return posts.stream()
            .filter(p -> p.getId() == id)
            .findFirst();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      // Создание нового поста
      long newId = idCounter.getAndIncrement();
      post.setId(newId);
      posts.add(post);
      return post;
    } else {
      // Обновление существующего поста
      Optional<Post> existingPost = getById(post.getId());
      if (existingPost.isPresent()) {
        posts.removeIf(p -> p.getId() == post.getId());
        posts.add(post);
        return post;
      } else {
        throw new NotFoundException("Post with id " + post.getId() + " not found");
      }
    }
  }

  public void removeById(long id) {
    if (!posts.removeIf(p -> p.getId() == id)) {
      throw new NotFoundException("Post with id " + id + " not found");
    }
  }
}