package main;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public class Diki {
    public static DictionaryElement downloadTranslation(String word) {
        String translation = "";

        try {
            Document document = Jsoup.connect("https://www.diki.pl/slownik-angielskiego?q=" + word).get();

            Elements foundTags = document.select(".phoneticTranscription .absmiddle");
            if (!foundTags.isEmpty()) {
                String src  = foundTags.get(0).absUrl("src");

                URL url = new URL(src);
                InputStream in = url.openStream();
                OutputStream out = new BufferedOutputStream(new FileOutputStream("files/pronunciation/" + word + ".png"));
                for (int b; (b = in.read()) != -1;) {
                    out.write(b);
                }
                out.close();
                in.close();
            }

            Elements foundMeanings = document.select(".foreignToNativeMeanings .hw");
            for (Element found_meaning : foundMeanings) {
               translation += found_meaning.text() + "; ";
            }
        }
        catch (HttpStatusException ex) {
            translation = "404";
        }
        catch (SocketTimeoutException | UnknownHostException ex){
            translation = "timeout";
        } catch (IOException e) {
            translation = "unknown error";
        }

        return new DictionaryElement(word, translation);
    }
}
