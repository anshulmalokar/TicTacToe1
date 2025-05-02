import java.lang.constant.PackageDesc;
import java.util.*;

class Main{
    public static void main(String[] args){
        Player x = new Player("1", "X", new HumanePlayerMovingStrategy(), Symbol.X);
        Player o = new Player("2", "O", new HumanePlayerMovingStrategy(), Symbol.O);
        GameManager manager = new GameManager(3, x, o);
        manager.start();
    }
}
class Player {
    public final String id;
    public final String name;
    private final IPlayerMoveStrategy strategy;
    public final Symbol symbol;
    public Player(String id, String name, IPlayerMoveStrategy strategy, Symbol symbols) {
        this.id = id;
        this.name = name;
        this.strategy = strategy;
        this.symbol = symbols;
    }

    public IPlayerMoveStrategy getStrategy() {
        return strategy;
    }
}
class GameManager{
      private final int size;
      private final GameContext gameContext;
      private final Player xPlayer;
      private final Player yPlayer;
      private final Board board;
      private Player currentPlayer;
    public GameManager(int size, Player xPlayer, Player yPlayer) {
        this.size = size;
        this.gameContext = new GameContext();
        this.xPlayer = xPlayer;
        this.yPlayer = yPlayer;
        this.currentPlayer = this.xPlayer;
        board = new Board(size);
    }
    public void printGrid(){
        board.printGrid();
    }
    public void start(){
        System.out.println("Game Started");
        while (gameOn()){
            board.printGrid();
            Positon positon = currentPlayer.getStrategy().move(board, currentPlayer.symbol);
            board.addMove(positon, currentPlayer.symbol);
            board.changeState(gameContext, currentPlayer.symbol);
            currentPlayer = nextPlayer();
        }
        showResult();
    }
    private Player nextPlayer(){
        return currentPlayer == xPlayer ? yPlayer : xPlayer;
    }
    private void showResult(){
        if(this.gameContext.state instanceof XWonState) System.out.println("X-Won");
        if(this.gameContext.state instanceof YWonState) System.out.println("Y-Won");
        else System.out.println("Draw");
    }
    private boolean gameOn(){
        return this.gameContext.state instanceof XPlayerState ||
                this.gameContext.state instanceof YPlayerState;
    }
}
class Board{
    private final int size;
    private final Symbol[][] board;
    private boolean isFilled;
    private boolean answerFound;
    private final int gridSize;
    private int filledGridSize;
    public Board(int size) {
        this.size = size;
        board = new Symbol[size][size];
        for(Symbol[] arr: board){
            Arrays.fill(arr, Symbol.EMPTY);
        }
        this.isFilled = false;
        this.answerFound = false;
        this.gridSize = size*size;
    }
    public boolean isValidMove(Positon positon, Symbol symbol){
        int x = positon.x;
        int y = positon.y;
        if((x >= size || y >= size || x < 0 || y < 0) || board[x][y] != Symbol.EMPTY) return false;
        return true;
    }
    public void addMove(Positon positon, Symbol symbol){
        int x = positon.x;
        int y = positon.y;
        System.out.println("Symbol " + symbol + " adding the move " + x + " " + y);
        board[positon.x][positon.y] = symbol;
        filledGridSize++;
        isGameOver(symbol);
    }
    public void printGrid(){
        for(int i = 0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                System.out.print(board[i][j].toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    private void isGameOver(Symbol symbol){
        boolean ans = answerFound(symbol);
        if(ans){
            this.answerFound = true;
        }
        if(this.filledGridSize == gridSize){
            this.isFilled = true;
        }
    }
    public boolean answerFound(Symbol symbol) {
        for (int i = 0; i < size; i++) {
            boolean rowMatch = true;
            boolean colMatch = true;
            for (int j = 0; j < size; j++) {
                if (board[i][j] != symbol) rowMatch = false;
                if (board[j][i] != symbol) colMatch = false;
            }
            if (rowMatch || colMatch) return true;
        }
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != symbol) diag1 = false;
            if (board[i][size - 1 - i] != symbol) diag2 = false;
        }

        return diag1 || diag2;
    }
    public void changeState(GameContext context, Symbol symbol){
        if(this.answerFound){
            if(symbol.equals(Symbol.X)){
                context.setState(new XWonState());
            }else if(symbol.equals(Symbol.O)){
                context.setState(new YWonState());
            }
            return;
        }
        if(this.isFilled){
            context.setState(new DrawState());
            return;
        }
        if(symbol.equals(Symbol.X)){
            context.state.next(context);
        }
        else if(symbol.equals(Symbol.O)){
            context.state.next(context);
        }
    }
}
class HumanePlayerMovingStrategy implements IPlayerMoveStrategy{
    @Override
    public Positon move(Board board, Symbol symbol) {
        System.out.println("The following player is making a move " + symbol);
        Scanner scn = new Scanner(System.in);
        int x = scn.nextInt();
        int y = scn.nextInt();
        Positon positon = new Positon(x,y);
        boolean isValid = board.isValidMove(positon, symbol);
        while (!isValid){
            System.out.println("Last move was not a valid move enter a valid move");
            int x1 = scn.nextInt();
            int y1 = scn.nextInt();
            positon = new Positon(x1, y1);
            isValid = board.isValidMove(positon, symbol);
        }
        return positon;
    }
}
interface IPlayerMoveStrategy{
    Positon move(Board board, Symbol symbol);
}
class Positon{
    public final int x;
    public final int y;

    public Positon(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
interface IGameState{
    void next(GameContext context);
}
class XPlayerState implements IGameState{
    @Override
    public void next(GameContext context) {
        context.setState(new YPlayerState());
    }
}
class YPlayerState implements IGameState{
    @Override
    public void next(GameContext context) {
        context.setState(new XPlayerState());
    }
}
class XWonState implements IGameState{
    @Override
    public void next(GameContext context) {

    }
}
class YWonState implements IGameState{
    @Override
    public void next(GameContext context) {

    }
}
class DrawState implements IGameState{
    @Override
    public void next(GameContext context) {

    }
}
class GameContext{
    public IGameState state;
    public GameContext(){
        state = new XPlayerState();
    }

    public void setState(IGameState iGameState){
        this.state = iGameState;
    }
}
enum Symbol{
    EMPTY,
    O,
    X
}