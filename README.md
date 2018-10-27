# [jcel](https://julianmendez.github.io/jcel/)

[![build](https://travis-ci.org/julianmendez/jcel.png?branch=master)](https://travis-ci.org/julianmendez/jcel)
[![maven central](https://maven-badges.herokuapp.com/maven-central/de.tu-dresden.inf.lat.jcel/jcel-parent/badge.svg)](https://search.maven.org/#search|ga|1|g%3A%22de.tu-dresden.inf.lat.jcel%22)
[![scrutinizer](https://scrutinizer-ci.com/g/julianmendez/jcel/badges/quality-score.png?b=master)](https://scrutinizer-ci.com/g/julianmendez/jcel/?branch=master)
[![license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![license](https://img.shields.io/badge/license-LGPL%203.0-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0.txt)
[![download](https://img.shields.io/sourceforge/dm/jcel.svg)](http://sourceforge.net/projects/jcel/files/)
[![follow](https://img.shields.io/twitter/follow/jcelreasoner.svg?style=social)](https://twitter.com/jcelreasoner)


**jcel** is a reasoner for the [description logic](http://dl.kr.org) EL+. It uses the [OWL API](https://owlcs.github.io/owlapi/) and can be used as a plug-in for [Protege](https://protege.stanford.edu/).


## Download

* [all-in-one ZIP file](https://sourceforge.net/projects/jcel/files/jcel/0.24.1/zip/jcel-0.24.1.zip/download)
* [The Central Repository](https://repo1.maven.org/maven2/de/tu-dresden/inf/lat/jcel/)
* [older releases](https://sourceforge.net/projects/jcel/files/)
* as dependency:

```xml
<dependency>
  <groupId>de.tu-dresden.inf.lat.jcel</groupId>
  <artifactId>jcel-owlapi</artifactId>
  <version>0.24.1</version>
</dependency>
```


## Author

[Julian Mendez](https://julianmendez.github.io)


## Licenses

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt), [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.txt)


## Main features

* is an [OWL 2 EL](https://www.w3.org/2007/OWL/wiki/OWL_Working_Group) reasoner (albeit currently with some limitations)
* uses the [OWL API](http://owlapi.sourceforge.net)
* can be used in [Prot&eacute;g&eacute;](https://protege.stanford.edu)
* is free software and is licensed under [GNU Lesser General Public License version 3](https://www.gnu.org/licenses/lgpl.txt) and [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)
* is fully implemented in [Java](https://www.oracle.com/java/technologies/java-se.html)
* evaluated by the [SEALS Community](https://www.seals-project.eu/news/storage-and-reasoning-systems-news) having the lowest Average Reasoning Time in 2010


## Suggested technologies

* [Java Development Kit 8](https://java.sun.com/) (or higher)
* [Apache Maven 3.0.3](https://maven.apache.org/) (or higher)
* [Eclipse Mars](https://www.eclipse.org/) (or higher)


## Source code

The project is hosted on [GitHub](https://github.com/julianmendez/jcel). To checkout and compile the project with [Apache Maven](https://maven.apache.org/):

```
$ git clone https://github.com/julianmendez/jcel.git
$ cd jcel
$ mvn clean install
```

The library, its sources and its Javadoc will be in `jcel-library/target`, the plug-in will be in `jcel-plugin/target`, the standalone will be in `jcel-standalone/target`, and the release ZIP file will be in `target`.

To compile the project offline, first download the dependencies:

```
$ mvn dependency:go-offline
```

and once offline, use:

```
$ mvn --offline clean install
```

The bundles uploaded to [Sonatype](https://oss.sonatype.org/) are created with:

```
$ mvn clean install -DperformRelease=true
```

and then on each module:

```
$ cd target
$ jar -cf bundle.jar jcel-*
```

and on the main directory:

```
$ cd target
$ jar -cf bundle.jar jcel-parent-*
```

The version number is updated with:

```
$ mvn versions:set -DnewVersion=NEW_VERSION
```

where *NEW_VERSION* is the new version.
The file [VersionInfo.java](https://github.com/julianmendez/jcel/blob/master/jcel-reasoner/src/main/java/de/tudresden/inf/lat/jcel/reasoner/main/VersionInfo.java) is updated manually.


## Architecture

### Modules

The version under development has the following modules:

* **jcel-coreontology** : set of normalized axioms
* **jcel-core** : classification algorithms using only normalized axioms
* **jcel-ontology** : set of all possible axioms and a procedure to normalize them
* **jcel-reasoner** : reasoner that can classify an ontology and can compute entailment
* **jcel-owlapi** : OWL API interface, performs the translation between the OWL API axioms and jcel axioms
* **jcel-protege** : module to connect to Protégé

It also has the following module used to build the release:

* **jcel-library** : module to create the library, its sources and its javadoc
* **jcel-plugin** : module to create the jar for Protégé
* **jcel-standalone** : module to create the standalone application
* **jcel-distribution** : module to create the release, a single ZIP file


### Algorithm

* translate OWL API axioms into jcel axioms, which are composed by integer numbers
* detect the expressivity used in the axioms
* apply normalization rules to the set of axioms, producing a set of normalized axioms and auxiliary entities if necessary
* saturate the set of normalized axioms with deduced object property axioms
* create an extended ontonlogy based on the set of normalized axioms
* apply the completion rules while there is some change in the affected sets
* remove the auxiliary entities
* build a graph containing direct subsumers (parents), direct subsumees (children) and equivalents
* create OWL API data structures based on the jcel's integer representation


### Package dependencies

If we consider each package as a node, and each dependency between two packages as a directed edge, the structure of package dependency should be a directed acyclic graph (DAG). The packages should only make public those classes that are or could be used by another package preserving the DAG structure.


## Example

This [file](https://github.com/julianmendez/jcel/blob/master/docs/data/start-jcel.sh.txt) is an example of how to start jcel.

This [file](https://github.com/julianmendez/jcel/blob/master/docs/data/example.owl) is an example ontology using ELHIfR+.

Ontologies:

* [Gene Ontology](http://www.geneontology.org/): input [owl](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/geneontology.owl.zip) [krss](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/go.cel.zip), output [xml](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/geneontology-inferred-0.12.0.xml.zip)
* [NCI Thesaurus](https://ncit.nci.nih.gov/ncitbrowser/): input [owl](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/nci.owl.zip), output [xml](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/nci-inferred-0.12.0.xml.zip)
* [CEL GALEN](http://www.opengalen.org/): input [owl](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen.owl.zip) [krss](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen.cel.zip), output [xml](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/celgalen-inferred-0.12.0.xml.zip)
* Not GALEN: input [owl](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen.owl.zip) [krss](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen.cel.zip), output [xml](https://lat.inf.tu-dresden.de/systems/jcel/ontologies/notgalen-inferred-0.12.0.xml.zip)
* [Foundational Model of Anatomy](http://sig.biostr.washington.edu/projects/fm/)
* [SNOMED CT](http://www.ihtsdo.org/our-standards/)


## Release notes
See [release notes](https://julianmendez.github.io/jcel/RELEASE-NOTES.html).


## Older versions

* [Git SHA](https://github.com/julianmendez/jcel/blob/master/docs/data/gitsha.txt) 
* [old SVN revision](https://github.com/julianmendez/jcel/blob/master/docs/data/svnrev.txt)
* [releases](https://sourceforge.net/projects/jcel/files/)


## More information

* [Frequently Asked Questions](https://julianmendez.github.io/jcel/docs/faq.html)


## References

* Theoretical foundation:
  * [Master's thesis by J. A. Mendez](https://lat.inf.tu-dresden.de/research/mas/Men-Mas-11.pdf)
  * [Master's thesis by Q. H. Vu](https://lat.inf.tu-dresden.de/research/mas/Vu-Mas-08.pdf)
  * [Ph.D. thesis by B. Suntisrivaraporn](https://lat.inf.tu-dresden.de/research/phd/Sun-PhD-09.pdf)
* CEL: [main page](https://lat.inf.tu-dresden.de/systems/cel) - [source code](https://github.com/julianmendez/cel)
* OWL API: [main page](http://owlapi.sourceforge.net/) - [examples](http://owlapi.sourceforge.net/documentation.html) — [javadoc](http://owlapi.sourceforge.net/javadoc)
* OWL 2: [OWL Working Group](https://www.w3.org/2007/OWL/wiki/OWL_Working_Group)


## Support

Any bug or unexpected behavior can be directly reported by sending a message to the author. Questions and suggestions are also very welcome.


## News
[@jcelreasoner](https://twitter.com/jcelreasoner)



