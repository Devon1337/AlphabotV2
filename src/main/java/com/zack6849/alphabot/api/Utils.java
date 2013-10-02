package com.zack6849.alphabot.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang.StringEscapeUtils;
import org.pircbotx.Colors;

import java.io.*;
import java.net.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isUrl(String s) {
        String url_regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(url_regex);
        Matcher m = p.matcher(s);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTitle(String link) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            Future<Response> future = client.prepareGet(link).setFollowRedirects(true).execute();
            Response response = future.get();
            String output = response.getResponseBody("UTF-8");
            Pattern p = Pattern.compile("<title>(.+)</title>", Pattern.DOTALL);
            Matcher m;
            for (String line : output.split("\n")) {
                m = p.matcher(line);
                if (m.find()) {
                    return StringEscapeUtils.unescapeHtml(m.group().split("<title>")[1].split("</title>")[0]);
                }
            }
            return "No title found.";
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String checkMojangServers() {
        String returns = null;
        try {
            URL url;
            url = new URL("http://status.mojang.com/check");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String result;
            while ((result = reader.readLine()) != null) {
                String a = result.replace("red", Colors.RED + "Offline" + Colors.NORMAL).replace("green", Colors.GREEN + "Online" + Colors.NORMAL).replace("[", "").replace("]", "");
                String final_result = a.replace("{", "").replace("}", "").replace(":", " is currently ").replace("\"", "").replaceAll(",", ", ");
                returns = final_result;
            }
            reader.close();
        } catch (IOException e) {
            if (e.getMessage().contains("503")) {
                returns = "The minecraft status server is temporarily unavailable, please try again later";
            }
            if (e.getMessage().contains("404")) {
                returns = "Uhoh, it would appear as if the status page has been removed or relocated >_>";
            }

        }
        return returns;
    }

    public static String shortenUrl(String longUrl) {
        String shortened = null;
        try {
            URL url;
            url = new URL("http://is.gd/create.php?format=simple&url=" + longUrl);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            shortened = bufferedreader.readLine();
            bufferedreader.close();
        } catch (Exception e) {
        }
        return shortened;
    }

    public static String checkServerStatus(InetAddress i, int port) {
        String returns = "Error.";
        try {
            Socket s = new Socket(i, 25565);
            DataInputStream SS_BF = new DataInputStream(s.getInputStream());
            DataOutputStream d = new DataOutputStream(s.getOutputStream());
            d.write(new byte[]
                    {
                            (byte) 0xFE, (byte) 0x01
                    });
            SS_BF.readByte();
            short length = SS_BF.readShort();
            StringBuilder sb = new StringBuilder();
            for (int in = 0; in < length; in++) {
                char ch = SS_BF.readChar();
                sb.append(ch);
            }
            String all = sb.toString().trim();
            System.out.println(all);
            String[] args1 = all.split("\u0000");
            if (args1[3].contains("§")) {
                returns = "MOTD: " + args1[3].replaceAll("§[a-m]", "").replaceAll("§[1234567890]", "") + "   players: [" + args1[4] + "/" + args1[5] + "]";
            } else {
                returns = "MOTD: " + args1[3] + "   players: [" + args1[4] + "/" + args1[5] + "]";
            }
        } catch (UnknownHostException e1) {
            returns = "the host you specified is unknown. check your settings.";
        } catch (IOException e1) {
            returns = "sorry, we couldn't reach this server, make sure that the server is up and has query enabled.";
        }
        return returns;
    }

    public static String google(String s) {
        try {
            String temp = String.format("https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=%s", URLEncoder.encode(s));
            URL u = new URL(temp);
            URLConnection c = u.openConnection();
            System.out.println("url = " + u);
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String json = "";
            String tmp = "";
            while ((tmp = in.readLine()) != null) {
                json += tmp + "\n";
                //System.out.println(tmp);
            }
            in.close();
            Gson gson = new Gson();
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject output = jelement.getAsJsonObject();
            output = output.getAsJsonObject("responseData").getAsJsonArray("results").get(0).getAsJsonObject();
            String result = String.format("Google: %s | %s | (%s)", StringEscapeUtils.unescapeHtml(output.get("titleNoFormatting").toString().replaceAll("\"", "")), StringEscapeUtils.unescapeHtml(output.get("content").toString().replaceAll("\\s+", " ").replaceAll("\\<.*?>", "").replaceAll("\"", "")), output.get("url").toString().replaceAll("\"", ""));

            if (result != null) {
                return result;
            } else {
                return "No results found for query " + s;
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
