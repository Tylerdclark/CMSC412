/*
 * File: Bankers.java
 * Author: Tyler Clark
 * Date: 10 April 2021
 ***********************/

package dev.tylerdclark;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * class that implements the Banker's algorithm. Pretty much the only thing that this
 * class doesn't do is handle the menu and resolving paths.
 */
public class Bankers {

    private final int res_ct; //# of resources
    private final int prc_ct; //# of processes
    private final int[][] max_matrix; //max resources used by a process
    private final int[][] al_matrix; // # of the allocated resources by a process
    private int[][] need_matrix; // # of resources for a process to execute
    private final int[] avail; // not allocated resources
    private final boolean[] marked; // used for finding safe orders
    private final Vector<Integer> safe; //Vector can be accessed by index and grows

    /**
     * Creates an instance of the Banker's algorithm. This constructor initializes the
     * arrays and parses the file.
     *
     * @param res number of resources
     * @param prc number of processes
     * @param file file containing the matrices to be read
     */
    public Bankers(int res, int prc, File file) {
        this.res_ct = res;
        this.prc_ct = prc;

        this.max_matrix = new int[prc][res];
        this.al_matrix = new int[prc][res];
        this.need_matrix = new int[prc][res];

        this.avail = new int[res];
        this.marked = new boolean[prc];
        this.safe = new Vector<>();

        parseFile(file);

    }

    /**
     * 
     * @param minuend matrix to be subtracted from
     * @param subtrahend matrix to subtract
     * @param res_ct number of resources
     * @param prc_ct number of processes
     * @return difference of the matrices
     */
    static int[][] subtract(int[][] minuend, int[][] subtrahend, int res_ct, int prc_ct) {
        int i, j;
        int[][] difference = new int[prc_ct][res_ct];

        for (i = 0; i < prc_ct; i++)
            for (j = 0; j < res_ct; j++)
                difference[i][j] = minuend[i][j] - subtrahend[i][j];

        return difference;
    }

    /**
     * Parses the file line by line. The file should have resources as the columns and
     * processes as the row. The matrices should be in the order: Max, allotted, and then
     * available resources. Available is just a list.
     * @param file To be parsed
     */
    public void parseFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            int line_ct = 0; //which is the currently read line of the file
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] line_arr = line.trim().split("\\s+");

                //use a queue to parse the ints in order
                Queue<String> line_queue = new LinkedList<>(Arrays.asList(line_arr));
                //only the first line has the available resource list
                if (line_ct == 0) {
                    for (int i = 0; i < res_ct; i++) {
                        max_matrix[0][i] = Integer.parseInt(line_queue.remove());
                    }
                    for (int i = 0; i < res_ct; i++) {
                        al_matrix[0][i] = Integer.parseInt(line_queue.remove());
                    }
                    for (int i = 0; i < res_ct; i++) {
                        avail[i] = Integer.parseInt(line_queue.remove());
                    }
                } else { //for all other lines after the first
                    for (int i = 0; i < res_ct; i++) {
                        max_matrix[line_ct][i] = Integer.parseInt(line_queue.remove());
                    }
                    for (int i = 0; i < res_ct; i++) {
                        al_matrix[line_ct][i] = Integer.parseInt(line_queue.remove());
                    }
                }
                line_ct++;
            }//calculate the need matrix AFTER the other matrices are filled
            need_matrix = subtract(max_matrix, al_matrix, res_ct, prc_ct);

        } catch (FileNotFoundException e) {
            System.out.println("Issue parsing" + file);
        }

    }

    /**
     * Determines if a process can be allocated by comparing the resources of the process
     * (which is passed by index) against the available list.
     *
     * @param process_id the index of process
     * @param need the need matrix
     * @param available the available list
     * @return where or not the process can be allocated
     */
    private boolean is_available(int process_id, int[][] need, int[] available) {

        boolean is_avail = true;
        for (int i = 0; i < res_ct; i++) {

            if (need[process_id][i] > available[i]) {
                is_avail = false;
                break;
            }
        }
        return is_avail;
    }

    /**
     * Recursively finds all of the safe sequences of the processes with the given
     * resources.
     * 
     * @param visited array which holds which processes have been visited
     * @param available available resources
     * @param safe vector which will hold the working safe path
     */
    public void method(boolean[] visited, int[] available, Vector<Integer> safe) {

        for (int i = 0; i < prc_ct; i++) {

            if (!visited[i] && is_available(i, this.need_matrix, available)) {

                // mark the ith process and add it to the allocated matrix
                visited[i] = true;
                for (int j = 0; j < res_ct; j++) {
                    available[j] += this.al_matrix[i][j];
                }
                safe.add(i); //add to end of safe vector

                method(visited, available, safe); //recursively check others

                // undo for the next calling of this method
                safe.removeElementAt(safe.size() - 1);
                visited[i] = false;
                for (int j = 0; j < res_ct; j++) {
                    available[j] -= al_matrix[i][j];
                }
            }
        }

        // for when the vector is of appropriate size
        if (safe.size() == prc_ct) {
            for (int i = 0; i < prc_ct; i++) {
                System.out.print("P" + (safe.get(i) + 1)); //not zero indexing
                if (i != (prc_ct - 1)) {
                    System.out.print(" > ");
                }
            }
            System.out.println();
        }
    }

    public boolean[] getMarked() {
        return marked;
    }
    public int[] getAvail() {
        return avail;
    }
    public Vector<Integer> getSafe() {
        return safe;
    }
}


