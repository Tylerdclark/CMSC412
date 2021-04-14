/*
 * File: Utils.java
 * Author: Tyler Clark
 * Date: 10 April 2021
 ***********************/
package dev.tylerdclark;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The class is mostly for the menu and resolving path
 */
public class Utils {

    /**
     * Continuously prompts the user for new banker's algorithm circumstances
     */
    public static void Menu() {
        Scanner scanner = new Scanner(System.in);
        boolean go_on = true;
        while (go_on) {

            System.out.println("How many Processes?");
            int prc_ct = scanner.nextInt();
            System.out.println("How many Resources?");
            int res_ct = scanner.nextInt();
            System.out.println("What is the location of the file?");
            String file_loc = scanner.next();
            File file = pathResolver(file_loc);

            Bankers bankers = new Bankers(res_ct, prc_ct, file);
            // marked, allocated, need, available, safe
            bankers.method(bankers.getMarked(), bankers.getAvail(), bankers.getSafe());
            // ask to keep going
            go_on = keepGoing(scanner);
        }
    }

    /**
     * asks the user is they wish to go on. ANYTHING other than 'y' or 'Y' returns false
     * 
     * @param scanner passing scanning resource
     * @return whether or not to continue
     */
    private static boolean keepGoing(Scanner scanner) {
        System.out.println("Would you like to read in a file? Y/N");
        String ans = scanner.next();
        return ans.equals("Y") || ans.equals("y");
    }

    /**
     * Resolves the file in a ugly way. If I have time will improve this.
     *
     * @param argument string file name to be resolved
     * @return the resolved file
     */
    private static File pathResolver(String argument) {

        File srcDir;
        File projectDir;
        File lastDir;

        try {
            srcDir = new File(Paths.get("src/dev/tylerdclark/" + argument).toAbsolutePath().toString());
            projectDir = new File(Paths.get("test/" + argument).toAbsolutePath().toString());
            lastDir = new File(Paths.get(argument).toAbsolutePath().toString());

            if (srcDir.exists()) {
                return srcDir;
            } else if (projectDir.exists()) {
                return projectDir;
            } else if (lastDir.exists()) {
                return lastDir;
            }

        } catch (NullPointerException nullPointerException) {
            System.err.println(argument + " Is not a valid file");
        }
        return new File(argument);
    }
}
