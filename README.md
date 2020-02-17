# Building and Execution
1. Used JDK `Java version: 1.8.0_191, vendor: Oracle Corporation` and Maven 3.6.0. 

Please use usual `mvn clean test` to compile and test, `mvn exec:java` to run.
2. Each Discount (Promotion) can be applied only once: for example, if basket contains 4 tins of soup, the customer will not get his loaf of bread free. Not a big deal to make multi-discount application anyway.

# Implementation Notes
1. The https://projectlombok.org/ is being used to auto-generate boilerplate code.
2. Implementing subclasses/lambdas for each type of promotions would be boring so I've decided to try SpEL (Spring Expression Language - https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html) without Spring. The result is surprisingly convenient and flexible, up to the point that we're able to create the new types of `Product`s and `Promotion`s runtime via REPL/CLI. Any expression language would work here but SpEL is, probably, the most reliable and resilient (especially in terms of the Context object interpretation).
3. Initially I planned to use JLine (https://github.com/jline/jline3) to implement a nice REPL but it seems that it might require some additional magic to work on Windows. As I do not know which OS will be used for testing, I had to implement my own home-made REPL. Sorry for the inconvenience.
4. Java Money (`org.javamoney.moneta:moneta-core`) is being used internally for the money-related operations. 

----------
# Original Notes: Java Exercise

This is a simple exercise to allow you to demostrate your software engineering skillset. It's completly up to you how long you give yourself, stop when you're happy with the quality of your work, but we don't expect it to take too long.

## Instructions
  1. Please fork this repository and work on your fork.
     _https://help.github.com/en/articles/fork-a-repo_
  2. Commit changes frequently so that we can review your progression with you later.
     _https://crealytics.com/blog/5-reasons-keeping-git-commits-small/_
  3. You should use a recognised build tool, e.g. _gradle_, _maven_...
  4. We are currently using [open JDK 8u181](https://cdn.azul.com/zulu/bin/zulu8.31.0.1-jdk8.0.181-win_x64.msi). But please use the JDK you think is most appropriate to demonstrate your skillset. 
  5. We don't expect 100% code coverage, we expect well tested code.
     _https://medium.com/@nicklee1/why-test-code-coverage-targets-are-a-bad-idea-1b9b8ef711ef_
  6. Any other tools or libraries used must be freely available. Any third-party jars must be accessible via [maven central](https://mvnrepository.com/repos/central).
  7. The specification is below.
  
## Specification

A local shop, Henry’s Grocery, has asked you to author an IT solution for them to price up a basket of shopping for their customers.

Henry’s Grocery, currently only stocks four items and has two promotions. These are as follows:

### Stock Items
        
|  **product** | **unit**   | **cost** |
| :---  | :---: | :---: |
|  soup    | tin    | 0.65 |
|  bread   | loaf   | 0.80 |
|  milk    | bottle | 1.30 |
|  apples  | single | 0.10 |

### Discounts
 
| **the offer**| **valid from** | **valid to** | 
| :---     | :---: | :---: |    
| Buy 2 tins of soup and get a loaf of bread half price | yesterday | for 7 days |
| Apples have a 10% discount | from 3 days hence | until the end of the following month |

### Inputs
 All basket items added via the command prompt.

### Outputs
All outputs must print to the command line.
     
### Tests
   - Price a basket containing: 3 tins of soup and 2 loaves of bread, bought today, 
     - Expected total cost = 3.15;
   - Price a basket containing: 6 apples and a bottle of milk, bought today, 
     - Expected total cost = 1.90;
   - Price a basket containing: 6 apples and a bottle of milk, bought in 5 days time,
     - Expected total cost = 1.84;
   - Price a basket containing: 3 apples, 2 tins of soup and a loaf of bread, bought in 5 days time,
     - Expected total cost = 1.97.
 
 
