package games.conways;

public class Logic{
	
	private boolean[][] in;

	public Logic() {
		
	}
	
	public boolean[][] getNextResult(boolean[][] in){
		this.in = in;
		boolean[][] result = new boolean[in.length][in[0].length];
		
		for(int i = 1; i < in.length-1; i++){
			for(int j = 1; j < in[i].length-1; j++){
				result[i][j] = isAlive(i, j);
			}
		}
		
		return result;
	}

	private boolean isAlive(int j, int i) {
		int alives = 0;
		for(int l = j-1; l <= j+1; l++){
			for(int k = i-1; k <= i+1; k++){
				if(in[l][k] == true){
					alives++;
				}else{
				}
			}
		}
		if(in[j][i]){
			alives--;
		}
		
		if((alives == 2 || alives == 3) && in[j][i]){
			return true;
		}else if(alives == 3){
			return true;
		}else{
			return false;
		}
	}
}
