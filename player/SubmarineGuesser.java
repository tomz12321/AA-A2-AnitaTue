class SubmarineGuesser{
	public Cell[][] board;
	public boolean isSunk;

	SubmarineGuesser(){
		Cell[][] board = new Cell[][]()<>;
		isSunk = false;
	}

	class Cell{
    	int row, colunm;
    	LinkedList<Cell> adjC;
    	Guess guess;
    	Coordinate coor;
    	boolean isHit;
    	int probValue;
    	
    	public Cell(int row, int column) {
    		this.row = row;
    		this.colunm = column;
    		this.adjC = new LinkedList<>();
    		this.guess = new Guess();
    		this.guess.row = row;
    		this.guess.column = colunm;
    		this.coor = new World().new Coordinate();
    		coor.row = row;
    		coor.column = column;
    		isHit = false;
    	}
    	
    	public void addAdj(Cell cell) {
    		this.adjC.add(cell);
    	}
    	
    	public LinkedList<Cell> getAdj(){
    		return this.adjC;
    	}

    	//getter
    	public Cell getCellValue(int vrow, int vcolumn){
    		return board[vrow][vcolumn];
    	}
    	public boolean getIsSunk(){
    		return isSunk;
    	}
    	//setter
    	public void setCellValue(int vrow, int vcolumn, int setProbValue){
    		this.board[vrow][vcolumn] = setProbValue;
    	}
    	public void setIsSunk(boolean setIsSunk){
    		this.isSunk = setIsSunk;
    	}
}