package common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    
    private static final String CHARSET = "UTF-8";
    
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return data;
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), CHARSET))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
            System.out.println("Successfully read file: " + filePath + ", total " + data.size() + " rows");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception while reading file: " + filePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unknown exception while reading file: " + filePath);
            e.printStackTrace();
        }
        
        return data;
    }
    
    public static void writeCSV(String filePath, List<String[]> data) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), CHARSET))) {
            for (String[] row : data) {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            System.out.println("Successfully wrote to file: " + filePath + ", total " + data.size() + " rows");
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path or no write permission: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception while writing to file: " + filePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unknown exception while writing to file: " + filePath);
            e.printStackTrace();
        }
    }
    
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    public static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
                System.out.println("Created new file: " + filePath);
            } catch (IOException e) {
                System.out.println("Failed to create file: " + filePath);
                e.printStackTrace();
            }
        }
    }
    
    public static List<String[]> searchByField(String filePath, int fieldIndex, String searchValue) {
        List<String[]> results = new ArrayList<>();
        List<String[]> allData = readCSV(filePath);
        
        for (String[] row : allData) {
            if (row.length > fieldIndex && row[fieldIndex].equals(searchValue)) {
                results.add(row);
            }
        }
        
        return results;
    }
    
    public static void updateRow(String filePath, int rowIndex, String[] newData) {
        List<String[]> allData = readCSV(filePath);
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            allData.set(rowIndex, newData);
            writeCSV(filePath, allData);
        } else {
            System.out.println("Invalid row index: " + rowIndex);
        }
    }
    
    public static void addRow(String filePath, String[] newRow) {
        List<String[]> allData = readCSV(filePath);
        allData.add(newRow);
        writeCSV(filePath, allData);
    }
    
    public static void deleteRow(String filePath, int rowIndex) {
        List<String[]> allData = readCSV(filePath);
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            allData.remove(rowIndex);
            writeCSV(filePath, allData);
        } else {
            System.out.println("Invalid row index: " + rowIndex);
        }
    }
}    