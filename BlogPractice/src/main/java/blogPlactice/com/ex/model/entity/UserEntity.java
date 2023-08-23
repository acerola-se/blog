package blogPlactice.com.ex.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity

// 以下"users"というテーブルの中身
@Table(name = "users")
public class UserEntity {

	// userId(PK)を表す
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	// NonNullはnullであることを許容しない
	// ユーザー名を表す
	@NonNull
	@Column(name = "user_name")
	private String userName;

	// emailを表す
	@NonNull
	@Column(name = "email")
	private String email;

	// パスワードを表す
	@NonNull
	@Column(name = "password")
	private String password;

	// 登録日時を表す
	@NonNull
	@Column(name = "register_date")
	private LocalDateTime registerDate;

}
