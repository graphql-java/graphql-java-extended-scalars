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

```java
implementation 'com.graphql-java:graphql-java-extended-scalars:20.0'
```

or the following into your Maven config

```xml
<dependency>
  <groupId>com.graphql-java</groupId>
  <artifactId>graphql-java-extended-scalars</artifactId>
  <version>20.0</version>
</dependency>
```

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

<table>
<tr>
<td>Scalar Name</td>
<td>Scalar Specification</td>
<td>Description</td>
</tr>
<tr>
<td><code>AliasedScalar</code></td>
<td><pre lang="graphql">
scalar AliasedScalar
</pre></td>
<td>You can create aliases for existing scalars to add more semantic meaning to them.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar DateTime
  @specifiedBy(url: 
    "https://scalars.graphql.org/andimarek/date-time.html"
  )
</pre></td>
<td>A RFC-3339 compliant date time scalar that accepts string values like <code>1996-12-19T16:39:57-08:00</code> and produces <code>java.time.OffsetDateTime</code> objects at runtime.</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar Date
  @specifiedBy(url: 
    "https://tools.ietf.org/html/rfc3339"
  )</pre></td>
<td>A RFC-3339 compliant date scalar that accepts string values like <code>1996-12-19</code> and produces <code>java.time.LocalDate</code> objects at runtime.</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar Time
  @specifiedBy(url: 
    "https://tools.ietf.org/html/rfc3339"
  )
</pre></td>
<td>A RFC-3339 compliant time scalar that accepts string values like <code>16:39:57-08:00</code> and produces <code>java.time.OffsetTime</code> objects at runtime.</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar LocalTime
</pre></td>
<td>24-hour clock time string in the format <code>hh:mm:ss.sss</code> or <code>hh:mm:ss</code> if partial seconds is zero and produces <code>java.time.LocalTime</code> objects at runtime.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar UUID
  @specifiedBy(url: 
    "https://tools.ietf.org/html/rfc4122"
  )
</pre></td>
<td>A universally unique identifier scalar that accepts uuid values like `2423f0a0-3b81-4115-a189-18df8b35e8fc` and produces `java.util.UUID` instances at runtime.</td>
</tr>
</table>

## Numeric Scalars

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">scalar PositiveInt</pre></td>
<td>An <code>Int</code> scalar that MUST be greater than zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NegativeInt</pre></td>
<td>An <code>Int</code> scalar that MUST be less than zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NonPositiveInt</pre></td>
<td>An <code>Int</code> scalar that MUST be less than or equal to zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NonNegativeInt</pre></td>
<td>An <code>Int</code> scalar that MUST be greater than or equal to zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar PositiveFloat</pre></td>
<td>An <code>Float</code> scalar that MUST be greater than zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NegativeFloat</pre></td>
<td>An <code>Float</code> scalar that MUST be less than zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NonPositiveFloat</pre></td>
<td>An <code>Float</code> scalar that MUST be less than or equal to zero.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar NonNegativeFloat</pre></td>
<td>An <code>Float</code> scalar that MUST be greater than or equal to zero.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLLong</pre></td>
<td>A scalar which represents <code>java.lang.Long<code></td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLShort</pre></td>
<td>A scalar which represents <code>java.lang.Short<code></td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLByte</pre></td>
<td>A scalar which represents <code>java.lang.Byte<code></td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLBigDecimal</pre></td>
<td>A scalar which represents <code>java.lang.BigDecimal<code></td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLBigInteger</pre></td>
<td>A scalar which represents <code>java.lang.BigInteger<code></td>
</tr>
<tr>
<td><pre lang="graphql">scalar GraphQLChar</pre></td>
<td>A scalar which represents <code>java.lang.Character<code></td>
</tr>
</table>

## Locale Scalar

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar Locale
  @specifiedBy(url: 
    "https://tools.ietf.org/html/bcp47"
  )
</pre></td>
<td>The Locale scalar handles <a href="https://tools.ietf.org/html/bcp47">IETF BCP 47</a> language tags via the JDK method <a href="https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html#forLanguageTag(java.lang.String)">Locale.forLanguageTag</a>.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar CountryCode 
  @specifiedBy(url: 
    "https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2"
  )
</pre></td>
<td>The CountryCode scalar type as defined by <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a>.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar Currency
  @specifiedBy(url: 
    "https://en.wikipedia.org/wiki/ISO_4217"
  )
</pre></td>
<td>A field whose value is an <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO-4217</a> currency.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">
scalar URL
  @specifiedBy(url: 
    "https://www.w3.org/Addressing/URL/url-spec.txt"
  )
</pre></td>
<td>An url scalar that accepts string values like `https://www.w3.org/Addressing/URL/url-spec.txt` and produces `java.net.URL` objects at runtime.</td>
</tr>
</table>

## Object / JSON Scalars

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">scalar Object</pre></td>
<td>An object scalar that accepts any object as a scalar value.</td>
</tr>
<tr>
<td><pre lang="graphql">scalar JSON</pre></td>
<td>A synonym for the `Object` scalar, it will accept any object as a scalar value.</td>
</tr>
</table>

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

<table>
<tr>
<td>Scalar</td>
<td>Description</td>
</tr>
<tr>
<td><pre lang="graphql">-</pre></td>
<td>Allows you to build a new scalar via a builder pattern using regular expressions.</td>
</tr>
</table>

The RegexScalar has a builder where you provide one or more regex patterns that control the acceptable values
for a new scalar.

You name the scalar and it provides an implementation.

For example, imagine a `phoneNumber` scalar like this :

```java

    RegexScalar phoneNumberScalar = ExtendedScalars.newRegexScalar("phoneNumber")
            .addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*"))
            .build()

```
