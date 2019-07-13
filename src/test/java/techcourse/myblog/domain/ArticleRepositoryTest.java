package techcourse.myblog.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.NotFoundArticleException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private static Article articleFirst = new Article(1, "타이틀1", "유알엘1", "컨텐트1");
    private static Article articleSecond = new Article(2, "타이틀2", "유알엘2", "컨텐트2");
    private static Article articleThird = new Article(3, "타이틀3", "유알엘3", "컨텐트4");

    @BeforeEach
    void setUp() {
        articleRepository = new ArticleRepository();

        articleRepository.save(articleFirst);
        articleRepository.save(articleSecond);
        articleRepository.save(articleThird);
    }

    @Test
    void 첫_번째_게시물_조회_테스트() {
        assertThat(articleRepository.findById(1)).isEqualTo(articleFirst);
    }

    @Test
    void 마지막_게시물_조회_테스트() {
        assertThat(articleRepository.findById(3)).isEqualTo(articleThird);
    }

    @Test
    void 존재하지_않는_게시물_조회시_예외처리() {
        assertThrows(NotFoundArticleException.class, () -> articleRepository.findById(4));
    }

    @Test
    void 전체_조회_테스트() {
        assertThat(articleRepository.findAll()).isEqualTo(Arrays.asList(articleFirst, articleSecond, articleThird));
    }

    @Test
    void 게시물_수정_테스트() {
        Article updatedArticle = new Article(1, "제목", "이미지경로", "내용");
        long updatedArticleId = articleRepository.update(updatedArticle);

        assertThat(articleRepository.findById(updatedArticleId)).isEqualTo(updatedArticle);
    }

    @Test
    void 게시물_삭제_테스트() {
        articleRepository.delete(1);
        assertThrows(NotFoundArticleException.class, () -> articleRepository.findById(1));
    }

    @AfterEach
    void tearDown() {
        articleRepository = null;
    }
}
