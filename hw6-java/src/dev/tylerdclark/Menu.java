/* FILE: Menu.java
 * AUTHOR: Tyler Clark
 * DATE: 23 April 2021
 */

package dev.tylerdclark;

import java.io.*;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Creates a persistent menu that allows the do certain file and directory processing
 */
public class Menu {

    private final Scanner scanner;
    private boolean keepGoing; // ends program with false
    private String path; // set with option 1 or else null

    /**
     * No arg constructor for the menu. Instantiates a Scanner object {@link #scanner} and
     * sets {@link #keepGoing} to true, which will allow a while loop in {@link #run()}
     */
    public Menu() {

        scanner = new Scanner(System.in);
        keepGoing = true;
        path = null;

    }

    /**
     * Commences a menu loop that will continue until the user gives 0 as input. The user
     * should select the directory (option 1) before using the other options.
     */
    public void run() {

        while (keepGoing) {
            System.out.println("***PROJECT 5***" + "\n0: Exit" + "\n1: Select Directory"
                    + "\n2: List Directory (first level)" + "\n3: List Directory (all levels)"
                    + "\n4: Delete File" + "\n5: Display File (hex view)"
                    + "\n6: Encrypt File (XOR with password)" + "\n7: Decrypt File (XOR with password)");

            keepGoing = userChoice();
        }
    }

    /**
     * Collects the user input using {@link #scanner} and handles
     * InputMismatchException. Individually calls methods for each of the menu
     * items.
     *
     * @return whether to continue or not
     */
    private boolean userChoice() {
        try {
            switch (scanner.nextInt()) {
                case 0:
                    return false;
                case 1:
                    path = selectDirectory(scanner);
                    break;
                case 2:
                    listDirContent(path);
                    break;
                case 3:
                    ListDirContentAll(path);
                    break;
                case 4:
                    deleteFile(scanner);
                    break;
                case 5:
                    displayFileHex(scanner);
                    break;
                case 6:
                    encryptDecryptFile(scanner, true);
                    break;
                case 7:
                    encryptDecryptFile(scanner, false);
                    break;
                default:
                    System.err.println("Please use 0-7");
            }
        } catch (InputMismatchException exception) {
            System.err.println("Please use 0-7");
            scanner.next(); //clear the scanner
            return true;
        }
        return true;
    }


    /**
     * Using the {@link #scanner} member, scans from the absolute path of the directory.
     * The found directory will be the directory from which the other methods will use.
     * Accordingly, this method must be called before the other ones. Returns null if the
     * path is correct.
     *
     * @param scanner scanner to collect the path
     * @return the string representation of the path OR null if it is an incorrect path
     */
    private String selectDirectory(Scanner scanner) {
        System.out.println("Please input the absolute path name.");
        String path = scanner.next();
        boolean badPath = true;
        try {
            badPath = !Files.exists(Paths.get(path));
        } catch (InvalidPathException exception) {
            System.out.println("Path was not found.");
        }

        if (badPath) {
            path = null; //important so that other methods don't crash
            System.out.println("Path was not found.");
        } else {
            System.out.println("Path was set to: ");
            System.out.println(path);
        }
        return path;
    }


    /**
     * Gets passed a string which contains the absolute path of a directory. From that
     * directory, this method prints all the files and subdirectories from the first level
     * @param path string absolute path to the directory
     */
    private void listDirContent(String path) {
        if ((path == null)) {
            System.out.println("Enter Directory using Option 1");

        } else {
            try {
                Files.list(new File(path).toPath()).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets passed a string which contains the absolute path of a directory. From that
     * directory, this method prints all the files and subdirectories using a depth first
     * traversal with Files.walk()
     *
     * @param path string absolute path to the directory
     */
    private void ListDirContentAll(String path) {
        if (path == null) {
            System.out.println("Enter Directory using Option 1");
        } else {
            try {
                Files.walk(Paths.get(path)).filter(Files::isRegularFile).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prompts the user for a filename and deletes that file from the selected directory.
     * If no directory was selected an error message must be displayed. If the directory
     * does not contain the file specified by the user, an error message must be displayed.
     *
     * @param scanner used to collect the file name for deletion
     */
    private void deleteFile(Scanner scanner) {
        if (path == null) {
            System.out.println("Enter Directory using Option 1");
        } else {
            System.out.print("Enter filename: ");
            String toDelete = scanner.next();
            String filePath = path + "/" + toDelete;
            try {
                Files.delete(Paths.get(filePath));
                System.out.println(filePath + " has been deleted");
            } catch (NoSuchFileException noSuchFileException) {
                System.out.println("No such file");
            } catch(DirectoryNotEmptyException directoryNotEmptyException) {
                System.out.println("Directory not empty");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prompts the user for a filename (from the selected directory) and displays the
     * content of that file on the screen, in hexadecimal view. If a directory has not been
     * chosen or the file is not present, a error message will be displayed.
     *
     * @param scanner used to collect the file name
     */
    private void displayFileHex(Scanner scanner) {
        if (path == null) {
            System.out.println("Enter Directory using Option 1");
        } else {
            System.out.print("Enter filename: ");
            String toDisplay = scanner.next();
            String filePath = path + "/" + toDisplay;

            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

                for (int i = 0; i < fileBytes.length; i++) {
                    if (i % 20 == 0) {
                        System.out.print("\n");
                    }
                    System.out.printf("%02X ", fileBytes[i]); //format the byte to hexadecimal
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Either encrypts or decrypts depending on the second parameter. Requires the path to be specified with
     * {@link #selectDirectory(Scanner)}.
     *
     * @param scanner scanner to collect the password and file
     * @param encrypt true is encrypting, decrypting otherwise
     */
    private void encryptDecryptFile(Scanner scanner, boolean encrypt) {
        if (path == null) {
            System.out.println("Enter Directory using Option 1");
        } else {

            byte[] fileBytes = null;

            //reading in the file to encrypt
            System.out.print("Enter filename: ");
            String fileName = scanner.next();
            String filePath = path + "/" + fileName;
            try {
                System.out.println("Reading in file...");
                fileBytes = Files.readAllBytes(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //reading in a potential password
            System.out.println("Enter password:");
            String password = scanner.next();
            byte[] passwordBytes = password.getBytes();
            if (passwordBytes.length > 256) {
                System.out.println("Password needs to be less than 256 bytes");
                return;
            }
            //attempt to encrypt
            if (fileBytes != null){
                byte[] encryptedFileBytes = new byte[fileBytes.length];
                for (int i = 0; i < fileBytes.length; i++) {
                    encryptedFileBytes[i] =  (encrypt)
                            ? (byte)(fileBytes[i] ^ passwordBytes[i % passwordBytes.length])
                            : (byte)(passwordBytes[i % passwordBytes.length] ^ fileBytes[i]);
                }

                //Creates new file

                fileName = (encrypt) ? "E-"+ fileName : "D-" + fileName.substring(2);

                File outputFile = new File(path + "/" + fileName);

                try (FileOutputStream stream = new FileOutputStream(outputFile)) {
                    stream.write(encryptedFileBytes);
                    System.out.println(fileName + " was" + ((encrypt) ? " encrypted" : " decrypted"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
