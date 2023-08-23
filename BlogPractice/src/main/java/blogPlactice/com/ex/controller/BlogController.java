package blogPlactice.com.ex.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import blogPlactice.com.ex.model.entity.BlogEntity;
import blogPlactice.com.ex.model.entity.UserEntity;
import blogPlactice.com.ex.service.BlogService;
import jakarta.servlet.http.HttpSession;

// "/user/blog"で始まるようにする
@RequestMapping("/user/blog")

@Controller
public class BlogController {

	// BlogServiceクラスのメソッドを呼び出し、指定した処理を実行できるようにする
	@Autowired
	private BlogService blogService;

	// HTTP GETリクエストを受け取るメソッド
	@Autowired
	private HttpSession session;

	@GetMapping("/list")

	public String getBlogListPage(Model model) {

		// セッションから現在のユーザー情報を取得するため、sessionオブジェクトを使用
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();

		// blogServiceのfindAllBlogPostメソッドを呼び出し、現在のユーザーに関連するブログ投稿を取得
		// 戻り値はBlogEntityのリストで、このリストをmodelに追加
		List<BlogEntity> blogList = blogService.findAllBlogPost(userId);

		// ModelクラスにuserNameとblogListを追加して、blog-main.htmlを返す。
		// blogListを表示するためのHTML
		model.addAttribute("userName", userName);
		model.addAttribute("blogList", blogList);
		return "blog-main.html";

	}

	@GetMapping("/register")
	public String getBlogRegisterPage(Model model) {

		// セッションから現在のユーザー情報を取得するため、sessionオブジェクトを使用
		UserEntity userList = (UserEntity) session.getAttribute("user");

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		model.addAttribute("registerMessage", "新規記事追加");
		return "blog-register.html";
	}

	@PostMapping("/register/process")

	// アップロードされたファイルの内容を扱うためのメソッド
	public String blogRegister(@RequestParam String blogTitle, @RequestParam LocalDate registerDate,
			@RequestParam String topics, @RequestParam MultipartFile blogImage, @RequestParam String blogDetail,
			Model model) {

		// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();

		// 現在の日時情報を元に、ファイル名を作成
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())
				+ blogImage.getOriginalFilename();
		try {
			// ファイルを実際にサーバー上に保存するための処理を行う
			Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// blogService.createBlogPost()メソッドが呼び出し、入力されたデータをデータベースに保存
		// blogDao.save()メソッドを使用して、データベースに保存
		if (blogService.createBlogPost(blogTitle, registerDate, fileName, blogDetail, topics, userId)) {
			return "redirect:/user/blog/list";
		} else {
			model.addAttribute("registerMessage", "既に登録済みです");
			return "blog-register.html";
		}

	}

	@GetMapping("/edit/{blogId}")

	// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
	public String getBlogEditPage(@PathVariable Long blogId, Model model) {
		UserEntity userList = (UserEntity) session.getAttribute("user");

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);

		// blogService.getBlogPost(blogId)を使用して、
		// 指定されたblogIdに対応するブログを取得し、blogListに代入。
		BlogEntity blogList = blogService.getBlogPost(blogId);
		if (blogList == null) {
			return "redirect:/user/blog/list";
		} else {
			// blogListと"記事編集"というメッセージをModelオブジェクトに追加し、"blog-edit.html"に遷移
			model.addAttribute("blogList", blogList);
			model.addAttribute("editMessage", "記事編集");
			return "blog-update.html";

		}
	}

	@PostMapping("/update")
	// ブログの投稿時に、ブログのタイトル、投稿日、カテゴリ、、詳細をパラメータとして受け取る
	public String blogUpdate(@RequestParam String blogTitle, @RequestParam LocalDate registerDate,
			@RequestParam String topics, @RequestParam String blogDetail, @RequestParam MultipartFile blogImage,
			@RequestParam Long blogId, Model model) {

		// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();
		// 現在の日時情報を元に、ファイル名を作成。SimpleDateFormatクラスを使用して、日時のフォーマットを指定
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
				+ blogImage.getOriginalFilename();
		try {

			// ファイルを実際にサーバー上に保存するための処理
			Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// blogService.editBlogPost()を使用して、指定されたblogIdに対応するブログを更新
		if (blogService.editBlogPost(blogTitle, registerDate, blogDetail, fileName, topics, userId, blogId)) {
			return "redirect:/user/blog/delete/list";
		} else {
			model.addAttribute("registerMessage", "更新に失敗しました");
			return "blog-update.html";
		}

	}

	@GetMapping("/image/edit/{blogId}")
	public String getBlogEditImagePage(@PathVariable Long blogId, Model model) {

		// セッションから現在のユーザー情報を取得するため、sessionオブジェクトを使用。
		UserEntity userList = (UserEntity) session.getAttribute("user");

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);
		// blogService.getBlogPost(blogId)を使用して、
		// 指定されたblogIdに対応するブログを取得し、blogListに代入
		BlogEntity blogList = blogService.getBlogPost(blogId);
		// blogListがnullであれば、"/user/blog/list"にリダイレクト
		if (blogList == null) {
			return "redirect:/user/blog/list";
		} else {
			// blogListと"画像編集"というメッセージをModelオブジェクトに追加し、"blog-edit.html"に遷移
			model.addAttribute("blogList", blogList);
			model.addAttribute("editImageMessage", "画像編集");
			return "blog-update.html";
		}

	}

	@PostMapping("/image/update")

	// MultipartFileは、アップロードされたファイルの内容を扱うためのメソッド
	public String blogImgUpdate(@RequestParam MultipartFile blogImage, @RequestParam Long blogId, Model model) {

		// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();

		// 現在の日時情報を元に、ファイル名を作成。SimpleDateFormatクラスを使用して、日時のフォーマットを指定
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
				+ blogImage.getOriginalFilename();
		try {

			// ファイルを実際にサーバー上に保存するための処理
			Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-img/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// blogService.editBlogImage()メソッドが呼び出され、入力されたデータをデータベースに保存
		if (blogService.editBlogImage(blogId, fileName, userId)) {
			return "blog-edit.html";
		} else {
			BlogEntity blogList = blogService.getBlogPost(blogId);
			model.addAttribute("blogList", blogList);
			model.addAttribute("editImageMessage", "更新失敗です");
			return "blog-update.html";
		}

	}

	@GetMapping("/delete/list")

	public String getBlogDeleteListPage(Model model) {

		// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();

		// 戻り値はBlogEntityのリストであり、このリストをmodelに追加
		List<BlogEntity> blogList = blogService.findAllBlogPost(userId);

		model.addAttribute("userName", userName);
		model.addAttribute("blogList", blogList);
		model.addAttribute("deleteMessage", "削除一覧");
		return "blog-edit.html";
	}

	@GetMapping("/delete/detail/{blogId}")
	public String getBlogDeleteDetailPage(@PathVariable Long blogId, Model model) {

		// UserEntityのインスタンスが取得できた場合、そのuserIdを取得
		UserEntity userList = (UserEntity) session.getAttribute("user");
		Long userId = userList.getUserId();

		// userListから現在ログインしている人のユーザー名を取得
		String userName = userList.getUserName();
		model.addAttribute("userName", userName);

		// blogService.getBlogPost(blogId)を使用し、
		// 指定されたblogIdに対応するブログを取得し、blogListに代入
		BlogEntity blogList = blogService.getBlogPost(blogId);

		// blogListがnullであれば、"/user/blog/list"にリダイレクト
		if (blogList == null) {
			return "redirect:/user/blog/list";
		} else {
			// blogListと"削除記事詳細というメッセージをModelオブジェクトに追加し、
			// "blog-edit.html"に遷移
			model.addAttribute("blogList", blogList);
			model.addAttribute("DeleteDetailMessage", "削除記事詳細");
			return "blog-edit.html";
		}

	}

	// blogを削除するためのコントローラー
	@PostMapping("/delete")
	public String blogDelete(@RequestParam Long blogId, Model model) {
		if (blogService.deleteBlog(blogId)) {
			return "blog-edit.html";
		} else {
			model.addAttribute("DeleteDetailMessage", "記事削除に失敗しました");
			return "blog-edit.html";
		}

	}

	// ログアウトするためのコントローラー
	@GetMapping("/logout")
	public String Logout() {

		session.invalidate();
		return "redirect:/user/login";
	}

}
