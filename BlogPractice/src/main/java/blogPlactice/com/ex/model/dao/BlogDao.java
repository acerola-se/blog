package blogPlactice.com.ex.model.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import blogPlactice.com.ex.model.entity.BlogEntity;
import jakarta.transaction.Transactional;

public interface BlogDao extends JpaRepository<BlogEntity, Long> {

	// findByUserIdというメソッドを定義
	List<BlogEntity> findByUserId(Long userId);

	// BlogEntityのオブジェクトを引数として受け取り、そのオブジェクトをデータベースに保存
	BlogEntity save(BlogEntity blogEntity);

	// blogTitleとregisterDateを検索条件として使用して、BlogEntityオブジェクトを取得
	BlogEntity findByBlogTitleAndRegisterDate(String blogTitle, LocalDate registerDate);

	// blogIdに一致する複数のBlogEntityを取得
	BlogEntity findByBlogId(Long blogId);

	// BlogEntityのIDに基づいて、該当するBlogEntityを削除するために使用
	@Transactional
	List<BlogEntity> deleteByBlogId(Long blogId);

}
