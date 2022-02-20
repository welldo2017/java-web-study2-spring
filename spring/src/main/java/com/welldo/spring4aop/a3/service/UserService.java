package com.welldo.spring4aop.a3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//给UserService的每个业务方法执行前添加日志
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
		users.add(new  User(1, "bob@example.com", "password", "Bob"));
		users.add(new  User(2, "alice@example.com", "password", "Alice"));
		users.add(new  User(3, "tom@example.com", "password", "Tom"));
	}

	//调用了 mailService
	public User login(String email, String password) {
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
				mailService.sendLoginMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}

	public User getUser(long id) {
		return this.users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
	}

	//调用了mailService
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
