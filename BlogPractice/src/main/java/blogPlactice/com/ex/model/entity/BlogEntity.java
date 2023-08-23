package blogPlactice.com.ex.model.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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

// 以下が"blogs"テーブルの中身
// @NonNullはnullを許容しない
@Table(name = "blogs")
public class BlogEntity {

	// ブログID（PK）
	@Id
	@Column(name = "blog-id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long blogId;

	// ブログタイトルを表す
	@NonNull
	@Column(name = "blog_title")
	private String blogTitle;

	// 登録日時を表す
	@NonNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "register_date")
	private LocalDate registerDate;

	// 画像名を表す
	@NonNull
	@Column(name = "blog_image")
	private String blogImage;

	// ブログの本文を表す
	@NonNull
	@Column(name = "blog_detail")
	private String blogDetail;

	// ブログのトピックスを表す
	@NonNull
	@Column(name = "topics")
	private String topics;

	// userIdを表す
	@Column(name = "user_id")
	private Long userId;

	// 以下6つのパラメータを取得する
	public BlogEntity(@NonNull String blogTitle, @NonNull LocalDate registerDate, @NonNull String blogImage,
			@NonNull String blogDetail, @NonNull String topics, Long userId) {
		this.blogTitle = blogTitle;
		this.registerDate = registerDate;
		this.blogImage = blogImage;
		this.blogDetail = blogDetail;
		this.topics = topics;
		this.userId = userId;
	}

}
