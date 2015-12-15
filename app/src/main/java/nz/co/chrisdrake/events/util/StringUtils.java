package nz.co.chrisdrake.events.util;

public final class StringUtils {
    public static String join(Iterable tokens, CharSequence delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    private StringUtils() {
        throw new AssertionError("Non-instantiable.");
    }
}
