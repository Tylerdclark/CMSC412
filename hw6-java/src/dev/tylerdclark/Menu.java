/* FILE: Menu.java
 * AUTHOR: Tyler Clark
 * DATE: 23 April 2021
 */

package dev.tylerdclark;

import java.util.Scanner;

public class Menu {

    public Menu() {

        Scanner scanner = new Scanner(System.in);
        boolean keepGoing = true;


        while (keepGoing) {
            System.out.println("***PROJECT 5***" + "\n0: Exit" + "\n1: Select Directory" +
                    "\n2: List Directory (first lvl)" + "\n3: List Directory (all lvls)" +
                    "\n4: Delete File" + "\n5: Display File (hex view)" + "\n6: Encrypt File (XOR with pwd)" +
                    "\n7: Decrypt File (XOR with pwd)" + "\n8: Print this help menu");

            System.out.println("\n\nDo you want to continue?");
            keepGoing = scanner.nextBoolean();
        }
    }
}
