import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.EmptyImage;
import javalib.worldimages.EquilateralTriangleImage;
import javalib.worldimages.FontStyle;
import javalib.worldimages.FrameImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import tester.Tester;

// example class of Minesweeper
public class MinesweeperTest {

  private Game testGame;
  private Game testGame2;
  private Game testGame3;
  private Cell cell1;
  private ArrayList<Cell> lc;

  void initData() {
    cell1 = new Cell();
    lc = new ArrayList<Cell>();
    lc.add(cell1);
    lc.add(cell1);
    lc.add(cell1);
    lc.add(cell1);
    lc.add(cell1);
    lc.add(cell1);
    lc.add(cell1);

    testGame = new Game(4, 5, 5, new Random(10));
    testGame2 = new Game(4, 5, 5, lc, new Random(5));
    testGame3 = new Game(4, 5, 3, lc, new Random(8));
  }

//  // tests the game bigbang
//  void testBigBang(Tester t) {
//    Game w = new Game(16, 30, 99);
//    int worldWidth = w.column * Game.SQUARE_WIDTH;
//    int worldHeight = w.row * Game.SQUARE_WIDTH;
//    w.bigBang(worldWidth, worldHeight, 0.01);
//  }

  // tests combineCells
  void testCombineCells(Tester t) {
    this.initData();
    t.checkExpect(testGame2.combineCells(0),
            new BesideImage(
                    new BesideImage(
                            new BesideImage(
                                    new BesideImage(
                                            new BesideImage(new EmptyImage(),
                                                    new FrameImage(
                                                            new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))));

    this.initData();
    ArrayList<Cell> twoCell = new ArrayList<Cell>();
    twoCell.add(cell1);
    twoCell.add(cell1);
    Game g2 = new Game(2, 2, 2, twoCell, new Random(3));
    t.checkExpect(g2.combineCells(0),
            new BesideImage(
                    new BesideImage(new EmptyImage(),
                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))));
  }

  // tests buildRows
  void testBuildRows(Tester t) {
    this.initData();
    ArrayList<Cell> empty = new ArrayList<Cell>();
    empty.add(cell1);
    empty.add(cell1);
    Game firstRow = new Game(1, 0, 1, empty, new Random(2));
    t.checkExpect(firstRow.buildRows(), new AboveImage(new EmptyImage(), new EmptyImage()));

    ArrayList<Cell> secondRow = new ArrayList<Cell>();
    secondRow.add(cell1);
    Game game2 = new Game(1, 1, 1, secondRow, new Random(3));
    t.checkExpect(game2.buildRows(),
            new AboveImage(new EmptyImage(),
                    new BesideImage(new EmptyImage(),
                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN)))));
  }

  // tests neighborLink
  void testNeighborLink(Tester t) {
    this.initData();
    Game testGame4 = new Game(2, 3, 2, new Random(10));
    testGame4.grid();
    testGame4.plantBomb();
    testGame4.neighborLink();
    t.checkExpect(testGame4.cells.get(1).neighbors.get(4), testGame4.cells.get(3));
  }

  // tests grid
  void testGrid(Tester t) {
    this.initData();
    t.checkExpect(testGame.cells.size(), 0);
    testGame.grid();
    t.checkExpect(testGame.cells.size(), 20);

    Game g = new Game(3, 3, 3, new ArrayList<Cell>(), new Random(3));
    t.checkExpect(g.cells.size(), 0);
    g.grid();
    t.checkExpect(g.cells.size(), 9);

    Game g1 = new Game(2, 3, 1, new ArrayList<Cell>(), new Random(2));
    t.checkExpect(g1.cells.size(), 0);
    g1.grid();
    t.checkExpect(g1.cells.size(), 6);
  }

  // tests bombs
  void testBombs(Tester t) {
    this.initData();
    ArrayList<Integer> ints2 = new ArrayList<Integer>();
    ints2.add(2);
    ints2.add(5);
    ints2.add(6);
    ints2.add(3);
    ints2.add(0);
    t.checkExpect(testGame2.bombs(), ints2);

    ArrayList<Integer> ints = new ArrayList<Integer>();
    ints.add(1);
    ints.add(5);
    ints.add(0);
    t.checkExpect(testGame3.bombs(), ints);
  }

  // tests plantBomb
  void testPlantBomb(Tester t) {
    this.initData();
    Game testGame0 = new Game(4, 5, 5, lc, new Random(5));
    testGame0.plantBomb();
    Cell c1 = new Cell(new ArrayList<Cell>(), true, false, false);
    t.checkExpect(testGame0.cells,
            new ArrayList<Cell>(Arrays.asList(c1, c1, c1, c1, c1, c1, c1)));
    Cell mine = new Cell(new ArrayList<Cell>(), true, false, false);
    ArrayList<Cell> mines = new ArrayList<Cell>();
    mines.add(mine);
    mines.add(mine);
    mines.add(mine);
    mines.add(mine);
    mines.add(mine);
    mines.add(mine);
    mines.add(mine);
    t.checkExpect(testGame0.cells, mines);

  }

  // tests makeMine
  void testMakeMine(Tester t) {
    this.initData();
    t.checkExpect(cell1.hasBomb, false);
    cell1.makeMine();
    t.checkExpect(cell1.hasBomb, true);
  }

  // tests countMines
  void testCountMines(Tester t) {
    this.initData();
    ArrayList<Cell> arrCell = new ArrayList<Cell>();
    t.checkExpect(new Cell(arrCell, false, false, false).countMines(), 0);
    arrCell.add(new Cell(new ArrayList<Cell>(), true, false, false));
    arrCell.add(new Cell(new ArrayList<Cell>(), true, false, false));
    arrCell.add(new Cell(new ArrayList<Cell>(), true, false, false));
    t.checkExpect(new Cell(arrCell, false, false, false).countMines(), 3);

    ArrayList<Cell> arrCell2 = new ArrayList<Cell>();
    arrCell2.add(new Cell(new ArrayList<Cell>(), true, false, false));
    t.checkExpect(new Cell(arrCell2, true, false, false).countMines(), 1);
  }

  // tests drawCell
  void testDrawCell(Tester t) {
    this.initData();
    t.checkExpect(cell1.drawCell(),
            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN)));

    Cell flag = new Cell(new ArrayList<Cell>(), false, false, true);
    t.checkExpect(flag.drawCell(),
            new OverlayImage(
                    new EquilateralTriangleImage(Game.TRI_SIZE, OutlineMode.SOLID, Color.RED.darker()),
                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))));

    Cell bomb = new Cell(new ArrayList<Cell>(), true, false, false);
    t.checkExpect(bomb.drawCell(),
            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN)));

    ArrayList<Cell> neighbors = new ArrayList<Cell>();
    neighbors.add(bomb);
    neighbors.add(bomb);
    neighbors.add(bomb);
    Cell num = new Cell(neighbors, false, true, false);
    t.checkExpect(num.drawCell(),
            new OverlayImage(new TextImage("3", Color.GREEN.darker()),
                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.GRAY))));

  }

  // tests onMouseClicked
  void testOnMouseClicked(Tester t) {
    this.initData();
    testGame3.onMouseClicked(new Posn(30, 30), "LeftButton");
    t.checkExpect(testGame3.cells.get(1), new Cell(new ArrayList<Cell>(), false, true, false));
    this.initData();
    ArrayList<Cell> empty = new ArrayList<Cell>();
    empty.add(cell1);
    empty.add(cell1);
    empty.add(cell1);
    Game tester = new Game(1, 3, 1, empty, new Random(2));
    tester.onMouseClicked(new Posn(30, 0), "RightButton");
    t.checkExpect(tester.cells.get(1), new Cell(new ArrayList<Cell>(), false, false, true));
    this.initData();
    testGame2.onMouseClicked(new Posn(0, 0), "up");
    t.checkExpect(testGame2.cells.get(0), new Cell(new ArrayList<Cell>(), false, false, false));
  }

  // tests worldEnds
  void testWorldEnds(Tester t) {
    this.initData();
    ArrayList<Cell> empty = new ArrayList<Cell>();
    empty.add(cell1);
    empty.add(cell1);
    Game firstRow = new Game(1, 0, 1, empty, new Random(2));

    t.checkExpect(firstRow.worldEnds(), new WorldEnd(false, firstRow.makeScene()));

    Cell winGame = new Cell(new ArrayList<Cell>(), false, true, false);
    ArrayList<Cell> winList = new ArrayList<Cell>();
    winList.add(winGame);
    Game winner = new Game(1, 0, 0, winList, new Random(3));

    t.checkExpect(winner.worldEnds(),
            new WorldEnd(true, winner.drawFinal("YOU ARE A MINESWEEPER MASTER")));

    Cell loseGame = new Cell(new ArrayList<Cell>(), true, true, false);
    ArrayList<Cell> loseList = new ArrayList<Cell>();
    loseList.add(loseGame);
    Game loser = new Game(1, 0, 0, loseList, new Random(3));

    t.checkExpect(loser.worldEnds(),
            new WorldEnd(true, loser.drawFinal("aww better luck next time")));

  }

  // tests drawFinal
  void testDrawFinal(Tester t) {
    this.initData();
    ArrayList<Cell> empty = new ArrayList<Cell>();
    empty.add(cell1);
    empty.add(cell1);
    Game firstRow = new Game(1, 0, 1, empty, new Random(2));
    ArrayList<Cell> secondRow = new ArrayList<Cell>();
    secondRow.add(cell1);
    Game game2 = new Game(1, 1, 1, secondRow, new Random(3));

    WorldScene firstScene = firstRow.makeScene();
    firstScene.placeImageXY(new RectangleImage(0, 0, OutlineMode.OUTLINE, Color.BLACK), 0, 0);
    firstScene.placeImageXY(new AboveImage(new EmptyImage(), new EmptyImage()), 0, 15);
    firstScene.placeImageXY(
            new TextImage("aww better luck next time", 100, FontStyle.BOLD, Color.BLACK), 0, 15);
    t.checkExpect(firstRow.drawFinal("aww better luck next time"), firstScene);

    WorldScene secondScene = game2.makeScene();
    secondScene.placeImageXY(new RectangleImage(0, 0, OutlineMode.OUTLINE, Color.BLACK), 0, 0);
    secondScene.placeImageXY(
            new AboveImage(new EmptyImage(),
                    new BesideImage(new EmptyImage(),
                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN)))),
            15, 15);
    secondScene.placeImageXY(
            new TextImage("WOW YOU ARE A MINESWEEPER MASTER", 100, FontStyle.BOLD, Color.BLACK), 15,
            15);
    t.checkExpect(game2.drawFinal("WOW YOU ARE A MINESWEEPER MASTER"), secondScene);

  }

  // tests lose
  void testLose(Tester t) {
    this.initData();
    t.checkExpect(testGame2.lose(), false);

    Cell bomb = new Cell(new ArrayList<Cell>(), true, true, false);
    lc.add(bomb);
    t.checkExpect(testGame2.lose(), true);
  }

  // tests win
  void testWin(Tester t) {
    this.initData();
    t.checkExpect(testGame2.win(), false);

    ArrayList<Cell> arr = new ArrayList<Cell>();
    Cell notBomb = new Cell(new ArrayList<Cell>(), false, true, false);
    arr.add(notBomb);
    arr.add(notBomb);
    Game winGame = new Game(1, 2, 0, arr, new Random(3));
    t.checkExpect(winGame.win(), true);

  }

  // tests open
  void testOpen(Tester t) {
    this.initData();
    cell1.open();
    t.checkExpect(cell1, new Cell(new ArrayList<Cell>(), false, true, false));
    Cell cell2 = new Cell(new ArrayList<Cell>(), false, true, false);
    cell2.open();
    t.checkExpect(cell2, new Cell(new ArrayList<Cell>(), false, true, false));
    Cell cell3 = new Cell(new ArrayList<Cell>(Arrays.asList(new Cell(), new Cell(), new Cell())),
            false, false, false);
    cell3.open();
    t.checkExpect(cell3,
            new Cell(
                    new ArrayList<Cell>(Arrays.asList(new Cell(new ArrayList<Cell>(), false, true, false),
                            new Cell(new ArrayList<Cell>(), false, true, false),
                            new Cell(new ArrayList<Cell>(), false, true, false))),
                    false, true, false));
  }

  // tests flag
  void testFlag(Tester t) {
    this.initData();
    t.checkExpect(this.cell1.isFlag, false);
    this.cell1.flag();
    t.checkExpect(this.cell1.isFlag, true);

    Cell open = new Cell(new ArrayList<Cell>(), false, true, false);
    t.checkExpect(open.isFlag, false);
    open.flag();
    t.checkExpect(open.isFlag, false);

    Cell flagged = new Cell(new ArrayList<Cell>(), false, false, true);
    t.checkExpect(flagged.isFlag, true);
    flagged.flag();
    t.checkExpect(flagged.isFlag, false);
  }

  // tests makeScene
  void testMakeScene(Tester t) {
    this.initData();
    ArrayList<Cell> onlyTwo = new ArrayList<Cell>();
    onlyTwo.add(cell1);
    onlyTwo.add(cell1);
    Game twoCell = new Game(1, 2, 0, onlyTwo, new Random(3));

    WorldScene ms = twoCell.getEmptyScene();
    ms.placeImageXY(new RectangleImage(0, 0, OutlineMode.OUTLINE, Color.BLACK), 0, 0);
    ms.placeImageXY(
            (new AboveImage(new EmptyImage(),
                    new BesideImage(
                            new BesideImage(new EmptyImage(),
                                    new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))),
                            new FrameImage(new RectangleImage(30, 30, OutlineMode.SOLID, Color.CYAN))))),
            30, 15);
    t.checkExpect(twoCell.makeScene(), ms);
  }
}
