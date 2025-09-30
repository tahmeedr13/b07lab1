import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coefficients;
	private int[] exp;

    public Polynomial() {
        this.coefficients = new double[]{0};
		this.exp = new int[]{0};
    }

    public Polynomial(double[] coefficients, int[] exp) {
        this.coefficients = new double[coefficients.length];
        System.arraycopy(coefficients, 0, this.coefficients, 0, coefficients.length);
		
		this.exp = new int[exp.length];
        System.arraycopy(exp, 0, this.exp, 0, exp.length);
    }

    public Polynomial add(Polynomial other) {
        int maxLength = Math.max(this.coefficients.length, other.coefficients.length);
		
		int a_length=this.coefficients.length + other.coefficients.length;
		
		double[] coef_result = new double[a_length];
		int[] exp_result = new int[a_length];


        // copying this coefficients array
		for (int i = 0; i < this.coefficients.length; i++) {
            coef_result[i]=this.coefficients[i];
			exp_result[i]=this.exp[i];
        }
		int e_size=this.exp.length;
		
		for(int j=0; j<other.exp.length; j++){
			double coef=other.coefficients[j];
			int o_exp=other.exp[j];
			
			boolean exists= false;
			
			for(int k=0; k<e_size; k++){
				if(o_exp==this.exp[k]){
					coef_result[k]=coef_result[k]+coef;
					exists=true;
					break;
				}
				
			}
			
			if(!exists){
					coef_result[e_size]=coef;
					exp_result[e_size]=o_exp;
					e_size++;
				}
		}
		
		// copying array w/ proper size
		int size=0;
		for(int n=0; n<a_length; n++){
			if(coef_result[n]!=0.0){
				coef_result[size]=coef_result[n];
				exp_result[size]=exp_result[n];
				size++;
			}
		}
		
		if (size == 0) {
			return new Polynomial(); /// no polynomial
		}
		
		double[] coef_final = new double[size];
		int[] exp_final = new int[size];
		
		for(int m=0; m<size; m++){
			coef_final[m]=coef_result[m];
			exp_final[m]=exp_result[m];
		}
		
		
		

        return new Polynomial(coef_final,exp_final);
    }
	
	/////////////////////////////////////////////////////////////
	public Polynomial multiply(Polynomial other){
		int a_length=this.coefficients.length * other.coefficients.length;
		
		double[] coef_result = new double[a_length];
		int[] exp_result = new int[a_length];
		int size=0;
		
		for (int i = 0; i < this.coefficients.length; i++){
			for (int j = 0; j < other.coefficients.length; j++) {
				double coef = this.coefficients[i] * other.coefficients[j];
				int o_exp = this.exp[i] + other.exp[j];
				
				boolean exists = false;
				for (int k = 0; k < size; k++){
					if (o_exp == exp_result[k]){
						coef_result[k] = coef_result[k] + coef;
						exists = true;
					}
				}
				if(!exists){
					coef_result[size]=coef;
					exp_result[size]=o_exp;
					size++;
				}
				
				
			}
		}
		
		int fsize=0;
		for(int n=0; n<size; n++){
			if(coef_result[n]!=0.0){
				coef_result[fsize]=coef_result[n];
				exp_result[fsize]=exp_result[n];
				fsize++;
			}
		}
		
		if (fsize == 0) {
			return new Polynomial();
		}
		
		double[] coef_final = new double[fsize];
		int[] exp_final = new int[fsize];
		
		for(int m=0; m<fsize; m++){
			coef_final[m]=coef_result[m];
			exp_final[m]=exp_result[m];
			
		}
		
		return new Polynomial(coef_final, exp_final);
		
	}
	////////////////////////////////////////////////////
    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exp[i]);
        }
        return result;
    } 
    
	public boolean hasRoot(double x) {
		return Math.abs(this.evaluate(x)) < 1e-9;
    }
	
	public Polynomial(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();
        
        if (line == null || line.trim().isEmpty()) {
            this.coefficients = new double[]{0};
            this.exp = new int[]{0};
            return;
        }
        
        splitPoly(line);
    }
	
	private void splitPoly(String poly) {
        poly = poly.replace("-", "+-");
        if (poly.startsWith("+")) {
            poly = poly.substring(1);
        }
        
        String[] terms = poly.split("\\+");
        List<Double> coefList = new ArrayList<>();
        List<Integer> expList = new ArrayList<>();
        
        for (String term : terms) {
            term = term.trim();
            if (term.isEmpty()) continue;
            
            if (!term.contains("x")) {
				
                coefList.add(Double.parseDouble(term));
                expList.add(0);
            } 
			else if (term.contains("x")) {
                String[] parts = term.split("x");
                
                double coef;
                if (parts[0].isEmpty() || parts[0].equals("+")) {
                    coef = 1.0;
                } 
				else if (parts[0].equals("-")) {
                    coef = -1.0;
                } 
				else {
                    coef = Double.parseDouble(parts[0]);
                }
                
                int exponent;
                if (parts.length == 1 || parts[1].isEmpty()) {
                    exponent = 1;
                } 
				else {
                    exponent = Integer.parseInt(parts[1]);
                }
                
                coefList.add(coef);
                expList.add(exponent);
            }
        }
        
        this.coefficients = new double[coefList.size()];
        this.exp = new int[expList.size()];
        
        for (int i = 0; i < coefList.size(); i++) {
            this.coefficients[i] = coefList.get(i);
            this.exp[i] = expList.get(i);
        }
    }
	
	///////// saving the poly in file
	public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(this.toString());
        writer.close();
    }
	
///////////// 
    public String toString() {
        if (coefficients.length == 0) {
            return "0";
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < coefficients.length; i++) {
            if (Math.abs(coefficients[i]) < 1e-9) continue;
            
// Add the signs
            if (i > 0 && coefficients[i] > 0) {
                sb.append("+");
            }
            
// Adding back the coefficient and x 
            if (exp[i] == 0) {
                sb.append(coefficients[i]);
            } else if (exp[i] == 1) {
                if (Math.abs(coefficients[i] - 1.0) < 1e-9) {
                    sb.append("x");
                } else if (Math.abs(coefficients[i] + 1.0) < 1e-9) {
                    sb.append("-x");
                } else {
                    sb.append(coefficients[i]).append("x");
                }
            } else {
                if (Math.abs(coefficients[i] - 1.0) < 1e-9) {
                    sb.append("x").append(exp[i]);
                } 
				else if (Math.abs(coefficients[i] + 1.0) < 1e-9) {
                    sb.append("-x").append(exp[i]);
                } 
				else {
                    sb.append(coefficients[i]).append("x").append(exp[i]);
                }
            }
        }
        
        String result = sb.toString();
        return result.isEmpty() ? "0" : result;
    }

}
}
