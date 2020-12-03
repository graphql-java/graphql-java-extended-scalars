# Extended Scalars for graphql-java

This library provides extended scalars for [graphql-java](https://github.com/graphql-java/graphql-java)

Scalars in graphql are the leaf nodes of a query, the non compound values that cant be queried further via sub field selections.

The graphql standard specifies that the `String`, `Int`, `Float`, `Boolean` and `ID` scalars must be present in a graphql type 
system but after that it is up to an implementation about what custom scalars are present.

You would use custom scalars when you want to describe more meaningful behavior or ranges of values.

To use this library put the following into your gradle config

    compile 'com.graphql-java:graphql-java-extended-scalars:1.0'
    
or the following into your Maven config

    <dependency>
      <groupId>com.graphql-java</groupId>
      <artifactId>graphql-java-extended-scalars</artifactId>
      <version>1.0</version>
    </dependency>
    
Then register the scalar with graphql-java

    RuntimeWiring.newRuntimeWiring().scalar(ExtendedScalars.DateTime)
    
And use it in your schema
    
    scalar DateTime
    type Something {
        someDateTime: DateTime
    }

## DateTime Scalars

* `DateTime`
  * An RFC-3339 compliant date time scalar that accepts string values like `1996-12-19T16:39:57-08:00` and produces 
  `java.time.OffsetDateTime` objects at runtime  
* `Time`
  * An RFC-3339 compliant time scalar that accepts string values like `16:39:57-08:00` and produces 
  `java.time.OffsetTime` objects at runtime  
* `Date`
  * An RFC-3339 compliant date scalar that accepts string values like `1996-12-19` and produces 
  `java.time.LocalDate` objects at runtime  

See [the rfc3339 spec](https://www.ietf.org/rfc/rfc3339.txt) for more details on the format.

An example declaration in SDL might be:

```graphql

    type Customer {
        birthDay : Date
        workStartTime : Time
        bornAt : DateTime
    }
    
    type Query {
        customers(bornAfter : DateTime) : [Customers]
    }
    
``` 

And example query might look like:
```graphql
    
    query {
        customers(bornAfter : "1996-12-19T16:39:57-08:00") {
            birthDay
            bornAt
        }
    }
    
``` 

## Object / JSON Scalars

* `Object`
  * An object scalar that accepts any object as a scalar value

* `JSON`
  * A synonym for the `Object` scalar, it will accept any object as a scalar value
  
One of the design goals of graphql, is that the type system describes the shape of the data returned.

The `Object` / `JSON` scalars work against this some what because they can return compound values outside the type system.  As such 
they should be used sparingly.  In general your should aim to describe the data via the graphql type system where you can and only
resort to the `Object` / `JSON` scalars in very rare circumstances. 

An example might be an extensible graphql system where systems can input custom metadata objects that cant be known at 
schema type design time.

An example declaration in SDL might be:

```graphql

    type Customer {
        name : String
        associatedMetaData : JSON
    }
    
    type Query {
        customers(filterSyntax : JSON) : [Customers]
    }
    
``` 

And example query might look like:

```graphql
    
    query {
        customers(filterSyntax : {
                startSpan : "First",
                matchCriteria : {
                    countryCode : "AU",
                    isoCodes : ["27B-34R", "95A-E23"],
                    
                }
            }) {
            name
            associatedMetaData    
        }
    }
    
``` 
   
Note : The `JSON` scalar is a simple alias type to the `Object` scalar because often the returned data is a blob of JSON.  They are 
all just objects at runtime in graphql-java terms and what network serialisation protocol is up to you.  Choose whichever name you think
adds more semantic readers to your schema consumers. 


## Numeric Scalars


* `PositiveInt`
  * An `Int` scalar that MUST be greater than zero   
* `NegativeInt`
  * An `Int` scalar that MUST be less than zero   
* `NonPositiveInt`
  * An `Int` scalar that MUST be less than or equal to zero   
* `NonNegativeInt`
  * An `Int` scalar that MUST be greater than or equal to zero   
* `PositiveFloat`
  * An `Float` scalar that MUST be greater than zero   
* `NegativeFloat`
  * An `Float` scalar that MUST be less than zero   
* `NonPositiveFloat`
  * An `Float` scalar that MUST be less than or equal to zero   
* `NonNegativeFloat`
  * An `Float` scalar that MUST be greater than or equal to zero   

The numeric scalars are derivations of the standard graphql `Int` and `Float` scalars that enforce range limits.

An example declaration in SDL might be:

```graphql

    type Customer {
        name : String
        currentHeight : PositiveInt
        weightLossGoal : NonPositiveInt
        averageWeightLoss : NegativeFloat
    }
    
    type Query {
        customers(height : PositiveInt) : [Customers]
    }
    
``` 

And example query might look like:

```graphql
    
    query {
        customers(height : 182) {
            name
            height
            weightLossGoal    
        }
    }
    
``` 


## Regex Scalars

The RegexScalar has a builder where you provide one or more regex patterns that control the acceptable values
for a new scalar.

You name the scalar and it provides an implementation.

For example, imagine a `phoneNumber` scalar like this :

```java

    RegexScalar phoneNumberScalar = ExtendedScalars.newRegexScalar("phoneNumber")
            .addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*"))
            .build()

```

## Locale Scalar

The Locale scalar handles [IETF BCP 47](https://tools.ietf.org/html/bcp47) language tags via the 
JDK method [Locale.forLanguageTag](https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html#forLanguageTag(java.lang.String))

```graphql

    type Customer {
        name : String
        locale : Locale
    }
    
    type Query {
        customers(inLocale : Locale) : [Customers]
    }
``` 

An example query to look for customers in the Romanian locale might look like:

```graphql
    
    query {
        customers(inLocale : "ro-RO") {
            name
            locale
        }
    }
    
``` 

## Alias Scalars

You can create aliases for existing scalars to add more semantic meaning to them.

For example a link to a social media post could be representing by a `String` but the name `SocialMediaLink` is a 
more semantically meaningful name for that scalar type.

For example, you would build it like this:

```java

    AliasedScalar socialMediaLink = ExtendedScalars.newAliasedScalar("SocialMediaLink")
            .aliasedScalar(Scalars.GraphQLString)
            .build()

```

And use it in a SDL schema like this :

```graphql

     type Customer {
           name : String
           socialMediaLink : SocialMediaLink
     }

```

Note: A future version of the graphql specification may add this capability but in the meantime you can use this facility.

## Other Scalars

* `Url`
  * An url scalar that accepts string values like `https://www.w3.org/Addressing/URL/url-spec.txt` and produces 
  `java.net.URL` objects at runtime  
  

