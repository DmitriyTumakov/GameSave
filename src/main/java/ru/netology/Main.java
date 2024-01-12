package ru.netology;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    private static List<String> saves = new ArrayList<>();

    public static void main(String[] args) {
        GameProgress save1 = new GameProgress(20, 5, 4, 20);
        GameProgress save2 = new GameProgress(46, 6, 2, 40);
        GameProgress save3 = new GameProgress(54, 2, 7, 13);

        saveGame("D:\\Games\\savegames\\save1.dat", save1);
        saveGame("D:\\Games\\savegames\\save2.dat", save2);
        saveGame("D:\\Games\\savegames\\save3.dat", save3);
        zipFiles("D:\\Games\\savegames\\saves.zip", saves);


        GameProgress saveLoad = null;

        openZip("D:\\Games\\savegames\\saves.zip", "D:\\Games\\savegames");
        saveLoad = openProgress("D:\\Games\\savegames\\save3.dat");
        if (saveLoad != null) {
            System.out.println(saveLoad.toString());
        }
    }

    private static GameProgress openProgress(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void openZip(String path, String filePath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(filePath + "\\" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void zipFiles(String path, List<String> saves) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (int i = 0; i < saves.size(); i++) {
                FileInputStream fis = new FileInputStream(saves.get(i));
                ZipEntry entry = new ZipEntry("save" + (i + 1) + ".dat");
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                fis.close();
            }
            zout.closeEntry();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < saves.size(); i++) {
            File file = new File(saves.get(i));
            if (file.delete()) {
                System.out.println(file.getName() + " удалён");
            } else {
                System.out.println("Ошибка при удалении файла");
            }
        }
    }

    private static void saveGame(String path, GameProgress gameSave) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameSave);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        saves.add(path);
    }
}