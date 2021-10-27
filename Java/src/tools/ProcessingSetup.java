package tools;


import application.EcosystemApp;
import processing.core.PApplet;
/**
 * 
 * @author franc
 * Esta class define o tamanho da janela e lanca a nossa PApplet
 */
public class ProcessingSetup extends PApplet {
	
	//referencia para a nossa app, por meio de uma interface
	//escrito de uma forma generica e valido para qualquer classe que implemente a interface
	public static IProcessingApp app;
	
	//mecanismo que facilite a temporizacao (tempo entre duas frames consecutivas)
	private int lastUpdate;
	
	public static void main(String[] args) {
				
		app = new EcosystemApp();
		
		///lancar a nossa PApplet  e definir alguns parametros
		//lancada sempre na classe ProcessingSetup
		// ProcessingSetup.setup() -> app.setup()
		PApplet.main(ProcessingSetup.class);


	}
	//alteracoes a janela sao feitas aqui
	@Override
	public void settings() {
		size(1800,900);
	}
	@Override
	//estas funcoes chamam as funcoes da nossa app
	//usamos o setup da app, e passamos lhe ESTE objecto PApplet
	public void setup(){
		app.setup(this);
		//ler o clock e regista quando tempo passou desde que a app arrancou
		lastUpdate = millis();
	}
	@Override 
	public void draw() {
		//a cada frame le o relogio e calcula quanto tempo passou entre frames (delta t)
		int now = millis();
		
		//dividir por 1000 para ter o tempo em segundos
		float dt = (now - lastUpdate) /1000f;
		
		//actualizar o lastUpdate para a proxima frame
		lastUpdate = now;
		app.draw(this, dt);
	}
	
	@Override
	public void mousePressed() {
		app.mousePressed(this);
	}
	
	@Override
	public void mouseReleased() {
		// TODO Auto-generated method stub
		app.mouseReleased(this);
		
	}
	@Override
	public void keyPressed() {
		app.keyPressed(this);
	}
	@Override
	public void mouseDragged() {
		app.mouseDragged(this);
	}

}
