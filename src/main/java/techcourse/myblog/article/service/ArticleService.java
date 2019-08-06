package techcourse.myblog.article.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.dto.ArticleCreateDto;
import techcourse.myblog.article.dto.ArticleResponseDto;
import techcourse.myblog.article.dto.ArticleUpdateDto;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<ArticleResponseDto> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    public ArticleResponseDto save(ArticleCreateDto articleDto, long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundUserException(authorId));
        Article newArticle = articleDto.toArticle(author);
        return modelMapper.map(articleRepository.save(newArticle), ArticleResponseDto.class);
    }

    public ArticleResponseDto findById(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        return modelMapper.map(article, ArticleResponseDto.class);
    }

    public ArticleResponseDto update(long articleId, ArticleUpdateDto articleDto, long authorId) {
        Article article = checkAuthority(articleId, authorId);
        return modelMapper.map(article.update(articleDto), ArticleResponseDto.class);
    }

    public void deleteById(long articleId, long authorId) {
        Article article = checkAuthority(articleId, authorId);
        articleRepository.delete(article);
    }

    private Article checkAuthority(long articleId, long authorId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        if (article.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        return article;
    }
}
