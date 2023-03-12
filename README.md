# Extended Scalars for graphql-java

[![Build Status](https://github.com/graphql-java/graphql-java-extended-scalars/actions/workflows/master.yml/badge.svg)](https://github.com/graphql-java/graphql-java-extended-scalars/actions/workflows/master.yml)
[![Latest Release](https://img.shields.io/maven-central/v/com.graphql-java/graphql-java-extended-scalars?versionPrefix=20.)](https://maven-badges.herokuapp.com/maven-central/com.graphql-java/graphql-java-extended-scalars/)
[![Latest Snapshot](https://img.shields.io/maven-central/v/com.graphql-java/graphql-java-extended-scalars?label=maven-central%20snapshot)](https://maven-badges.herokuapp.com/maven-central/com.graphql-java/graphql-java-extended-scalars/)
[![MIT licensed](https://img.shields.io/badge/license-MIT-green)](https://github.com/graphql-java/graphql-java-extended-scalars/blob/master/LICENSE.md)

## Overview

This library provides extended scalars for [graphql-java](https://github.com/graphql-java/graphql-java)

[GraphQL Scalars](https://spec.graphql.org/October2021/#sec-Scalars) are the leaf values in the GraphQL type system and can't be queried further via sub-field selections.

The GraphQL Specification defines `String`, `Int`, `Float`, `Boolean` and `ID` as well-defined [built-in scalars](https://spec.graphql.org/October2021/#sec-Scalars.Built-in-Scalars) that must be present in a graphql type
system. Beyond these, it is up to an implementation about what [custom scalars](https://spec.graphql.org/October2021/#sec-Scalars.Custom-Scalars) are present.

The GraphQL Specification recommends the use of the [@specifiedBy](https://spec.graphql.org/October2021/#sec--specifiedBy) built-in schema directive to provide a scalar specification URL for specifying the behavior of custom scalar types.

You would use custom scalars when you want to describe more meaningful behavior or ranges of values.

# Getting started

## How to install

To use this library put the following into your gradle config

    implementation 'com.graphql-java:graphql-java-extended-scalars:20.0'

or the following into your Maven config

    <dependency>
      <groupId>com.graphql-java</groupId>
      <artifactId>graphql-java-extended-scalars</artifactId>
      <version>20.0</version>
    </dependency>

> Note:
>
> use 19.0 or above for graphql-java 19.x and above
>
> use 20.0 or above for graphql-java 20.x and above

It's currently available from Maven Central.

## How to use extended scalars

Register the scalar with `graphql-java`

```java
    RuntimeWiring.newRuntimeWiring().scalar(ExtendedScalars.DateTime)
```

Or if using [Spring for GraphQL](https://docs.spring.io/spring-graphql/docs/current/reference/html/), register the scalar with `RuntimeWiringConfigurer`

```java
    @Configuration
    public class GraphQlConfig {
        @Bean
        public RuntimeWiringConfigurer runtimeWiringConfigurer() {
            return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.DateTime);
        }
    }
```

And use the scalar in your schema

```graphql
scalar DateTime
  @specifiedBy(url: "https://scalars.graphql.org/andimarek/date-time.html")
type Something {
  someDateTime: DateTime
}
```

# Custom Scalars

## Alias Scalars

| Scalar Name     | Scalar Specification                           | Description                                                                       |
| --------------- | ---------------------------------------------- | --------------------------------------------------------------------------------- |
| `AliasedScalar` | <pre lang="graphql">scalar AliasedScalar</pre> | You can create aliases for existing scalars to add more semantic meaning to them. |

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
  name: String
  socialMediaLink: SocialMediaLink
}
```

## Date & Time Scalars

| Scalar Name | Scalar Definition                                                                                                                  | Description                                                                                                                                                    |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `DateTime`  | <pre lang="graphql">scalar DateTime @specifiedBy(url: "https://scalars.graphql.org/andimarek/date-time.html")</pre lang="graphql"> | An RFC-3339 compliant date time scalar that accepts string values like `1996-12-19T16:39:57-08:00` and produces `java.time.OffsetDateTime` objects at runtime. |
| `Time`      | <pre lang="graphql">scalar Time @specifiedBy(url: "https://tools.ietf.org/html/rfc3339")</pre>                                     | An RFC-3339 compliant time scalar that accepts string values like `16:39:57-08:00` and produces `java.time.OffsetTime` objects at runtime                      |
| `LocalTime` | <pre lang="graphql">scalar LocalTime</pre>                                                                                         | 24-hour clock time string in the format `hh:mm:ss.sss` or `hh:mm:ss` if partial seconds is zero and produces `java.time.LocalTime` objects at runtime.         |
| `Date`      | <pre lang="graphql">scalar Date @specifiedBy(url: "https://tools.ietf.org/html/rfc3339")</pre>                                     | An RFC-3339 compliant date scalar that accepts string values like `1996-12-19` and produces `java.time.LocalDate` objects at runtime                           |

An example declaration in SDL might be:

```graphql
type Customer {
  birthDay: Date
  workStartTime: Time
  bornAt: DateTime
}

type Query {
  customers(bornAfter: DateTime): [Customers]
}
```

And example query might look like:

```graphql
query {
  customers(bornAfter: "1996-12-19T16:39:57-08:00") {
    birthDay
    bornAt
  }
}
```

## ID Scalars

| Scalar Name | Scalar Definition                                                                              | Description                                                                                                                                                     |
| ----------- | ---------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `UUID`      | <pre lang="graphql">scalar UUID @specifiedBy(url: "https://tools.ietf.org/html/rfc4122")</pre> | A universally unique identifier scalar that accepts uuid values like `2423f0a0-3b81-4115-a189-18df8b35e8fc` and produces `java.util.UUID` instances at runtime. |

## Numeric Scalars

| Scalar Name        | Scalar Definition                                 | Description                                                   |
| ------------------ | ------------------------------------------------- | ------------------------------------------------------------- |
| `PositiveInt`      | <pre lang="graphql">scalar PositiveInt</pre>      | An `Int` scalar that MUST be greater than zero.               |
| `NegativeInt`      | <pre lang="graphql">scalar NegativeInt</pre>      | An `Int` scalar that MUST be less than zero.                  |
| `NonPositiveInt`   | <pre lang="graphql">scalar NonPositiveInt</pre>   | An `Int` scalar that MUST be less than or equal to zero.      |
| `NonNegativeInt`   | <pre lang="graphql">scalar NonNegativeInt</pre>   | An `Int` scalar that MUST be greater than or equal to zero.   |
| `PositiveFloat`    | <pre lang="graphql">scalar PositiveFloat</pre>    | An `Float` scalar that MUST be greater than zero.             |
| `NegativeFloat`    | <pre lang="graphql">scalar NegativeFloat</pre>    | An `Float` scalar that MUST be less than zero.                |
| `NonPositiveFloat` | <pre lang="graphql">scalar NonPositiveFloat</pre> | An `Float` scalar that MUST be less than or equal to zero.    |
| `NonNegativeFloat` | <pre lang="graphql">scalar NonNegativeFloat</pre> | An `Float` scalar that MUST be greater than or equal to zero. |

The numeric scalars are derivations of the standard GraphQL `Int` and `Float` scalars that enforce range limits.

An example declaration in SDL might be:

```graphql
type Customer {
  name: String
  currentHeight: PositiveInt
  weightLossGoal: NonPositiveInt
  averageWeightLoss: NegativeFloat
}

type Query {
  customers(height: PositiveInt): [Customers]
}
```

And example query might look like:

```graphql
query {
  customers(height: 182) {
    name
    height
    weightLossGoal
  }
}
```

## Java Primitives

| Scalar Name         | Scalar Specification                               | Description                                      |
| ------------------- | -------------------------------------------------- | ------------------------------------------------ |
| `GraphQLLong`       | <pre lang="graphql">scalar GraphQLLong</pre>       | A scalar which represents `java.lang.Long`       |
| `GraphQLShort`      | <pre lang="graphql">scalar GraphQLShort</pre>      | A scalar which represents `java.lang.Short`      |
| `GraphQLByte`       | <pre lang="graphql">scalar GraphQLByte</pre>       | A scalar which represents `java.lang.Byte`       |
| `GraphQLBigDecimal` | <pre lang="graphql">scalar GraphQLBigDecimal</pre> | A scalar which represents `java.math.BigDecimal` |
| `GraphQLBigInteger` | <pre lang="graphql">scalar GraphQLBigInteger</pre> | A scalar which represents `java.math.BigInteger` |
| `GraphQLChar`       | <pre lang="graphql">scalar GraphQLChar</pre>       | A scalar which represents `java.lang.Character`  |

## Locale Scalar

| Scalar Name | Scalar Specification                                                                           | Description                                                                                                                                                                                                                             |
| ----------- | ---------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Locale`    | <pre lang="graphql">scalar Locale @specifiedBy(url: "https://tools.ietf.org/html/bcp47")</pre> | The Locale scalar handles [IETF BCP 47](https://tools.ietf.org/html/bcp47) language tags via the JDK method [Locale.forLanguageTag](<https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html#forLanguageTag(java.lang.String)>) |

```graphql
type Customer {
  name: String
  locale: Locale
}

type Query {
  customers(inLocale: Locale): [Customers]
}
```

An example query to look for customers in the Romanian locale might look like:

```graphql
query {
  customers(inLocale: "ro-RO") {
    name
    locale
  }
}
```

## Country Code Scalar

| Scalar Name   | Scalar Specification                         | Description                                                                                                       |
| ------------- | -------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `CountryCode` | <pre lang="graphql">scalar CountryCode</pre> | The CountryCode scalar type as defined by [ISO 3166-1 alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2). |

An example declaration in SDL might be:

```graphql
scalar CountryCode

type Customer {
  name: String
  countryCode: CountryCode
}
```

And example query might look like:

```graphql
query {
  customers(code: "US") {
    name
    countryCode
  }
}
```

## Currency Scalar

| Scalar Name | Scalar Specification                      | Description                                                                            |
| ----------- | ----------------------------------------- | -------------------------------------------------------------------------------------- |
| `Currency`  | <pre lang="graphql">scalar Currency</pre> | A field whose value is an [ISO-4217](https://en.wikipedia.org/wiki/ISO_4217) currency. |

An example declaration in SDL might be:

```graphql
scalar Currency

type Account {
  id: String
  currency: Currency
  accountNumber: String
}
```

And example query might look like:

```graphql
query {
  accounts(currency: "USD") {
    id
    currency
    accountNumber
  }
}
```

## URL Scalars

| Scalar Name | Scalar Specification                                                                                     | Description                                                                                                                                    |
| ----------- | -------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `Url`       | <pre lang="graphql">scalar URL @specifiedBy(url: "https://www.w3.org/Addressing/URL/url-spec.txt")</pre> | An url scalar that accepts string values like `https://www.w3.org/Addressing/URL/url-spec.txt` and produces `java.net.URL` objects at runtime. |

## Object / JSON Scalars

| Scalar Name | Scalar Specification                    | Description                                                                     |
| ----------- | --------------------------------------- | ------------------------------------------------------------------------------- |
| `Object`    | <pre lang="graphql">scalar Object</pre> | An object scalar that accepts any object as a scalar value.                     |
| `JSON`      | <pre lang="graphql">scalar JSON</pre>   | A synonym for the `Object` scalar, it will accept any object as a scalar value. |

One of the design goals of GraphQL, is that the type system describes the shape of the data returned.

The `Object` / `JSON` scalars work against this some what because they can return compound values outside the type system. As such
they should be used sparingly. In general your should aim to describe the data via the GraphQL type system where you can and only
resort to the `Object` / `JSON` scalars in very rare circumstances.

An example might be an extensible GraphQL system where systems can input custom metadata objects that cant be known at
schema type design time.

An example declaration in SDL might be:

```graphql
type Customer {
  name: String
  associatedMetaData: JSON
}

type Query {
  customers(filterSyntax: JSON): [Customers]
}
```

And example query might look like:

```graphql
query {
  customers(
    filterSyntax: {
      startSpan: "First"
      matchCriteria: { countryCode: "AU", isoCodes: ["27B-34R", "95A-E23"] }
    }
  ) {
    name
    associatedMetaData
  }
}
```

Note : The `JSON` scalar is a simple alias type to the `Object` scalar because often the returned data is a blob of JSON. They are
all just objects at runtime in `graphql-java` terms and what network serialization protocol is up to you. Choose whichever name you think
adds more semantic readers to your schema consumers.

## Regex Scalars

| Scalar Name   | Scalar Specification | Description                                                                      |
| ------------- | -------------------- | -------------------------------------------------------------------------------- |
| `RegexScalar` | -                    | Allows you to build a new scalar via a builder pattern using regular expressions |

The RegexScalar has a builder where you provide one or more regex patterns that control the acceptable values
for a new scalar.

You name the scalar and it provides an implementation.

For example, imagine a `phoneNumber` scalar like this :

```java

    RegexScalar phoneNumberScalar = ExtendedScalars.newRegexScalar("phoneNumber")
            .addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*"))
            .build()

```
