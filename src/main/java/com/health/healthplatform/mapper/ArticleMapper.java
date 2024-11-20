package com.health.healthplatform.mapper;

import com.health.healthplatform.entity.Article;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("INSERT INTO articles(title, content, html_content, cover_image, category_id, user_id, " +
            "status, visibility, allow_comment, publish_time) " +
            "VALUES(#{title}, #{content}, #{htmlContent}, #{coverImage}, #{categoryId}, #{userId}, " +
            "#{status}, #{visibility}, #{allowComment}, #{publishTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Article article);

    @Update("UPDATE articles SET title=#{title}, content=#{content}, html_content=#{htmlContent}, " +
            "cover_image=#{coverImage}, category_id=#{categoryId}, status=#{status}, " +
            "visibility=#{visibility}, allow_comment=#{allowComment}, publish_time=#{publishTime} " +
            "WHERE id=#{id}")
    void update(Article article);

    @Select("SELECT * FROM articles WHERE id=#{id}")
    @Results({
            @Result(property = "htmlContent", column = "html_content"),
            @Result(property = "coverImage", column = "cover_image"),
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "allowComment", column = "allow_comment"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "publishTime", column = "publish_time"),
            @Result(property = "tags", column = "id",
                    javaType = List.class,
                    many = @Many(select = "selectArticleTags")),
            @Result(property = "author", column = "user_id",
                    one = @One(select = "com.health.healthplatform.mapper.UserMapper.selectById")),
            @Result(property = "category", column = "category_id",
                    one = @One(select = "com.health.healthplatform.mapper.CategoryMapper.selectById"))
    })
    Article selectById(Long id);

    @Select("SELECT a.*, u.username as author_name, u.avatar as author_avatar " +
            "FROM articles a " +
            "LEFT JOIN user u ON a.user_id = u.id " +
            "WHERE (#{categoryId} IS NULL OR a.category_id = #{categoryId}) " +
            "AND a.status = 1 " +
            "ORDER BY a.created_at DESC LIMIT #{offset}, #{size}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tags", column = "id",
                    many = @Many(select = "selectArticleTags")),
            @Result(property = "author", column = "user_id",
                    one = @One(select = "com.health.healthplatform.mapper.UserMapper.selectById"))
    })
    List<Article> selectArticles(@Param("categoryId") Integer categoryId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    @Select("SELECT name FROM tags t " +
            "INNER JOIN article_tags at ON t.id = at.tag_id " +
            "WHERE at.article_id = #{articleId}")
    List<String> selectArticleTags(Long articleId);

    @Insert("INSERT INTO article_tags(article_id, tag_id) VALUES(#{articleId}, #{tagId})")
    void insertArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);

    @Delete("DELETE FROM article_tags WHERE article_id = #{articleId}")
    void deleteArticleTags(Long articleId);

    @Select("SELECT COUNT(*) FROM articles WHERE category_id = #{categoryId} AND status = 1")
    int countArticles(@Param("categoryId") Integer categoryId);

    @Update("UPDATE articles SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);

    @Select("SELECT COUNT(*) > 0 FROM likes WHERE article_id = #{articleId} AND user_id = #{userId}")
    boolean checkUserLiked(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM favorites WHERE article_id = #{articleId} AND user_id = #{userId}")
    boolean checkUserFavorited(@Param("articleId") Long articleId, @Param("userId") Integer userId);
}