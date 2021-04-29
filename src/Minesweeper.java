import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Minesweeper {
  public static void main(String args[]) {
    int row = 16;
    int column = 30;
    int minesNum = 99;
    JFrame frame = new JFrame();
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-play")) {
        try {
          row = Integer.valueOf(args[i + 1]);
          column = Integer.valueOf(args[i + 1]);
          minesNum = Integer.valueOf(args[i + 1]);
        } catch (Exception e) {
          // catch no input given
          JOptionPane.showMessageDialog(frame, "Invalid input",
                  "Warning",
                  JOptionPane.WARNING_MESSAGE);
        }
      }
    }
    Game w = new Game(row, column, minesNum);
    int worldWidth = w.column * Game.SQUARE_WIDTH;
    int worldHeight = w.row * Game.SQUARE_WIDTH;
    try {
      w.bigBang(worldWidth, worldHeight, 0.01);
    } catch (Exception c) {
      // catch any error
      JOptionPane.showMessageDialog(frame, "Can't play sorry :(",
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }
}
