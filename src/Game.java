import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.*;

import java.awt.Color;

import javalib.worldimages.*;

// a Game class that represents Minesweeper game
public class Game extends World {

  static final int SQUARE_WIDTH = 30;
  static final int CIRCLE_RAD = 10;
  static final int TRI_SIZE = 10;

  int row;
  int column;
  int minesNum;
  ArrayList<Cell> cells;
  Random r;

  // the constructor
  Game(int row, int column, int minesNum, ArrayList<Cell> cells, Random r) {
    this.row = row;
    this.column = column;
    this.minesNum = minesNum;
    this.cells = cells;
    this.r = r;
  }

  // the main minesweeper constructor
  Game(int row, int column, int minesNum) {
    this(row, column, minesNum, new ArrayList<Cell>(), new Random());
    this.grid();
    this.plantBomb();
    this.neighborLink();
  }

  // the convenience constructor
  Game(int row, int column, int minesNum, Random r) {
    this(row, column, minesNum, new ArrayList<Cell>(), r);
  }

  // draws the board for Minesweeper
  public WorldScene makeScene() {
    WorldScene drawBoard = this.getEmptyScene();
    drawBoard.placeImageXY(this.buildRows(), this.column * SQUARE_WIDTH / 2,
            this.row * SQUARE_WIDTH / 2);
    return drawBoard;
  }

  // attaches all the cells in a row in an image
  public WorldImage combineCells(int idx) {
    WorldImage starter = new EmptyImage();
    for (int i = 0; i < this.column; i++) {
      starter = new BesideImage(starter, cells.get(idx + i).drawCell());
    }
    return starter;
  }

  // stacks all rows of cells on top of each other in an image
  public WorldImage buildRows() {
    WorldImage starter = new EmptyImage();
    for (int i = 0; i < this.row; i++) {
      starter = new AboveImage(starter, combineCells(i * this.column));
    }
    return starter;
  }

  // links all neighboring cells for each cell
  // EFFECT: Modifies each cell's neighbor list to contain its neighbors
  void neighborLink() {
    for (Cell c : cells) {
      boolean notFirstRow = cells.indexOf(c) >= this.column;
      boolean notLastRow = this.column * this.row - cells.indexOf(c) > this.column;
      boolean notFirstColumn = cells.indexOf(c) % this.column > 0;
      boolean notLastColumn = cells.indexOf(c) % this.column < this.column - 1;
      if (notLastColumn) {
        c.neighbors.add(cells.get(cells.indexOf(c) + 1));
      }
      if (notFirstColumn) {
        c.neighbors.add(cells.get(cells.indexOf(c) - 1));
      }
      if (notFirstRow) {
        c.neighbors.add(cells.get(cells.indexOf(c) - this.column));
      }
      if (notLastRow) {
        c.neighbors.add(cells.get(cells.indexOf(c) + this.column));
      }
      if (notLastColumn && notLastRow) {
        c.neighbors.add(cells.get(cells.indexOf(c) + this.column + 1));
      }
      if (notFirstColumn && notLastRow) {
        c.neighbors.add(cells.get(cells.indexOf(c) + this.column - 1));
      }
      if (notFirstRow && notLastColumn) {
        c.neighbors.add(cells.get(cells.indexOf(c) - this.column + 1));
      }
      if (notFirstColumn && notFirstRow) {
        c.neighbors.add(cells.get(cells.indexOf(c) - this.column - 1));
      }
    }
  }

  // Fills up the board with cells
  // EFFECT: Modifies the list of cells by adding on cells
  void grid() {
    for (int i = 0; i < this.column; i++) {
      for (int j = 0; j < this.row; j++) {
        this.cells.add(new Cell());
      }
    }
  }

  // Creates a list of random positions of bombs
  ArrayList<Integer> bombs() {
    ArrayList<Integer> b = new ArrayList<Integer>();
    ArrayList<Integer> bombList = new ArrayList<Integer>();

    for (int i = 0; i < cells.size(); i++) {
      b.add(i);
    }
    for (int i = 0; i < minesNum; i++) {
      int n = r.nextInt(b.size());
      bombList.add(b.get(n));
      b.remove(n);
    }
    return bombList;
  }

  // Utilizes a random bomb list to plant bombs across the board
  // EFFECT: Modifies the list of cells by turning some into mines
  void plantBomb() {
    ArrayList<Integer> bombs = this.bombs();
    for (int i = 0; i < this.cells.size(); i++) {
      if (bombs.contains(i)) {
        this.cells.get(i).makeMine();
      }
    }
  }

  // either flags a tile or opens a non-flagged tile
  // EFFECT: modifies the clicked-on cell by updating its isFlag or isOpen
  public void onMouseClicked(Posn pos, String mouse) {
    if (mouse.equals("LeftButton")) {
      cells.get(pos.x / SQUARE_WIDTH + this.column * (pos.y / SQUARE_WIDTH)).open();
    } else if (mouse.equals("RightButton")) {
      cells.get(pos.x / SQUARE_WIDTH + this.column * (pos.y / SQUARE_WIDTH)).flag();
    }
  }

  // ends the game if the player opened a bomb cell or opened all non-bomb cells
  public WorldEnd worldEnds() {
    if (this.lose()) {
      return new WorldEnd(true, this.drawFinal("aww better luck next time"));
    } else if (this.win()) {
      return new WorldEnd(true, this.drawFinal("YOU ARE A MINESWEEPER MASTER"));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // returns the end screen image with a win or lose message
  public WorldScene drawFinal(String msg) {
    WorldScene finalImg = this.makeScene();
    finalImg.placeImageXY(new TextImage(msg, this.column * 1.5, FontStyle.BOLD, Color.BLACK),
            this.column * Game.SQUARE_WIDTH / 2, this.row * Game.SQUARE_WIDTH / 2);
    return finalImg;
  }

  // checks if the player lost the game if a bomb is clicked on
  boolean lose() {
    boolean storage = false;
    for (int i = 0; i < this.cells.size(); i++) {
      if (this.cells.get(i).hasBomb && this.cells.get(i).isOpen) {
        storage = true;
      }
    }
    return storage;
  }

  // checks if the player won if only all non-bomb cells are revealed
  boolean win() {
    int notMines = this.cells.size() - this.minesNum;
    int count = 0;
    for (int i = 0; i < this.cells.size(); i++) {
      if (this.cells.get(i).isOpen && !this.cells.get(i).hasBomb) {
        count++;
      }
    }
    return notMines == count;
  }
}