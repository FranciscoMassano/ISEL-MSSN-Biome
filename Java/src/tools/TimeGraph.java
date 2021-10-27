package tools;

import processing.core.PApplet;

public class TimeGraph {
	private SubPlot plt;
	private PApplet p;
	private int[] color;
	private float[][] last;
	private float ref;
	
	public TimeGraph(PApplet p, SubPlot plt, int color, float ref) {
		this.color = new int[1];
		this.color[0] = color;
		this.setRef(ref);
		initGraph(p, plt, this.color, ref);
	}
	
	public TimeGraph(PApplet p, SubPlot plt, int[] color, float ref) {
		initGraph(p, plt, color, ref);
	}
	
	private void initGraph(PApplet p, SubPlot plt, int[] color, float ref) {
		this.p = p;
		this.plt = plt;
		this.color = color;
		double[] window = plt.getWindow();
		
		last = new float[color.length][2];
		for (int i = 0; i<color.length; i++) {
			last[i] = plt.getPixelCoord(window[0], window[2]);
		}
		
		float[] bb = plt.getBoundingBox();
		p.pushStyle();
		p.fill(255);
		p.rect(bb[0], bb[1], bb[2], bb[3]);
		
		p.stroke(0);
		float[] pontoInicial = plt.getPixelCoord(window[0], ref);
		float[] pontoFinal = plt.getPixelCoord(window[1], ref);
		p.line(pontoInicial[0], pontoInicial[1], pontoFinal[0], pontoFinal[1]);
		p.textSize(12);
		p.fill(0);
		p.text(String.format("%.1f", ref), pontoInicial[0]+3, pontoInicial[1]-3);
		p.popStyle();
		
	}

	public void plot(float t, float y) {
		plot(t,y,0);
		
	}
	
	public void plot(float t, float y, int ix) {
		

		if (t > plt.getWindow()[1] || y > plt.getWindow()[3]) {
			return;
		}
		
		float[] pp = plt.getPixelCoord(t, y);
		p.pushStyle();
		p.stroke(color[ix]);
		p.line(last[ix][0], last[ix][1], pp[0], pp[1]);
		p.popStyle();
		last[ix] = pp;
	}

	public float getRef() {
		return ref;
	}

	public void setRef(float ref) {
		this.ref = ref;
	}
	
	

	

}
