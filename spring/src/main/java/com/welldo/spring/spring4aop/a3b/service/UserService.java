package com.welldo.spring.spring4aop.a3b.service;

import com.welldo.spring.spring4aop.a3b.metric.MetricTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

	@Autowired
	MailService mailService;

	public UserService(@Autowired MailService mailService) {
		this.mailService = mailService;
	}

	private List<User> users = null;
	{
		users = new ArrayList<>();
		users.add(new User(1, "bob@example.com", "password", "Bob"));
		users.add(new User(2, "alice@example.com", "password", "Alice"));
		users.add(new User(3, "tom@example.com", "password", "Tom"));
	}


	//调用了mailService
	@MetricTime("register")
	public User register(String email, String password, String name) {
		users.forEach((user) -> {
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users
				.stream()
				.mapToLong(u -> u.getId())
				.max()
				.getAsLong(), email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;
	}
}
