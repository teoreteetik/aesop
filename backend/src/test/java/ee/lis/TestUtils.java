package ee.lis;


import static ee.lis.util.LowLevelUtils.*;

/**
 * @author Lembit Gerz (lembit.gerz@gmail.com)
 */
public class TestUtils {

    public static String convertLowLevelChars(String string) {
        String result = string
            .replaceAll("<STX>", "" + STX)
            .replaceAll("<ENQ>", "" + ENQ)
            .replaceAll("<EOT>", "" + EOT)
            .replaceAll("<ACK>", "" + ACK)
            .replaceAll("<ETX>", "" + ETX)
            .replaceAll("<CR>", "" + CR)
            .replaceAll("<LF>", "" + LF);
        return result.replaceAll("\\{CS\\}", getCheckSum(result));
    }

}
