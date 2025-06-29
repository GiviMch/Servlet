package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.Constants;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
  private static final Gson gson = new Gson(); // Синглтон для Gson
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(Constants.APPLICATION_JSON);
    writeResponse(response, service.all());
  }

  public void getById(long id, HttpServletResponse response) {
    // TODO: deserialize request & serialize response
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(Constants.APPLICATION_JSON);
    final var post = gson.fromJson(body, Post.class);
    writeResponse(response, service.save(post));
  }

  public void removeById(long id, HttpServletResponse response) {
    // TODO: deserialize request & serialize response
  }

  private void writeResponse(HttpServletResponse response, Object data) throws IOException {
    response.getWriter().print(gson.toJson(data));
  }
}