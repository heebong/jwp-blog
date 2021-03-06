package techcourse.myblog.comment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CommentResponseDto {
    private long id;
    private String contents;
    private User author;
    private Article article;
}
