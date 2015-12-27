package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface RI2Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the object property on the left-hand part of the axiom
	 * 
	 * @return the object property on the left-hand part of the axiom
	 */
	int getSubProperty();

	/**
	 * Returns the object property on the right-hand part of the axiom
	 * 
	 * @return the object property on the right-hand part of the axiom
	 */
	int getSuperProperty();

}