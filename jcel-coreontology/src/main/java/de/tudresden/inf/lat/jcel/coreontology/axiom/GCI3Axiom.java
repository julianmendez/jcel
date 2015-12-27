package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface GCI3Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the class on the left-hand part of the axiom.
	 * 
	 * @return the class on the left-hand part of the axiom
	 */
	int getClassInSubClass();

	/**
	 * Returns the object property on the left-hand part of the axiom.
	 * 
	 * @return the object property on the left-hand part of the axiom
	 */
	int getPropertyInSubClass();

	/**
	 * Returns the superclass in the axiom.
	 * 
	 * @return the superclass in the axiom
	 */
	int getSuperClass();

}