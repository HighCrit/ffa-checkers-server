package com.highcrit.ffacheckers.socket.game.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.entities.Move;
import com.highcrit.ffacheckers.domain.entities.Piece;
import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import lombok.Getter;

public class Board {
  public static final int BLACK_SQUARES = 162;
  private static final Pattern FEN_PATTERN =
      Pattern.compile("(?<color>[YBGR])(?<pieces>(K?\\d+(,K?\\d+)*))");
  private static final String FEN_DELIMITER = ":";
  private static final String FEN_PIECE_DELIMITER = ",";

  @Getter private final Piece[] grid = new Piece[BLACK_SQUARES];
  @Getter private final EnumMap<PlayerColor, List<Piece>> pieces = new EnumMap<>(PlayerColor.class);
  @Getter private final LinkedList<Move> moveHistory = new LinkedList<>();
  @Getter private final String initialFen;

  public Board(List<Piece> pieces) {
    placePieces(pieces);
    this.initialFen = this.toFen();
  }

  /**
   * Constructs a board instance form a FEN string
   * @see <a href="https://en.wikipedia.org/wiki/Portable_Draughts_Notation">Wikipedia PDN</a>
   * @param fen fen string
   * @return board instance
   */
  public static Board fromFen(String fen) {
    List<Piece> pieces = new ArrayList<>();

    Matcher matcher = FEN_PATTERN.matcher(fen);
    while (matcher.find()) {
      PlayerColor playerColor = PlayerColor.fromColorChar(matcher.group("color").charAt(0));
      String[] playerPieces = matcher.group("pieces").split(FEN_PIECE_DELIMITER);
      for (String piece : playerPieces) {
        if (piece.charAt(0) == 'K') {
          pieces.add(new Piece(playerColor, Integer.parseInt(piece.substring(1)), true));
        } else {
          pieces.add(new Piece(playerColor, Integer.parseInt(piece), false));
        }
      }
    }
    return new Board(pieces);
  }

  /**
   * Places pieces on the board and adds them to the players
   * @param pieces list of pieces to add
   */
  private void placePieces(List<Piece> pieces) {
    pieces.forEach(
        piece -> {
          grid[piece.getPosition()] = piece;
          // if key doesn't exist create new with array containing piece
          // if it does exist add piece to existing array
          this.pieces.merge(
              piece.getPlayerColor(),
              new ArrayList<>(Collections.singletonList(piece)),
              (o, n) -> {
                o.add(piece);
                return o;
              });
        });
  }

  /**
   * Executes move and saves it
   * @param move move to execute
   */
  public void applyMove(Move move) {
    Piece piece = grid[move.getStart()];
    piece.setPosition(move.getEnd());

    grid[move.getStart()] = null;
    grid[move.getEnd()] = piece;
    if (move.getTakes() != null) {
      grid[move.getTakes().getPosition()] = null;
      pieces.get(move.getTakes().getPlayerColor()).remove(move.getTakes());
    }

    moveHistory.add(move);
  }

  /**
   * Undoes last move
   */
  public void undoMove() {
    Move move = moveHistory.removeLast();

    Piece piece = grid[move.getEnd()];
    piece.setPosition(move.getStart());
    grid[move.getEnd()] = null;
    grid[move.getStart()] = piece;
    if (move.getTakes() != null) {
      grid[move.getTakes().getPosition()] = move.getTakes();
      pieces.get(move.getTakes().getPlayerColor()).add(move.getTakes());
    }
  }

  /**
   * Constructs a FEN string from current board instance
   * @see <a href="https://en.wikipedia.org/wiki/Portable_Draughts_Notation">Wikipedia PDN</a>
   * @return FEN string
   */
  public String toFen() {
    StringBuilder sb = new StringBuilder();

    this.pieces.forEach(
        (playerColor, p) -> {
          sb.append(playerColor.getColorChar());
          sb.append(
              p.stream().map(Objects::toString).collect(Collectors.joining(FEN_PIECE_DELIMITER)));
          sb.append(FEN_DELIMITER);
        });

    return sb.substring(0, sb.length() - 1);
  }
}
