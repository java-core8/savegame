package ru.tcreator;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static final String ROOT_PATH = "/home/azathoth/project/java/Games/";
    public static void main(String[] args) throws IOException {
        GameProgress oneSaveProgress = new GameProgress(99, 23, 20, 2.23);
        GameProgress twoSaveProgress = new GameProgress(67, 29, 28,4.23);
        GameProgress threeSaveProgress = new GameProgress(99, 231, 90,900.23);
        String pathSaveProgress = ROOT_PATH + "savegames/";
        saveGame(pathSaveProgress, oneSaveProgress);
        saveGame(pathSaveProgress, twoSaveProgress);
        saveGame(pathSaveProgress, threeSaveProgress);
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
     * формирует слежующий номер файла на основе предыдущих номеров
     * @param File[] fileList список файлов в директории
     * @return int
     */
    public static int getNextNumber(File[] fileList) {
        ArrayList<Integer> integersList = new ArrayList<>();
        if(fileList.length == 0) {
            return 1;
        }
        for (File elem: fileList) {
            String num = elem.getName().split(".dat")[0].substring(4);
            int intNum = Integer.parseInt(num);
            integersList.add(intNum);
        }
        return integersList.stream()
                .max(Integer::compare)
                .get() + 1;
    }

    public static void zipFiles(String path, String pathToZip) {

    }
}
