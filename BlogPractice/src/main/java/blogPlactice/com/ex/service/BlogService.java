package blogPlactice.com.ex.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blogPlactice.com.ex.model.dao.BlogDao;
import blogPlactice.com.ex.model.entity.BlogEntity;

@Service
public class BlogService {

	// UserDaoインターフェースを使用可能とする
	@Autowired
	private BlogDao blogDao;

	public List<BlogEntity> findAllBlogPost(Long userId) {

		if (userId == null) {
			return null;
		} else {
			return blogDao.findByUserId(userId);
		}
	}

	// 以下はブログ記事を作成するためのメソッド
	public boolean createBlogPost(String blogTitle, LocalDate registerDate, String fileName, String blogDetail,
			String topics, Long userId) {
		BlogEntity blogList = blogDao.findByBlogTitleAndRegisterDate(blogTitle, registerDate);

		if (blogList == null) {
			blogDao.save(new BlogEntity(blogTitle, registerDate, fileName, blogDetail, topics, userId));

			return true;
		} else {
			return false;
		}
	}

	// blogIdに基づいて、blogDaoから該当するBlogEntityを取得して返す。
	public BlogEntity getBlogPost(Long blogId) {
		if (blogId == null) {
			return null;
		} else {
			return blogDao.findByBlogId(blogId);
		}
	}

	// ブログ記事のタイトル、登録日、詳細などの情報を受け取り、指定されたblogIdに対応するブログ記事を更新
	public boolean editBlogPost(String blogTitle, LocalDate registerDate, String blogDetail, String fileName,
			String topics, Long userId, Long blogId) {
		BlogEntity blogList = blogDao.findByBlogId(blogId);
		if (userId == null) {
			return false;
		} else {
			blogList.setBlogId(blogId);
			blogList.setBlogTitle(blogTitle);
			blogList.setBlogImage(fileName);
			blogList.setRegisterDate(registerDate);
			blogList.setTopics(topics);
			blogList.setBlogDetail(blogDetail);
			blogList.setUserId(userId);
			blogDao.save(blogList);
			return true;
		}
	}

	// メソッド名と引数を定義。ブログID、ファイル名、ユーザーIDを引数として受け取る
	public boolean editBlogImage(Long blogId, String fileName, Long userId) {
		BlogEntity blogList = blogDao.findByBlogId(blogId);

		if (fileName == null || blogList.getBlogImage().equals(fileName)) {
			return false;
		} else {
			blogList.setBlogId(blogId);
			blogList.setBlogImage(fileName);
			blogList.setUserId(userId);
			blogDao.save(blogList);
			return true;
		}

	}

	// 削除処理を行うためのメソッド
	public boolean deleteBlog(Long blogId) {
		if (blogId == null) {
			return false;
		} else {
			blogDao.deleteByBlogId(blogId);
			return true;
		}
	}

}
