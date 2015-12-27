package de.tudresden.inf.lat.jcel.coreontology.axiom;

public interface RangeAxiom extends NormalizedIntegerAxiom {

	/**
	 * Returns the object property in the axiom.
	 * 
	 * @return the object property in the axiom
	 */
	int getProperty();

	/**
	 * Returns the class identifier in the axiom.
	 * 
	 * @return the class identifier in the axiom
	 */
	int getRange();

}