package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface GCI2Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the class in the right-hand part of the axiom.
	 * 
	 * @return the class in the right-hand part of the axiom
	 */
	int getClassInSuperClass();

	/**
	 * Returns the object property on the right-hand part of the axiom.
	 * 
	 * @return the object property on the right-hand part of the axiom
	 */
	int getPropertyInSuperClass();

	/**
	 * Returns the subclass in the axiom.
	 * 
	 * @return the subclass in the axiom
	 */
	int getSubClass();

}