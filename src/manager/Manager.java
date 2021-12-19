package manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Cuadrado;
import model.Raton;

public class Manager extends JPanel{
	private Cuadrado[][] rec;
	private Raton raton;
	private Random rand;
	private String[][] referencia;
	private boolean cont;
	
	public Manager() {
		raton=new Raton(this);
		JFrame f = new JFrame("Buscaminas");
		f.add(this);
		f.setSize(333, 356);
		f.setResizable(false);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addMouseListener(raton);
		rec=new Cuadrado[9][9];
		referencia=new String[9][9];
		cont=true;
		rand=new Random();
		System.out.println(rec.length+" "+rec[0].length);
		Cuadrado guarda = null;
		for(int i=0; i<rec.length; i++) {
			for(int j=0; j<rec[0].length; j++) {
				if(i==0 && j==0) {
					System.out.println(i+" "+j);
					rec[i][j]=new Cuadrado(f);
					guarda=rec[i][j];
				} else {
					System.out.println(i+" "+j);
					rec[i][j]=new Cuadrado(f,"["+i+"]["+j+"]",guarda);
					guarda=rec[i][j];
				}
				if(i!=0&&j==0) {
					rec[i][j].setX(-rec[i][j].getX());
					rec[i][j].setY(rec[i][j].getDimensiones());
				}
			}
		}
	}
	
	public void actualizar() {
		boolean d=false;
		while(true) {
			raton.actualizar(this);
			for(int i=0; i<rec.length; i++) {
				for(int j=0; j<rec[0].length; j++) {
					if(raton.isClickI() && raton.getBounds().intersects(rec[i][j].getBounds())) {
						if(rec[i][j].getBotonPulsado()!=2) {
							rec[i][j].setBotonPulsado(1);
							d=descubrir(cont, i, j);
							if(d) {
								finDelJuego(false);
							}
							cont=false;
						}
						System.out.println(rec[i][j].getPos());
						raton.setClickI(false);
					} else if(raton.isClickD() && raton.getBounds().intersects(rec[i][j].getBounds())) {
						if(rec[i][j].getBotonPulsado()!=1) {
							if(rec[i][j].getBotonPulsado()==2) {
								rec[i][j].setBotonPulsado(0);
								rec[i][j].setCaracter("   ");
								
							}else {
								rec[i][j].setBotonPulsado(2);
								rec[i][j].setCaracter(" < ");
							}
						}
						raton.setClickD(false);
					}
				}
			}
			boolean w=false;
			if(cont) {
				for(int i=0; i<rec.length && !w; i++) {
					for(int j=0; j<rec[0].length && !w; j++) {
						if(rec[i][j].getCaracter().equals(" < ")) {
							w=true;
						}
					}
				}
			}
			int g=0;
			if(!w) {
				g=ganar();
				w=false;
			}
			if(g==10) {
				finDelJuego(true);
			}
			this.repaint();
		}
	}
	public void finDelJuego(boolean b) {
		if(b) {
			JOptionPane.showMessageDialog(this, "¡Has ganado!" , "¡Felicidades!", JOptionPane.YES_NO_OPTION);
			System.exit(ABORT);
			
		} else {
			JOptionPane.showMessageDialog(this, "¡Has encontrado una mina!" , "¡Mala suerte!", JOptionPane.YES_NO_OPTION);
			System.exit(ABORT);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for(int i=0; i<rec.length; i++) {
			for(int j=0; j<rec[0].length; j++) {
				rec[i][j].paint(g2d);
			}
		}
		raton.paint(g2d);
	}
	
	public int ganar() {
		int ganar=0, banderas=0;
		for(int i=0; i<rec.length; i++) {
			for(int j=0; j<rec[i].length; j++) {
				if(rec[i][j].getCaracter().equals(" < ")) {
					banderas++;
				}
				if(rec[i][j].getCaracter().equals("   ") || (rec[i][j].getCaracter().equals(" < ") && referencia[i][j].equals(" + "))) {
					ganar++;
					System.out.println(ganar);
				}
			}
		}
		if(ganar==10 && banderas<=10) {
			System.out.println("Has ganado!");
			return ganar;
		} else {
			return 0;
		}
	}
	
	public boolean descubrir(boolean cont, int fila, int colum) {
		boolean mina=false;
		if(!rec[fila][colum].getCaracter().equals(" < ")) {
			if(cont) {
				for(int i=0; i<referencia.length; i++) {
					Arrays.fill(referencia[i], " · ");
				}
				for(int i=0; i<10; i++) {
					boolean det=false;
					do {
						int fil=rand.nextInt(9);
						int col=rand.nextInt(9);
						if(referencia[fil][col].equals(" + ") || ((col==colum||col==colum+1||col==colum-1)&&(fil==fila||fil==fila+1||fil==fila-1))) {
							det=true;
						} else {
							referencia[fil][col]=" + ";
							det=false;
						}
					}while(det);
				}
				for(int i=0; i<referencia.length; i++) {
					for(int j=0; j<referencia[i].length; j++) {
						if(referencia[i][j].equals(" + ")) {
							for(int k=i-1; k<=i+1; k++) {
								for(int l=j-1; l<=j+1; l++) {
									int contM=1;
									if((k>=0&&k<=8) && (l>=0&&l<=8)) {
										if(!referencia[k][l].equals(referencia[i][j])) {
											if(!referencia[k][l].equals(" · ")) {
												int contM2=Integer.parseInt(referencia[k][l].trim());
												contM2++;
												referencia[k][l]=" "+contM2+" ";
												
											}else {
												referencia[k][l]=" "+contM+" ";
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(referencia[fila][colum].equals(" · ")) {
				descEspOptimizado(fila, colum);
			} else if(referencia[fila][colum].equals(" + ")){
				mina=true;
				System.err.println("Has encontrado una mina!");
			}
			rec[fila][colum].setCaracter(referencia[fila][colum]);
		}
		return mina;
	}

	public void descEspOptimizado(int f, int c) {
		if(referencia[f][c].equals(" · ")) {
			Stack nodosAVisitar=new Stack();
			nodosAVisitar.push(f+","+c);
			while(nodosAVisitar.size()!=0) {
				String nodoEncurso=(String) nodosAVisitar.pop();
				String[] pos=nodoEncurso.split(",");
				int f2=Integer.parseInt(pos[0]);
				int c2=Integer.parseInt(pos[1]);
				for(int i=f2-1; i<=f2+1; i++) {
					for(int j=c2-1; j<=c2+1; j++) {
						if(i>=0 && i<=8 && j>=0 && j<=8) {
							if(!(i==f2 && j==c2) && rec[i][j].getCaracter().equals("   ")) {
								rec[i][j].setBotonPulsado(1);
								rec[i][j].setCaracter(referencia[i][j]);
								if(rec[i][j].getCaracter().equals(" · ")) {
									nodosAVisitar.push(i+","+j);
									System.out.println(nodosAVisitar.size());
								}
							}
						}
					}
				}
			}
		}
	}
}
