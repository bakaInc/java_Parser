

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.*;
import java.util.stream.Collectors;


public class Main {

    private static final String PATH = "C:\\Users\\admin\\IdeaProjects\\ParseHTML\\src\\files\\";

    public static void main(String[] args) throws IOException {

        //getHref("https://briefly.ru/authors/", "letter", "fileData");
        //getData();

        //getId("https://briefly.ru/defo/zhizn_i_udivitelnye_prikljuchenija_robinzona_kruzo/", "text", "Robinzon");


        var Array = Files.list(Paths.get(PATH + "data")).collect(Collectors.toList()).toArray().length;

        for (int i = 0; i < Array; i++) {
            if (readDataFromFile(PATH + "data\\" + String.valueOf(i)).length() <= 0) {
                continue;
            }

            getId(readDataFromFile(PATH + "data\\" + String.valueOf(i)), "text", String.valueOf(i));
        }


        return;
    }

    public static String readDataFromFile(String file) throws IOException {
        String html = null;
        FileReader reader = null;
        try {
            File fileData = new File(file);
            reader = new FileReader(fileData);
            char[] chars = new char[(int) (fileData.length())];
            reader.read(chars);
            html = new String(chars);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
            reader.close();
        }
        System.out.println(html);

        return html;

    }

    public static void getData() throws IOException {

        String html = readDataFromFile(PATH + "fileData");

        int indexD = 0;
        String[] hrefs = html.split("\n");
        for (String elem : hrefs) {
            //System.out.println(elem);
            if (elem.matches("https://briefly.ru/wanted/\\.*") || elem.contains("https://wiki.briefly.ru/") || elem.length() < 6) {
                continue;
            } else {
                getHref(elem, "author_body", String.valueOf(indexD++));
            }

        }

    }

    public static void getHref(String url, String className, String fileName) throws IOException {

        File fileOut = new File(PATH + fileName);

        FileWriter writer = new FileWriter(fileOut);

        Document doc = Jsoup.connect(url).get();

        Elements newsHeadlines = doc.getElementsByClass(className);

        for (Element elem : newsHeadlines) {
            Elements links = elem.getElementsByTag("a");

            for (Element link : links) {
                String linkHref = link.attr("href");
                String linkText = link.text();
                //System.out.println(linkHref);
                if (linkHref.matches(".*/wanted/.*") || linkHref.contains("https://wiki.briefly.ru/") || linkHref.length() < 6) {
                    continue;
                }

                System.out.println("@#####################" + linkHref);
                if (linkHref.indexOf("http") >= 0) {

                    writer.write(linkHref + "\n");
                } else {
                    writer.write("https://briefly.ru" + linkHref + "\n");
                }

            }

        }
        writer.close();

    }

    public static void getId(String url, String id, String fileName) throws IOException {
        try {

            System.out.println(url + " " + url.length());
            Document doc = Jsoup.connect(url).get();
            Element textData = doc.getElementById(id);


            File fileOut = new File(PATH + "text\\" + fileName);
            FileWriter writer = new FileWriter(fileOut);
            BufferedWriter out = new BufferedWriter(writer);


            if (textData == null) {
                return;
            }
            out.write(textData.text());
            out.close();


        } catch (HttpStatusException e) {
            System.out.println("er r 404");
        } catch (MalformedURLException e) {
            System.out.println("er r hernya");
        } catch (IllegalArgumentException e) {
            System.out.println("er r hernya2");
        }


    }
}


