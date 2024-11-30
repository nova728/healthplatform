package com.health.healthplatform.service;

import com.health.healthplatform.entity.Article;
import com.health.healthplatform.entity.Category;
import com.health.healthplatform.entity.Comment;
import com.health.healthplatform.mapper.ArticleMapper;
import com.health.healthplatform.mapper.CategoryMapper;
import com.health.healthplatform.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Transactional
    public Article createArticle(Article article, Integer userId) {
        System.out.println("Starting createArticle in service");
        try {
            // 检查标签是否为空
            if (article.getTags() == null) {
                System.out.println("Tags is null, initializing empty list");
                article.setTags(new ArrayList<>());
            }

            // 设置基本信息
            article.setUserId(userId);
            LocalDateTime now = LocalDateTime.now();
            article.setCreatedAt(now);
            article.setUpdatedAt(now);

            if (article.getStatus() == 1 && article.getPublishTime() == null) {
                System.out.println("Setting publish time to now");
                article.setPublishTime(now);
            }

            // 打印准备插入的文章数据
            System.out.println("Preparing to insert article: " + article);

            try {
                // 插入文章
                articleMapper.insert(article);
                System.out.println("Article inserted with ID: " + article.getId());
            } catch (Exception e) {
                System.err.println("Error in articleMapper.insert: " + e);
                e.printStackTrace();
                throw e;
            }

            // 处理标签
            System.out.println("Processing tags: " + article.getTags());
            for (String tagName : article.getTags()) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    try {
                        // 获取或创建标签
                        Long tagId = tagMapper.getOrCreateTag(tagName.trim());
                        if (tagId != null) {
                            System.out.println("Inserting tag: " + tagName + " with ID: " + tagId);
                            articleMapper.insertArticleTag(article.getId(), tagId);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing tag: " + tagName + ", Error: " + e);
                        e.printStackTrace();
                    }
                }
            }

            Article result = getArticle(article.getId(), userId);
            System.out.println("Article created successfully: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error in createArticle service: " + e);
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    public Article updateArticle(Article article, Integer userId) {
        // 验证文章所有权
        Article existingArticle = articleMapper.selectById(article.getId());
        if (existingArticle == null) {
            throw new RuntimeException("文章不存在");
        }

        if (!existingArticle.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此文章");
        }

        // 更新文章
        articleMapper.update(article);

        // 更新标签
        if (article.getTags() != null) {
            articleMapper.deleteArticleTags(article.getId());
            if (!article.getTags().isEmpty()) {
                for (String tagName : article.getTags()) {
                    Long tagId = tagMapper.getOrCreateTag(tagName);
                    articleMapper.insertArticleTag(article.getId(), tagId);
                }
            }
        }

        return getArticle(article.getId(), userId);
    }

    public Article getArticle(Long id, Integer userId) {
        if (id == null) {
            throw new RuntimeException("文章ID不能为空");
        }
        Article article = articleMapper.selectById(id);
        System.out.println(article);
        if (article == null) {
            System.out.println(id);
            throw new RuntimeException("文章不存在");
        }

        // 增加浏览量
        articleMapper.incrementViewCount(id);

        // 检查用户是否点赞和收藏
        if (userId != null) {
            article.setIsLiked(articleMapper.checkUserLiked(id, userId));
            article.setIsFavorited(articleMapper.checkUserFavorited(id, userId));
        }

        return article;
    }

    public Map<String, Object> getArticles(Integer categoryId, String search, int page, int size, Integer userId) {
        Map<String, Object> result = new HashMap<>();

        // 计算偏移量
        int offset = (page - 1) * size;

        // 获取文章列表
        List<Article> articles = articleMapper.selectArticles(categoryId, search, offset, size);

        // 检查用户是否点赞和收藏
        if (userId != null) {
            for (Article article : articles) {
                article.setIsLiked(articleMapper.checkUserLiked(article.getId(), userId));
                article.setIsFavorited(articleMapper.checkUserFavorited(article.getId(), userId));
            }
        }

        // 获取总数
        int total = articleMapper.countArticles(categoryId, search);

        result.put("articles", articles);
        result.put("total", total);

        return result;
    }

    @Transactional
    public Article likeArticle(Long articleId, Integer userId) {
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        if (articleMapper.checkUserLiked(articleId, userId)) {
            throw new RuntimeException("已经点赞过了");
        }

        articleMapper.insertLike(articleId, userId);
        articleMapper.increaseLikeCount(articleId);

        // 返回更新后的文章信息
        return getArticle(articleId, userId);
    }

    @Transactional
    public Article unlikeArticle(Long articleId, Integer userId) {
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        if (!articleMapper.checkUserLiked(articleId, userId)) {
            throw new RuntimeException("尚未点赞");
        }

        articleMapper.deleteLike(articleId, userId);
        articleMapper.decreaseLikeCount(articleId);

        // 返回更新后的文章信息
        return getArticle(articleId, userId);
    }

    @Transactional
    public void favoriteArticle(Long articleId, Integer userId) {
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        if (articleMapper.checkUserFavorited(articleId, userId)) {
            throw new RuntimeException("已经收藏过了");
        }

        articleMapper.insertFavorite(articleId, userId);
    }

    @Transactional
    public void unfavoriteArticle(Long articleId, Integer userId) {
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        if (!articleMapper.checkUserFavorited(articleId, userId)) {
            throw new RuntimeException("尚未收藏");
        }

        articleMapper.deleteFavorite(articleId, userId);
    }

    // 增加浏览量
    public void incrementViewCount(Long articleId) {
        articleMapper.incrementViewCount(articleId);
    }

    @Transactional
    public Comment createComment(Long articleId, Integer userId, String content, Long parentId) {
        System.out.println("Creating comment for article ID: " + articleId + ", User ID: " + userId);
        System.out.println("Parent ID: " + parentId); // 添加日志

        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("评论内容不能为空");
        }

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId); // 设置父评论ID
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setReplyCount(0);

        try {
            articleMapper.insertComment(comment);
            articleMapper.increaseCommentCount(articleId);

            if (parentId != null) {
                articleMapper.increaseReplyCount(parentId);
            }

            Comment createdComment = articleMapper.selectCommentById(comment.getId());
            System.out.println("Comment created successfully: " + createdComment); // 添加日志
            return createdComment;
        } catch (Exception e) {
            System.err.println("Error creating comment: " + e.getMessage());
            throw e;
        }
    }


    public Map<String, Object> getComments(Long articleId, int page, int size) {
        Map<String, Object> result = new HashMap<>();

        // 获取所有评论
        List<Comment> allComments = articleMapper.selectAllComments(articleId);

        // 构建评论树结构
        Map<Long, Comment> commentMap = new HashMap<>();
        List<Comment> rootComments = new ArrayList<>();

        for (Comment comment : allComments) {
            commentMap.put(comment.getId(), comment);
            if (comment.getReplies() == null) {
                comment.setReplies(new ArrayList<>());
            }
        }

        for (Comment comment : allComments) {
            if (comment.getParentId() == null) {
                rootComments.add(comment);
            } else {
                Comment parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null && parentComment.getReplies() != null) {
                    parentComment.getReplies().add(comment);
                }
            }
        }

        // 分页处理
        int start = (page - 1) * size;
        int end = Math.min(start + size, rootComments.size());
        List<Comment> pagedComments = start < rootComments.size() ?
                rootComments.subList(start, end) :
                new ArrayList<>();

        result.put("comments", pagedComments);
        result.put("total", rootComments.size());
        result.put("page", page);
        result.put("size", size);
        result.put("pages", (rootComments.size() + size - 1) / size);

        return result;
    }

}