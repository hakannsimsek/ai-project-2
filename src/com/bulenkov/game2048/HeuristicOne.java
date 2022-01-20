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

public class HeuristicOne {
    LinkedList<Game2048.Tile[]> leafNodesList = new LinkedList<Game2048.Tile[]>();
    LinkedList<String> leafNodeNames = new LinkedList<String>();
    String directionHistory = "";

    public void run(Game2048 game, int xply) {
        Game2048.Tile[] currentBoard = game.myTiles;
        Game2048.printBoard(currentBoard);
        while (true) {
            leafNodesList = Game2048.gimmeLeaves(Game2048.deepCopyBoard(currentBoard), xply, leafNodeNames);
            for (int i = 0; i < leafNodesList.size(); i++) {
                Game2048.Tile[] board = leafNodesList.get(i);
                if (board == null) monotonicityValue[i]=-1;
                else monotonicityValue[i] = partitionIntoDirections(board);
            }

            int random;
            boolean isRepetition = checkIfRepetition(directionHistory);
            random = giveMaximumIndex(monotonicityValue,maxMonotonicity,isRepetition);
            if(random == -1){
                System.out.println("Game Over");
                break;
            }

            char nextMove = leafNodeNames.get(random).charAt(0);
            directionHistory += nextMove;
            System.out.println("Next move will be : " + nextMove);
            game.myTiles = Game2048.deepCopyBoard(currentBoard);
            System.out.println("Current Board : ");
            Game2048.printBoard(currentBoard);
            currentBoard = game.makeMove(nextMove);
            if (currentBoard == null){
                System.out.println("Game Over");
                break;
            }
            System.out.println("Directions moved : " + directionHistory);
            System.out.println("Board after move : ");
            Game2048.printBoard(currentBoard);

            //checkMonotonicity(leafNodesList);
            System.out.println("");
            System.out.println("");
        }

    }
}
