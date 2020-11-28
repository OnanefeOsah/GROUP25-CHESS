package game_chess.gui;

import game_chess.engine.board.*;
import game_chess.engine.player.MoveTransition;
import game_chess.engine.pieces.Piece;
import game_chess.engine.player.ai.Minimax;
import game_chess.engine.player.ai.MoveStrategy;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * Author: Ade Oyefeso
 * Author: Robert Odoh
 * Date Created:
 *
 * GUI class for the Board
 * **/

public class Frame extends Observable{

    private JFrame gameFrame;
    private GameHistoryPanel gameHistoryPanel;
    private TakenPiecesPanel takenPiecesPanel;
    private BoardPanel boardPanel;
    private MoveLog moveLog;
    private GameSetup gameSetup;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private Move computerMove;
    private GameSetup temp;


    private boolean highlightLegalMoves;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    //path to the folder containing the piece pictures
    private static String defaultPieceImagesPath = "chess graphics/GIF-github/05/";

    private Color lightTileColor = Color.decode("#808080");
    private Color darkTileColor = Color.decode("#0000FF");

    private static final Frame INSTANCE = new Frame();

    public Frame(){

        this.gameFrame = new JFrame("CHESS 2D - Group 25");
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.temp = gameSetup;
        this.gameFrame.add(new TestPane());
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setResizable(true);
        this.gameFrame.setVisible(true);


        /**
            this.gameFrame.setLayout(new BorderLayout());
            final JMenuBar tableMenuBar = createTableMenuBar();
            this.gameFrame.setJMenuBar(tableMenuBar);
            this.chessBoard = Board.createStandardBoard();
            this.gameHistoryPanel = new GameHistoryPanel();
            this.takenPiecesPanel = new TakenPiecesPanel();
            this.boardPanel = new BoardPanel();
            this.moveLog = new MoveLog();
            this.addObserver((new TableGameAIWatcher()));
            this.boardDirection = BoardDirection.NORMAL;
            this.highlightLegalMoves = false;
            this.gameSetup = new GameSetup(this.gameFrame, true);

            this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
            this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
            this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        }
         **/
    }


    public void startGame(){
        this.gameFrame.dispatchEvent(new WindowEvent(this.gameFrame, WindowEvent.WINDOW_CLOSING));
        this.gameFrame = new JFrame("CHESS 2D - Group 25");
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver((new TableGameAIWatcher()));
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = true;
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setResizable(true);
        this.gameFrame.setVisible(true);

        show();
    }

    public class TestPane extends JPanel {

        private List<String> menuItems;
        private String selectMenuItem = null;
        private String focusedItem = null;

        private MenuItemPainter painter;
        private Map<String, Rectangle> menuBounds;

        public TestPane() {
            setBackground(Color.BLACK);
            painter = new SimpleMenuItemPainter();
            menuItems = new ArrayList<>(25);
            menuItems.add("Start Game");
            menuItems.add("Options");
            menuItems.add("Exit");

            MouseAdapter ma = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    //String newItem = null;
                    for (String text : menuItems) {
                        Rectangle bounds = menuBounds.get(text);
                        if (bounds.contains(e.getPoint())) {
                            //newItem = text;
                            selectMenuItem = text;
                            if(text.equalsIgnoreCase("Start game")){
                                //System.out.println("fish test");
                                startGame();
                                break;
                            }
                            else if(text.equalsIgnoreCase("Options")){
                                //System.out.println("cat test");

                                //TODO
                                break;
                            }
                            else if(text.equalsIgnoreCase("Exit")){
                                System.exit(0);
                            }
                        }
                    }
                    /**
                    if (newItem != null && !newItem.equals(selectMenuItem)) {
                        selectMenuItem = newItem;
                        repaint();
                    }
                     */
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    focusedItem = null;
                    for (String text : menuItems) {
                        Rectangle bounds = menuBounds.get(text);
                        if (bounds.contains(e.getPoint())) {
                            focusedItem = text;
                            repaint();
                            break;
                        }
                    }
                }

            };

            addMouseListener(ma);
            addMouseMotionListener(ma);

            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "arrowDown");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "arrowUp");

            am.put("arrowDown", new MenuAction(1));
            am.put("arrowUp", new MenuAction(-1));

        }

        @Override
        public void invalidate() {
            menuBounds = null;
            super.invalidate();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (menuBounds == null) {
                menuBounds = new HashMap<>(menuItems.size());
                int width = 0;
                int height = 0;
                for (String text : menuItems) {
                    Dimension dim = painter.getPreferredSize(g2d, text);
                    width = Math.max(width, dim.width);
                    height = Math.max(height, dim.height);
                }

                int x = (getWidth() - (width + 10)) / 2;

                int totalHeight = (height + 10) * menuItems.size();
                totalHeight += 5 * (menuItems.size() - 1);

                int y = (getHeight() - totalHeight) / 2;

                for (String text : menuItems) {
                    menuBounds.put(text, new Rectangle(x, y, width + 10, height + 10));
                    y += height + 10 + 5;
                }

            }
            for (String text : menuItems) {
                Rectangle bounds = menuBounds.get(text);
                boolean isSelected = text.equals(selectMenuItem);
                boolean isFocused = text.equals(focusedItem);
                painter.paint(g2d, text, bounds, isSelected, isFocused);
            }
            g2d.dispose();
        }

        public class MenuAction extends AbstractAction {

            private final int delta;

            public MenuAction(int delta) {
                this.delta = delta;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = menuItems.indexOf(selectMenuItem);
                if (index < 0) {
                    selectMenuItem = menuItems.get(0);
                }
                index += delta;
                if (index < 0) {
                    selectMenuItem = menuItems.get(menuItems.size() - 1);
                } else if (index >= menuItems.size()) {
                    selectMenuItem = menuItems.get(0);
                } else {
                    selectMenuItem = menuItems.get(index);
                }
                repaint();
            }

        }

    }

    public interface MenuItemPainter {

        public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused);

        public Dimension getPreferredSize(Graphics2D g2d, String text);

    }

    public class SimpleMenuItemPainter implements MenuItemPainter {

        public Dimension getPreferredSize(Graphics2D g2d, String text) {
            return g2d.getFontMetrics().getStringBounds(text, g2d).getBounds().getSize();
        }

        @Override
        public void paint(Graphics2D g2d, String text, Rectangle bounds, boolean isSelected, boolean isFocused) {
            FontMetrics fm = g2d.getFontMetrics();
            if (isSelected) {
                paintBackground(g2d, bounds, Color.BLUE, Color.WHITE);
            } else if (isFocused) {
                paintBackground(g2d, bounds, Color.MAGENTA, Color.BLACK);
            } else {
                paintBackground(g2d, bounds, Color.DARK_GRAY, Color.LIGHT_GRAY);
            }
            int x = bounds.x + ((bounds.width - fm.stringWidth(text)) / 2);
            int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();
            g2d.setColor(isSelected ? Color.WHITE : Color.LIGHT_GRAY);
            g2d.drawString(text, x, y);
        }

        protected void paintBackground(Graphics2D g2d, Rectangle bounds, Color background, Color foreground) {
            g2d.setColor(background);
            g2d.fill(bounds);
            g2d.setColor(foreground);
            g2d.draw(bounds);
        }

    }

    public static Frame get(){
        return INSTANCE;
    }

    public void show() {
        Frame.get().getMoveLog().clear();
        Frame.get().getGameHistoryPanel().redo(chessBoard, Frame.get().getMoveLog());
        Frame.get().getTakenPiecesPanel().redo(Frame.get().getMoveLog());
        Frame.get().getBoardPanel().drawBoard(Frame.get().getGameBoard());
    }

    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    //"File" Menu for game
    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");

        /**
        //a PGN file is a "Portable Game Notation File" to store and load games
        final JMenuItem openPGN = new JMenuItem("Load PGN file: ");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

            }
        });
         */
        //fileMenu.add(openPGN);

        /**
        final JMenuItem openFEN = new JMenuItem("Load FEN File");
        openFEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fenString = JOptionPane.showInputDialog("Input FEN");

            }
        });

         */
        //fileMenu.add(openFEN);

        /**
        final JMenuItem saveToPGN = new JMenuItem("Save Game");
        saveToPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
         */
        //fileMenu.add(saveToPGN);
        final JMenuItem newMenuItem = new JMenuItem("New Game");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.get().getMoveLog().clear();
                Frame.get().getGameHistoryPanel().redo(chessBoard, Frame.get().getMoveLog());
                Frame.get().getTakenPiecesPanel().redo(Frame.get().getMoveLog());
                Frame.get().getBoardPanel().drawBoard(Frame.get().getGameBoard());
            }
        });
        fileMenu.add(newMenuItem);

        //Menu item to exit game
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e){
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        //preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight legal moves", true);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);

        final JMenuItem tileColourMenuItem = new JMenuItem("Change Tile color");
        tileColourMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTileColor = JColorChooser.showDialog(null, "Pick Light Tile color", lightTileColor);
                darkTileColor = JColorChooser.showDialog(null, "Pick Dark Tile color", darkTileColor);
                boardPanel.drawBoard(chessBoard);
            }
        });

        preferencesMenu.add(tileColourMenuItem);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){

        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setUpGameMenuItem = new JMenuItem("Setup Game");
        setUpGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.get().getGameSetup().promptUser();
                Frame.get().setUpdate(Frame.get().getGameSetup());
            }
        });
        optionsMenu.add(setUpGameMenuItem);
        return optionsMenu;
    }

    /**
    private static void loadPGNFile(final File pgnFile) {
        try {

        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    */

    private void setUpdate(GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private static class TableGameAIWatcher implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            if (Frame.get().getGameSetup().isAIPlayer(Frame.get().getGameBoard().currentPlayer()) &&
                    !(Frame.get().getGameBoard().currentPlayer().isInCheckMate()) &&
                    !(Frame.get().getGameBoard().currentPlayer().isInStaleMate())){
                System.out.println(Frame.get().getGameBoard().currentPlayer() + " is set to AI, thinking....");
                final AIThinkTank thinkTank = new AIThinkTank( );
                thinkTank.execute();
            }

            if (Frame.get().getGameBoard().currentPlayer().isInCheckMate()) {
                JOptionPane.showMessageDialog(Frame.get().getBoardPanel(),
                        "Game Over: Player " + Frame.get().getGameBoard().currentPlayer() + " is in checkmate!", "GameOver",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (Frame.get().getGameBoard().currentPlayer().isInStaleMate()) {
                JOptionPane.showMessageDialog(Frame.get().getBoardPanel(),
                        "Game Over: Player " + Frame.get().getGameBoard().currentPlayer() + " is in stalemate!", "GameOver",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String>{
        private AIThinkTank(){
        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new Minimax(Frame.get().getGameSetup().getSearchDepth());
            final Move bestMove = miniMax.execute(Frame.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done(){
            try {
                final Move bestMove = get();
                Frame.get().updateComputerMove(bestMove);
                Frame.get().updateGameBoard(Frame.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Frame.get().getMoveLog().addMove(bestMove);
                Frame.get().getGameHistoryPanel().redo(Frame.get().getGameBoard(), Frame.get().getMoveLog());
                Frame.get().getTakenPiecesPanel().redo(Frame.get().getMoveLog());
                Frame.get().getBoardPanel().drawBoard(Frame.get().getGameBoard());
                Frame.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    public enum BoardDirection{

        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                return boardTiles;
            }
            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                Collections.reverse(boardTiles);
                return boardTiles;
            }
            @Override
            BoardDirection opposite(){
                return NORMAL;
            }
        };


        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

        abstract BoardDirection opposite();

    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;

        BoardPanel(){
            super(new GridLayout(8,8)); //Chess boards are 8x8
            this.boardTiles = new ArrayList<>();

            //add each chess tile to board panel
            for(int i = 0; i < BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardTiles){ //boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    //Stores game moves
    public static class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(final Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }

    }

    enum PlayerType{
        HUMAN,
        COMPUTER
    }

    private class TilePanel extends JPanel{

        final private int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                //listen for clicks on board
                @Override
                public void mouseClicked(final MouseEvent e){
                    //invalid move if user right clicks
                    if(isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;

                        //valid move for left click
                    }else if (isLeftMouseButton(e)){
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        }else{
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run(){
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
                                    Frame.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e){

                }
                @Override
                public void mouseReleased(final MouseEvent e){

                }
                @Override
                public void mouseEntered(final MouseEvent e){

                }
                @Override
                public void mouseExited(final MouseEvent e){

                }
            });
            validate();
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                            board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //highlight the moves the player can make
        private void highlightLegals(final Board board){
            String filePath = "chess graphics/GIF-github/misc/green_dot.png";
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board) ) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(filePath)))));
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board){
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor(){
            if(BoardUtils.EIGHTH_RANK[this.tileId] ||
                    BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] ||
                    BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(BoardUtils.SEVENTH_RANK[this.tileId] ||
                    BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] ||
                    BoardUtils.FIRST_RANK[this.tileId]){
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }

}
