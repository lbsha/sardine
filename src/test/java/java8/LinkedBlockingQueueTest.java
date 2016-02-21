package java8;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bruce on 15/11/12.
 */
public class LinkedBlockingQueueTest {

    public static void main(String[] args) {

        LinkedBlockingQueue<Person> bq = new LinkedBlockingQueue<Person>(1024);

        bq.add(new Person());


        System.out.println(bq.size());
        System.out.println(bq);
    }

    static class Person {
        private int age = 1;
        private String name = "abc";
    }


}
