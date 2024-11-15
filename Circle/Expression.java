import java.util.Map;

public class Expression {
	public static double eval(final String str, Map<String, Double> variables) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() { ch = (++pos < str.length()) ? str.charAt(pos) : -1; }

			boolean eat(int charToEat) {
				while (ch == ' ')
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+'))
						x += parseTerm(); // addition
					else if (eat('-'))
						x -= parseTerm(); // subtraction
					else
						return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*'))
						x *= parseFactor(); // multiplication
					else if (eat('/'))
						x /= parseFactor(); // division
					else
						return x;
				}
			}

			double parseFactor() {
				if (eat('+'))
					return +parseFactor(); // unary plus
				if (eat('-'))
					return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					if (!eat(')'))
						throw new RuntimeException("Missing ')'");
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions or variables
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String name = str.substring(startPos, this.pos);
					if (eat('(')) {
						x = parseExpression();
						if (!eat(')'))
							throw new RuntimeException(
							        "Missing ')' " +
							        "after argument " +
							        "to " +
							        name);
						if (name.equals("sqrt"))
							x = Math.sqrt(x);
						else if (name.equals("sin"))
							x = Math.sin(
							        Math.toRadians(x));
						else if (name.equals("cos"))
							x = Math.cos(
							        Math.toRadians(x));
						else if (name.equals("tan"))
							x = Math.tan(
							        Math.toRadians(x));
						else
							throw new RuntimeException(
							        "Unknown " +
							        "function: " +
							        name);
					} else {
						if (variables.containsKey(name)) {
							x = variables.get(name);
						} else {
							throw new RuntimeException(
							        "Unknown " +
							        "variable: " +
							        name);
						}
					}
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^'))
					x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}
	public static void main(String[] args) {
        String expression = args[0] == null ? "2 * x + 3 * x" : args[0];
        Map<String, Double> variables = Map.of("x", 2.0, "omega", 0.1);
        System.out.println(eval(expression, variables));
    }
}
