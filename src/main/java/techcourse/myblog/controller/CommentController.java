package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.domain.*;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.controller.CommentController.COMMENT_URL;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(COMMENT_URL)
public class CommentController {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    public static final String COMMENT_URL = "/comment";

    @PostMapping
    public String save(HttpSession session, CommentDto commentDto) {
        log.debug(">>> post commentDto : {}", commentDto);
        User user = (User) session.getAttribute("user");
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(user, article);
        log.debug(">>> post comment : {}", comment);
        log.debug(">>> post user : {}", user);
        commentRepository.save(comment);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @DeleteMapping
    public String delete(HttpSession session, CommentDto commentDto) {
        log.debug(">>> delete commentDto : {}", commentDto);
        User user = (User) session.getAttribute("user");
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(user, article);
        log.debug(">>> delete comment : {}", comment);
        commentRepository.delete(comment);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @GetMapping("{id}/edit")
    public String showEditPage(@PathVariable long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PutMapping
    public String update(CommentDto commentDto, HttpSession httpSession) {
        log.debug(">>> update commentDto : {}", commentDto);
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        User user = (User) httpSession.getAttribute("user");
        Comment comment = commentDto.toComment(user, article);
        commentRepository.save(comment);
        return "redirect:/articles/" + article.getId();
    }

}
