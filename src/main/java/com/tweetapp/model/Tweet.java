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
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String tweet;
	@Column(nullable = true)
	private Date date;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Tweet tweet = (Tweet) o;
		return id != null && Objects.equals(id, tweet.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
