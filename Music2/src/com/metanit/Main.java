package com.metanit;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static  final String IN_FILE2_TXT = "inFile2.txt";
    private static final String IN_FILE_TXT = "inFile.txt";
    private static final String OUT_FILE_TXT = "outFile.txt";
    private static final String OUT_FILE2_TXT = "outFile2.txt";
    private static final String PATH_TO_MUSIC = "music\\music";
    private static final String PATH_TO_PNG = "png\\png";

    public static void main(String[] args) {
        String Url;
        String Url2;
        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT));
             BufferedReader inFile2 = new BufferedReader(new FileReader(IN_FILE2_TXT));
             BufferedWriter outFile2 = new BufferedWriter(new FileWriter(OUT_FILE2_TXT))) {
            while ((Url = inFile.readLine()) != null && (Url2 = inFile2.readLine()) != null) {
                URL url = new URL(Url);
                URL url2 = new URL(Url2);

                String result;
                String result2;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                     BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(url2.openStream()))) {
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                    result2 = bufferedReader2.lines().collect(Collectors.joining("\n"));
                }
                Pattern email_pattern = Pattern.compile("/track/dl/\\d{8}/\\S*\\.mp3");
                Matcher matcher = email_pattern.matcher(result);
                Pattern email_pattern2 = Pattern.compile("//static.wixstatic.com/media/\\S*.jpg");
                Matcher matcher2 = email_pattern2.matcher(result2);
                int i = 0;
                while (matcher.find() && i<4) {
                    outFile.write("https://musify.club" + matcher.group() + "\r\n");
                    i++;
                }
                int a = 0;
                while ( matcher2.find() && a<4){
                    outFile2.write(  matcher2.group() + "\r\n");
                    a++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT));
             BufferedReader pngFile = new BufferedReader(new FileReader(OUT_FILE2_TXT))) {
            String music;
            String png;
            int count = 1;
            int count2 = 1;
            try {
                while ((music = musicFile.readLine()) != null ) {
                    downloadUsingNIO(music, PATH_TO_MUSIC + String.valueOf(count) + ".mp3");

                    count++;
                }
                while ((png = pngFile.readLine()) != null){
                    downloadUsingNIO(png, PATH_TO_PNG + String.valueOf(count2) + ".jpg");

                    count2++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream()); // Создает байтовый канал для чтения сайта
        FileOutputStream stream = new FileOutputStream(file); // Создает поток для записи
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);//Записывает в поток данные
        stream.close();
        byteChannel.close();
    }
}