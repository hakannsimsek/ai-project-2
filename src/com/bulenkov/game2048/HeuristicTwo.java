/*
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

package com.bulenkov.game2048;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class HeuristicTwo {
    LinkedList<Game2048.Tile[]> leafNodesList = new LinkedList<Game2048.Tile[]>();
    LinkedList<String> leafNodeNames = new LinkedList<String>();
    String directionHistory = "";

    public void run(Game2048 game,int xply) {
        Game2048.Tile[] currentBoard = game.myTiles;
        game.printBoard(currentBoard);
        while (true) {
            leafNodesList = Game2048.gimmeLeaves(Game2048.deepCopyBoard(currentBoard), xply, leafNodeNames);
            int[] appealingOfLeaves = new int[leafNodesList.size()];
            for (int i = 0; i < leafNodesList.size(); i++) {
                Game2048.Tile[] board = leafNodesList.get(i);
                if (board == null) {
                    appealingOfLeaves[i] = -1;
                }
                else {
                    appealingOfLeaves[i] = getNumberOfPossibleMerges(board);
                }
            }
            int maxAppealing = highestAppealingValue(appealingOfLeaves);

            boolean isRepetition = checkIfRepetition(directionHistory);
            System.out.println(isRepetition + ". History = " + directionHistory);
            int random = giveMaximumIndex(appealingOfLeaves,maxAppealing,isRepetition);
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
            if (currentBoard == null)
                System.out.println("Game Over");
            System.out.println("Directions moved : " + directionHistory);
            System.out.println("Board after move : ");
            game.printBoard(currentBoard);
            //köşedeki degerin log2 si + boş tile sayısı = evalution function
            System.out.println();
            System.out.println();
        }


    }

    private int findMaximumTile(Game2048.Tile[] tiles) {
        int max = -1;
        for (Game2048.Tile tile: tiles) {
            max = Math.max(max, tile.value);
        }
        return max;
    }

    private int giveMaximumIndex(int appealingValues[],int maxAppealing,boolean isRepetition) {

        int index = 0;
        if ( appealingValues.length == 0 ) return -1;
        if (!isRepetition) {
            int random;
            while (true) {
                random = (int) (Math.random() * leafNodesList.size());
                try {
                    if (appealingValues[random] == maxAppealing){
                        index = random;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            int max = -1;
            int i = (int)Math.random()*appealingValues.length;
            for ( ; i < appealingValues.length ; i++) {
                if (appealingValues[i] > max && appealingValues[i] != maxAppealing){
                    index = i;
                    max = appealingValues[i];
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

    private boolean checkIfRepetitionTen(String history) {
        if (history.length()<15) return false;
        String substr = history.substring(history.length()-6);
        boolean sub1 = substr.charAt(0) == substr.charAt(1)  && substr.charAt(1) == substr.charAt(2) && substr.charAt(2) == substr.charAt(3) && substr.charAt(3) == substr.charAt(4) && substr.charAt(4) == substr.charAt(5);
        substr = history.substring(history.length()-12,history.length()-6);
        boolean sub2 = substr.charAt(0) == substr.charAt(1)  && substr.charAt(1) == substr.charAt(2) && substr.charAt(2) == substr.charAt(3) && substr.charAt(3) == substr.charAt(4) && substr.charAt(4) == substr.charAt(5);
        return sub1 && sub2;

    }


    private int highestAppealingValue(int[] appealingOfLeaves) {
        int max = -1;
        for (int value : appealingOfLeaves) {
            if (max < value) max = value;
        }
        return max;
    }


    private int getNumberOfPossibleMerges(Game2048.Tile[] board) {
        AtomicInteger numberOfPossibleMerges = new AtomicInteger();
        Arrays.stream(getVerticalLines(board)).forEach((line) -> numberOfPossibleMerges.addAndGet(getNumberOfPossibleMergesForALine(line)));
        Arrays.stream(getHorizontalLines(board)).forEach((line) -> numberOfPossibleMerges.addAndGet(getNumberOfPossibleMergesForALine(line)));
        return numberOfPossibleMerges.get();
    }

    private int getNumberOfPossibleMergesForALine(Game2048.Tile[] line) {
        int numberOfPossibleMerges = 0;
        int previousValue = -1;
        for (Game2048.Tile tile: line) {
            if (tile.value != 0 && previousValue == tile.value) {
                numberOfPossibleMerges++;
            }
            previousValue = tile.value;
        }
        return numberOfPossibleMerges;
    }

    private Game2048.Tile[][] getVerticalLines(Game2048.Tile[] board) {
        Game2048.Tile[][] verticalLines = new Game2048.Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                verticalLines[i][j] = board[i + j * 4];
            }
        }
        return verticalLines;
    }

    private Game2048.Tile[][] getHorizontalLines(Game2048.Tile[] board) {
        Game2048.Tile[][] horizontalLines = new Game2048.Tile[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                horizontalLines[i][j] = board[i * 4 + j];
            }
        }
        return horizontalLines;
    }
}
