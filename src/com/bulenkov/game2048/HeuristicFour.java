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

import java.util.LinkedList;

public class HeuristicFour {
    //Game2048.Tile tiles[];

    LinkedList<Game2048.Tile[]> leafNodesList = new LinkedList<Game2048.Tile[]>();
    LinkedList<String> leafNodeNames = new LinkedList<String>();
    //4.heuristic bonus open squares and for having large values on the edge
    public void run(Game2048 game,int xply) {
        Game2048.Tile[] currentBoard = game.myTiles;
        game.printBoard(currentBoard);
        while (true) {
            leafNodesList = Game2048.gimmeLeaves(Game2048.deepCopyBoard(currentBoard), xply, leafNodeNames);
            int[][] openSquaresAndLargestValue = new int[leafNodesList.size()][2];
            int[] appealingOfLeaves = new int[leafNodesList.size()];
            for (int i = 0; i < leafNodesList.size(); i++) {
                Game2048.Tile[] board = leafNodesList.get(i);
                if (board == null) {
                    openSquaresAndLargestValue[i][0] = -1;
                    openSquaresAndLargestValue[i][1] = -1;
                    appealingOfLeaves[i] = -1;
                }
                else {
                    openSquaresAndLargestValue[i][0] = calculateOpenSquares(board);
                    openSquaresAndLargestValue[i][1] = largestValueOnTheEdge(board);
                    int appealingOfLeaf = log2(openSquaresAndLargestValue[i][1]);
                    appealingOfLeaf += openSquaresAndLargestValue[i][0];
                    appealingOfLeaves[i] = appealingOfLeaf;
                }
            }
            int maxAppealing = highestAppealingValue(appealingOfLeaves);
            int random;
            while (true) {
                random = (int) (Math.random() * leafNodesList.size());
                if (appealingOfLeaves[random] == maxAppealing)
                    break;
            }
            char nextMove = (char) leafNodeNames.get(random).charAt(0);
            System.out.println("Next move will be : " + nextMove);
            game.myTiles = Game2048.deepCopyBoard(currentBoard);
            game.printBoard(currentBoard);
            currentBoard = game.makeMove(nextMove);
            if (currentBoard == null)
                System.out.println("Game Over");
            game.printBoard(currentBoard);
            //köşedeki degerin log2 si + boş tile sayısı = evalution function
            System.out.println();
            System.out.println();
        }


    }

    private int highestAppealingValue(int[] appealingOfLeaves) {
        int max = 0;
        for (int value : appealingOfLeaves) {
            if (max < value) max = value;
        }
        return max;
    }

    public int log2(int N)
    {
        if(N == 0) return 0;
        return (int)(Math.log(N) / Math.log(2));
    }

    private int calculateOpenSquares(Game2048.Tile[] maze){
        int openSquareCount = 0;
        for (Game2048.Tile tile : maze) {
            if(tile.value == 0) openSquareCount++;
        }
        return openSquareCount;
    }

    private int largestValueOnTheEdge(Game2048.Tile[] maze){
        int largestValueOnTheEdge = 0;
        int[] edgeIndexes = {0,3,12,15};
        for (int index : edgeIndexes) {
            if ( maze[index].value > largestValueOnTheEdge )
                largestValueOnTheEdge = maze[index].value;
        }
        return largestValueOnTheEdge;

    }



    public void printTile(Game2048.Tile maze[]){
        for (int i = 0 ; i < 4 ; i++){
            for (int j = 0 ; j < 4 ; j++)
                System.out.print(maze[i*4+j].value + " | ");
            System.out.println("");
        }
        System.out.println("-----------------");
    }


    public Game2048.Tile[] deepCopyTiles(Game2048.Tile tiles[]) {
        Game2048.Tile[] newTiles = new Game2048.Tile[tiles.length];
        for (int i = 0 ; i < tiles.length ; i++){
            Game2048.Tile newTile = new Game2048.Tile();
            newTile.value = tiles[i].value;
            newTiles[i] = newTile;
        }
        return newTiles;
    }


}
