package ee.lis.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LowLevelUtils {
    public static final char STX = 2;
    public static final char ETX = 3;
    public static final char EOT = 4;
    public static final char ENQ = 5;
    public static final char ACK = 6;
    public static final char LF = 10;
    public static final char CR = 13;
    public static final char NAK = 21;
    public static final char ETB = 23;

    public static final Map<Character, String> charToHumanReadableString;
    public static final Map<String, Character> humanReadableStringToChar;

    static {
        charToHumanReadableString = Collections.unmodifiableMap(new HashMap<Character, String>() {{
            put(STX, "<STX>");
            put(ETX, "<ETX>");
            put(EOT, "<EOT>");
            put(ENQ, "<ENQ>");
            put(ACK, "<ACK>");
            put(LF,  "<LF>");
            put(CR,  "<CR>");
            put(NAK, "<NAK>");
            put(ETB, "<ETB>");
        }});
        humanReadableStringToChar = Collections.unmodifiableMap(new HashMap<String, Character>() {{
            put("<STX>", STX);
            put("<ETX>", ETX);
            put("<EOT>", EOT);
            put("<ENQ>", ENQ);
            put("<ACK>", ACK);
            put("<LF>",  LF);
            put("<CR>", CR);
            put("<NAK>", NAK);
            put("<ETB>", ETB);
        }});
    }

    public static String getCheckSum(String frame) {
        byte[] bytes = frame.getBytes();
        int sum = 0;
        for (byte b : bytes) {
            if (b == STX)
                continue;
            sum += b;
            sum %= 256;
            if (b == ETX || b == ETB)
                break;
        }
        String checksum = Integer.toHexString(sum);
        if (checksum.length() == 1)
            checksum = "0" + checksum;
        return checksum.toUpperCase();
    }

    public static String formatToHumanReadable(String string) {
        StringBuilder result = new StringBuilder();
        for (char c : string.toCharArray()) {
            String readable = charToHumanReadableString.get(c);
            if (readable == null) {
                result.append(c);
            } else {
                result.append(readable);
            }
        }
        return result.toString();
    }
}