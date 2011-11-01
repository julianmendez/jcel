jcel
----



  Copyright 2009 Julian Mendez


  This file is part of jcel.

  jcel is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  jcel is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with jcel.  If not, see <http://www.gnu.org/licenses/>.


For more details about the license, please read the following files:
   * copying.txt : text of General Public License
   * copying-lesser.txt : text of Lesser General Public License



Description
-----------

This is a subproject of jcel. It contains the project's core, which is 
a reasoner to classify ontologies.



How to compile this project
---------------------------

In order to compile this project, the following technologies are suggested:

  * Development environment:
    Eclipse 3.5.0
    http://www.eclipse.org

  * Compiler :
    Sun Java Development Kit 6
    http://java.sun.com

  * Build tool :
    Apache Ant 1.7.1
    http://ant.apache.org


Once Java and Apache Ant are installed, it is enough to write "ant" in the main 
directory. Apache Ant reads the "build.xml" file and builds the project.

The jar will be generated in "dist/lib". This jar is needed by the main project.

The directories have the following structure.

  Source directories: (src)
    * src/doc : text for the javadocs
    * src/main : source code of the main part
    * src/manifest : the manifest.mf file
    * src/test : source code of the unit tests

  Generated directories: (dist)      
    * dist/bin : compiled classes
    * dist/javadoc : generated javadocs
    * dist/lib : generated jar file
    * dist/report : generated reports of unit tests
    * dist/test : compiled unit test classes



Contact information
-------------------

  If you have any question or suggestion, please contact:
    Julian Mendez
    julianmendez@sourceforge.net

