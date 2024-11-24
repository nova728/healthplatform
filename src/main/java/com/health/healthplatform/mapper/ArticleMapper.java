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
    })
    Article selectById(Long id);

    @Select("SELECT a.*, u.username as author_name, u.avatar as author_avatar " +
            "FROM articles a " +
            "LEFT JOIN user u ON a.user_id = u.id " +
            "WHERE a.status = 1 " +
            "AND IF(#{categoryId} IS NOT NULL, a.category_id = #{categoryId}, 1=1) " +
            "AND IF(#{search} IS NOT NULL AND #{search} != '', " +
            "(a.title LIKE CONCAT('%', #{search}, '%') OR a.content LIKE CONCAT('%', #{search}, '%')), 1=1) " +
            "ORDER BY a.created_at DESC " +
            "LIMIT #{offset}, #{size}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "tags", column = "id",
                    many = @Many(select = "selectArticleTags")),
            @Result(property = "author", column = "user_id",
                    one = @One(select = "com.health.healthplatform.mapper.UserMapper.selectById"))
    })
    List<Article> selectArticles(
            @Param("categoryId") Integer categoryId,
            @Param("search") String search,
            @Param("offset") int offset,
            @Param("size") int size
    );

    @Select("SELECT COUNT(*) FROM articles " +
            "WHERE status = 1 " +
            "AND IF(#{categoryId} IS NOT NULL, category_id = #{categoryId}, 1=1) " +
            "AND IF(#{search} IS NOT NULL AND #{search} != '', " +
            "(title LIKE CONCAT('%', #{search}, '%') OR content LIKE CONCAT('%', #{search}, '%')), 1=1)")
    int countArticles(@Param("categoryId") Integer categoryId, @Param("search") String search);


    @Select("SELECT name FROM tags t " +
            "INNER JOIN article_tags at ON t.id = at.tag_id " +
            "WHERE at.article_id = #{articleId}")
    List<String> selectArticleTags(Long articleId);

    @Insert("INSERT INTO article_tags(article_id, tag_id) "+
            "SELECT #{articleId}, #{tagId} FROM DUAL "+
            "WHERE NOT EXISTS ("+
            "    SELECT 1 FROM article_tags "+
            "    WHERE article_id = #{articleId} AND tag_id = #{tagId}"+
            ")")
    void insertArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);

    @Delete("DELETE FROM article_tags WHERE article_id = #{articleId}")
    void deleteArticleTags(Long articleId);

    @Update("UPDATE articles SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);

    @Select("SELECT COUNT(*) > 0 FROM likes WHERE article_id = #{articleId} AND user_id = #{userId}")
    boolean checkUserLiked(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM favorites WHERE article_id = #{articleId} AND user_id = #{userId}")
    boolean checkUserFavorited(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Insert("INSERT INTO likes(article_id, user_id) VALUES(#{articleId}, #{userId})")
    void insertLike(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Delete("DELETE FROM likes WHERE article_id = #{articleId} AND user_id = #{userId}")
    void deleteLike(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Update("UPDATE articles SET like_count = like_count + 1 WHERE id = #{id}")
    void increaseLikeCount(Long id);

    @Update("UPDATE articles SET like_count = like_count - 1 WHERE id = #{id}")
    void decreaseLikeCount(Long id);

    @Insert("INSERT INTO favorites(article_id, user_id) VALUES(#{articleId}, #{userId})")
    void insertFavorite(@Param("articleId") Long articleId, @Param("userId") Integer userId);

    @Delete("DELETE FROM favorites WHERE article_id = #{articleId} AND user_id = #{userId}")
    void deleteFavorite(@Param("articleId") Long articleId, @Param("userId") Integer userId);
}