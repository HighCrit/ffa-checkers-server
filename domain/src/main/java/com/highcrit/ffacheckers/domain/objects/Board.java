package com.highcrit.ffacheckers.domain.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.highcrit.ffacheckers.domain.enums.PlayerColor;
import com.highcrit.ffacheckers.domain.objects.moves.Move;

public class Board {
  public static final int BLACK_SQUARES = 162;
  private static final Pattern FEN_PATTERN =
      Pattern.compile("(?<color>[YBGR])(?<pieces>(K?\\d+(,K?\\d+)*))");
  private static final String FEN_DELIMITER = ":";
  private static final String FEN_PIECE_DELIMITER = ",";

  private final Piece[] board = new Piece[BLACK_SQUARES];
  private final HashMap<PlayerColor, List<Piece>> pieces = new HashMap<>();

  public Board(List<Piece> pieces) {
    placePieces(pieces);
  }

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

    if (pieces.isEmpty()) return null;

    return new Board(pieces);
  }

  private void placePieces(List<Piece> pieces) {
    pieces.forEach(
        piece -> {
          board[piece.getPosition()] = piece;
          // if key doesn't exist create new with array containing piece
          // if it does exist add piece to existing array
          this.pieces.merge(
              piece.getPlayerColor(),
              new ArrayList<>() {
                {
                  add(piece);
                }
              },
              (o, n) -> {
                o.add(piece);
                return o;
              });
        });
  }

  public void applyMove(Move move) {
    board[move.getEnd()] = board[move.getStart()];
    board[move.getStart()] = null;
    //    for (Piece take : move.getTakes()) {
    //      board[take.getPosition()] = null;
    //    }
  }

  public void undoMove(Move move) {
    board[move.getStart()] = board[move.getEnd()];
    board[move.getEnd()] = null;
    //    for (Piece take : move.getTakes()) {
    //      board[take.getPosition()] = take;
    //    }
  }

  public String toFen() {
    StringBuilder sb = new StringBuilder();

    this.pieces.forEach(
        (playerColor, pieces) -> {
          sb.append(playerColor.getColorChar());
          sb.append(
              pieces.stream()
                  .map(Objects::toString)
                  .collect(Collectors.joining(FEN_PIECE_DELIMITER)));
          sb.append(FEN_DELIMITER);
        });

    return sb.substring(0, sb.length() - 1);
  }

  public Piece[] getBoard() {
    return board;
  }

  public HashMap<PlayerColor, List<Piece>> getPieces() {
    return pieces;
  }
}
