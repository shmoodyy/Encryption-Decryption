package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static final int N = 127; // max char is '~' ASCII decimal code = 126, therefore amount range = 127
    static boolean preferInputConsole;
    static boolean outputConsole = true;
    static boolean isShiftAlgo = true;

    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;
        String data = "";
        String inputFilename;
        String outputFilename = "";
        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-mode"      -> mode = args[i + 1];
                case "-key"       -> key  = Integer.parseInt(args[i + 1]);
                case "-alg"       -> {
                    if (args[i + 1].equalsIgnoreCase("unicode")) isShiftAlgo = false;
                } case "-data"    -> {
                    preferInputConsole = true;
                    data = args[i + 1];
                } case "-in"      -> {
                    if (!preferInputConsole) {
                        inputFilename = args[i + 1];
                        data = readFromFile(inputFilename);
                    }
                } case "-out"     -> {
                    outputConsole = false;
                    outputFilename = args[i + 1];
                }
            }
        }

        char[] dataInput = data.toCharArray();
        switch (mode) {
            case "enc"   -> {
                if (outputConsole) System.out.println(encode(dataInput, key));
                else writeToFile(outputFilename, encode(dataInput, key));
            } case "dec" -> {
                if (outputConsole) System.out.println(decode(dataInput, key));
                else writeToFile(outputFilename, decode(dataInput, key));
            }
        }
    }

    public static String readFromFile(String filename) {
        StringBuilder inputFromFile = new StringBuilder();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                inputFromFile.append(line);
                System.out.println(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found.");
            e.printStackTrace();
        }
        return inputFromFile.toString();
    }

    public static void writeToFile(String filename, String text) {
        System.out.println(text);
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(text);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static String encode(char[] dataInput, int key) {
        StringBuilder encoded = new StringBuilder();
        if (isShiftAlgo) {
            for (char c : dataInput) {
                if (c >= 97 && c <= 122) {
                    c = (char) ((c + key) % 122);
                    c += c < 97 ? 96 : 0;
                }
                encoded.append(c);
            }
        } else {
            for (char c : dataInput) {
                c = (char) ((c + key) % (N));
                encoded.append(c);
            }
        }
        return encoded.toString();
    }

    public static String decode(char[] dataInput, int key) {
        StringBuilder decoded = new StringBuilder();
        if (isShiftAlgo) {
            for (char c : dataInput) {
                if (c >= 97 && c <= 122) {
                    c = (char) ((122 + c - key) % 122);
                    c += c < 97 ? 26 : 0;
                }
                decoded.append(c);
            }
        } else {
            for (char c : dataInput) {
                c = (char) ((N + c - key) % N);
                decoded.append(c);
            }
        }
        return decoded.toString();
    }
}