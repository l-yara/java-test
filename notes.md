# Building and Execution
1. Used JDK *Java version: 1.8.0_191, vendor: Oracle Corporation* and Maven 3.6.0. Usual `mvn clean test` to compile and tests.
2. 

# Implementation Notes
1. Used https://projectlombok.org/ to auto-generate boilerplate code, including `equals()` and `hashCode()` methods for the domain objects (hope you trust me that I know the spec for these two).
2. Each Discount (Promotion) can be applied only once: for example, if basket contains 4 tins of soup, the customer will not get his loaf of bread free. Not a big deal to make multi-discount application anyway.