package graphql.scalars.country.code

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseValueException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkCountryCode
import static graphql.scalars.util.TestKit.mkStringValue

class CountryCodeScalarTest extends Specification {

    def coercing = ExtendedScalars.CountryCode.getCoercing()

    @Unroll
    def "invoke parseValue for countryCode"() {
        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input               | expectedValue
        "US"                | mkCountryCode("US")
        "IN"                | mkCountryCode("IN")
        mkCountryCode("US") | mkCountryCode("US")
    }


    @Unroll
    def "invoke parseLiteral for countryCode"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                 | expectedValue
        new StringValue("GB") | mkCountryCode("GB")
        new StringValue("US") | mkCountryCode("US")
        new StringValue("IN") | mkCountryCode("IN")
    }

    @Unroll
    def "invoke serialize with countryCode"() {
        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input               | expectedValue
        "GB"                | "GB"
        "US"                | "US"
        "IN"                | "IN"
        mkCountryCode("US") | "US"
    }

    @Unroll
    def "invoke valueToLiteral with countryCode"() {

        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input               | expectedValue
        "GB"                | mkStringValue("GB")
        "US"                | mkStringValue("US")
        "IN"                | mkStringValue("IN")
        mkCountryCode("US") | mkStringValue("US")
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        coercing.parseValue(value)
        then:
        thrown(CoercingParseValueException)

        where:
        value                | _
        ""                   | _
        "US(UNITED STATES)"  | _
        "not a countryCode " | _
        "42.3"               | _
        new Double(42.3)     | _
        new Float(42.3)      | _
        new Object()         | _
    }


    @Unroll
    def "invoke parseValue with all countryCode list"() {
        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input | expectedValue
        "GB"  | mkCountryCode("GB")
        "US"  | mkCountryCode("US")
        "IN"  | mkCountryCode("IN")
        "AF"  | mkCountryCode("AF")
        "AX"  | mkCountryCode("AX")
        "AL"  | mkCountryCode("AL")
        "DZ"  | mkCountryCode("DZ")
        "AS"  | mkCountryCode("AS")
        "AD"  | mkCountryCode("AD")
        "AO"  | mkCountryCode("AO")
        "AI"  | mkCountryCode("AI")
        "AQ"  | mkCountryCode("AQ")
        "AG"  | mkCountryCode("AG")
        "AR"  | mkCountryCode("AR")
        "AM"  | mkCountryCode("AM")
        "AW"  | mkCountryCode("AW")
        "AU"  | mkCountryCode("AU")
        "AT"  | mkCountryCode("AT")
        "AZ"  | mkCountryCode("AZ")
        "BS"  | mkCountryCode("BS")
        "BH"  | mkCountryCode("BH")
        "BD"  | mkCountryCode("BD")
        "BB"  | mkCountryCode("BB")
        "BY"  | mkCountryCode("BY")
        "BE"  | mkCountryCode("BE")
        "BZ"  | mkCountryCode("BZ")
        "BJ"  | mkCountryCode("BJ")
        "BM"  | mkCountryCode("BM")
        "BT"  | mkCountryCode("BT")
        "BO"  | mkCountryCode("BO")
        "BQ"  | mkCountryCode("BQ")
        "BA"  | mkCountryCode("BA")
        "BW"  | mkCountryCode("BW")
        "BV"  | mkCountryCode("BV")
        "BR"  | mkCountryCode("BR")
        "IO"  | mkCountryCode("IO")
        "BN"  | mkCountryCode("BN")
        "BG"  | mkCountryCode("BG")
        "BF"  | mkCountryCode("BF")
        "BI"  | mkCountryCode("BI")
        "KH"  | mkCountryCode("KH")
        "CM"  | mkCountryCode("CM")
        "CA"  | mkCountryCode("CA")
        "CV"  | mkCountryCode("CV")
        "KY"  | mkCountryCode("KY")
        "CF"  | mkCountryCode("CF")
        "TD"  | mkCountryCode("TD")
        "CL"  | mkCountryCode("CL")
        "CN"  | mkCountryCode("CN")
        "CX"  | mkCountryCode("CX")
        "CC"  | mkCountryCode("CC")
        "CO"  | mkCountryCode("CO")
        "KM"  | mkCountryCode("KM")
        "CG"  | mkCountryCode("CG")
        "CD"  | mkCountryCode("CD")
        "CK"  | mkCountryCode("CK")
        "CR"  | mkCountryCode("CR")
        "CI"  | mkCountryCode("CI")
        "HR"  | mkCountryCode("HR")
        "CU"  | mkCountryCode("CU")
        "CW"  | mkCountryCode("CW")
        "CY"  | mkCountryCode("CY")
        "CZ"  | mkCountryCode("CZ")
        "DK"  | mkCountryCode("DK")
        "DJ"  | mkCountryCode("DJ")
        "DM"  | mkCountryCode("DM")
        "DO"  | mkCountryCode("DO")
        "EC"  | mkCountryCode("EC")
        "EG"  | mkCountryCode("EG")
        "SV"  | mkCountryCode("SV")
        "GQ"  | mkCountryCode("GQ")
        "ER"  | mkCountryCode("ER")
        "EE"  | mkCountryCode("EE")
        "ET"  | mkCountryCode("ET")
        "FK"  | mkCountryCode("FK")
        "FO"  | mkCountryCode("FO")
        "FJ"  | mkCountryCode("FJ")
        "FI"  | mkCountryCode("FI")
        "FR"  | mkCountryCode("FR")
        "GF"  | mkCountryCode("GF")
        "PF"  | mkCountryCode("PF")
        "TF"  | mkCountryCode("TF")
        "GA"  | mkCountryCode("GA")
        "GM"  | mkCountryCode("GM")
        "GE"  | mkCountryCode("GE")
        "DE"  | mkCountryCode("DE")
        "GH"  | mkCountryCode("GH")
        "GI"  | mkCountryCode("GI")
        "GR"  | mkCountryCode("GR")
        "GL"  | mkCountryCode("GL")
        "GD"  | mkCountryCode("GD")
        "GP"  | mkCountryCode("GP")
        "GU"  | mkCountryCode("GU")
        "GT"  | mkCountryCode("GT")
        "GG"  | mkCountryCode("GG")
        "GN"  | mkCountryCode("GN")
        "GW"  | mkCountryCode("GW")
        "GY"  | mkCountryCode("GY")
        "HT"  | mkCountryCode("HT")
        "HM"  | mkCountryCode("HM")
        "VA"  | mkCountryCode("VA")
        "HN"  | mkCountryCode("HN")
        "HK"  | mkCountryCode("HK")
        "HU"  | mkCountryCode("HU")
        "IS"  | mkCountryCode("IS")
        "ID"  | mkCountryCode("ID")
        "IR"  | mkCountryCode("IR")
        "IQ"  | mkCountryCode("IQ")
        "IE"  | mkCountryCode("IE")
        "IM"  | mkCountryCode("IM")
        "IL"  | mkCountryCode("IL")
        "IT"  | mkCountryCode("IT")
        "JM"  | mkCountryCode("JM")
        "JP"  | mkCountryCode("JP")
        "JE"  | mkCountryCode("JE")
        "JO"  | mkCountryCode("JO")
        "KZ"  | mkCountryCode("KZ")
        "KE"  | mkCountryCode("KE")
        "KI"  | mkCountryCode("KI")
        "KP"  | mkCountryCode("KP")
        "KR"  | mkCountryCode("KR")
        "KW"  | mkCountryCode("KW")
        "KG"  | mkCountryCode("KG")
        "LA"  | mkCountryCode("LA")
        "LV"  | mkCountryCode("LV")
        "LB"  | mkCountryCode("LB")
        "LS"  | mkCountryCode("LS")
        "LR"  | mkCountryCode("LR")
        "LY"  | mkCountryCode("LY")
        "LI"  | mkCountryCode("LI")
        "LT"  | mkCountryCode("LT")
        "LU"  | mkCountryCode("LU")
        "MO"  | mkCountryCode("MO")
        "MK"  | mkCountryCode("MK")
        "MG"  | mkCountryCode("MG")
        "MW"  | mkCountryCode("MW")
        "MY"  | mkCountryCode("MY")
        "MV"  | mkCountryCode("MV")
        "ML"  | mkCountryCode("ML")
        "MT"  | mkCountryCode("MT")
        "MH"  | mkCountryCode("MH")
        "MQ"  | mkCountryCode("MQ")
        "MR"  | mkCountryCode("MR")
        "MU"  | mkCountryCode("MU")
        "YT"  | mkCountryCode("YT")
        "MX"  | mkCountryCode("MX")
        "FM"  | mkCountryCode("FM")
        "MD"  | mkCountryCode("MD")
        "MC"  | mkCountryCode("MC")
        "MN"  | mkCountryCode("MN")
        "ME"  | mkCountryCode("ME")
        "MS"  | mkCountryCode("MS")
        "MA"  | mkCountryCode("MA")
        "MZ"  | mkCountryCode("MZ")
        "MM"  | mkCountryCode("MM")
        "NA"  | mkCountryCode("NA")
        "NR"  | mkCountryCode("NR")
        "NP"  | mkCountryCode("NP")
        "NL"  | mkCountryCode("NL")
        "NC"  | mkCountryCode("NC")
        "NZ"  | mkCountryCode("NZ")
        "NI"  | mkCountryCode("NI")
        "NE"  | mkCountryCode("NE")
        "NG"  | mkCountryCode("NG")
        "NU"  | mkCountryCode("NU")
        "NF"  | mkCountryCode("NF")
        "MP"  | mkCountryCode("MP")
        "NO"  | mkCountryCode("NO")
        "OM"  | mkCountryCode("OM")
        "PK"  | mkCountryCode("PK")
        "PW"  | mkCountryCode("PW")
        "PS"  | mkCountryCode("PS")
        "PA"  | mkCountryCode("PA")
        "PG"  | mkCountryCode("PG")
        "PY"  | mkCountryCode("PY")
        "PE"  | mkCountryCode("PE")
        "PH"  | mkCountryCode("PH")
        "PN"  | mkCountryCode("PN")
        "PL"  | mkCountryCode("PL")
        "PT"  | mkCountryCode("PT")
        "PR"  | mkCountryCode("PR")
        "QA"  | mkCountryCode("QA")
        "RE"  | mkCountryCode("RE")
        "RO"  | mkCountryCode("RO")
        "RU"  | mkCountryCode("RU")
        "RW"  | mkCountryCode("RW")
        "BL"  | mkCountryCode("BL")
        "SH"  | mkCountryCode("SH")
        "KN"  | mkCountryCode("KN")
        "LC"  | mkCountryCode("LC")
        "MF"  | mkCountryCode("MF")
        "PM"  | mkCountryCode("PM")
        "VC"  | mkCountryCode("VC")
        "WS"  | mkCountryCode("WS")
        "SM"  | mkCountryCode("SM")
        "ST"  | mkCountryCode("ST")
        "SA"  | mkCountryCode("SA")
        "SN"  | mkCountryCode("SN")
        "RS"  | mkCountryCode("RS")
        "SC"  | mkCountryCode("SC")
        "SL"  | mkCountryCode("SL")
        "SG"  | mkCountryCode("SG")
        "SX"  | mkCountryCode("SX")
        "SK"  | mkCountryCode("SK")
        "SI"  | mkCountryCode("SI")
        "SB"  | mkCountryCode("SB")
        "SO"  | mkCountryCode("SO")
        "ZA"  | mkCountryCode("ZA")
        "GS"  | mkCountryCode("GS")
        "SS"  | mkCountryCode("SS")
        "ES"  | mkCountryCode("ES")
        "LK"  | mkCountryCode("LK")
        "SD"  | mkCountryCode("SD")
        "SR"  | mkCountryCode("SR")
        "SJ"  | mkCountryCode("SJ")
        "SZ"  | mkCountryCode("SZ")
        "SE"  | mkCountryCode("SE")
        "CH"  | mkCountryCode("CH")
        "SY"  | mkCountryCode("SY")
        "TW"  | mkCountryCode("TW")
        "TJ"  | mkCountryCode("TJ")
        "TZ"  | mkCountryCode("TZ")
        "TH"  | mkCountryCode("TH")
        "TL"  | mkCountryCode("TL")
        "TG"  | mkCountryCode("TG")
        "TK"  | mkCountryCode("TK")
        "TO"  | mkCountryCode("TO")
        "TT"  | mkCountryCode("TT")
        "TN"  | mkCountryCode("TN")
        "TR"  | mkCountryCode("TR")
        "TM"  | mkCountryCode("TM")
        "TC"  | mkCountryCode("TC")
        "TV"  | mkCountryCode("TV")
        "UG"  | mkCountryCode("UG")
        "UA"  | mkCountryCode("UA")
        "AE"  | mkCountryCode("AE")
        "UM"  | mkCountryCode("UM")
        "UY"  | mkCountryCode("UY")
        "UZ"  | mkCountryCode("UZ")
        "VU"  | mkCountryCode("VU")
        "VE"  | mkCountryCode("VE")
        "VN"  | mkCountryCode("VN")
        "VG"  | mkCountryCode("VG")
        "VI"  | mkCountryCode("VI")
        "WF"  | mkCountryCode("WF")
        "EH"  | mkCountryCode("EH")
        "YE"  | mkCountryCode("YE")
        "ZM"  | mkCountryCode("ZM")
        "ZW"  | mkCountryCode("ZW")
    }

}
