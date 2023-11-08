import java.util.Scanner;

public class mRNAProcessing {
    public static void main(String[] args) {

          //////////////
         // Database //
        //////////////
        String[][] codonTable = {
            {"AUG"}, // Methionine (START)
            {"UUU", "UUC"}, // Phenylalanine
            {"UUA", "UUG", "CUU", "CUC", "CUA", "CUG"}, // Leucine
            {"UCU", "UCC", "UCA", "UCG", "AGU", "AGC"}, // Serine
            {"UAU", "UAC"}, // Tyrosine
            {"UGU", "UGC"}, // Cystosine
            {"CCU", "CCC", "CCA", "CCG"}, // Proline
            {"CAU", "CAC"}, // Histidine
            {"CAA", "CAG"}, // Glutamine
            {"CGU", "CGC", "CGA", "CGG", "AGA", "AGG"}, // Arginine
            {"AUU", "AUC", "AUA"}, // Isoleucine
            {"ACU", "ACC", "ACA", "ACG"}, // Threonine
            {"AAU", "AAC"}, // Asparagine
            {"AAA", "AAG"}, // Lysine
            {"GUU", "GUC", "GUA", "GUG"}, // Valine
            {"GCU", "GCC", "GCA", "GCG"}, // Alanine
            {"GAU", "GAC"}, // Aspartic Acid
            {"GAA", "GAG"}, // Glutamic Acid
            {"GGU", "GGC", "GGA", "GGG"}, // Glycine
            {"UGG"}, // Tryptophan
            {"UAA", "UAG", "UGA"} // STOP
        };

        String[] aminoAcidTable = {"Met", "Phe", "Leu", "Ser", "Tyr", "Cys", "Pro", "His", "Gln", 
            "Arg", "Ile", "Thr", "Asp", "Lys", "Val", "Ala", "Asp", "Glu", "Gly", "Try", "END"
        };


          /////////////////////////////////////
         // User Input Receiver and Storage //
        ////////////////////////////////////
        Scanner userInput = new Scanner(System.in);
        System.out.print("Please enter the 5' --> 3' mRNA: ");
        String mRNA = userInput.nextLine().toUpperCase();
        Boolean isValidForAminoAcidSequence = true;
        Boolean isValid = true;


         ///////////////////////////
        // mRNA Validity Checker //
       ///////////////////////////
       // Checks if codons of 3 and checks if there is only A, G, U, C, and no numbers or spaces 
        System.out.println(mRNA + "X");
        if ((mRNA.length() % 3 != 0)) {
            isValidForAminoAcidSequence = false;
        }
        for (int i = 0; i < mRNA.length(); i++) {
            char currChar = mRNA.charAt(i);
            if (Character.isDigit(currChar) || Character.isWhitespace(currChar) || (currChar != 'A' && currChar != 'G' && currChar != 'U' && currChar != 'C')) {
                isValid = false;
            }
        }
        if (!isValid) {
            System.out.print("Invalid mRNA sequence! (Number or non-nucleotide character in sequence)");
        }


          ///////////////////////
         // Program Selection //
        ///////////////////////
        else {
            System.out.println("Please select which program you would like to run: ");
            System.out.println("1 --> Amino Acid Sequence");
            System.out.println("2 --> PCR Primer Annealing Temperature");
            System.out.println("0 --> Stop Program");

            int programSelection = userInput.nextInt();
            if (programSelection == 1) {
                if (!isValidForAminoAcidSequence) {
                    System.out.println("MRNA sequence does not work for amino acid sequencing! (Not a multiple of 3)");
                }
                else {
                    System.out.println("The amino acid sequence is: ");
                    System.out.print(aminoAcidSequence(mRNA, codonTable, aminoAcidTable));
                }
            }
            else if (programSelection == 2) {
                System.out.print("The estimated annealing temperature is: ");
                System.out.println(annealingTempCalculator(mRNA) + " C");
            }
        }
        userInput.close();
    }


    public static String aminoAcidSequence(String mRNA, String[][] codonTable, String[] aminoAcidTable) {
        String aminoAcidSequence = "";
        String currCodon;
        Boolean met = false;

        for (int i = 0; i < mRNA.length() - 2; i++) {
            if (i < mRNA.length() - 2) {
                currCodon = mRNA.substring(i, i+3);
            }
            else {
                currCodon = mRNA.substring(i);
            }
            for (int j = 0; j < codonTable.length; j++) {
                for (int k = 0; k < codonTable[j].length; k++) {
                    if (currCodon.equals(codonTable[j][k])) {
                        if(currCodon.equals("AUG")) {
                            met = true;
                        }
                        else if (j == 20) {
                            met = false;
                        }
                        if(met) {
                            aminoAcidSequence += aminoAcidTable[j] + " - ";
                        }
                    }
                }
            }
            i++;
            i++;
        }
        return aminoAcidSequence.substring(0, aminoAcidSequence.length()-2);   
    }


    public static int annealingTempCalculator(String mRNA) {
        int annealingTemp = 0;
        for (int i = 0; i < mRNA.length(); i++) {
            char currNucleotide = mRNA.charAt(i);
            if (currNucleotide == 'G' || currNucleotide == 'C') {
                annealingTemp += 4;
            }
            else {
                annealingTemp += 2;
            }
        }
        return annealingTemp;
    }
}