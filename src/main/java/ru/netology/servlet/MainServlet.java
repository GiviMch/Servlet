package ru.netology.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.AppConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String METHOD_DELETE = "DELETE";
  private static final String PATH_API_POSTS = "/api/posts";
  private static final String PATH_API_POSTS_ID = "/api/posts/\\d+";
  private static final int SC_NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;
  private static final int SC_INTERNAL_SERVER_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

  private PostController controller;

  @Override
  public void init() {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals(METHOD_GET) && path.equals(PATH_API_POSTS)) {
        controller.all(resp);
        return;
      }
      if (method.equals(METHOD_GET) && path.matches(PATH_API_POSTS_ID)) {
        final var id = extractId(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(METHOD_POST) && path.equals(PATH_API_POSTS)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(METHOD_DELETE) && path.matches(PATH_API_POSTS_ID)) {
        final var id = extractId(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long extractId(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/")));
  }
}