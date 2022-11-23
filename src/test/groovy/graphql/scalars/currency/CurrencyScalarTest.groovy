package graphql.scalars.currency

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseValueException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkCurrency
import static graphql.scalars.util.TestKit.mkStringValue

class CurrencyScalarTest extends Specification {


    def coercing = ExtendedScalars.Currency.getCoercing()

    @Unroll
    def "currency parseValue cases"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input             | expectedValue

        "USD"             | mkCurrency("USD")
        mkCurrency("USD") | mkCurrency("USD")
        "GBP"             | mkCurrency("GBP")
        "EUR"             | mkCurrency("EUR")
        "CNY"             | mkCurrency("CNY")
        "ARS"             | mkCurrency("ARS")
        mkCurrency("ARS") | mkCurrency("ARS")
        mkCurrency("ALL") | mkCurrency("ALL")
        mkCurrency("AMD") | mkCurrency("AMD")
        mkCurrency("ANG") | mkCurrency("ANG")
        mkCurrency("AOA") | mkCurrency("AOA")
    }


    @Unroll
    def "currency parseLiteral"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                  | expectedValue
        new StringValue("EUR") | mkCurrency("EUR")
        new StringValue("USD") | mkCurrency("USD")
        new StringValue("GBP") | mkCurrency("GBP")
    }

    @Unroll
    def "currency serialize"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input             | expectedValue
        "USD"             | "USD"
        mkCurrency("USD") | "USD"
    }

    @Unroll
    def "currency valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input             | expectedValue
        "USD"             | mkStringValue("USD")
        mkCurrency("USD") | mkStringValue("USD")
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        coercing.parseValue(value)
        then:
        thrown(CoercingParseValueException)

        where:
        value             | _
        ""                | _
        "US_DOLLAR"       | _
        "not a currency " | _
        "42.3"            | _
        new Double(42.3)  | _
        new Float(42.3)   | _
        new Object()      | _
    }


    @Unroll
    def "all currency ISO list parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input | expectedValue

        "USD" | mkCurrency("USD")
        "EUR" | mkCurrency("EUR")
        "GBP" | mkCurrency("GBP")
        "CNY" | mkCurrency("CNY")
        "AED" | mkCurrency("AED")
        "AFN" | mkCurrency("AFN")
        "ALL" | mkCurrency("ALL")
        "AMD" | mkCurrency("AMD")
        "ANG" | mkCurrency("ANG")
        "AOA" | mkCurrency("AOA")
        "ARS" | mkCurrency("ARS")
        "AUD" | mkCurrency("AUD")
        "AWG" | mkCurrency("AWG")
        "AZN" | mkCurrency("AZN")
        "BAM" | mkCurrency("BAM")
        "BBD" | mkCurrency("BBD")
        "BDT" | mkCurrency("BDT")
        "BGN" | mkCurrency("BGN")
        "BHD" | mkCurrency("BHD")
        "BIF" | mkCurrency("BIF")
        "BMD" | mkCurrency("BMD")
        "BND" | mkCurrency("BND")
        "BOB" | mkCurrency("BOB")
        "BOV" | mkCurrency("BOV")
        "BRL" | mkCurrency("BRL")
        "BSD" | mkCurrency("BSD")
        "BTN" | mkCurrency("BTN")
        "BWP" | mkCurrency("BWP")
        "BYN" | mkCurrency("BYN")
        "BZD" | mkCurrency("BZD")
        "CAD" | mkCurrency("CAD")
        "CDF" | mkCurrency("CDF")
        "CHE" | mkCurrency("CHE")
        "CHF" | mkCurrency("CHF")
        "CHW" | mkCurrency("CHW")
        "CLF" | mkCurrency("CLF")
        "CLP" | mkCurrency("CLP")
        "COP" | mkCurrency("COP")
        "COU" | mkCurrency("COU")
        "CRC" | mkCurrency("CRC")
        "CUC" | mkCurrency("CUC")
        "CUP" | mkCurrency("CUP")
        "CVE" | mkCurrency("CVE")
        "CZK" | mkCurrency("CZK")
        "DJF" | mkCurrency("DJF")
        "DKK" | mkCurrency("DKK")
        "DOP" | mkCurrency("DOP")
        "DZD" | mkCurrency("DZD")
        "EGP" | mkCurrency("EGP")
        "ERN" | mkCurrency("ERN")
        "ETB" | mkCurrency("ETB")
        "FJD" | mkCurrency("FJD")
        "FKP" | mkCurrency("FKP")
        "GEL" | mkCurrency("GEL")
        "GHS" | mkCurrency("GHS")
        "GIP" | mkCurrency("GIP")
        "GMD" | mkCurrency("GMD")
        "GNF" | mkCurrency("GNF")
        "GTQ" | mkCurrency("GTQ")
        "GYD" | mkCurrency("GYD")
        "HKD" | mkCurrency("HKD")
        "HNL" | mkCurrency("HNL")
        "HRK" | mkCurrency("HRK")
        "HTG" | mkCurrency("HTG")
        "HUF" | mkCurrency("HUF")
        "IDR" | mkCurrency("IDR")
        "ILS" | mkCurrency("ILS")
        "INR" | mkCurrency("INR")
        "IQD" | mkCurrency("IQD")
        "IRR" | mkCurrency("IRR")
        "ISK" | mkCurrency("ISK")
        "JMD" | mkCurrency("JMD")
        "JOD" | mkCurrency("JOD")
        "JPY" | mkCurrency("JPY")
        "KES" | mkCurrency("KES")
        "KGS" | mkCurrency("KGS")
        "KHR" | mkCurrency("KHR")
        "KMF" | mkCurrency("KMF")
        "KPW" | mkCurrency("KPW")
        "KRW" | mkCurrency("KRW")
        "KWD" | mkCurrency("KWD")
        "KYD" | mkCurrency("KYD")
        "KZT" | mkCurrency("KZT")
        "LAK" | mkCurrency("LAK")
        "LBP" | mkCurrency("LBP")
        "LKR" | mkCurrency("LKR")
        "LRD" | mkCurrency("LRD")
        "LSL" | mkCurrency("LSL")
        "LYD" | mkCurrency("LYD")
        "MAD" | mkCurrency("MAD")
        "MDL" | mkCurrency("MDL")
        "MGA" | mkCurrency("MGA")
        "MKD" | mkCurrency("MKD")
        "MMK" | mkCurrency("MMK")
        "MNT" | mkCurrency("MNT")
        "MOP" | mkCurrency("MOP")
        "MRU" | mkCurrency("MRU")
        "MUR" | mkCurrency("MUR")
        "MVR" | mkCurrency("MVR")
        "MWK" | mkCurrency("MWK")
        "MXN" | mkCurrency("MXN")
        "MXV" | mkCurrency("MXV")
        "MYR" | mkCurrency("MYR")
        "MZN" | mkCurrency("MZN")
        "NAD" | mkCurrency("NAD")
        "NGN" | mkCurrency("NGN")
        "NIO" | mkCurrency("NIO")
        "NOK" | mkCurrency("NOK")
        "NPR" | mkCurrency("NPR")
        "NZD" | mkCurrency("NZD")
        "OMR" | mkCurrency("OMR")
        "PAB" | mkCurrency("PAB")
        "PEN" | mkCurrency("PEN")
        "PGK" | mkCurrency("PGK")
        "PHP" | mkCurrency("PHP")
        "PKR" | mkCurrency("PKR")
        "PLN" | mkCurrency("PLN")
        "PYG" | mkCurrency("PYG")
        "QAR" | mkCurrency("QAR")
        "RON" | mkCurrency("RON")
        "RSD" | mkCurrency("RSD")
        "RWF" | mkCurrency("RWF")
        "RUB" | mkCurrency("RUB")
        "SAR" | mkCurrency("SAR")
        "SBD" | mkCurrency("SBD")
        "SCR" | mkCurrency("SCR")
        "SDG" | mkCurrency("SDG")
        "SEK" | mkCurrency("SEK")
        "SGD" | mkCurrency("SGD")
        "SHP" | mkCurrency("SHP")
        "SLL" | mkCurrency("SLL")
        "SOS" | mkCurrency("SOS")
        "SRD" | mkCurrency("SRD")
        "SSP" | mkCurrency("SSP")
        "STN" | mkCurrency("STN")
        "SVC" | mkCurrency("SVC")
        "SYP" | mkCurrency("SYP")
        "SZL" | mkCurrency("SZL")
        "THB" | mkCurrency("THB")
        "TJS" | mkCurrency("TJS")
        "TMT" | mkCurrency("TMT")
        "TND" | mkCurrency("TND")
        "TOP" | mkCurrency("TOP")
        "TRY" | mkCurrency("TRY")
        "TTD" | mkCurrency("TTD")
        "TWD" | mkCurrency("TWD")
        "TZS" | mkCurrency("TZS")
        "UAH" | mkCurrency("UAH")
        "UGX" | mkCurrency("UGX")
        "USN" | mkCurrency("USN")
        "UYI" | mkCurrency("UYI")
        "UYU" | mkCurrency("UYU")
        "UZS" | mkCurrency("UZS")
        "VEF" | mkCurrency("VEF")
        "VND" | mkCurrency("VND")
        "VUV" | mkCurrency("VUV")
        "WST" | mkCurrency("WST")
        "XAF" | mkCurrency("XAF")
        "XCD" | mkCurrency("XCD")
        "XDR" | mkCurrency("XDR")
        "XOF" | mkCurrency("XOF")
        "XPF" | mkCurrency("XPF")
        "XSU" | mkCurrency("XSU")
        "XUA" | mkCurrency("XUA")
        "YER" | mkCurrency("YER")
        "ZAR" | mkCurrency("ZAR")
        "ZMW" | mkCurrency("ZMW")
        "ZWL" | mkCurrency("ZWL")
    }


}
