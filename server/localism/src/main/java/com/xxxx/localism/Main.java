package com.xxxx.localism;

import com.xxxx.localism.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Object obj=(String)"abc";
        System.out.println(obj);
        String str=(String) obj;
        System.out.println(str);
    }
}
