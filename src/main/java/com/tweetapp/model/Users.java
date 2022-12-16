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
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(nullable = false)
	private String email;
	@Column(unique = true,nullable = false)
	private String username;
	@Column(nullable = false)
	private String firstName;
	private String lastName;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String contactNumber;
	@Column(nullable = true)
	private Date dob;
	@Column(nullable = true)
	private String gender;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Users users = (Users) o;
		return id != null && Objects.equals(id, users.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
