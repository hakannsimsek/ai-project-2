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
    //4.heuristic bonus open squares and for having large values on the edge
    public void run(Game2048 game,int xply) {

        leafNodesList = Game2048.gimmeLeaves(game.myTiles,xply);
        int[][] openSquaresAndLargestValue = new int[leafNodesList.size()][2];
        for (int i = 0 ; i < leafNodesList.size() ; i++) {
            openSquaresAndLargestValue[i][0] = calculateOpenSquares(leafNodesList.get(i));
            openSquaresAndLargestValue[i][1] = largestValueOnTheEdge(leafNodesList.get(i));
        }
        //köşedeki degerin log2 si + boş tile sayısı = evalution function
        System.out.println();
        System.out.println();


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


    /*public void givePlyGameResults(Game2048.Tile tilees[],int deep) {
        deep++;

        char[] directions = {'r', 'd', 'l', 'u'};

        for (char direction : directions) {
            if (deep == xply) {
                continue;
            } else {
                Game2048.Tile parent[] = tilees;
                str += direction;
                System.out.println("String : " + str);
                game2048.myTiles = deepCopyTiles(tilees);
                tilees = game2048.makeMove(direction);
                if (str.length() == xply) {
                    printTile(tilees);
                    leafNodesList.add(tilees);
                }
                givePlyGameResults(deepCopyTiles(tilees), deep);
                tilees = parent;
            }
            str = str.substring(0, str.length() - 1);
        }

    }*/
        /*


        System.out.println("deep level : " + deep);
        if ( deep == xply ) { return; }
        else {
            Game2048.Tile parent[] = tilees;
            str += "r";
            game2048.myTiles = deepCopyTiles(tilees);
            tilees = game2048.makeMove('r');
            if(str.length() == xply){
                printTile(tilees);
                leafNodesList.add(tilees);
            }
            givePlyGameResults(deepCopyTiles(tilees),deep);
            tilees = parent;
        }
        str = str.substring(0,str.length()-1);


        if ( deep == xply ) { return; }
        else {
            Game2048.Tile parent[] = tilees;
            str += "d";
            game2048.myTiles = deepCopyTiles(tilees);
            tilees = game2048.makeMove('d');
            if(str.length() == xply){
                printTile(tilees);
                leafNodesList.add(tilees);
            }
            givePlyGameResults(deepCopyTiles(tilees),deep);
            tilees = parent;
        }
        str = str.substring(0,str.length()-1);

        System.out.println("deep level : " + deep);
        if ( deep == xply ) { return; }
        else {
            Game2048.Tile parent[] = tilees;
            str += "l";
            game2048.myTiles = deepCopyTiles(tilees);
            tilees = game2048.makeMove('l');
            if(str.length() == xply){
                printTile(tilees);
                leafNodesList.add(tilees);
            }
            givePlyGameResults(deepCopyTiles(tilees),deep);
            tilees = parent;
        }
        str = str.substring(0,str.length()-1);

        System.out.println("deep level : " + deep);
        if ( deep == xply ) { return; }
        else {
            Game2048.Tile parent[] = tilees;
            str += "u";
            game2048.myTiles = deepCopyTiles(tilees);
            tilees = game2048.makeMove('u');
            if(str.length() == xply){
                printTile(tilees);
                leafNodesList.add(tilees);
            }
            givePlyGameResults(deepCopyTiles(tilees),deep);
            tilees = parent;
        }
        str = str.substring(0,str.length()-1);
        */



/*
    public void givePlyGameResults(Game2048.Tile tilees[],int iteration){

        if( iteration == xply ) {
            System.out.println(str);
            str = str.substring(0,iteration-1);
            iteration--;
            return;
        };
        System.out.print("Iteration :" + iteration);
        game2048.makeMove('r');
        str += "r";
        givePlyGameResults(tilees.clone(),++iteration);
        if( iteration == xply ) {
            System.out.println(str);
            str = str.substring(0,iteration-1);
            iteration--;
            return;
        };
        game2048.makeMove('d');
        str += "d";
        givePlyGameResults(tilees.clone(),++iteration);

    }
*/

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
