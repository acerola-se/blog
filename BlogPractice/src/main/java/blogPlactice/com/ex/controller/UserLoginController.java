package blogPlactice.com.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blogPlactice.com.ex.model.entity.UserEntity;
import blogPlactice.com.ex.service.UserService;
import jakarta.servlet.http.HttpSession;

// /userから始まるように記載
@RequestMapping("/user")

@Controller
public class UserLoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;

	// "/user/login"でログイン可能
	// getUserLoginPage()メソッドは、ログイン画面"login.html"を表示

	@GetMapping("/login")
	public String getUserLoginPage() {

		return "login.html";
	}

	// public String login(@RequestParam String email,@RequestParam String password)
	// このメソッドがリクエストパラメータを受け取る
	// emailとpasswordがリクエストパラメータとして渡される。

	@PostMapping("/login/process")
	public String login(@RequestParam String email, @RequestParam String password) {

		UserEntity userList = userService.loginAccount(email, password);
		if (userList == null) {
			return "redirect:/user/login";
		} else {
			session.setAttribute("user", userList);
			return "redirect:/user/blog/list";
		}

		// 上記のソースがログイン機能となる

	}

}
