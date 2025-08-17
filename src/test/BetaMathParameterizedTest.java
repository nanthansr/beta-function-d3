{\rtf1\ansi\ansicpg1252\cocoartf2822
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 import static org.junit.jupiter.api.Assertions.*;\
import java.util.stream.Stream;\
import org.junit.jupiter.params.ParameterizedTest;\
import org.junit.jupiter.params.provider.MethodSource;\
\
public class BetaMathParameterizedTest \{\
  private static final double EPS = 1e-8;\
\
  static Stream<double[]> typicalPairs() \{\
    return Stream.of(\
        new double[]\{0.5, 1.0\},  // B(1/2,1) = 2\
        new double[]\{1.0, 2.0\},  // B(1,2)   = 1/2\
        new double[]\{2.0, 2.0\},  // B(2,2)   = 1/6\
        new double[]\{3.0, 1.0\}   // B(3,1)   = 1/3\
    );\
  \}\
\
  @ParameterizedTest\
  @MethodSource("typicalPairs")\
  void matches_simple_closed_forms(double x, double y) \{\
    double expected;\
    if (x == 0.5 && y == 1.0) expected = 2.0;\
    else if (x == 1.0 && y == 2.0) expected = 1.0 / 2.0;\
    else if (x == 2.0 && y == 2.0) expected = 1.0 / 6.0;\
    else if (x == 3.0 && y == 1.0) expected = 1.0 / 3.0;\
    else expected = Double.NaN; // unreachable\
\
    assertEquals(expected, BetaMath.beta(x, y), EPS);\
  \}\
\}\
}