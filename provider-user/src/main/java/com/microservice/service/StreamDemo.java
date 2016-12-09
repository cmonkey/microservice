package com.microservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 流编程并不属于并发工具包，但是并发编程中过多过少的会适用到表示数据移动，移动过程中可能会对数据进行处理
// 不同于IO流，表示流对象
// 操作分为中间操作和终端操作
// 中间操作会产生一个新流
// 终端操作会消费流
// 获取流stream/parallelStream
// 操作， sort/max/min/
// 过滤， 排序， 缩减，映射，收集，迭代
public class StreamDemo{

    public static void main (String [] args) {

        List<String> ls = new ArrayList<>();

        ls.add("abc");
        ls.add("def");
        ls.add("ddd");
        ls.add("eee");
        ls.add("def");
        ls.add("cha");

        Optional<String> max = ls.stream().max(String::compareTo);

        System.out.println(max.get());

        ls.stream().sorted().forEach(e -> System.out.println(e));

        System.out.println(ls.stream().distinct().count());
    }

}
