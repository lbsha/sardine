package java8;

/**
 * @auth bruce-sha
 * @date 2015/8/10
 */
public class IPTest {
    public static void main(String[] args) {
        String clientIP = " 192.168.0.1 ,192.168.0.2";
        clientIP = "127.0.0.1";
        clientIP = clientIP.split(",")[0];

        System.out.println(clientIP);
    }
}
