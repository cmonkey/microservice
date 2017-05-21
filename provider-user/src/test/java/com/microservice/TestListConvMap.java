package com.microservice;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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

		assertThat(map.size(),is(4));
		assertThat(map.get(1), is("name1"));

    }
}

