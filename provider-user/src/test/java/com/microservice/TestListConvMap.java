package com.microservice;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cmonkey on 12/9/16.
 */
public class TestListConvMap {
    @Test
    public void testListConvMap(){
        List<User> list = new ArrayList<>();

        User user1 = new User(1, "name1");
        User user2 = new User(2, "name2");
        User user3 = new User(3, "name3");
        User user4 = new User(4, "name4");

        list.add(user1);
		list.add(user2);
		list.add(user3);
		list.add(user4);

		Map<Integer, String> map = list.stream().collect(Collectors.toMap(User::getId, User::getName));

		list.forEach(item -> map.put(item.getId(), item.getName()));
		list.stream().forEachOrdered(item -> map.putIfAbsent(item.getId(), item.getName()));

		System.out.println(map);
    }
}
class User{
	private int id;
	private String name;

	public User(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
