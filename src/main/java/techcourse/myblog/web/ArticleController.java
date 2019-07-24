package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticlePage(Article article) {
        log.debug(">>> save article : {}", article);

        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/")
    public String showArticlesPage(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticleByIdPage(Article article) {
        log.debug(">>> Article : {}", article);
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId) {
        log.debug(">>> article Id : {}", articleId);
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
