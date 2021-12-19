package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Cuadrado {
	private int dimensiones;
	private String pos;
	private int x;
	private int y;
	private Cuadrado precursor;
	private JFrame f;
	private int botonPulsado;
	private String caracter;
	
	public Cuadrado(JFrame f) {
		this.f=f;
		this.dimensiones=(f.getWidth()-15)/9;
		this.x=0;
		this.y=0;
		this.pos="[0][0]";
		this.botonPulsado=0;
		this.caracter="   ";
	}
	
	public Cuadrado(JFrame f, String pos, Cuadrado p) {
		this.f=f;
		this.dimensiones=(f.getWidth()-15)/9;
		this.precursor=p;
		this.pos=pos;
		this.x=precursor.getX()+dimensiones;
		this.y=precursor.getY();
		this.botonPulsado=0;
		this.caracter="   ";
	}
	
	public void paint(Graphics2D g) {
		if(botonPulsado==1) {
			g.setColor(Color.LIGHT_GRAY);
		} else {
			g.setColor(Color.GRAY);
		}
		g.fillRect(x+(dimensiones/2)-((dimensiones-1)/2), y+(dimensiones/2)-((dimensiones-1)/2), dimensiones-1, dimensiones-1);
		if(!(caracter.equals("   ")) && !(caracter.equals(" · ")) ) {
			g.setColor(Color.RED);
			g.setFont(new Font("Verdana", Font.BOLD, 20));
			g.drawString(caracter, x+3, y+dimensiones-(20/2));
		}
	}

	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}

	public int getBotonPulsado() {
		return botonPulsado;
	}

	public void setBotonPulsado(int botonPulsado) {
		this.botonPulsado = botonPulsado;
	}

	public int getDimensiones() {
		return dimensiones;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x += x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y += y;
	}

	public Cuadrado getPrecursor() {
		return precursor;
	}

	public void setPrecursor(Cuadrado precursor) {
		this.precursor = precursor;
	}

	@Override
	public String toString() {
		return "Cuadrado [pos=" + pos + ", x=" + x + ", y=" + y + "]";
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, dimensiones, dimensiones);
	}
}
