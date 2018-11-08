package io.crawlerbot.crawler.utils;

import com.crawler.config.domain.Channel;
import io.crawlerbot.crawler.messaging.MessengerConverter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileConfigUtils {
    public static List<File> getAllConfigFiles() {
        File folders = new File("src/main/resources/00_sites/");
        List<File> files = new ArrayList<>();
        if (folders != null && folders.isDirectory()) {

            File[] configFolders = folders.listFiles();
            for (File folder : configFolders) {
                if (folder.isDirectory()) {
                    File[] configFiles = folder.listFiles();
                    for (File subFile : configFiles) {
                        if (subFile.getName() != null && subFile.getName().equalsIgnoreCase("config.json")) {
                            files.add(subFile);
                        }
                    }
                }
            }
        }
        return files;
    }

    public static List<File> getAllChannelFilesV1() {
        File folders = new File("src/main/resources/en-us/v1/");
        List<File> files = new ArrayList<>();
        if (folders != null && folders.isDirectory()) {

            File[] configFolders = folders.listFiles();
            for (File folder : configFolders) {
                if (folder.isDirectory()) {
                    File[] configFiles = folder.listFiles();

                    for (File subFile : configFiles) {
                        if(subFile.isDirectory() && subFile.getPath().contains("channel")) {
                            File[] channels = subFile.listFiles();
                           for(File file: channels) {
                               files.add(file);
                           }
                        }
                    }
                }
            }
        }
        return files;
    }

    public static List<File> getAllChannelFiles(String languageCode) {
        File folders = new File(String.format("src/main/resources/%s/v1/", languageCode));
        List<File> files = new ArrayList<>();
        if (folders != null && folders.isDirectory()) {

            File[] categoryFolders = folders.listFiles();
            for (File folder : categoryFolders) {
                if (folder.isDirectory()) {
                    File[] domainFolders = folder.listFiles();
                    for(File domainFolder: domainFolders ) {
                        if(domainFolder.isDirectory()) {
                            File[] configFiles = domainFolder.listFiles();
                            for (File subFile : configFiles) {
                                if(subFile.isDirectory() && subFile.getPath().contains("channel")) {
                                    File[] channels = subFile.listFiles();
                                    for(File file: channels) {

                                            files.add(file);
                                    }
                                }

                            }
                        }
                    }


                }
            }
        }
        return files;
    }

    public static List<Channel> getChannels(String languageCode) {
        return getAllChannelFiles(languageCode).stream().map(file -> {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                Channel channel = MessengerConverter.convertStringToChannel(fileContent);
                return channel;
            }catch (Exception ex) {
                return null;
            }
        }).collect(Collectors.toList());
    }
    public static List<Channel> getChannels(String languageCode, String filterName) {
        return getAllChannelFiles(languageCode).stream().filter(file -> file.getPath().contains(filterName)).map(file -> {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                Channel channel = MessengerConverter.convertStringToChannel(fileContent);
                return channel;
            }catch (Exception ex) {
                return null;
            }
        }).collect(Collectors.toList());
    }
    public static List<Channel> getAllChannels(String languageCode) {
        return getAllChannelFiles(languageCode).stream().map(file -> {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                Channel channel = MessengerConverter.convertStringToChannel(fileContent);
                return channel;
            }catch (Exception ex) {
                return null;
            }
        }).collect(Collectors.toList());
    }


}
