package blogPlactice.com.ex.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blogPlactice.com.ex.model.dao.UserDao;
import blogPlactice.com.ex.model.entity.UserEntity;

@Service
public class UserService {

	// UserDaoインターフェースを使えるようにする
	@Autowired
	private UserDao userDao;

	// 以下はユーザーアカウントを作成するためのメソッド
	public boolean createAccount(String userName, String email, String password) {

		// 現在の日時を取得して、registerDateに保存
		LocalDateTime registerDate = LocalDateTime.now();

		UserEntity userEntity = userDao.findByEmail(email);

		// UserEntityが見つからなかった場合
		if (userEntity == null) {

			// 新しいUserEntityオブジェクトを作成し、引数として受け取ったuserName、email、password、registerDateを設定
			// UserDaoのsaveメソッドを呼び出して、 新しいUserEntityオブジェクトを保存。そして、trueを返す。
			userDao.save(new UserEntity(userName, email, password, registerDate));
			return true;
		} else {
			// UserEntityが見つかった場合、Falseを返す。
			return false;
		}
	}

	// ユーザーアカウントの機能を実装するためのメソッド
	// ユーザーアカウントのログインが成功したかの判断をするために使用されるメソッド
	public UserEntity loginAccount(String email, String password) {

		UserEntity userEntity = userDao.findByEmailAndPassword(email, password);

		if (userEntity == null) {
			return null;
		} else {

			return userEntity;
		}

	}

}
