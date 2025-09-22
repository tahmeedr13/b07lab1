public class Polynomial {
    private double[] coefficients;

    public Polynomial() {
        this.coefficients = new double[]{0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = new double[coefficients.length];
        System.arraycopy(coefficients, 0, this.coefficients, 0, coefficients.length);
    }

    public Polynomial add(Polynomial other) {
        int maxLength = Math.max(this.coefficients.length, other.coefficients.length);
        double[] result = new double[maxLength];

        for (int i = 0; i < maxLength; i++) {
            double a = (i < this.coefficients.length) ? this.coefficients[i] : 0;
            double b = (i < other.coefficients.length) ? other.coefficients[i] : 0;
            result[i] = a + b;
        }

        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    
    public boolean hasRoot(double x) {
        return Math.abs(this.evaluate(x)) < 1e-9; 
    }
}