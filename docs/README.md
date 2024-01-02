# [jcel](https://julianmendez.github.io/jcel/)

[![License 1](https://img.shields.io/badge/License%201-Apache%202.0-blue.svg)][license1]
[![license 2](https://img.shields.io/badge/License%202-LGPL%203.0-blue.svg)][license2]
[![Maven Central](https://img.shields.io/maven-central/v/de.tu-dresden.inf.lat.jcel/jcel-parent.svg?label=Maven%20Central)][maven-central]
[![build](https://github.com/julianmendez/jcel/workflows/Java%20CI/badge.svg)][build-status]
[![download](https://img.shields.io/sourceforge/dm/jcel.svg)][releases]
[![follow](https://img.shields.io/twitter/follow/jcelreasoner.svg?style=social)][twitter]

**jcel** is a reasoner for the [description logic][description-logics] EL+. It uses the [OWL
API][owl-api] and can be used as a plug-in for [Protege][protege].


## Download

* [all-in-one ZIP file][zip-file]
* [The Central Repository][central-repository]
* [all releases][releases]
* as dependency:

```xml
<dependency>
  <groupId>de.tu-dresden.inf.lat.jcel</groupId>
  <artifactId>jcel-owlapi</artifactId>
  <version>0.24.1</version>
</dependency>
```


## Author

[Julian Alfredo Mendez][author]


## Licenses

[Apache 2.0][license1], [LGPL 3.0][license2]


## Main features

* is an [OWL 2 EL][owl-2-el] reasoner (albeit currently with some limitations)
* uses the [OWL API][owl-api]
* can be used in [Prot&eacute;g&eacute;][protege]
* is free software and is licensed under [Apache License, Version 2.0][license1] and
[GNU Lesser General Public License version 3][license2]
* is fully implemented in [Java][java]
* evaluated by the [SEALS Community](https://web.archive.org/web/20130723132747/http://www.seals-project.eu/news/storage-and-reasoning-systems-news) having the lowest Average Reasoning Time in 2010


## Suggested technologies

* [Java Development Kit 11][java] (or higher)
* [Apache Maven 3.6.3][maven] (or higher)


## Source code

The project is hosted on [GitHub][jcel-source].
To check out and compile the project with [Apache Maven][maven]:

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

The bundles uploaded to [Sonatype][sonatype] are created with:

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
The file [VersionInfo.java][jcel-version-java] is updated manually.


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
* create an extended ontology based on the set of normalized axioms
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

* [Gene Ontology](http://geneontology.org/): input [owl](https://sourceforge.net/projects/jcel/files/ontologies/geneontology.owl.zip) [krss](https://sourceforge.net/projects/jcel/files/ontologies/go.cel.zip), output [xml](https://sourceforge.net/projects/jcel/files/ontologies/geneontology-inferred-0.12.0.xml.zip)
* [NCI Thesaurus](https://ncit.nci.nih.gov/ncitbrowser/): input [owl](https://sourceforge.net/projects/jcel/files/ontologies/nci.owl.zip), output [xml](https://sourceforge.net/projects/jcel/files/ontologies/nci-inferred-0.12.0.xml.zip)
* [CEL GALEN](http://www.opengalen.org/): input [owl](https://sourceforge.net/projects/jcel/files/ontologies/celgalen.owl.zip) [krss](https://sourceforge.net/projects/jcel/files/ontologies/celgalen.cel.zip), output [xml](https://sourceforge.net/projects/jcel/files/ontologies/celgalen-inferred-0.12.0.xml.zip)
* Not GALEN: input [owl](https://sourceforge.net/projects/jcel/files/ontologies/notgalen.owl.zip), output [xml](https://sourceforge.net/projects/jcel/files/ontologies/notgalen-inferred-0.12.0.xml.zip)
* [Foundational Model of Anatomy](http://si.washington.edu/projects/fma)
* [SNOMED CT](https://www.snomed.org/)


## Release notes
See [release notes][release-notes].


## Older versions

* [Git SHA](https://github.com/julianmendez/jcel/blob/master/docs/data/gitsha.txt)
* [old SVN revision](https://github.com/julianmendez/jcel/blob/master/docs/data/svnrev.txt)
* [releases][releases]


## More information

* [Frequently Asked Questions][faq]


## References

* Theoretical foundation:
  * [Master's thesis by J. A. Mendez](https://lat.inf.tu-dresden.de/research/mas/Men-Mas-11.pdf)
  * [Master's thesis by Q. H. Vu](https://lat.inf.tu-dresden.de/research/mas/Vu-Mas-08.pdf)
  * [Ph.D. thesis by B. Suntisrivaraporn](https://lat.inf.tu-dresden.de/research/phd/Sun-PhD-09.pdf)
* CEL: [main page][cel-home] - [source code][cel-source]
* OWL API: [main page][owl-api]
* OWL 2: [OWL Working Group](https://www.w3.org/2007/OWL/wiki/OWL_Working_Group)


## Support

Any bug or unexpected behavior can be directly reported by sending a message to the author.
Questions and suggestions are also very welcome.
In case you need more information, please contact [julianmendez][author].


## News
[@jcelreasoner][twitter]

[author]: https://julianmendez.github.io
[license1]: https://www.apache.org/licenses/LICENSE-2.0.txt
[license2]: https://www.gnu.org/licenses/lgpl-3.0.txt
[maven-central]: https://search.maven.org/artifact/de.tu-dresden.inf.lat.jcel/jcel-owlapi
[build-status]: https://github.com/julianmendez/jcel/actions
[central-repository]: https://repo1.maven.org/maven2/de/tu-dresden/inf/lat/jcel/
[zip-file]: https://sourceforge.net/projects/jcel/files/jcel/0.24.1/zip/jcel-0.24.1.zip/download
[releases]: https://sourceforge.net/projects/jcel/files/
[release-notes]: https://julianmendez.github.io/jcel/RELEASE-NOTES.html
[faq]: https://julianmendez.github.io/jcel/docs/faq.html
[jcel-home]: https://julianmendez.github.io/jcel
[jcel-source]: https://github.com/julianmendez/jcel
[twitter]: https://twitter.com/jcelreasoner
[jcel-version-java]: https://github.com/julianmendez/jcel/blob/master/jcel-reasoner/src/main/java/de/tudresden/inf/lat/jcel/reasoner/main/VersionInfo.java
[sonatype]: https://oss.sonatype.org
[java]: https://www.oracle.com/java/technologies/
[maven]: https://maven.apache.org
[description-logics]: http://dl.kr.org
[owl-api]: https://owlcs.github.io/owlapi/
[owl-2-el]: https://www.w3.org/TR/owl2-profiles/#OWL_2_EL
[protege]: https://protege.stanford.edu
[cel-home]: https://julianmendez.github.io/cel
[cel-source]: https://github.com/julianmendez/cel


