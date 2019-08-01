package techcourse.myblog.article.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<ArticleResponseDto> findAll() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    public Article save(ArticleCreateDto articleDto, long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundUserException(authorId));
        Article newArticle = articleDto.toArticle(author);
        return articleRepository.save(newArticle);
    }

    public ArticleResponseDto findById(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        return modelMapper.map(article, ArticleResponseDto.class);
    }

    public long update(long articleId, ArticleUpdateDto articleDto, long authorId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        if (article.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        article.update(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
        return article.getId();
    }

    public void deleteById(long articleId, long authorId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        if (article.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        articleRepository.deleteById(articleId);
    }
}
