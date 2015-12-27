package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface GCI1Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the left subclass in the axiom.
	 * 
	 * @return the left subclass in the axiom
	 */
	int getLeftSubClass();

	/**
	 * Returns the right subclass in the axiom.
	 * 
	 * @return the right subclass in the axiom
	 */
	int getRightSubClass();

	/**
	 * Returns the superclass in the axiom.
	 * 
	 * @return the superclass in the axiom
	 */
	int getSuperClass();

}
