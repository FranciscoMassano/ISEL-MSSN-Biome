package tools;

public class Histogram {
	
	int[] hist;
	int nBins;//dimensao do histograma (estados diferentes)
	
	//contruir o histograma
	public Histogram(int[] data, int nBins) {
		this.nBins = nBins;
		hist = new int[nBins];
		
		for (int i = 0; i < data.length; i++) {
			hist[data[i]]++;
		}
	}
	
	//devolve o histograma criado
	public int[] getDistribution() {
		return hist;
	}
	
	//obter Moda, precorrendo o histograma e guardando o maior valor (o estado que aparece mais vezes)
	//necessario para aplicar a regra da maioria 
	//o parametro de entrada usado para desempatar, se dois indices forem iguais, escolhe o menor
	public int getMode(int preference) {
		int maxVal = 0;
		int mode = 0;
		
		//obter o valor maximo e o indice do mesmo
		for (int i = 0; i < nBins; i++) {
			//comprar o valor no indice com o valor max
			if (hist[i] > maxVal) {
				maxVal = hist[i];
				mode = i;
			}
		}
		
		//se houver empate, decide-se pela preferencia
		if (hist[preference] == hist[mode]) {
			return preference;
		}
		
		return mode;
	}
	
	//escrever na consola a informacao 
	public void display() {
		for (int i = 0; i < nBins; i++) {
			System.out.println(hist[i]);
		}
		System.out.println();
	}
}
