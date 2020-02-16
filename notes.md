# Building and Execution
1. Used JDK `Java version: 1.8.0_191, vendor: Oracle Corporation` and Maven 3.6.0. Usual `mvn clean test` to compile and tests, `mvn exec:java` to run.
2. 

# Implementation Notes
1. Used https://projectlombok.org/ to auto-generate boilerplate code, including `equals()` and `hashCode()` methods for the domain objects.
2. Implementing subclasses/lambdas for each type of promotions would be boring so I've decided to try SpEL (Spring Expression Language - https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html) without Spring. The result is surprisingly convenient and flexible - up to the point that we're able to create the new types of `Promotion` runtime via REPL/CLI. Actually, any expression language would do but SpEL is, probably, the most reliable and resilient (especially in terms of the Context object interpretation).
3. Each Discount (Promotion) can be applied only once: for example, if basket contains 4 tins of soup, the customer will not get his loaf of bread free. Not a big deal to make multi-discount application anyway.