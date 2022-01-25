package com.bulenkov.game2048;/*
 * Copyright 1998-2022 Konstantin Bulenkov http://bulenkov.com/about
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.LinkedList;

public class HeuristicThree {

    LinkedList<Game2048.Tile[]> leafNodesList = new LinkedList<Game2048.Tile[]>();
    LinkedList<String> leafNodeNames = new LinkedList<String>();
    String directionHistory = "";

    //Monotonicity: values of the tiles are all either increasing or decreasing along all directions
    public void run(Game2048 game, int xply) {
        Game2048.Tile[] currentBoard = game.myTiles;
        game.printBoard(currentBoard);
        while (true) {
            leafNodesList = Game2048.gimmeLeaves(Game2048.deepCopyBoard(currentBoard), xply, leafNodeNames);
            int[] monotonicityValue = new int[leafNodesList.size()];
            for (int i = 0; i < leafNodesList.size(); i++) {
                Game2048.Tile[] board = leafNodesList.get(i);
                if (board == null) monotonicityValue[i]=-1;
                else monotonicityValue[i] = partitionIntoDirections(board);
            }
            int maxMonotonicity = highestMonotonicityValue(monotonicityValue);

            int random;
            boolean isRepetition = checkIfRepetition(directionHistory);
            random = giveMaximumIndex(monotonicityValue,maxMonotonicity,isRepetition);
            if(checkIfRepetitionTen(directionHistory)){
                System.out.println("Game Over");
                break;
            }
            if(random == -1){
                System.out.println("Game Over");
                break;
            }

            char nextMove = leafNodeNames.get(random).charAt(0);
            directionHistory += nextMove;
            System.out.println("Next move will be : " + nextMove);
            game.myTiles = Game2048.deepCopyBoard(currentBoard);
            System.out.println("Current Board : ");
            game.printBoard(currentBoard);
            currentBoard = game.makeMove(nextMove);
            if (currentBoard == null){
                System.out.println("Game Over");
                break;
            }
            System.out.println("Directions moved : " + directionHistory);
            System.out.println("Board after move : ");
            game.printBoard(currentBoard);

            //checkMonotonicity(leafNodesList);
            System.out.println("");
            System.out.println("");
        }

    }

    /*while (true) {
                random = (int) (Math.random() * leafNodesList.size());
                if (monotonicityValue[random] == maxMonotonicity)
                    break;
            }*/

    private int giveMaximumIndex(int monotonicityValue[],int maxMonotonicity,boolean isRepetition) {

        int index = 0;
        if ( monotonicityValue.length == 0 ) return -1;
        if (!isRepetition) {
            int random;
            while (true) {
                random = (int) (Math.random() * leafNodesList.size());
                try {
                    if (monotonicityValue[random] == maxMonotonicity){
                        index = random;
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        } else {
            int max = -1;
            int i = (int)Math.random()*monotonicityValue.length;
            for ( ; i < monotonicityValue.length ; i++) {
                if (monotonicityValue[i] > max && monotonicityValue[i] != maxMonotonicity) {
                    index = i;
                    max = monotonicityValue[i];
                }
            }
        }
        return index;

    }

    private boolean checkIfRepetition (String history) {
        if (history.length()<3) return false;
        String substr = history.substring(history.length()-3);
        return substr.charAt(0) == substr.charAt(1) && substr.charAt(1) == substr.charAt(2);
    }

    private int highestMonotonicityValue(int[] monotonicityValues) {
        int max = 0;
        for (int value : monotonicityValues) {
            if (max < value) max = value;
        }
        return max;
    }

    private boolean checkIfRepetitionTen(String history) {
        if (history.length()<15) return false;
        String substr = history.substring(history.length()-6);
        boolean sub1 = substr.charAt(0) == substr.charAt(1)  && substr.charAt(1) == substr.charAt(2) && substr.charAt(2) == substr.charAt(3) && substr.charAt(3) == substr.charAt(4) && substr.charAt(4) == substr.charAt(5);
        substr = history.substring(history.length()-12,history.length()-6);
        boolean sub2 = substr.charAt(0) == substr.charAt(1)  && substr.charAt(1) == substr.charAt(2) && substr.charAt(2) == substr.charAt(3) && substr.charAt(3) == substr.charAt(4) && substr.charAt(4) == substr.charAt(5);
        return sub1 && sub2;

    }

    //artıyosa +monotonicity, artmıyosa sabit
    public int checkMonotonicity(Game2048.Tile[] line) {
        int monotonicityPoint = 0;
        int isIncreasing = -2;
        for (int i = 0; i < line.length - 1; i++) {
            if(line[i].value != 0 && line[i + 1].value != 0){
                if (line[i].value < line[i + 1].value) {
                    if (isIncreasing == -1) {
                        return 0;
                    } else if (isIncreasing == 1) {//eger önceki artıyosa ve şimdide artmışsa +monotonicity
                        monotonicityPoint+=3;
                    } else {
                        monotonicityPoint++;
                    }
                    if(line[i].value*2 == line[i + 1].value){
                        monotonicityPoint += 4;
                    }
                    isIncreasing = 1;
                } else if (line[i].value > line[i + 1].value) {
                    if (isIncreasing == 1) {
                        return 0;
                    } else if (isIncreasing == -1) {//eger önceki azalıyorsa ve şimdide azalmıssa +monotonicity
                        monotonicityPoint+=3;
                    } else {
                        monotonicityPoint++;
                    }
                    if(line[i].value == line[i + 1].value*2){
                        monotonicityPoint += 4;
                    }
                    isIncreasing = -1;
                }else{
                    monotonicityPoint+=4;
                }
            }

        }
        return monotonicityPoint;
    }


    public int partitionIntoDirections(Game2048.Tile[] board) {
        int monotonicityOfLeaf = 0;
        Game2048.Tile[] line = new Game2048.Tile[4];
        for (int i = 0; i < board.length; i++) {
            line[i % 4] = board[i];
            if (i % 4 == 3) {
                monotonicityOfLeaf += checkMonotonicity(line);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 4; x++) {
                int columnIndex = 4 * x + i;
                line[x] = board[columnIndex];
                if (x % 4 == 3) {
                    monotonicityOfLeaf += checkMonotonicity(line);
                }
            }
        }
        return monotonicityOfLeaf;

    }

}
