package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface GCI0Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the subclass in this axiom.
	 * 
	 * @return the subclass in this axiom
	 */
	int getSubClass();

	/**
	 * Returns the superclass in this axiom.
	 * 
	 * @return the superclass in this axiom
	 */
	int getSuperClass();

}
