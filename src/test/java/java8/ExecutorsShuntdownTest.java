package java8;

import java.util.concurrent.*;

/**
 * Created by bruce on 15/11/27.
 */
public class ExecutorsShuntdownTest {

    final static BlockingQueue<String> q = new LinkedBlockingQueue<String>();

    volatile static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                while (!flag) {
//                    try {
//                        Thread.sleep(1000*3);
                    System.out.println("xxx");
//                        String e = q.take();
//                        System.out.println("sss" + e);
//                    } catch (Exception e) {
//                        System.out.println("eee"+e);
//                    }
//                    System.out.println(Thread.currentThread().isInterrupted());
//                    if (Thread.currentThread().isInterrupted()) {
//                        System.out.println("interrupted");
//                       /*if(flag) */break;
////                    System.out.println(flag);
//                    }
                }
//                System.out.println(flag);
            }
        });

        Thread.sleep(1000 * 5);

        flag = true;

        executor.shutdown();
//        executor.shutdownNow();

        if (!executor.awaitTermination(100, TimeUnit.MICROSECONDS)) {
            System.out.println("Still waiting after 100ms: calling System.exit(0)...");
//            System.exit(0);
        }
        System.out.println("Exiting normally...");
    }


}
