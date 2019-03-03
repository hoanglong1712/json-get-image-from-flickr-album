
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.json.Json;
import javax.json.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 0h07-4-17-8-2016
 */
public class jjson {

    public static void main(String[] args) throws MalformedURLException, IOException {
        URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=e3303ddb150272bfd3f45c111e217298&photoset_id=72157689606161593&format=json&nojsoncallback=1&api_sig=dc5a2a560c28970a1e0a70de5bbf5a4a");

        //  URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=015f6549349d226bd35da23bef97436f&photoset_id=72157692261866131&user_id=152673763%40N04&format=json&auth_token=72157694788849074-7c830a678d826444&api_sig=eda2f2ec13a3955269efcde588069b8d");
        
        ArrayList<Photo> photo_list = new ArrayList<>();
        try (InputStream is = url.openStream();
                JsonReader rdr = Json.createReader(is)) {
            FileWriter fw = new FileWriter(new File("out.txt"));
            JsonObject obj = rdr.readObject();
            JsonObject set = (JsonObject) obj.get("photoset");
            JsonArray photos = set.getJsonArray("photo");
            String s;
            for (JsonObject photo : photos.getValuesAs(JsonObject.class)) {
                s = photo.getString("title") + " [img]https://farm" + photo.getInt("farm");
                s += ".staticflickr.com/" + photo.getString("server");
                s += "/" + photo.getString("id");
                s += "_" + photo.getString("secret") + "_c.jpg[/img]";

                //System.out.println(s);
                photo_list.add(new Photo(photo.getString("title"), s));
                fw.write(s);
                fw.write(System.lineSeparator());
            }
            fw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        Collections.sort(photo_list);
        
        for (Photo photo : photo_list) {
            System.out.println(photo.str);
        }
    }
}

class Photo implements Comparable<Photo> {

    String title;
    String str;

    public Photo(String title, String str) {
        this.title = title;
        this.str = str;
    }

    @Override
    public int compareTo(Photo o) {
        return title.compareTo(o.title);
    }

}
