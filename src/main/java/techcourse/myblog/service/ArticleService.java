package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.List;

@Slf4j
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article findArticleById(long id) {
        return articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }

    public List<Comment> findAllCommentsByArticleId(long id) {
        return commentRepository.findAllByArticle_Id(id);
    }

    @Transactional
    public Article find(long articleId, User user) {
        Article article = findArticleById(articleId);
        article.checkMatchAuthor(user);
        return article;
    }

    @Transactional
    public Article update(ArticleDto articleDto, long articleId, User user) {
        Article preArticle = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        return preArticle.update(articleDto, user);
    }

    @Transactional
    public void delete(long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        if (article.isMatchAuthor(user)) {
            deleteById(articleId);
        }
    }

    private void deleteById(long articleId) {
        articleRepository.deleteById(articleId);
    }

    private void delete(Article article) {
        articleRepository.delete(article);
    }
}
