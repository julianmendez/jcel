package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface RI3Axiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the object property on the left-hand part of the composition.
	 * 
	 * @return the object property on the left-hand part of the composition
	 */
	int getLeftSubProperty();

	/**
	 * Returns the object property on the right-hand part of the composition.
	 * 
	 * @return the object property on the right-hand part of the composition
	 */
	int getRightSubProperty();

	/**
	 * Returns the super object property.
	 * 
	 * @return the super object property
	 */
	int getSuperProperty();

}