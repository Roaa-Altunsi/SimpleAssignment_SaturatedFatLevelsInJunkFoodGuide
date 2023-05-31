/*
    * Name: Roaa Hatim Altunsi
    * Id: 1914946
    * Section: IAR
    * Course: CPCS203 
 */
package assignment1;

import java.util.Scanner;
import java.util.Date;
import java.io.*;
public class Project_1_Roaa_Altunsi_1914946 {
    
    static int lowIn1 = 0, lowIn2 = 0, lowIn3 = 0;
    static int highIn1 = 0, highIn2 = 0, highIn3 = 0;
    
    public static void main(String[] args) throws Exception{
        
        // Create the File objects 
        File inputF = new File("input.txt");
        File outputF = new File("output.txt");
        if(!inputF.exists()){ // Make sure the file exist
            System.out.println("The file doesn't exist.\nCheck in " + inputF.getAbsolutePath());
            System.exit(0);
        }
        Scanner read = new Scanner(inputF);
        PrintWriter write = new PrintWriter(outputF);
        
        // Start reading from the file and declare the Arrays
        String[] category = new String[read.nextInt()];
        String[][] groups = new String[category.length][];
        String[][][] braPro = new String[category.length][][];
        double[][][] sFat = new double[category.length][][];
        
        for(int i=0; i<category.length; i++){
            groups[i] = new String[read.nextInt()];
            braPro[i] = new String[groups[i].length][];
            sFat[i] = new double[groups[i].length][];
        }
        
        write.println("\n##################################################################");
        write.println("#             Saturated Fat Levels in Junk Food Guide            #");
        write.println("##################################################################");
        write.println("\n- Number of category types: " + category.length);
        
        // Add and Print Categories items
        if(read.next().equals("add_fastfoodcategories")){
            write.println("\n- Command: add_fastfoodcategories");
            String[] readLine = read.nextLine().trim().split(" ");
            for(int i=0; i<category.length; i++){
                category[i] = readLine[i];
                write.printf("\t+ %s", category[i]);
            }
        }
        
        // Add and Print Gropus items
        while(read.hasNext("add_groups")){
            read.next();
            int indexCat = getCategoryIndex(read.next());
            write.println("\n\n- Command: add_groups");
            write.println("	-> Category: " + category[indexCat]);
            String[] readLine = read.nextLine().trim().split(" ");
                for(int i=0; i<groups[indexCat].length; i++){
                    groups[indexCat][i] = readLine[i];
                    write.printf("\t+ %s", groups[indexCat][i]);
                }
        }
        
        // Add and Print Products items
        while(read.hasNext("add_products")){
            read.next();
            int indexCat = getCategoryIndex(read.next());
            int indexGro = getGroupIndex(read.next());
            int num = read.nextInt();
            
            braPro[indexCat][indexGro] = new String[num];
            sFat[indexCat][indexGro] = new double[num];
            write.println("\n\n- Command: add_product");
            write.println("	-> Category: " + category[indexCat]);
            write.println("	-> Group: " + groups[indexCat][indexGro]);
            write.println("	-> Number of products: " + num);
            write.println("    ----------------------------------------------------------------------------------------");
            write.println("	Sr #      	Brand     		Product   		SF (per 100g) ");
 
            for(int i=0; i<num; i++){
                braPro[indexCat][indexGro][i] = read.next();
                sFat[indexCat][indexGro][i] = read.nextDouble();
                
                String[] s = braPro[indexCat][indexGro][i].split("_");
                write.printf("\t%-16s%-24s%-24s%1.2f\n", s[2], s[1], s[0], sFat[indexCat][indexGro][i]);
            }
            write.print("    ----------------------------------------------------------------------------------------");    
        }
        
        double currentH_Sfat = sFat[0][0][0], currentL_Sfat = sFat[0][0][0]; //To find highest/lowest saturated fat in all categories
        
        // Print result
        while(read.hasNext("print_result")){
            read.next();
            int indexCat = getCategoryIndex(read.next());
            int indexGro = getGroupIndex(read.next());
            int num = braPro[indexCat][indexGro].length;
            double sumsFat = 0;
            double highCategory = 0, lowCategory = 100; //To find highest/lowest saturated fat in the same Category&Group
            String s1 = ""; // Used to store product information for High Category
            String s2 = ""; // Used to store product information for Low Category
            
            write.println("\n\n- Command: print_result");
            write.println("	-> Category: " + category[indexCat]);
            write.println("	-> Group: " + groups[indexCat][indexGro]);
            write.println("	-> Number of products: " + num);
            write.println("    -------------------------------------------------------------------------------------------------------");
            write.println("	Sr #      	Brand     		Product   		SF Level	Status");
            write.println("    -------------------------------------------------------------------------------------------------------");
            
            for(int i=0; i<num; i++){
                String[] s = braPro[indexCat][indexGro][i].split("_");
                String statusResult = status(sFat[indexCat][indexGro][i]);
                sumsFat += sFat[indexCat][indexGro][i];
                
                // Find the maximum and minimum saturated fat in the same Category&Group
                if(sFat[indexCat][indexGro][i] > highCategory){
                    highCategory = sFat[indexCat][indexGro][i];
                    s1 = "" + s[1] + " " + s[2] + " (" + s[0] + ")";
                }
                if(sFat[indexCat][indexGro][i] < lowCategory){
                    lowCategory = sFat[indexCat][indexGro][i];
                    s2 = "" + s[1] + " " + s[2] + " (" + s[0] + ")"; 
                }
                
                // Find the maximum and minimum saturated fat in all categories
                if(sFat[indexCat][indexGro][i] > currentH_Sfat){
                    currentH_Sfat = sFat[indexCat][indexGro][i];
                    highIn1 = indexCat; highIn2 = indexGro; highIn3 = i;
                }
                if(sFat[indexCat][indexGro][i] < currentL_Sfat){
                    currentL_Sfat = sFat[indexCat][indexGro][i];
                    lowIn1 = indexCat; lowIn2 = indexGro; lowIn3 = i;
                }
                
                write.printf("\t%-16s%-24s%-24s%-16.2f%-5s\n", s[2], s[1], s[0], sFat[indexCat][indexGro][i], statusResult);
            } 
            
            write.print("	-------------------------------------------------------------------------------------------------------");
            write.printf("%s%1.2f","\n	* The average SFLevel is ", (sumsFat/num));
            write.printf("%s%10s","\n	* The high category is ", s1);
            write.printf("%s%10s","\n	* The low category is ", s2);
        }
        
        // The Average sfLevel
        if(read.next().equals("find_average_sflevel")){
            write.println("\n\n- Command: find_average_sflevel");
            write.printf("%s%1.2f","	* The average SFLevel for all categories in all category types is ", averageSfLevel(sFat));
        }
        
        // The highest sfLevel
        if(read.next().equals("find_highest_sflevel")){
            write.println("\n\n- Command:  find_high_sflevel");
            write.println("	* The high category sflevel in all category types is:");
            write.println("	-------------------------------------------------------------------------------------------------------");
            write.println("	Sr #      	Brand     		Product   		SF (per 100g)	Status");
            write.println("	-------------------------------------------------------------------------------------------------------");
            String[] s1 = braPro[highIn1][highIn2][highIn3].split("_");
            write.printf("\t%-16s%-24s%-24s%-16.2f%-5s\n", s1[2], s1[1], s1[0], sFat[highIn1][highIn2][highIn3], 
                    status(sFat[highIn1][highIn2][highIn3]));
            write.println("	-------------------------------------------------------------------------------------------------------");
            write.print("	In Category: " + category[highIn1] + ", Group: " + groups[highIn1][highIn2]);
        }
        
        // The Lowest sfLevel
        if(read.hasNext("find_lowest_sflevel")){
            read.next();
            /* This is the code for print the Lowest sfLevel, 
            I write it as comment because it doesn't exist in the output File*/
               
            /*write.println("\n\n- Command:  find_lowest_sflevel");
            write.println("	* The lowest category sflevel in all category types is:");
            write.println("	-------------------------------------------------------------------------------------------------------");
            write.println("	Sr #      	Brand     		Product   		SF (per 100g)	Status");
            write.println("	-------------------------------------------------------------------------------------------------------");
            String[] s1 = braPro[lowIn1][lowIn2][lowIn3].split("_");
            write.printf("\t%-16s%-24s%-24s%-16.2f%-5s\n", s1[2], s1[1], s1[0], sFat[lowIn1][lowIn2][lowIn3], 
                    status(sFat[lowIn1][lowIn2][lowIn3]));
            write.println("	-------------------------------------------------------------------------------------------------------");
            write.print("	In Category: " + category[lowIn1] + ", Group: " + groups[lowIn1][lowIn2]);*/
        }
        
        // About Guide
        if(read.next().equals("about_guide"))
            write.println("\n\n- Command: about_guide\n	-> Developed By: Roaa Hatim Altunsi\n" +
                    "	-> University ID: 1914946\n	-> Section: IAR");
        
        // Exist
        if(read.next().equals("exit")){
            Date theDate = new Date();
            write.print("\nThank You! :)\nReport generated on " + theDate);
            write.flush();
            write.close();
        }
    }
    
    public static int getCategoryIndex(String s){   // Return the index of category
        switch(s){
            case "Fast-Food": 
                return 0;
            case "Processed-Meat": 
                return 1;
            case "Condiments": 
                return 2;
            default: 
                return -1;
        }
    }
    
    public static int getGroupIndex(String s){  // Return the index of Group
        switch(s){
            case "Pizza":
            case "Sausage":
            case "Mayonnaise": return 0;
            case "Burger":
            case "SmokedMeat":
            case "BBQSauce": return 1;
            case "Broast": return 2;
            default: return -1;
        }
    }
    
    public static String status(double sFat){   // Check if the saturated fat is Hight/Low/Normal
        if(sFat > 5)
            return "High";
        else if(sFat == 5)
            return "Normal";
        else
            return "Low";
    }
    
    public static double averageSfLevel(double[][][] sFat){ // Calculate the average saturated fat for all items
        double sum = 0;
        int count = 0;
        for (double[][] sFat1 : sFat) {
            for (double[] sFat11 : sFat1) {
                for (int k = 0; k < sFat11.length; k++) {
                    sum += sFat11[k];
                    count++;
                }
            }
        }
        return sum / count;
    }
    
}


