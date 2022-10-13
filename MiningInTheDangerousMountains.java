//Bruno Chanan

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class MiningInTheDangerousMountains  {
    static String[][] dangerousMountains;
    static int size;
    static String[][] memory1;
    static String[][][] memory2;
    static ArrayList<String> path1 = new ArrayList<String>();
    static ArrayList<String> path2 = new ArrayList<String>();

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);

        size = Integer.parseInt(scanner.nextLine());
        dangerousMountains = new String[size][size];
        memory1 = new String[size][size];
        startMemory(memory1);
        memory2 = new String[size][size][2];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dangerousMountains[i][j] = String.valueOf(scanner.next());
            }
        }
        scanner.close();

        // comment the line bellow if running tests with a matrix bigger than 10x10
        System.out.println("Silly recursion: " + recNoMem(size - 1, 0) + " gold");

        System.out.println("Improved recursion: " + recMem(size - 1, 0) + " gold");
        findPath();
        System.out.println("Improved recursion path: " + path1.toString());

        System.out.println("Without recursion: " + withoutRec() + " gold");
        System.out.println("Without recursion path: " + path2.toString());
    }

    public static String recNoMem(int row, int column) {
        if (row < 0 || column < 0 || row >= size || column >= size){
            return "x";
        }

        if (dangerousMountains[row][column].equals("x")){
            return "x";
        }

        if (row == 0 && column == size - 1){
            return dangerousMountains[row][column];
        }
    
        String goldN = recNoMem(row - 1, column);
        String goldE = recNoMem(row, column + 1);
        String goldNE = recNoMem(row - 1, column + 1);

        String maxGold = goldN;

        if (goldE != "x") {
            if (maxGold == "x") {
                maxGold = goldE;
            } else {
                maxGold = String.valueOf(Math.max(Integer.parseInt(maxGold), Integer.parseInt(goldE)));
            }
        }

        if (goldNE != "x") {
            if (maxGold == "x") {
                maxGold = goldNE;
            } else {
                maxGold = String.valueOf(Math.max(Integer.parseInt(maxGold), Integer.parseInt(goldNE)));
            }
        }

        if (maxGold == "x") {
            return "x";
        } else {
            return String.valueOf(Integer.parseInt(maxGold) + Integer.parseInt(dangerousMountains[row][column]));
        }
    }


    public static String recMem(int row, int column) {
        if (row < 0 || column < 0 || row >= size || column >= size){
            return "x";
        }
    
        if (dangerousMountains[row][column].equals("x")){
            return "x";
        }

        if (row == 0 && column == size - 1){
            return dangerousMountains[row][column];
        }
           
        if (!memory1[row][column].equals(String.valueOf(Integer.MIN_VALUE))){
            return memory1[row][column];
        }
            
        String goldN = recMem(row - 1, column);
        String goldE = recMem(row, column + 1);
        String goldNE = recMem(row - 1, column + 1);

        String maxGold = goldN;

        if (goldE != "x") {
            if (maxGold == "x") {
                maxGold = goldE;
            } else {
                maxGold = String.valueOf(Math.max(Integer.parseInt(maxGold), Integer.parseInt(goldE)));
            }

        }
        if (goldNE != "x") {
            if (maxGold == "x") {
                maxGold = goldNE;
            } else {
                maxGold = String.valueOf(Math.max(Integer.parseInt(maxGold), Integer.parseInt(goldNE)));
            }

        }

        if (maxGold == "x") {
            return "x";
        } else {
            maxGold = String.valueOf(Integer.parseInt(maxGold) + Integer.parseInt(dangerousMountains[row][column]));
            memory1[row][column] = maxGold;
            return maxGold;
        }
    }

    public static String withoutRec() {
        int row = size - 1;
        int column = 0;
        if (dangerousMountains[row][column].equals("x")){
            return "x";
        }
            
        for (row = size - 1; row >= 0; row--) {
            for ( column = 0; column < size; column++) {
                if (dangerousMountains[row][column].equals("x")) {
                    memory2[row][column][0] = "x";
                    memory2[row][column][1] = "x";
                    continue;
                }
                if (row == size - 1 && column == 0) {
                    memory2[row][column][0] = dangerousMountains[row][column];
                    memory2[row][column][1] = "start";
                    continue;
                }
                
                String goldW = "";
                String goldS = "";
                String goldSW = "";

                if (validPath(row, column - 1, memory2)) {
                    goldW = String.valueOf(memory2[row][column - 1][0]);
                } else {
                    goldW = "x";
                }

                if (validPath(row + 1, column, memory2)) {
                    goldS = String.valueOf(memory2[row + 1][column][0]);
                } else {
                    goldS = "x";
                }

                if (validPath(row + 1, column - 1, memory2)) {
                    goldSW = String.valueOf(memory2[row + 1][column - 1][0]);
                } else {
                    goldSW = "x";
                }

                String maxGold = goldW;
                String goldPath = "W";
                if (!goldS.equals("x")) {
                    if (maxGold.equals("x") || Integer.parseInt(maxGold) < Integer.parseInt(goldS)) {
                        maxGold = goldS;
                        goldPath = "S";
                    }
                }
                if (!goldSW.equals("x")) {
                    if (maxGold.equals("x") || Integer.parseInt(maxGold) < Integer.parseInt(goldSW)) {
                        maxGold = goldSW;
                        goldPath = "SW";
                    }
                }

                if (maxGold.equals("x")) {
                    memory2[row][column][0] = "x";
                    memory2[row][column][1] = "x";
                } else {
                    memory2[row][column][0] = String.valueOf(Integer.parseInt(maxGold) + Integer.parseInt(dangerousMountains[row][column]));
                    memory2[row][column][1] = goldPath;
                }
            }
        }

        int row1 = 0;
        int column1 = size - 1;
        int gold = Integer.parseInt(memory2[row1][column1][0]);
        while (!memory2[row1][column1][1].equals("start")) {
            switch (memory2[row1][column1][1]) {
                case "S":
                    path2.add(0,"N");
                    row1++;
                    break;
                case "W":
                    path2.add(0,"E");
                    column1--;
                    break;
                case "SW":
                    path2.add(0,"NE");
                    row1++;
                    column1--;
                    break;
            }
        }
        return "" + gold;
    }

    public static void findPath() {
        int row = size - 1;
        int column = 0;
        int highGold = 0;

        while (row != 0 || column != size - 1) {
            if (dangerousMountains[row][column].equals("x"))
                break;
            int N = Integer.MIN_VALUE;
            int E = Integer.MIN_VALUE;
            int NE = Integer.MIN_VALUE;

            if (row - 1 < 0 && column + 1 < size) {
                E = Integer.parseInt(memory1[row][column]);
                highGold = E;
            } else if (row - 1 >= 0 && column + 1 > size - 1) {
                N = Integer.parseInt(memory1[row][column]);
                highGold = N;
            } else {
                N = Integer.parseInt(memory1[row - 1][column]);
                E = Integer.parseInt(memory1[row][column + 1]);
                NE = Integer.parseInt(memory1[row - 1][column + 1]);

                highGold = Math.max(Math.max(N, E), NE);
            }
            if (highGold == N) {
                path1.add("N");
                row--;
            } else if (highGold == E) {
                path1.add("E");
                column++;
            } else {
                path1.add("NE");
                row--;
                column++;
            }
        }
    }

    public static void startMemory(String[][] memory) {
        for (int row = 0; row < size; row++)
        {
            for (int column = 0; column < size; column++) {
                memory[row][column] = String.valueOf(Integer.MIN_VALUE);
            }
        }
    }

    private static boolean validPath(int row, int column, String[][][] memory2) {
        boolean outOfDangerousMountain = row < 0 || column < 0 || row >= size || column >= size;
        return !outOfDangerousMountain && !memory2[row][column][0].equals("x");
    }
}
