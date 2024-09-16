package com.chesshouzs.server.constants;

public class GameConstants {
    public static String WHITE_COLOR = "WHITE";
    public static String BLACK_COLOR = "BLACK";

	public static char NONCHARACTER_WALL = '0';

	public static char NONCHARACTER_EMPTY = '.';

    public static char WHITE_CHARACTER_PAWN = 'P'; 
	public static char WHITE_CHARACTER_KNIGHT = 'N'; 
	public static char WHITE_CHARACTER_KING = 'K';
	public static char WHITE_CHARACTER_QUEEN = 'Q';
	public static char WHITE_CHARACTER_BISHOP = 'B';
	public static char WHITE_CHARACTER_ROOK = 'R';
	public static char WHITE_CHARACTER_EVOLVED_PAWN = 'E';

    public static char BLACK_CHARACTER_PAWN = 'p'; 
	public static char BLACK_CHARACTER_KNIGHT = 'n'; 
	public static char BLACK_CHARACTER_KING = 'k';
	public static char BLACK_CHARACTER_QUEEN = 'q';
	public static char BLACK_CHARACTER_BISHOP = 'b';
	public static char BLACK_CHARACTER_ROOK = 'r';
	public static char BLACK_CHARACTER_EVOLVED_PAWN = 'e';

	public static String END_GAME_CHECKMATE_TYPE = "CHECKMATE"; 
	public static String END_GAME_RESIGN_TYPE = "RESIGN";
	public static String END_GAME_STALEMATE_TYPE= "STALEMATE";
	public static String END_GAME_DRAW_TYPE= "DRAW";
	public static String END_GAME_TIMEOUT_TYPE = "TIMEOUT";



	public static Integer ELO_CALC_K_FACTOR = 20;

	public static String KEY_VALID_MOVE = "valid_move";
	public static String KEY_CHARACTER = "character";
	public static String KEY_IS_DOUBLE = "is_double";
	public static String KEY_COLOR = "color";
	public static String KEY_VALID = "valid";

	public static String KEY_OLD_POSITION = "old_position";
	public static String KEY_NEW_POSITION = "new_position";

	public static String KEY_IS_IN_CHECK = "is_in_check"; 
	public static String KEY_ATTACKERS = "attackers"; 
	public static String KEY_INVALID_MOVES = "invalid_moves";
	public static String KEY_VALID_MOVES = "valid_moves";

	
}
