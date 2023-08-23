package blogPlactice.com.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blogPlactice.com.ex.service.UserService;

///userから始まるように記載
@RequestMapping("/user")

@Controller
public class UserRegisterController {

	@Autowired
	private UserService userService;

	// "/register"と指定しているため、"/user/register"でアクセス可能
	// getUserRegisterPage()メソッドは、"register.html"を返すことで、新規登録画面を表示
	@GetMapping("/register")
	public String getUserRegisterPage() {
		return "register.html";
	}

	// register()メソッドはユーザー情報を取り出し、
	// UserServiceのcreateAccount()メソッドを呼び出して、新規登録処理を行う。処理が成功した場合、"/user/login"にリダイレクト。
	@PostMapping("/register/process")
	public String register(@RequestParam String userName, @RequestParam String email, @RequestParam String password) {
		userService.createAccount(userName, email, password);
		return "redirect:/user/login";
	}

	// 上記が登録画面のソースとなる。

}
