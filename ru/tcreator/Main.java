package ru.tcreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.*;
import java.util.ArrayList;

public class Main {
    static final String ROOT_PATH = (!System.getProperty("os.name").equals("Windows 10"))
            ? "/home/azathoth/project/java/Games/"
            : "C:\\Users\\m.tuypina\\IdeaProjects\\savegame\\ru\\tcreator\\";
    static final char SEPARATOR = File.separatorChar;
    public static void main(String[] args) throws IOException {
        GameProgress oneSaveProgress = new GameProgress(99, 23, 20, 2.23);
        GameProgress twoSaveProgress = new GameProgress(67, 29, 28,4.23);
        GameProgress threeSaveProgress = new GameProgress(99, 231, 90, 900.23);
        String pathSaveProgress = ROOT_PATH + "savegames" + SEPARATOR;


        saveGame(pathSaveProgress, oneSaveProgress);
        saveGame(pathSaveProgress, twoSaveProgress);
        saveGame(pathSaveProgress, threeSaveProgress);

        zipFiles(pathSaveProgress, pathSaveProgress, "output.zip");
    }

    static public void saveGame(String path, GameProgress progress) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            File[] fileLists = file.listFiles();
            StringBuilder newPathName = new StringBuilder(path)
                    .append("save")
                    .append(getNextNumber(fileLists))
                    .append(".dat");

            File newSaveFile = new File(newPathName.toString());
            try {
                newSaveFile.createNewFile();

                try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(newPathName.toString()))) {
                    objectOutputStream.writeObject(progress);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("file not exists");
        }
    }

    /**
     * формирует следующий номер файла на основе предыдущих номеров
     * @param fileList список файлов в директории
     * @return int
     */
    public static int getNextNumber(File[] fileList) {
        ArrayList<Integer> integersList = new ArrayList<>();
        if(fileList.length == 0) {
            return 1;
        }
        for (File elem: fileList) {
            if(!elem.getName().contains(".zip")) {
                String num = elem.getName().split(".dat")[0].substring(4);
                int intNum = Integer.parseInt(num);
                integersList.add(intNum);
            }

        }
        return integersList.stream()
                .max(Integer::compare)
                .get() + 1;
    }


    /**
     * Упаковывает .dat файлы в zip архив и удаляет исходники
     * @param pathFiles
     * @param pathToZip
     * @param zipName
     * @throws IOException
     */
    public static void zipFiles(String pathFiles, String pathToZip, String zipName) throws IOException {
        File path = new File(pathFiles);
        try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(pathToZip + zipName))) {
            for (String file : path.list()) {
                if(file.contains(".dat")) {
                    String pathFileToZip = pathFiles + file;
                    try(FileInputStream fis = new FileInputStream(pathFileToZip)) {
                        ZipEntry entry1 = new ZipEntry(file);
                        zip.putNextEntry(entry1);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        zip.write(buffer);
                        zip.closeEntry();
                        fis.close();
                        Files.delete(Paths.get(pathFileToZip));
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
