package net.resultadofinal.micomercial.model.contable;

import java.util.ArrayList;
import java.util.List;

public class UtilAsientoContable {
	private List<Integer> cuentas;
	private List<Float> debes;
	private List<Float> haberes;
	public List<Integer> getCuentas() {
		return cuentas;
	}
	public void setCuentas(List<Integer> cuentas) {
		this.cuentas = cuentas;
	}
	public List<Float> getDebes() {
		return debes;
	}
	public void setDebes(List<Float> debes) {
		this.debes = debes;
	}
	public List<Float> getHaberes() {
		return haberes;
	}
	public void setHaberes(List<Float> haberes) {
		this.haberes = haberes;
	}
	public UtilAsientoContable() {
		super();
		cuentas = new ArrayList<Integer>();
		debes = new ArrayList<Float>();
		haberes = new ArrayList<Float>();
	}
	public void addTransactionSimple(Integer cuentaDebe,Integer cuentaHaber, Float monto) {
		addDetalle(cuentaDebe, monto, true);//detalle asiento debe
		addDetalle(cuentaHaber, monto, false);//detalle Asiento haber
	}
	public void addDetalle(Integer cuenta,Float monto,boolean esDebe) {
		cuentas.add(cuenta);
		debes.add(esDebe?monto:0f);
		haberes.add(esDebe?0f:monto);
	}
	public Integer[] getVectorCuenta() {
		return cuentas.toArray(new Integer[cuentas.size()]);
	}
	public Float[] getVectorDebe() {
		return debes.toArray(new Float[debes.size()]);
	}
	public Float[] getVectorHaber() {
		return haberes.toArray(new Float[haberes.size()]);
	}
	public void clear() {
		cuentas.clear();
		debes.clear();
		haberes.clear();
	}
}
