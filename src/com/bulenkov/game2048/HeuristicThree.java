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
    //Monotonicity: values of the tiles are all either increasing or decreasing along all directions
    public void run(Game2048 game,int xply) {

        leafNodesList = Game2048.gimmeLeaves(game.myTiles,xply);
        for (Game2048.Tile[] maze: leafNodesList) {
            partitionIntoDirections(maze);
        }
        //checkMonotonicity(leafNodesList);

    }

    public void checkMonotonicity(Game2048.Tile[] line) {

        int isDecreasing = -2;
        for (int i = 0 ; i < line.length-1 ; i++) {
            if( line[i].value > line[i+1].value ) {
                if( isDecreasing == -1 ){
                    System.out.println("no monotonicity");
                }
                isDecreasing = 1;
            }else if( line[i].value < line[i+1].value ) {
                if( isDecreasing == 1 ){
                    System.out.println("no monotonicity");
                }
                isDecreasing = -1;
            }else {
                isDecreasing = 0;
            }
        }

    }



    public void partitionIntoDirections(Game2048.Tile[] leaves) {
        Game2048.printTile(leaves);
        Game2048.Tile[] line = new Game2048.Tile[4];
        for (int i = 0 ; i < leaves.length ; i++){
            line[i%4] = leaves[i];
            if ( i%4 == 3) {
                checkMonotonicity(line);
            }
        }
        for (int i = 0 ; i < 4 ; i++) {
            for (int x = 0; x < 4; x++) {
                int asd = 4 * x + i;
                line[x] = leaves[asd];
                if (x % 4 == 3) {
                    checkMonotonicity(line);
                }
            }
        }


    }

}
