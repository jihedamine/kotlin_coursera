package rationals

import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.math.BigInteger

class Rational {
    private val numerator: BigInteger
    private val denominator: BigInteger

    constructor(numerator: BigInteger) : this(numerator, BigInteger.ONE)

    constructor(numerator: Number): this(numerator, 1)

    constructor(numerator: Number, denominator: Number) :
            this(BigInteger.valueOf(numerator.toLong()), BigInteger.valueOf(denominator.toLong()))

    constructor(numerator: BigInteger, denominator: BigInteger) {
        if (denominator == BigInteger.ZERO) throw IllegalArgumentException("Denominator cannot be zero")

        val sign = denominator.signum().toBigInteger()
        val signedNumerator = sign * numerator
        val signedDenominator = sign * denominator

        val gcd = signedNumerator.gcd(signedDenominator)
        this.numerator = signedNumerator.divide(gcd)
        this.denominator = signedDenominator.divide(gcd)
    }

    operator fun plus(that: Rational) = Rational(
            (this.numerator * that.denominator) + (that.numerator * this.denominator),
            this.denominator * that.denominator)

    operator fun minus(that: Rational) = plus(-that)

    operator fun times(that: Rational) = Rational(this.numerator * that.numerator, this.denominator * that.denominator)

    operator fun div(that: Rational) = Rational(this.numerator * that.denominator, this.denominator * that.numerator)

    operator fun unaryMinus() = Rational(-numerator, denominator)

    operator fun compareTo(that: Rational) : Int {
        return ((this.numerator * that.denominator) - (that.numerator * this.denominator)).signum()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false
        return this.numerator == other.numerator && this.denominator == other.denominator
    }

    override fun toString() = if (denominator == BigInteger.ONE) "$numerator" else "$numerator/$denominator"

    operator fun rangeTo(that: Rational): RationalRange {
        return RationalRange(this, that)
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    class RationalRange(val startRational: Rational, val endRational: Rational) {
        operator fun contains(rational: Rational) = rational >= startRational && rational <= endRational
    }
}

infix fun <T : Number> T.divBy(denominator: T) = Rational(this, denominator)

fun String.toRational() : Rational {
    fun String.toBigIntegerOrFail() = toBigIntegerOrNull() ?:
    throw IllegalArgumentException("${this@toRational} cannot be converted to a Rational")

    if (!contains("/")) return Rational(toBigIntegerOrFail())
    val (elem1, elem2) = split("/")
    return Rational(elem1.toBigIntegerOrFail(), elem2.toBigIntegerOrFail())
}

fun Int.toRational() = Rational(this)

fun main() {
    "a/f".toRational()
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(3.toRational().toString() == "3")

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}