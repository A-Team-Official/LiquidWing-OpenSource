package ad.util.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;



public class HWIDChecker {

    public static String getCpuId() throws IOException {
        Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId"});

        process.getOutputStream().close();
        Scanner sc = new Scanner(process.getInputStream());
        String property = md5(convertMD5(md5(sc.next())));
        String serial = md5(sc.next());

        System.out.println(property + ": " + serial);
        //GuiLogin.LOVEU *= 10;
        return serial;
    }

    public static String get(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();
        return response.toString();
    }

    public static String md5(String text) {
        Object secretBytes = null;

        byte[] abyte;

        try {
            abyte = MessageDigest.getInstance("md5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new IllegalStateException("md5 errror");
        }

        String md5code = (new BigInteger(1, abyte)).toString(16);

        for (int i = 0; i < 32 - md5code.length(); ++i) {
            md5code = "0" + md5code;
        }

        return md5code;
    }

    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();

        for (int s = 0; s < a.length; ++s) {
            a[s] = (char) (a[s] ^ 116);
        }

        String s = new String(a);

        return s;
    }

    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;

        if (left != null && !left.isEmpty()) {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        } else {
            zLen = 0;
        }

        int yLen = text.indexOf(right, zLen);

        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }

        result = text.substring(zLen, yLen);
        return result;
    }

    public static String getTime() {
        String times = null;

        try {
            times = getSubString(get("http://vv6.video.qq.com/checktime"), "<root><s>o</s><t>", "</t><ip>");
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        return times;
    }
}
