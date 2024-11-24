package com.health.healthplatform.controller;

import com.health.healthplatform.entity.Article;
import com.health.healthplatform.service.ArticleService;
import com.health.healthplatform.service.FileService;
import com.health.healthplatform.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8088"}, allowCredentials = "true")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Autowired
    private FileService fileService;

    // 创建文章
    @PostMapping("/{userId}")
    public Result createArticle(@PathVariable Integer userId, @RequestBody Article article) {
        try {
            // 打印接收到的数据
            System.out.println("Received userId: " + userId);
            System.out.println("Received article data: " + article);

            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            // 验证字段
            if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
                return Result.failure(400, "文章标题不能为空");
            }

            System.out.println("Article title validation passed");

            // 打印关键字段
            System.out.println("Title: " + article.getTitle());
            System.out.println("Content: " + article.getContent());
            System.out.println("CategoryId: " + article.getCategoryId());
            System.out.println("Tags: " + article.getTags());
            System.out.println("PublishTime: " + article.getPublishTime());

            // 设置默认值
            article.setUserId(userId);
            article.setStatus(article.getStatus() == null ? 0 : article.getStatus());
            article.setVisibility(article.getVisibility() == null ? "public" : article.getVisibility());
            article.setAllowComment(article.getAllowComment() == null ? true : article.getAllowComment());
            article.setViewCount(0);
            article.setLikeCount(0);
            article.setCommentCount(0);

            System.out.println("Default values set");

            try {
                Article savedArticle = articleService.createArticle(article, userId);
                System.out.println("Article saved successfully: " + savedArticle);
                return Result.success(savedArticle);
            } catch (Exception e) {
                System.err.println("Error in articleService.createArticle: " + e);
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Error in createArticle endpoint: " + e);
            e.printStackTrace();
            return Result.failure(500, "创建文章失败: " + e.getMessage());
        }
    }

    // 更新文章
    @PutMapping("/{userId}/{id}")
    public Result updateArticle(@PathVariable Integer userId, @PathVariable Long id, @RequestBody Article article) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            article.setId(id);
            Article updatedArticle = articleService.updateArticle(article, userId);
            return Result.success(updatedArticle);
        } catch (Exception e) {
            return Result.failure(500, "更新文章失败: " + e.getMessage());
        }
    }

    // 获取文章详情
    @GetMapping("/{userId}/{id}")
    public Result getArticle(@PathVariable Integer userId, @PathVariable Long id) {
        try {
            Article article = articleService.getArticle(id, userId);
            return Result.success(article);
        } catch (Exception e) {
            return Result.failure(500, "获取文章失败: " + e.getMessage());
        }
    }

    // 获取文章列表
    @GetMapping("/{userId}")
    public Result getArticles(
            @PathVariable Integer userId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = articleService.getArticles(categoryId, search, page, size, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(500, "获取文章列表失败: " + e.getMessage());
        }
    }

    // 上传图片
    @PostMapping("/{userId}/upload-image")
    public Result uploadImage(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            if (file.isEmpty()) {
                return Result.failure(400, "文件为空");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.failure(400, "只支持上传图片文件");
            }

            // 限制文件大小（例如：2MB）
            long maxSize = 2 * 1024 * 1024; // 2MB in bytes
            if (file.getSize() > maxSize) {
                return Result.failure(400, "图片大小不能超过2MB");
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString() + originalFilename;

            // 直接使用 FileService 的 uploadFile 方法
            String url = fileService.uploadFile(filename, file.getInputStream());

            // 返回正确格式的响应数据
            return Result.success(Map.of("url", url));

        } catch (IOException e) {
            return Result.failure(500, "图片上传失败: " + e.getMessage());
        }
    }

    // 点赞文章
    @PostMapping("/{userId}/{id}/like")
    public Result likeArticle(@PathVariable Integer userId, @PathVariable Long id) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            articleService.likeArticle(id, userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(500, "点赞失败: " + e.getMessage());
        }
    }

    // 取消点赞
    @DeleteMapping("/{userId}/{id}/like")
    public Result unlikeArticle(@PathVariable Integer userId, @PathVariable Long id) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            articleService.unlikeArticle(id, userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(500, "取消点赞失败: " + e.getMessage());
        }
    }

    // 收藏文章
    @PostMapping("/{userId}/{id}/favorite")
    public Result favoriteArticle(@PathVariable Integer userId,@PathVariable Long id, HttpSession session) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            articleService.favoriteArticle(id, userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(500, "收藏失败: " + e.getMessage());
        }
    }

    // 取消收藏
    @DeleteMapping("/{userId}/{id}/favorite")
    public Result unfavoriteArticle(@PathVariable Integer userId,@PathVariable Long id, HttpSession session) {
        try {
            if (userId == null) {
                return Result.failure(401, "请先登录");
            }

            articleService.unfavoriteArticle(id, userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(500, "取消收藏失败: " + e.getMessage());
        }
    }
}