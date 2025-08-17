{\rtf1\ansi\ansicpg1252\cocoartf2822
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\froman\fcharset0 Times-Roman;}
{\colortbl;\red255\green255\blue255;\red0\green0\blue0;}
{\*\expandedcolortbl;;\cssrgb\c0\c0\c0;}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\deftab720
\pard\pardeftab720\partightenfactor0

\f0\fs24 \cf0 \expnd0\expndtw0\kerning0
\outl0\strokewidth0 \strokec2 import static org.junit.jupiter.api.Assertions.*;\
import org.junit.jupiter.api.Test;\
\
public class BetaMathTest \{\
  private static final double EPS = 1e-9;\
\
  // --- Known values ---\
  @Test\
  void b_1_1_is_1() \{\
    assertEquals(1.0, BetaMath.beta(1.0, 1.0), EPS);\
  \}\
\
  @Test\
  void b_half_half_is_pi() \{\
    assertEquals(Math.PI, BetaMath.beta(0.5, 0.5), 1e-8);\
  \}\
\
  @Test\
  void b_2_3_is_one_over_twelve() \{\
    assertEquals(1.0 / 12.0, BetaMath.beta(2.0, 3.0), EPS);\
  \}\
\
  // --- Symmetry: B(x,y) == B(y,x) ---\
  @Test\
  void symmetry_holds_for_typical_values() \{\
    double x = 0.8, y = 2.5;\
    assertEquals(BetaMath.beta(x, y), BetaMath.beta(y, x), 1e-10);\
  \}\
\
  // --- Simple recurrence:\
  // B(x, y) = ((x - 1) / (x + y - 1)) * B(x - 1, y) for x > 1\
  @Test\
  void recurrence_in_x_matches() \{\
    double x = 3.2, y = 1.7; // x>1\
    double left = BetaMath.beta(x, y);\
    double right = ((x - 1.0) / (x + y - 1.0)) * BetaMath.beta(x - 1.0, y);\
    assertEquals(left, right, 1e-10);\
  \}\
\
  // --- Another exact value: B(n,1) = 1/n for n>0\
  @Test\
  void b_n_1_is_one_over_n() \{\
    double n = 4.0;\
    assertEquals(1.0 / n, BetaMath.beta(n, 1.0), 1e-12);\
  \}\
\
  // --- Domain errors ---\
  @Test\
  void invalid_domain_throws() \{\
    assertThrows(IllegalArgumentException.class, () -> BetaMath.beta(0.0, 1.0));\
    assertThrows(IllegalArgumentException.class, () -> BetaMath.beta(1.0, 0.0));\
    assertThrows(IllegalArgumentException.class, () -> BetaMath.beta(-0.1, 2.0));\
    assertThrows(IllegalArgumentException.class, () -> BetaMath.beta(2.0, -0.5));\
  \}\
\
  // --- Finite results (no NaN/Inf) for typical inputs ---\
  @Test\
  void finite_for_typical_inputs() \{\
    double v = BetaMath.beta(1.2, 2.4);\
    assertFalse(Double.isNaN(v));\
    assertFalse(Double.isInfinite(v));\
  \}\
\}\
}