import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class enzymePathwayCalculator {
    public static void main(String[] args) throws InputMismatchException {

        ////////////////////////////////////////////////////////////
        // Pre-Requisite Data Collection and Variable Declaration //
        ////////////////////////////////////////////////////////////
        Scanner userInput = new Scanner(System.in);
        System.out.print("Input the number of enzymes present in the pathway: ");
        
        int numberOfEnzymesInPath = userInput.nextInt();
        String[][][] enzymesAndProperties = new String[numberOfEnzymesInPath][][];

        System.out.print("Input the number of essential metabolic products: ");

        int numberOfEssentialProducts = userInput.nextInt();
        String[] essentials = new String[numberOfEssentialProducts];

        for (int i = 0; i < numberOfEssentialProducts; i++) {
            System.out.print("Input essential product number " + (i + 1) + ": ");
            essentials[i] = userInput.next();
        }

        System.out.print("Are there any lethal intermediates (Y/N): ");
        String lethalOrNoLethal = userInput.next();
        String lethalIntermediate = "";

        if (lethalOrNoLethal.equals("Y")) {
            System.out.print("Input the lethal intermediate: ");
            lethalIntermediate = userInput.next();
        }

        ArrayList<String> allProducts = new ArrayList<>();


        ////////////////////////////
        // Data Collection Driver // (Took 1 Day)
        ////////////////////////////
        for (int i = 0; i < numberOfEnzymesInPath; i++) {
            System.out.print("Input the number of times enzyme " + (i + 1) + " is present: ");
            int instancesOfEnzyme = userInput.nextInt();
            enzymesAndProperties[i] = new String[instancesOfEnzyme][2];
            for (int j = 0; j < instancesOfEnzyme; j++) {
                System.out.print("Input the metabolic product it uses and the product it produces (X Y): ");
                String pointer = enzymesAndProperties[i][j][0] = userInput.next();
                String pointed = enzymesAndProperties[i][j][1] = userInput.next();
                if (!allProducts.contains(pointer)) {
                    allProducts.add(pointer);
                }
                if (!allProducts.contains(pointed)) {
                    allProducts.add(pointed);
                }
            }
        }

        // Print the Pathway //
        printEnzymeArray(enzymesAndProperties);

        // Mutation Type Picker //
        System.out.print("Select the simulation of a mutant(1) or double mutant(2): ");
        int mutationSelection = userInput.nextInt();

        // Simulation Type Picker //
        System.out.print("Simulate all mutants in all possible mediums(1) or simulate something specific(2): ");
        int runAllOrSpecific = userInput.nextInt();


        // If else to pick between single or double mutant
        //////////////////////////////////////
        // Single Mutation Simulator Driver //
        //////////////////////////////////////
        if (mutationSelection == 1) {
            // If else to simulate all possible or specific
            if (runAllOrSpecific == 1) {
                simulateAllSingle(enzymesAndProperties, essentials, allProducts, lethalIntermediate);
            }
            else {
                specificSimulatorSingle(userInput, enzymesAndProperties, essentials, lethalIntermediate); 
            }
        }

        //////////////////////////////////////
        // Double Mutation Simulator Driver //
        //////////////////////////////////////
        else if (mutationSelection == 2){

        }
        userInput.close();
    }


    ///////////////////////////////////
    // Visual Enzyme Pathway Printer //
    ///////////////////////////////////
    public static void printEnzymeArray (String[][][] enzymesAndProperties) {
        for (int i = 0; i < enzymesAndProperties.length; i++) {
            System.out.print("Enzyme " + (i + 1) + ": ");
            for (int j = 0; j < enzymesAndProperties[i].length; j++) {
                System.out.print("{" + enzymesAndProperties[i][j][0] + " -> " + enzymesAndProperties[i][j][1] + "} ");
            }
            System.out.println();
        }
    }


    /////////////////////////
    // Specific Simulation //
    /////////////////////////
    public static void specificSimulatorSingle (Scanner userInput, String[][][]enzymesAndProperties, String[] essentials, String lethalIntermediate) {
        int moreCheck = 1;
        // While loop to ask for multiple prompts if the user wants to try different combos
        while (moreCheck == 1) {
            System.out.print("Input the mutated enzyme number: ");
            int mutatedEnzyme = userInput.nextInt() - 1; // Convert to 0-based index

            // Collect further neccesary data about supplements 
            System.out.print("Input how many metabolic products are supplemented: ");
            int numberOfSupplements = userInput.nextInt();
            String[] supplements = new String[numberOfSupplements];
            for (int i = 0; i < numberOfSupplements; i++) {
                System.out.print("Input supplement number " + (i+1) + ": ");
                supplements[i] = userInput.next();
            }
            
            boolean grows = growthChecker(mutatedEnzyme, enzymesAndProperties, essentials, supplements, lethalIntermediate);
            
            System.out.println();
            if (grows) {
                System.out.println("Colony will grow.");
            }
            else {
                System.out.println("Colony will not grow");
            }
            System.out.println();

            System.out.print("Do you want to check another combination? (1 Yes) (0 No) ");
            moreCheck = userInput.nextInt();
        }
    }


    ///////////////////////////////
    // Simulate All Combinations //
    ///////////////////////////////
    public static void simulateAllSingle (String[][][] enzymesAndProperties, String[] essentials, ArrayList<String> allProducts, String lethalIntermediate) {
        // Print headers
        System.out.print("Supplement: ");
        String currentSupplement;
        for (int i = 0; i < allProducts.size(); i++) {
            currentSupplement = allProducts.get(i);
            System.out.print(currentSupplement + " ");
        }
        System.out.println();
        // Simulation cycler and printer
        for (int i = 0; i < enzymesAndProperties.length; i++) {
            int currentMutant = i;
            System.out.print("Mutant " + (i+1) + ":   ");
            for (int j = 0; j < allProducts.size(); j++) {
                currentSupplement = allProducts.get(j);
                String[] supplements = {currentSupplement};
                boolean grows = growthChecker(currentMutant, enzymesAndProperties, essentials, supplements, lethalIntermediate);
                if (grows) {
                    System.out.print("+ ");
                }
                else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }


    //////////////////////////////
    // Growth Checker Algorithm // (Took 1 Day)
    //////////////////////////////
    public static Boolean growthChecker(int mutatedEnzyme, String[][][] enzymesAndProperties, String[] essentials, String[] supplements, String lethalIntermediate) {
        Boolean grows = true;

        // Create two lists to store reachable products
        ArrayList<String> reachableProducts = new ArrayList<>();
        ArrayList<String> actuallyReachableProducts = new ArrayList<>();

        // Add the supplemented product(s) to the reachable lists
        for (int i = 0; i < supplements.length; i++) {
            reachableProducts.add(supplements[i]);
            actuallyReachableProducts.add(supplements[i]);
        }
        // Add the initial metabolic product to the reachable lists
        reachableProducts.add("A");
        actuallyReachableProducts.add("A");

        // Reachable Products Collection //
        // Initial traversal, gets reachable not considering pointer pre-requisite
        // Traverses different enzymes
        for (int i = 0; i < enzymesAndProperties.length; i++) {
            // Dont traverse if mutated
            if (i != mutatedEnzyme){
                // Traverses different pathways
                for (int j = 0; j < enzymesAndProperties[i].length; j++) {
                    String pointed = enzymesAndProperties[i][j][1];
                    reachableProducts.add(pointed);
                }
            }
        }

        // Second traversal, gets reachable considering pre-requisite needs //
        //Traverses different enzymes
        for (int i = 0; i < enzymesAndProperties.length; i++) {
            // Dont traverse if mutated
            if (i != mutatedEnzyme) {
                // Traverses different pathways
                for (int j = 0; j < enzymesAndProperties[i].length; j++) {
                    String pointer = enzymesAndProperties[i][j][0];
                    String pointed = enzymesAndProperties[i][j][1];
                    if (reachableProducts.contains(pointer)) {
                        actuallyReachableProducts.add(pointed);
                    }
                }
            }
        }

         // Essentials Checker //
        for (int i = 0; i < essentials.length; i++) {
            String currEssential = essentials[i];
            if (!actuallyReachableProducts.contains(currEssential)) {
                grows = false;
                break;
            }
        }

        // Unmetabolized Toxic Checker //
        ArrayList<String> metabolized = new ArrayList<>();
        for (int i = 0; i < enzymesAndProperties.length; i++) {
            if (i != mutatedEnzyme) {
                for (int j = 0; j < enzymesAndProperties[i].length; j++) {
                    metabolized.add(enzymesAndProperties[i][j][0]);
                }
            }
        }
        if (!metabolized.contains(lethalIntermediate) && lethalIntermediate.length() > 0) {
            grows = false;
        }
        
        return grows;
    }
}