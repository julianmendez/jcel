package de.tudresden.inf.lat.jcel.core.algorithm.module;

import java.util.Set;

import de.tudresden.inf.lat.jcel.coreontology.axiom.NormalizedIntegerAxiom;

/**
 * This models a wrapper for a normalized axiom.
 * 
 * @author Julian Mendez
 *
 */
public interface ExtendedNormalizedAxiom {

	/**
	 * Returns the normalized axiom.
	 * 
	 * @return the normalized axiom
	 */
	NormalizedIntegerAxiom getAxiom();

	/**
	 * Returns the class identifiers found on the left-hand side of the given
	 * axiom.
	 *
	 * @return the class identifiers found on the left-hand side of the given
	 *         axiom
	 */
	Set<Integer> getClassesOnTheLeft();

	/**
	 * Returns the class identifiers found on the right-hand side of the given
	 * axiom.
	 * 
	 * @return the class identifiers found on the right-hand side of the given
	 *         axiom
	 */
	Set<Integer> getClassesOnTheRight();

	/**
	 * Returns the object property identifiers found on the left-hand side of
	 * the given axiom.
	 * 
	 * @return the object property identifiers found on the left-hand side of
	 *         the given axiom
	 */
	Set<Integer> getObjectPropertiesOnTheLeft();

	/**
	 * Returns the object property found on the right-hand side of the given
	 * axiom.
	 * 
	 * @return the object property identifiers found on the right-hand side of
	 *         the given axiom
	 */
	Set<Integer> getObjectPropertiesOnTheRight();

}
