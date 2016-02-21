package cache;

/**
 * Created by bruce on 15/11/11.
 */
public class TestProcessorsMain {
    public static void main(String[] args) {
        final int NCPU = Runtime.getRuntime().availableProcessors();

        System.out.println(NCPU);
        final int NUMBER_OF_READ_BUFFERS = ceilingNextPowerOfTwo(5);

        System.out.println(NUMBER_OF_READ_BUFFERS);
    }

    static int ceilingNextPowerOfTwo(int x) {
        // From Hacker's Delight, Chapter 3, Harry S. Warren Jr.
        return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(x - 1));
    }
}
