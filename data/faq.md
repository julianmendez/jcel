
## Frequently asked questions


### What is jcel?
jcel is reasoner for description logics of the EL family.


### What is a reasoner?
A reasoner is a piece of software that can compute consequences derived from a set of axioms.


### What is a description logic?
Description logics are a family of languages used in knowledge representation. More expressive than propositional logic, description logics are the formal basics of the Semantic Web.


### What is the Semantic Web?
The Semantic Web is a set of technologies to give computers the ability to process the information on the World Wide Web, relating the content to capture its semantic meaning. The term was coined by Tim Berners-Lee as "a web of data that can be processed directly and indirectly by machines."


### What is Protégé?
Protégé is an open source ontology editor and framework for knowledge bases. It can be easily extended by adding plug-ins.


### What is a plug-in?
A plug-in is a software component that extends a larger software application.


### What can jcel classify?
jcel is able to classify description logics of the EL family. These are:


#### operational
* EL with GCIs : ⊤, A ⊓ B, ∃ r. A, A ⊑ B
* bottom : ⊥
* H : r ⊑ s
* R+ : r ∘ r ⊑ r
* role composition : r1 ∘ ... ∘ rn ⊑ s

#### experimental
* I : r-
* F : r functional
* ABox : C(a), r(a,b)
* O : {a}

#### planned
* (D) : p(x1, ... , xn)

#### not planned
* universal : ∀r.A
* disjunction : A ⊔ B
* C : ¬C
* N : (≤ n )
* Q : (≤ n C)


where
* n : natural number,
* A, B, C : concepts,
* s, r, r1, ..., rn : roles,
* a, b : individuals,
* p : concrete predicate,
* x1, ..., xn : concrete objects



### How can I use jcel?
jcel can be used in three different ways: as a Protégé plug-in, as a standalone application, or as a library.


### How can I download jcel?
Please refer to the download page.




