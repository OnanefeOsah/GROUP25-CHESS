package game_chess.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;
import javax.imageio.ImageIO;

import game_chess.engine.board.Move;
import game_chess.engine.pieces.Piece;
import static game_chess.gui.Frame.*;

/**
 * Author: Ade Oyefeso
 * Author: Robert Odoh
 * Date Created:
 *
 * One of the GUI classes for the Board
 * This class is a panel for the pieces taken during the game
 * **/

public class TakenPiecesPanel extends JPanel{

	private final JPanel northPanel;
	private final JPanel southPanel;

	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,40);
	public static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

	public TakenPiecesPanel(){
		super(new BorderLayout());
		this.setBackground(PANEL_COLOR);
		this.setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8,2));
		this.southPanel = new JPanel(new GridLayout(8,2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.northPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
	}

	public void redo(final MoveLog moveLog){

		this.southPanel.removeAll();
		this.northPanel.removeAll();

		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();

		for (final Move move : moveLog.getMoves() ) {
			if (move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if (takenPiece.getPieceAlliance().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				}else if (takenPiece.getPieceAlliance().isBlack()) {
					blackTakenPieces.add(takenPiece);
				}
				else{
					throw new RuntimeException("Invalid");
				}
			}
		}

		Collections.sort(whiteTakenPieces, new Comparator<Piece>(){
			@Override
			public int compare(Piece o1, Piece o2){
				return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		Collections.sort(blackTakenPieces, new Comparator<Piece>(){
			@Override
			public int compare(Piece o1, Piece o2){
				return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});

		for(final Piece takenPiece : whiteTakenPieces ) {
			try{
				final BufferedImage image = ImageIO.read(new File("chess graphics/GIF-github/01" + takenPiece.getPieceAlliance().toString().substring(0,1) + "" + takenPiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.southPanel.add(imageLabel);
			}catch(final IOException e){
				e.printStackTrace();
			}
		}

		for (final Piece takenPiece : blackTakenPieces ) {
			try{
				final BufferedImage image = ImageIO.read(new File("plain art pieces" + takenPiece.getPieceAlliance().toString().substring(0,1) + "" + takenPiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.southPanel.add(imageLabel);
			}catch(final IOException e){
				e.printStackTrace();
			}
		}
		validate();
	}
}