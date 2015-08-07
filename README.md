# [jcel](http://julianmendez.github.io/jcel/)

[![Build Status](https://travis-ci.org/julianmendez/jcel.png?branch=master)](https://travis-ci.org/julianmendez/jcel)

**jcel** is a reasoner for the [description logic](http://dl.kr.org) EL+. It uses the [OWL API](http://owlcs.github.io/owlapi/) and can be used as a plug-in for [Protege](http://protege.stanford.edu/).

Author: [Julian Mendez](http://lat.inf.tu-dresden.de/~mendez/)

Licenses: [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt), [LGPL 3.0](http://www.gnu.org/licenses/lgpl-3.0.txt)


## [download](http://lat.inf.tu-dresden.de/projects/jcel/files/jcel/0.21.0/zip/jcel-0.21.0.zip)


## main features

* is an [OWL 2 EL](http://www.w3.org/2007/OWL/wiki/OWL_Working_Group) reasoner (albeit currently with some limitations)
* uses the [OWL API](http://owlapi.sourceforge.net)
* can be used in [Prot&eacute;g&eacute;](http://protege.stanford.edu)
* is free software and is licensed under [GNU Lesser General Public License version 3](http://www.gnu.org/licenses/lgpl.txt) and [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
* is fully implemented in [Java](http://www.oracle.com/us/technologies/java/standard-edition/overview/index.html)
* evaluated by the [SEALS Community](http://www.seals-project.eu/news/storage-and-reasoning-systems-news) having the lowest Average Reasoning Time in 2010


## suggested technologies

* [Java Development Kit 8](http://java.sun.com/) (or higher)
* [Apache Maven 3.0.3](http://maven.apache.org/) (or higher)
* [Eclipse Luna](http://www.eclipse.org/) (or higher)


## source code

The project is hosted on [GitHub](https://github.com/julianmendez/jcel). To checkout and compile the project, use:

```
$ git clone https://github.com/julianmendez/jcel.git
$ cd jcel
$ mvn clean install
```

To compile with [Apache Maven](http://maven.apache.org/) to be used offline:
```
$ mvn dependency:go-offline
$ mvn --offline clean package
```

To compile with [Apache Ant + Apache Ivy](http://ant.apache.org/ivy/):
```
$ ant
```

The library, its sources and its Javadoc will be in `jcel-build/jcel-library/target`, the plug-in will be in `jcel-build/jcel-plugin/target`, the standalone will be in `jcel-build/jcel-standalone/target`, and the release ZIP file will be in `jcel-build/target`.


## modules

The version under development has the following modules:

* **jcel-coreontology** : set of normalized axioms
* **jcel-core** : classification algorithms using only normalized axioms
* **jcel-ontology** : set of all possible axioms and a procedure to normalize them
* **jcel-reasoner** : reasoner that can classify an ontology and can compute entailment
* **jcel-owlapi** : OWL API interface, performs the translation between the OWL API axioms and jcel axioms
* **jcel-protege** : module to connect to Protégé

It also has the following module used to build the release:

* **jcel-build** : module to produce the release, which includes javadoc, sources, the plug-in, the library, and the standalone application, including the following submodules:
 * **jcel-library** : submodule to create the library, its sources and its javadoc
 * **jcel-plugin** : submodule to create the jar for Protégé
 * **jcel-standalone** : submodule to create the standalone application
 * **jcel-release** : submodule to create the release, a single ZIP file


## example

This [file](http://julianmendez.github.io/jcel/data/start-jcel.sh.txt) is an example of how to start jcel.

This [file](http://julianmendez.github.io/jcel/data/example.owl) is an example ontology using ELHIfR+.

Ontologies:
* [Gene Ontology](http://www.geneontology.org/): input [owl](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/geneontology.owl.zip) [krss](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/go.cel.zip), output [xml](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/geneontology-inferred-0.12.0.xml.zip)
* [NCI Thesaurus](http://ncit.nci.nih.gov/): input [owl](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/nci.owl.zip), output [xml](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/nci-inferred-0.12.0.xml.zip)
* [CEL GALEN](http://www.opengalen.org/): input [owl](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen.owl.zip) [krss](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen.cel.zip), output [xml](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen-inferred-0.12.0.xml.zip)
* Not GALEN: input [owl](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen.owl.zip) [krss](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen.cel.zip), output [xml](http://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen-inferred-0.12.0.xml.zip)
* [Foundational Model of Anatomy](http://sig.biostr.washington.edu/projects/fm/)
* [SNOMED CT](http://www.ihtsdo.org/our-standards/)


## algorithm
* translate OWL API axioms into jcel axioms, which are composed by integer numbers
* detect the expressivity used in the axioms
* apply normalization rules to the set of axioms, producing a set of normalized axioms and auxiliary entities if necessary
* saturate the set of normalized axioms with deduced object property axioms
* create an extended ontonlogy based on the set of normalized axioms
* apply the completion rules while there is some change in the affected sets
* remove the auxiliary entities
* build a graph containing direct subsumers (parents), direct subsumees (children) and equivalents
* create OWL API data structures based on the jcel's integer representation


## package dependencies

If we consider each package as a node, and each dependency between two packages as a directed edge, the structure of package dependency should be a directed acyclic graph (DAG). The packages should only make public those classes that are or could be used by another package preserving the DAG structure.


## references

* Theoretical foundation:
 * [Master's thesis by J. A. Mendez](http://lat.inf.tu-dresden.de/research/mas/Men-Mas-11.pdf)
 * [Master's thesis by Q. H. Vu](http://lat.inf.tu-dresden.de/research/mas/Vu-Mas-08.pdf)
 * [Ph.D. thesis by B. Suntisrivaraporn](http://lat.inf.tu-dresden.de/research/phd/Sun-PhD-09.pdf)
* CEL: [main page](http://lat.inf.tu-dresden.de/systems/cel) - [source code](https://github.com/julianmendez/cel)
* OWL API: [main page](http://owlapi.sourceforge.net/) - [examples](http://owlapi.sourceforge.net/documentation.html) — [javadoc](http://owlapi.sourceforge.net/javadoc)
* OWL 2: [OWL Working Group](http://www.w3.org/2007/OWL/wiki/OWL_Working_Group)


## [release notes](http://julianmendez.github.io/jcel/data/releasenotes.md)


## [old versions](http://julianmendez.github.io/jcel/data/oldversions.md)


## [older releases](http://sourceforge.net/projects/jcel/files/)


## [Git SHA](http://julianmendez.github.io/jcel/data/gitsha.txt) 


## [old SVN revision](http://julianmendez.github.io/jcel/data/svnrev.txt)


## [frequently asked questions](http://julianmendez.github.io/jcel/data/faq.md)


## support

Any bug, or unexpected behavior can be reported to the e-mail addresses at this [web page](http://lat.inf.tu-dresden.de/~mendez). Questions and suggestions are also very welcome.


## news [@jcelreasoner](http://twitter.com/jcelreasoner)

(e-mail group: [jcel](https://groups.google.com/group/jcel?lnk=) google group ([join](https://groups.google.com/group/jcel/subscribe?note=1)))



