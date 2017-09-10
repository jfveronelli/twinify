package ar.org.crossknight.twinify.util;

public class UidGenerator {

    public static final int LENGTH = 6;
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";

    private int nextValue;

    public String next() {
        StringBuilder value = new StringBuilder("");
        int remainder = nextValue++;
        for (int c = 0; c < LENGTH; c++) {
            value.insert(0, SYMBOLS.charAt(remainder % SYMBOLS.length()));
            remainder /= SYMBOLS.length();
        }
        return value.toString();
    }

}