package ar.org.crossknight.twinify.util;

public class UidGenerator {

    public static final int LENGTH = 6;
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";

    private int nextValue;

    public String next() {
        String value = "";
        int remainder = nextValue++;
        for (int c = 0; c < LENGTH; c++) {
            value = SYMBOLS.charAt(remainder % SYMBOLS.length()) + value;
            remainder /= SYMBOLS.length();
        }
        return value;
    }

}