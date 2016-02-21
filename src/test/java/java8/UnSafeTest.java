package java8;

import sun.misc.Unsafe;

/**
 * Created by bruce on 15/11/26.
 */
public class UnSafeTest {
    public static void main(String[] args) {
        Unsafe.getUnsafe().throwException(new NullPointerException("hello"));
    }
}
