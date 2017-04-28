
## Release notes

| version | release date | Java | OWL API       | Protégé       |
|:--------|:-------------|:----:|:--------------|:--------------|
| v0.25.0 | (unreleased) | 8    | 5.0.0         | (TBD)         |
| v0.24.1 | 2017-04-28   | 8    | 4.2.8         | 5.2.0         |
| v0.24.0 | 2016-07-08   | 8    | 4.2.5         | 5.0.0         |
| v0.23.2 | 2015-12-24   | 7    | 4.1.3         | 5.0.0-beta-21 |
| v0.23.1 | 2015-12-23   | 7    | 3.5.1         | 5.0.0-beta-17 |
| v0.23.0 | 2015-11-30   | 7    | 4.1.3         | 5.0.0-beta-21 |
| v0.22.0 | 2015-08-24   | 7    | 3.5.1         | 5.0.0-beta-17 |
| v0.21.0 | 2015-04-13   | 7    | 3.5.0         | 4.3.0         |
| v0.20.0 | 2014-05-18   | 7    | 3.5.0         | 4.3.0         |
| v0.19.1 | 2013-07-16   | 6    | 3.4.4         | 4.1.0         |
| v0.19.0 | 2013-06-25   | 6    | 3.4.4         | 4.1.0         |
| v0.18.2 | 2013-03-05   | 6    | 3.4.2         | 4.1.0         |
| v0.18.1 | 2013-02-14   | 6    | 3.4.2         | 4.1.0         |
| v0.18.0 | 2012-12-02   | 6    | 3.4.1         | 4.1.0         |
| v0.17.1 | 2012-05-04   | 6    | 3.2.4         | 4.1.0         |
| v0.17.0 | 2012-04-16   | 6    | 3.2.4         | 4.1.0         |
| v0.16.1 | 2012-01-09   | 6    | 3.2.4         | 4.1.0         |
| v0.16.0 | 2011-10-31   | 6    | 3.2.4         | 4.1.0         |
| v0.15.0 | 2011-07-11   | 6    | 3.2.3         | 4.1.0         |
| v0.14.0 | 2011-04-01   | 6    | 2.2.0 , 3.2.2 | 4.1.0         |
| v0.13.0 | 2011-03-11   | 6    | 2.2.0 , 3.2.2 | 4.1.0         |
| v0.12.0 | 2011-03-01   | 6    | 2.2.0 , 3.2.2 | 4.1.0         |
| v0.11.0 | 2011-01-01   | 6    | 2.2.0 , 3.1.0 | 4.1.0         |
| v0.10.0 | 2010-11-01   | 6    | 2.2.0 , 3.1.0 | 4.1.0         |
| v0.9.0  | 2010-10-28   | 6    | 2.2.0 , 3.0.0 | 4.0.2         |
| v0.8.0  | 2010-04-30   | 6    | 2.2.0 , 3.0.0 | 4.0.2         |
| v0.7.0  | 2010-04-14   | 6    | 2.2.0 , 3.0.0 | 4.0.2         |
| v0.6.0  | 2010-03-23   | 6    | 2.2.0 , 3.0.0 | 4.0.2         |
| v0.5.0  | 2010-02-26   | 6    | 2.2.0         | 4.0.2         |



### v0.25.0
*(unreleased)*
* uses the OWL API 5.0.0
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.25.0.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.25.0.jar`


### v0.24.1
*2017-04-28*
* uses the OWL API 4.2.8
* can be used as a plug-in for Protégé 5.2.0
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.24.1.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.24.1.jar`


### v0.24.0
*(2016-07-08)*
* runs on Java 8
* uses the OWL API 4.2.5
* can be used as a plug-in for Protégé 5.0.0
* fixes module extractor
* improves annotations in normalized axioms
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.24.0.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.24.0.jar`


### v0.23.2
*(2015-12-24)*
* uses the OWL API 4.1.3
* can be used as a plug-in for Protégé 5.0.0-beta-21
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.23.2.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.23.2.jar`


### v0.23.1
*(2015-12-23)*
* uses the OWL API 3.5.1
* can be used as a plug-in for Protégé 5.0.0-beta-17
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.23.1.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.23.1.jar`


### v0.23.0
*(2015-11-30)*
* does not longer have [Apache Ant + Apache Ivy](https://ant.apache.org/ivy/) build files
* uses the OWL API 4.1.3
* can be used as a plug-in for Protégé 5.0.0-beta-21
* the plug-in for Protégé can be used as a library
* the module `jcel-build` has been removed
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.23.0.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.23.0.jar`


### v0.22.0
*(2015-08-24)*
* POM files changed: the new `groupId` is `de.tu-dresden.inf.lat.jcel`
* includes Maven POM files to be deployed in [Sonatype](https://oss.sonatype.org/)
* is available at [The Central Repository](https://repo1.maven.org/maven2/de/tu-dresden/inf/lat/jcel/)
* module `jcel-build` contains only [Apache Ant + Apache Ivy](https://ant.apache.org/ivy/) configuration files, and its submodules became modules:
 * `jcel-library` : creates the library
 * `jcel-plugin` : creates the plug-in for Protégé
 * `jcel-standalone` : creates a standlone application
 * `jcel-distribution` : creates the release 
* uses the OWL API 3.5.1, and can be used as a plug-in for Protégé 5.0.0-beta-17
* build commands:
```
$ mvn clean install
```
* release: `target/jcel-0.22.0.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.22.0.jar`
* or
```
$ ant
```
* release: `jcel-build/target/jcel-0.22.0.zip`
* plug-in: `jcel-plugin/target/de.tu-dresden.inf.lat.jcel-0.22.0.jar`


### v0.21.0
*(2015-04-13)*
* normalizes the ontology propagating the annotations
* build commands:
```
$ cd jcel
$ mvn clean install
```
* or
```
$ cd jcel
$ ant
```
* release: `jcel/jcel-build/target/jcel-0.21.0.zip`
* plug-in: `jcel/jcel-build/jcel-plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.20.0
*(2014-05-18)*
* uses [Apache Ivy](https://ant.apache.org/ivy/) to compile the project
* build commands:
```
$ cd jcel/jcel-build
$ ant
```
* release: `jcel/jcel-build/target/jcel-0.20.0.zip`
* plug-in: `jcel/jcel-build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.19.1
*(2013-07-16)*
* includes a configurable timeout for the classification phase
* build commands:
```
$ cd jcel/jcel-build
$ ant
```
* release: `jcel/jcel-build/target/jcel-0.19.1.zip`
* plug-in: `jcel/jcel-build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.19.0
*(2013-06-25)*
* is dual-licensed, i.e. released under two different licenses: [GNU Lesser General Public License version 3](https://www.gnu.org/licenses/lgpl-3.0.txt) and [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)
* has subprojects with a different name convention: they use hyphen ("`jcel-`") instead of dot ("`jcel.`")
* shows a progress monitor when executed in Protégé
* includes new parameters in the console mode, including consistency, sat, classification, and entailment
* uses OWL API 3.4.4 and JUnit 4.11
* build commands:
```
$ cd jcel/jcel-build
$ ant
```
* release: `jcel/jcel-build/target/jcel-0.19.0.zip`
* plug-in: `jcel/jcel-build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.18.2
*(2013-03-05)*
* fixes a bug in processing Nothing (bottom class)
* performs a faster classification of small ontologies
* build commands:
```
$ cd jcel/jcel.build
$ ant
```
* release: `jcel/jcel.build/target/jcel-0.18.2.zip`
* plug-in: `jcel/jcel.build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.18.1
*(2013-02-14)*
* fixes a bug in the retrieval of subclasses and superclasses of Nothing (bottom class) and Thing (top class)
* build commands:
```
$ cd jcel/jcel.build
$ ant
```
* release: `jcel/jcel.build/target/jcel-0.18.1.zip`
* plug-in: `jcel/jcel.build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.18.0
*(2012-12-02)*
* classifies faster and using less memory
* fixes a bug in entailment of subclasses
* uses a more efficient way of processing the S-entries and R-entries
* has a single subproject for the build process, `jcel.build`, which includes the following subdirectories:
 * `jars` : contains the external jars needed by the build
 * `library` : creates the library
 * `plugin` : creates the plug-in for Protégé
 * `standalone` : creates a standlone application
* build commands:
```
$ cd jcel/jcel.build
$ ant
```
* release: `jcel/jcel.build/target/jcel-0.18.0.zip`
* plug-in: `jcel/jcel.build/plugin/target/de.tudresden.inf.lat.jcel.jar`


### v0.17.1
*(2012-05-04)*
* corrects the retrieval of equivalent classes
* excludes the original class when computing ancestors and descendants
* includes new unit tests in `jcel.reasoner` and `jcel.core`
* build commands:
```
$ cd jcel/jcel.distribution
$ ant
```
* release: `jcel/jcel.distribution/target/jcel-0.17.1.zip`
* plug-in: `jcel/jcel.bundle/target/de.tudresden.inf.lat.jcel.jar`


### v0.17.0
*(2012-04-16)*
* has a more efficient use of memory
* has the following modules:
 * `jcel.coreontology` : set of normalized axioms
 * `jcel.core` : classification algorithms using only normalized axioms
 * `jcel.ontology` : set of all possible axioms and a procedure to normalize them
 * `jcel.reasoner` : reasoner that can classify an ontology and can compute entailment
 * `jcel.owlapi` : OWL API 3.2.4 interface, performs the translation between the OWL API axioms and jcel axioms
 * `jcel.protege` : module to connect to Protégé
and the following modules are used to build the release:
 * `jcel.jars` : local repository of required jars
 * `jcel.bundle` : module to create the bundle, i.e., the jar for Protégé
 * `jcel.distribution` : module to produce the release, which includes libs, javadoc, sources, and the bundle
* build commands:
```
$ cd jcel/jcel.distribution
$ ant
```
* release: `jcel/jcel.distribution/target/jcel-0.17.0.zip`
* plug-in: `jcel/jcel.bundle/target/de.tudresden.inf.lat.jcel.jar`


### v0.16.1
*(2012-01-09)*
* fixes a bug in entailment present in version 0.16.0
* build commands:
```
$ cd jcel/jcel.protege
$ ant -buildfile build-bundle.xml
```
* release: `jcel/jcel.protege/target/de.tudresden.inf.lat.jcel.jar`


### v0.16.0
*(2011-10-31)*
* can answer complex queries
* has a new package in `jcel.core` (reasoner) to answer the queries, changing the interface between `jcel.owlapi` and `jcel.core`
* has unique identifiers in the core managed by a factory, making extension and debugging easier
* build commands:
```
$ cd jcel/jcel.protege
$ ant -buildfile build-bundle.xml
```
* release: `jcel/jcel.protege/target/de.tudresden.inf.lat.jcel.jar`


### v0.15.0
*(2011-07-11)*
* breaks the backwards compatibility with OWL API 2.2.0
* cannot be used in Protégé 4.0.2
* uses a new module called `jcel.ontology` which contains the axioms extracted from the core, therefore the modules are:
 * `jcel.ontology`: interfaces and classes to manage an internal representation of axioms and ontologies
 * `jcel.core`: interfaces and classes implementing the algorithms used by the reasoner
 * `jcel.owlapi`: interfaces and classes connecting with the OWL API 3.2.3
 * `jcel.protege`: interfaces and classes connecting with Protege 4.1 (build 235)
* build commands:
```
$ cd jcel/jcel.protege
$ ant -buildfile build-bundle.xml
```
* release: `jcel/jcel.protege/target/de.tudresden.inf.lat.jcel.jar`
 

### v0.14.0
*(2011-04-01)*
* is identical to v0.13.0 in the Java source code, but is created using a different repository structure.
* can be compiled using [Apache Maven](https://maven.apache.org/).
* build commands:
```
$ cd jcel/protege
$ ant -buildfile build-bundle.xml
```
* release: `jcel/protege/target/de.tudresden.inf.lat.jcel.jar`


### v0.13.0
*(2011-03-11)*
* classifies ELHIfR+ using less memory
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.12.0
*(2011-03-01)*
* can classify the description logic ELHIfR+
* uses the OWL API 3.2.3 and can be used in Protégé 4.1 beta (build 218)
* can classify SNOMED CT faster
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.11.0
*(2011-01-01)*
* includes a rule-based algorithm
* can classify SNOMED CT faster
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.10.0
*(2010-11-01)*
* is compatible with the OWL API 3.1.0, and still with the OWL API 2.2.0, but not with the OWL API 3.0.0
* can still be used in Protégé 4.0.2 and in Protégé 4.1 beta (build 213)
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.9.0
*(2010-10-28)*
* has a check for non-nullity for every public method
* is a little faster
* has a smaller output avoiding singletons for equivalent classes and equivalent object properties
* can still be used in Protégé 4.0.2 and in Protégé 4.1 (up to build 206)
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.8.0
*(2010-04-30)*
* includes a module processor which divides a set of axioms in modules
* has an improved interface in the core which simplifies its extension
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.7.0
*(2010-04-14)*
* can be used in Protégé 4.0.2 and in Protégé 4.1 (build 102)
* has improvements in the core to reduce the use of memory
* generates an inferred ontology that is compatible with the OWL 2 specification
* can classify large ontologies like SNOMED CT
* build commands:
```
$ cd jcel-jars
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.6.0
*(2010-03-23)*
* passes the basic tests for an OWL 2 EL reasoner (albeit currently with some limitations)
* uses the OWL API 3.0.0
* uses an adapter to keep backward compatibility with OWL API 2.2.0
* can be used in Protégé 4.0.2
* has improvements in the core to reduce classification times
* has the following modules:
 * `jcel-core` : core, module that makes the classification
 * `jcel-owlapi` : an OWL API 3 interface, transforms data between the OWL API and the internal representation used by the core
 * `jcel-adapter` : adapter to transform an OWL API 3 into an OWL API 2, it is an independent module
 * `jcel-protege` : module to make jcel a plug-in for Protégé 4.0.2
* build commands:
```
$ cd de.tudresden.inf.lat.jcel.protege
$ ant
```
* release: `de.tudresden.inf.lat.jcel.protege/dist/bundle/de.tudresden.inf.lat.jcel.jar`


### v0.5.0
*(2010-02-26)*
* is the first beta version
* can be used in Protégé 4.0.2
* uses the OWL API 2.2.0
* has the following modules:
 * `jcel-core` : core
 * `jcel-protege` : module to make jcel a plug-in for Protégé 4.0.2
* build commamds:
```
$ cd de.tudresden.inf.lat.jcel
$ ant
```
* release: `de.tudresden.inf.lat.jcel/dist/bundle/de.tudresden.inf.lat.jcel.jar`


