import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {
    public static String ip = null;

    public static void main(String[] args) {
        while (true) {
            //args0: key(ifttt)
            //args1: eventName(ip)
            HttpURLConnection connection = null;
            InputStream is = null;
            BufferedReader br = null;
            try {
                URL url = new URL("https://api.ip.sb/ip");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(60000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    StringBuilder sbf = new StringBuilder();
                    String temp;
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    String newIp = sbf.toString();
                    if (ip == null || !ip.equals(newIp)){
                        send(newIp, args[0], args[1]);
                        ip = newIp;
                    } else {
                        Thread.sleep(50000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    public static void send(String ip, String key, String eventName) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://maker.ifttt.com/trigger/" + eventName + "/with/key/" + key + "?value1=" + ip);
            System.out.println(url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            System.out.println(connection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
