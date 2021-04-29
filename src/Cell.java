import java.awt.*;
import java.util.ArrayList;

import javalib.worldimages.CircleImage;
import javalib.worldimages.EquilateralTriangleImage;
import javalib.worldimages.FrameImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

// represent a cell
public class Cell {
  ArrayList<Cell> neighbors;
  boolean hasBomb;
  boolean isOpen;
  boolean isFlag;

  public Cell() {
    this.neighbors = new ArrayList<Cell>();
    this.hasBomb = false;
    this.isOpen = false;
    this.isFlag = false;
  }

  public Cell(ArrayList<Cell> neighbors, boolean hasBomb, boolean isOpen, boolean isFlag) {
    this.neighbors = neighbors;
    this.hasBomb = hasBomb;
    this.isOpen = isOpen;
    this.isFlag = isFlag;
  }

  // changes the current cell to a mine
  // EFFECT: Modifies this cell's hasBomb field to be true
  void makeMine() {
    this.hasBomb = true;
  }

  // count how many mines this cell has around it
  int countMines() {
    int mineNum = 0;
    for (Cell c : this.neighbors) {
      if (c.hasBomb) {
        mineNum++;
      }
    }
    return mineNum;
  }

  // draws a cell
  WorldImage drawCell() {
    FrameImage bloc = new FrameImage(
            new RectangleImage(Game.SQUARE_WIDTH, Game.SQUARE_WIDTH, OutlineMode.SOLID, Color.CYAN));
    FrameImage openedBloc = new FrameImage(
            new RectangleImage(Game.SQUARE_WIDTH, Game.SQUARE_WIDTH, OutlineMode.SOLID, Color.GRAY));
    WorldImage mine = new CircleImage(Game.CIRCLE_RAD, OutlineMode.SOLID, Color.RED);
    ArrayList<Color> colors = new ArrayList<Color>();
    colors.add(Color.BLACK);
    colors.add(Color.BLUE);
    colors.add(Color.RED);
    colors.add(Color.GREEN.darker());
    colors.add(Color.RED.darker());
    colors.add(Color.CYAN.darker());
    colors.add(Color.BLACK);
    colors.add(Color.GRAY);
    if (this.isOpen) {
      if (this.hasBomb) {
        return new OverlayImage(mine, openedBloc);
      } else if (this.countMines() != 0) {
        return new OverlayImage(
                new TextImage(Integer.toString(this.countMines()), colors.get(this.countMines())),
                openedBloc);
      } else {
        return openedBloc;
      }
    } else if (this.isFlag) {
      return new OverlayImage(
              new EquilateralTriangleImage(Game.TRI_SIZE, OutlineMode.SOLID, colors.get(4)),
              bloc);
    } else {
      return bloc;
    }
  }

  // opens this cell if unflagged and initiates flooding on neighbors if this mine
  // count is 0
  // EFFECT: modifies this cell by revealing this cell and its neighboring cells
  void open() {
    if (this.isFlag || this.isOpen) {
      return;
    } else {
      this.isOpen = true;
      for (int i = 0; i < this.neighbors.size(); i++) {
        if (this.countMines() == 0 && !this.hasBomb) {
          this.neighbors.get(i).open();
        }
      }
    }
  }

  // flags or unflags this cell
  // EFFECT: modifies this cell by flagging or unflagging
  void flag() {
    if (!this.isOpen) {
      this.isFlag = !this.isFlag;
    }
  }

}
