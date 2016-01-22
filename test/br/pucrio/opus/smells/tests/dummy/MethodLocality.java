package br.pucrio.opus.smells.tests.dummy;

public class MethodLocality {

	public void localA() {

	}

	public void localB() {

	}

	public void localC() {

	}
	
	public void localD() {

	}

	public void superLocal() {
		this.localA();
		this.localB();
		this.localC();
		this.localD();
	}
	
	public void superForeign(int a) {
		new String().toCharArray();
		new String().toLowerCase();
		Integer.bitCount(12);
	}
	
	public void moreLocal(int a, int b) {
		this.localA();
		this.localB();
		this.localC();
		this.localD();
		new String().toCharArray();
		new String().toLowerCase();
		Integer.bitCount(12);
	}
	
	public void moreForeign(int a, int b, int c) {
		this.moreLocal(a, b);
		this.localD();
		new String().toCharArray();
		new String().toLowerCase();
		Integer.bitCount(12);
	}
}
