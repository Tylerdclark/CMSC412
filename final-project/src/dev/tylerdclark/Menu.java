/* FILE: Menu.java
 * AUTHOR: Tyler Clark
 * DATE: 07 May 2021
 */

package dev.tylerdclark;

import java.io.IOException;
import java.util.*;

/**
 * Menu class that will drive the program. Contains a loop and a {@link Scanner} to take input and interact with the
 * user.
 */
public class Menu {

    private final Scanner scanner;
    private boolean keepGoing;
    private int[] referenceString;
    private int[][] matrix;
    private int physicalFrames;
    private final static int MIN_PHY_FRAME = 1;
    private final static int MAX_PHY_FRAME = 8;
    private final static int MIN_VIRT_FRAME = 0;
    private final static int MAX_VIRT_FRAME = 9;


    /**
     * Constructor that will create an instance of the {@link Menu} class. Initializes a {@link Scanner} and the variable
     * that the main menu loop uses. The user will input integers corresponding to menu choices with this object.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        keepGoing = true;
        referenceString = null;
    }

    /**
     * commences the main loop and gets user input using {@link #userChoice()}
     */
    public void run() {
        while(keepGoing){
            System.out.println("*** Final Project - Demand Paging Simulator *** \nSelect from the following options:" +
                    "\n0 - Exit \n1 - Read reference string \n2 - Generate reference string \n3 - Display current " +
                    "reference string \n4 - Simulate FIFO \n5 - Simulate OPT \n6 - Simulate LRU \n7 - Simulate LFU"
            );

            keepGoing = userChoice();
        }
    }

    /**
     * Using a {@link Scanner}, this method collects the user input and calls the method according to the integer given.
     * If the user gives something other than the valid options (0-7) a warning message will be displayed.
     *
     * @return whether or not to continue the program based on the user input
     */
    private boolean userChoice(){
        try{
            switch (scanner.nextInt()){
                case 0:
                    return false;
                case 1:
                    readRefString();
                    break;
                case 2:
                    generateReferenceString();
                    break;
                case 3:
                    showReferenceString();
                    break;
                case 4:
                    simulateFIFO();
                    break;
                case 5:
                    simulateOPT();
                    break;
                case 6:
                    simulateLRU();
                    break;
                case 7:
                    simulateLFU();
                    break;
                default:
                    System.err.println("Please use 0-7");
            }
            
        }catch (InputMismatchException exception){
            System.err.println("Please use 0-7");
            scanner.next(); // consume the current token
            return true;
        }
        return true;
    }

    /**
     * Prompts the user for a reference string and collects it using {@link Scanner}. This method then goes on to
     * ensure the numbers passed are valid and within {@link #MIN_VIRT_FRAME} and {@link #MAX_VIRT_FRAME}. If everything
     * is good, the reference string is stored in {@link #referenceString} as an int array.
     *
     * Also asks for physical frames and checks if they are within {@link #MIN_PHY_FRAME} and {@link #MAX_PHY_FRAME}.
     */
    private void readRefString() {

        String userInput = null;
        System.out.println("Please enter a reference string");
        try {
            scanner.nextLine();
            userInput = scanner.nextLine();
        }catch (NoSuchElementException exception){
            System.out.println("You must enter something!");
            exception.printStackTrace();
        }
        if(userInput != null){
            String[] refStringArr = userInput.split("\\s+");
            referenceString = new int[refStringArr.length];
            try{
                for (int i = 0; i < refStringArr.length; i++) {
                    int tempInt  = Integer.parseInt(refStringArr[i]);
                    if (tempInt >= MIN_VIRT_FRAME && tempInt <= MAX_VIRT_FRAME){
                        referenceString[i] = tempInt;
                    }else{
                        referenceString[i] = -1; // -1 for any bad values
                    }

                } //check if any are bad
                if (Arrays.stream(referenceString).anyMatch( (e)-> e ==-1)){
                    referenceString = null;
                    System.err.println("Please use ten frames for the reference string numbered 0-9");
                    return;
                }

                // Do the physical frames
                System.out.println("How many physical frames? (between 1 and 8)");
                try {
                    physicalFrames = scanner.nextInt();
                }catch (InputMismatchException exception){
                    System.err.println("Frames must be an Integer");
                    return;
                }

                if (physicalFrames <= MIN_PHY_FRAME || physicalFrames > MAX_PHY_FRAME){
                    System.err.println("Physical frames must be between 1 and 8");
                    physicalFrames = 0;
                    return;
                }

            }catch (NumberFormatException exception){
                System.err.println("Trouble parsing Integers from reference");
            }

            System.out.println(Arrays.toString(referenceString));
            System.out.println("Was added as the reference string.");
        }
    }

    /**
     * Creates a random reference string with physical frames
     */
    private void generateReferenceString() {
        Random random = new Random();

        System.out.println("What is the length of the new reference string?");
        int newRefStringLength;

        try {
            newRefStringLength = scanner.nextInt();
        }catch (InputMismatchException exception){
            System.err.println("Reference string must be an Integer.");
            return;
        }
        if (newRefStringLength < 1){
            System.out.println("Length needs to be greater than 1");
        }else{
            referenceString = new int[newRefStringLength];
            for (int i = 0; i < newRefStringLength-1; i++) {
                referenceString[i] = random.nextInt(10);
            }

            System.out.println("How many physical frames (between 1 and 8)");
            int response;
            try {
                response = scanner.nextInt();
            }catch (InputMismatchException exception){
                System.err.println("Frames must be an Integer");
                return;
            }
            if (response >= MIN_PHY_FRAME && response <= MAX_PHY_FRAME){
                physicalFrames = response;
                System.out.println("New reference string:");
                System.out.println(Arrays.toString(referenceString));
                System.out.println("Physical frame count:");
                System.out.println(physicalFrames);
            }else {
                System.err.println("Physical frames must be between 1 and 8");
                physicalFrames = 0;
            }
        }
    }

    /**
     * Displays the stored reference string if it is stored or suggests the user to create one with options 1 or 2
     * otherwise.
     */
    private void showReferenceString() {
        if (referenceString != null){
            System.out.println("The current reference string is :");
            System.out.println(Arrays.toString(referenceString));
        }else{
            System.err.println("No current reference string! Create one with option 1 or 2");
        }

        if (physicalFrames == 0){
            System.err.println("You should add a physical frame count using option 1 or 2 for this program to operate " +
                    "correctly. The number should be between 1 and 8");
        }else{
            System.out.println("Physical frame count:");
            System.out.println(physicalFrames);
        }
    }

    /**
     * First ensures that the simulation is properly set up before running through a first in first out simulation.
     * After each step, the method prompts the user for a key in order to continue using the
     * {@link #pressAnyKeyToContinue()}
     */
    private void simulateFIFO(){
        if (setupSimulation()){

            ArrayList<Integer> memory = new ArrayList<>(physicalFrames);
            int faultCt = 0;
            int currentFrame = 0;

            printMatrix();
            pressAnyKeyToContinue();
            for (int i = 0; i < referenceString.length; ++i) {
                int currentVirtFrame = referenceString[i];
                if (!memory.contains(currentVirtFrame)){
                    if(memory.size() < physicalFrames){
                        memory.add(currentFrame,currentVirtFrame);
                        //set the corresponding fault row to true
                    }else{ //memory is full
                        if(currentFrame >=physicalFrames){
                            currentFrame = 0;
                        }

                        //make the 0th element in memory the victim
                        matrix[physicalFrames+1][i] = memory.get(currentFrame);
                        memory.set(currentFrame, referenceString[i]);
                        //set the corresponding fault row to true
                    }
                    ++currentFrame;
                    matrix[physicalFrames+2][i] = 1;
                    faultCt++;
                }else{
                    System.out.println(currentVirtFrame+ " in memory.");
                }

                for (int j = 0; j < memory.size(); j++) {
                    matrix[j+1][i] = memory.get(j);
                }
                System.out.println("Memory: "+memory);
                printMatrix();
                pressAnyKeyToContinue();
            }
            System.out.println("Table finished.");
            System.out.println("Faults: "+ faultCt);
        }
    }

    /**
     * First ensures that the simulation is properly set up before running through an optimal simulation.
     * After each step, the method prompts the user for a key in order to continue using the
     * {@link #pressAnyKeyToContinue()}
     */
    private void simulateOPT() {

        if(setupSimulation()){
            ArrayList<Integer> memory = new ArrayList<>(physicalFrames);
            ArrayList<Integer> reference = new ArrayList<>();

            for (int element: referenceString) {
                reference.add(element);
            }

            System.out.println(reference);

            int faultCt = 0;
            int currentFrame = 0;
            int max = -1;
            int index;
            int victim = -1;

            printMatrix();
            pressAnyKeyToContinue();
            for (int i = 0; i < referenceString.length; ++i) {
                int currentVirtFrame = referenceString[i];
                if (!memory.contains(currentVirtFrame)){
                    if(memory.size() < physicalFrames){
                        memory.add(currentFrame,currentVirtFrame);
                        reference.remove((Integer) referenceString[i]);
                        ++currentFrame;
                        faultCt++;

                    }else{ //memory is full
                        faultCt++;
                        int temp = reference.get(0);
                        reference.remove(0);

                        for (int element : memory) {
                            index = reference.indexOf(element);

                            if (index == -1) {
                                victim = element;
                                break;
                            }

                            if (index > max) {
                                victim = element;
                                max = index;
                            }
                        }

                        memory.set(memory.indexOf(victim), temp); // Swap
                        max = -1;  // Reset max
                        matrix[physicalFrames+1][i] = victim;

                    }//mark the fault row
                    matrix[physicalFrames+2][i] = 1;

                }else{
                    reference.remove(0);
                    System.out.println(currentVirtFrame+ " in memory.");
                }

                for (int j = 0; j < memory.size(); j++) {
                    matrix[j+1][i] = memory.get(j);
                }
                System.out.println("Memory: "+memory);
                printMatrix();
                pressAnyKeyToContinue();
            }
            System.out.println("Table finished.");
            System.out.println("Faults: "+ faultCt);

        }
    }
    /**
     * First ensures that the simulation is properly set up before running through an least recently used simulation.
     * After each step, the method prompts the user for a key in order to continue using the
     * {@link #pressAnyKeyToContinue()}
     */
    private void simulateLRU(){
        if (setupSimulation()){
            ArrayList<Integer> memory = new ArrayList<>(physicalFrames);
            int[] lruCount = new int[physicalFrames];

            int faultCt = 0;
            int currentFrame = 0;
            int max, index, victim;

            printMatrix();
            pressAnyKeyToContinue();
            for (int i = 0; i < referenceString.length; ++i) {
                int currentVirtFrame = referenceString[i];
                if (!memory.contains(currentVirtFrame)){
                    if(memory.size() < physicalFrames){
                        memory.add(currentFrame,currentVirtFrame);
                        for (int j = 0; j < lruCount.length; j++) {
                            lruCount[j]++;
                        }
                        lruCount[currentFrame] = 1; // Reset current back to 1
                        ++currentFrame;
                        faultCt++;

                    }else{ //memory is full

                        faultCt++;
                        index = 0;
                        max = -1;  // Reset max

                        // Find max lruCount
                        for (int j = 0; j < lruCount.length; ++j) {
                            if (lruCount[j] > max) {
                                max = lruCount[j];
                                index = j;
                            }
                        }
                        victim = memory.get(index);
                        memory.set(index, referenceString[i]);
                        for (int j = 0; j < lruCount.length; ++j) {
                            lruCount[j]++;
                        }
                        lruCount[memory.indexOf(referenceString[i])] = 1;

                        matrix[physicalFrames+1][i] = victim;

                    }//mark the fault row
                    matrix[physicalFrames+2][i] = 1;

                }else{
                    System.out.println(currentVirtFrame+ " in memory.");
                    for (int j = 0; j < lruCount.length; ++j) {
                        lruCount[j]++;
                    }
                    lruCount[memory.indexOf(referenceString[i])] = 1;
                }

                for (int j = 0; j < memory.size(); j++) {
                    matrix[j+1][i] = memory.get(j);
                }
                System.out.println("Memory: "+memory);
                printMatrix();
                pressAnyKeyToContinue();
            }
            System.out.println("Table finished.");
            System.out.println("Faults: "+ faultCt);
        }
    }
    /**
     * First ensures that the simulation is properly set up before running through an least frequently used simulation.
     * After each step, the method prompts the user for a key in order to continue using the
     * {@link #pressAnyKeyToContinue()}
     */
    private void simulateLFU(){
        if(setupSimulation()){
            ArrayList<Integer> memory = new ArrayList<>(physicalFrames);
            HashMap<Integer, Integer> lfuCount = new HashMap<>();

            int victim;
            int currentFrame = 0;
            int faultCt = 0;
            int min, index, count;

            printMatrix();
            pressAnyKeyToContinue();

            for (int i = 0; i < referenceString.length; ++i) {
                int currentVirtFrame = referenceString[i];
                if (!memory.contains(currentVirtFrame)){
                    if(memory.size() < physicalFrames){
                        memory.add(currentFrame,currentVirtFrame);
                        lfuCount.put(referenceString[i], 1);
                        ++currentFrame;
                        faultCt++;

                    }else{ //memory is full
                        min = lfuCount.get(memory.get(0));
                        faultCt++;
                        index = 0;

                        // Find lowest freq frame in phy memory
                        for (int j = 0; j < memory.size(); ++j) {
                            if (lfuCount.get(memory.get(j)) < min) {
                                min = lfuCount.get(memory.get(j));
                                index = j;
                            }
                        }
                        victim = memory.get(index);
                        memory.set(index, referenceString[i]);

                        // Update LFU Freq map on new entry.
                        if (lfuCount.containsKey(referenceString[i])) {
                            count = lfuCount.get(referenceString[i]);
                            count++;
                            lfuCount.put(referenceString[i], count);
                        } else {
                            lfuCount.put(referenceString[i], 1);
                        }
                        matrix[physicalFrames+1][i] = victim;

                    }//mark the fault row
                    matrix[physicalFrames+2][i] = 1;

                }else{
                    System.out.println(currentVirtFrame+ " in memory.");
                    count = lfuCount.get(referenceString[i]);
                    count++;
                    lfuCount.put(referenceString[i], count); // Update frequency map
                }

                for (int j = 0; j < memory.size(); j++) {
                    matrix[j+1][i] = memory.get(j);
                }
                System.out.println("Memory: "+memory);
                printMatrix();
                pressAnyKeyToContinue();
            }
            System.out.println("Table finished.");
            System.out.println("Faults: "+ faultCt);
        }
    }


    /**
     * First checks for the proper requirements for the simulations before setting up the matrix. Fills the matrix with
     * -1's except for the top row which will contain the reference string. Uses {@link #matrix} for all of the
     * simulations.
     *
     * @return Whether or not the proper requirements for the simulations have been fulfilled.
     */
    private boolean setupSimulation(){
        if (referenceString == null || physicalFrames == 0){
            System.out.println("You need to have a valid reference string and set a physical frame between 1 and 8");
            return false;
        }else{ // +2 because of the victim row and page fault row
            matrix = new int[physicalFrames+3][referenceString.length];
            for (int[] row: matrix){
                Arrays.fill(row, -1); //fill with -1's
            }//fill the first row with the reference string
            System.arraycopy(referenceString, 0, matrix[0], 0, referenceString.length);
            return true;
        }
    }

    /**
     * Formats {@link #matrix} with row and column titles and displays the contents of the cells IF they
     * do not contain -1. -1 essentially means nothing is there, since 0 is a valid value in this program.
     * This method will never be called unless {@link #setupSimulation()} has returned true.
     */
    public void printMatrix(){ //add 3 for header row, victim row and fault row
        int rowCount = physicalFrames + 3;

        // for each row
        for (int i = 0; i < rowCount; i++) {

            if (i == 0){ //top row
                System.out.print("Reference String:\t");
            }else if (i == rowCount - 1){ //second to last row
                System.out.print("Page Faults:\t\t");
            }else if(i == rowCount - 2){ //last row
                System.out.print("Victim frames\t\t");
            } else { //all other rows
                System.out.print("Physical Frame "+ (i-1)+ ":\t");
            }

            // for each column
            for (int j = 0; j < referenceString.length; j++) {
                //if it is -1, just print a empty space
                if(i==rowCount-1){
                    //If there is a 1 in the fault row, print a F
                    System.out.print((matrix[i][j] == 1) ? "F\t" : "" + "\t");
                }else{
                    //If there is a -1, print nothing, else the contents
                    System.out.print((matrix[i][j] == -1) ? "\t" : matrix[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * Utility method to pause the program until the user presses enter.
     */
    private void pressAnyKeyToContinue()
    {
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
        }
        catch(IOException e){
            System.err.println("IO problem with System.in.read()");
        }
    }
}
