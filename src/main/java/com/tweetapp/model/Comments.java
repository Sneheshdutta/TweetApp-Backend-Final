package com.tweetapp.model;



import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	private String comment;
	private long tweetId;
	private Date date;
	private String username;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Comments comments = (Comments) o;
		return id != null && Objects.equals(id, comments.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
