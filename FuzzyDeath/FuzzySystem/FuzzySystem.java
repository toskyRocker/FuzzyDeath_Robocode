package FuzzySystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class FuzzySystem {
	
	final int  maxSpeed = 8;
	// getMaxDistance()
	double hypotenuse;
	double den = 0.0;
	double crisp;
	
	FuzzyVariable distance;
	FuzzyVariable speed;
	OutputVariable firepower;
	Rule rule;
	LinkedList<Rule> rules;
	Map<String, FuzzyVariable> input;
	Map<String, FuzzyVariable> output;
	OutputVariable firePower;
	
	public FuzzySystem(double w, double h) {
		
		setHypotenuse( h,  w);
		
		// classi della distanza
				// la diagonale del campo di battaglia è la massima distanza fra 2 robot
				Map<String, Class> distanceClasses = new HashMap<String, Class>();
				Class Dbassa = new InputClass(0.00*hypotenuse, 0.00*hypotenuse, 0.1 * hypotenuse, 0.25* hypotenuse, "D_bassa");
				Class DmedioBassa = new InputClass(0.18*hypotenuse, 0.28*hypotenuse,0.35*hypotenuse,0.4*hypotenuse, "D_medioBassa");
				Class Dmedia = new InputClass(0.36*hypotenuse, 0.45*hypotenuse, 0.51*hypotenuse, 0.60*hypotenuse, "D_media");
				Class Dalta = new InputClass(0.61*hypotenuse, 0.81*hypotenuse, 1*hypotenuse,  1.01*hypotenuse, "D_alta");
				
				distanceClasses.put(Dbassa.getName(), Dbassa); 
				distanceClasses.put(DmedioBassa.getName(), DmedioBassa);
				distanceClasses.put(Dmedia.getName(), Dmedia); 
				distanceClasses.put(Dalta.getName(), Dalta);
				
				
				// variabile della distanza
				 distance = new InputVariable(distanceClasses, "distanza");
				
				// velocità del robot avversario
				Map<String, Class> speedClasses = new HashMap<String, Class>();
				Class Vbassa = new InputClass(-0.01, 0.00*maxSpeed, 0.2*maxSpeed, 0.30*maxSpeed, "V_bassa");
				Class VmedioBassa = new InputClass(0.21*maxSpeed, 0.31*maxSpeed, 0.4*maxSpeed, 0.6*maxSpeed, "V_medioBassa");
				Class Vmedia = new InputClass(0.41*maxSpeed, 0.61*maxSpeed, 0.7*maxSpeed, 0.8*maxSpeed, "V_media");
				Class Valta = new InputClass(0.71*maxSpeed, Math.abs(0.81*maxSpeed), 1.01*maxSpeed, 1.01*maxSpeed, "V_alta");
				
				
				speedClasses.put(Vbassa.getName(), Vbassa);
				speedClasses.put(VmedioBassa.getName(), VmedioBassa);
				speedClasses.put(Vmedia.getName(), Vmedia);
				speedClasses.put(Valta.getName(), Valta);
				
				 speed = new InputVariable(speedClasses, "velocita");
				
				
				
			    // classi della potenza di sparo del cannone
				// il range è [0, 3]
				Map<String, Class> firePowerClasses = new HashMap<String, Class>();
				Class Fbassa = new OutputClass(1, 1, 1, 1.5, "F_bassa");
				Class FmedioBassa = new OutputClass(1.2, 1.5, 1.5, 2, "F_medioBassa"); 
				Class Fmedia = new OutputClass(1.7, 2, 2, 2.5, "F_media");
				Class Falta = new OutputClass(2.2, 3, 3, 3, "F_alta");
				
				firePowerClasses.put(Fbassa.getName(), Fbassa);
				firePowerClasses.put(FmedioBassa.getName(), FmedioBassa);
				firePowerClasses.put(Fmedia.getName(), Fmedia);
				firePowerClasses.put(Falta.getName(), Falta);	
				
				// variabile della potenza di sparo del cannone
				firePower = new OutputVariable(firePowerClasses, "firePower");
				
				 rules = new LinkedList<Rule>();

				
				// FAM  
				Map<String, String> inputHashmap = new HashMap<String, String>();
				Map<String, String> outputHashmap = new HashMap<String, String>();
				
				// Distanza Bassa

				inputHashmap.put("distanza", "D_bassa");
				inputHashmap.put("velocita", "V_bassa");
				outputHashmap.put("firePower", "F_alta");
				
				 rule = new Rule(inputHashmap, outputHashmap);
				rules.add(rule);
						
				inputHashmap = new HashMap<String, String>();
				outputHashmap = new HashMap<String, String>();
				inputHashmap.put("distanza", "D_bassa");
				inputHashmap.put("velocita", "V_medioBassa");
				outputHashmap.put("firePower", "F_media");
				
				 rule = new Rule(inputHashmap, outputHashmap);
				 rules.add(rule);
				
			    inputHashmap = new HashMap<String, String>();
			    outputHashmap = new HashMap<String, String>();
				inputHashmap.put("distanza", "D_bassa");
				inputHashmap.put("velocita", "V_media");
				outputHashmap.put("firePower", "F_media");
				
				 rule = new Rule(inputHashmap, outputHashmap);
				rules.add(rule);
				
				inputHashmap = new HashMap<String, String>();
				outputHashmap = new HashMap<String, String>();
				inputHashmap.put("distanza", "D_bassa");
				inputHashmap.put("velocita", "V_alta");
				outputHashmap.put("firePower", "F_alta");
				
				 rule = new Rule(inputHashmap, outputHashmap);
				 rules.add(rule);
				 
				 // Distanza MedioBassa
				 inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
				 inputHashmap.put("distanza", "D_medioBassa");
					inputHashmap.put("velocita", "V_bassa");
					outputHashmap.put("firePower", "F_alta");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_medioBassa");
					inputHashmap.put("velocita", "V_medioBassa");
					outputHashmap.put("firePower", "F_media");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_medioBassa");
					inputHashmap.put("velocita", "V_media");
					outputHashmap.put("firePower", "F_media");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_medioBassa");
					inputHashmap.put("velocita", "V_alta");
					outputHashmap.put("firePower", "F_media");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					// Distanza Media
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_media");
					inputHashmap.put("velocita", "V_bassa");
					outputHashmap.put("firePower", "F_media");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_media");
					inputHashmap.put("velocita", "V_medioBassa");
					outputHashmap.put("firePower", "F_media");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_media");
					inputHashmap.put("velocita", "V_media");
					outputHashmap.put("firePower", "F_medioBassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_media");
					inputHashmap.put("velocita", "V_alta");
					outputHashmap.put("firePower", "F_medioBassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					// Distanza Alta
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_alta");
					inputHashmap.put("velocita", "V_bassa");
					outputHashmap.put("firePower", "F_medioBassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_alta");
					inputHashmap.put("velocita", "V_medioBassa");
					outputHashmap.put("firePower", "F_medioBassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_alta");
					inputHashmap.put("velocita", "V_media");
					outputHashmap.put("firePower", "F_bassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
					
					inputHashmap = new HashMap<String, String>();
					outputHashmap = new HashMap<String, String>();
					inputHashmap.put("distanza", "D_alta");
					inputHashmap.put("velocita", "V_alta");
					outputHashmap.put("firePower", "F_bassa");
					
					 rule = new Rule(inputHashmap, outputHashmap);
					rules.add(rule);
				
				 input = new HashMap<String, FuzzyVariable>();
				input.put(distance.getName(), distance);
				input.put(speed.getName(), speed);
				
				 output = new HashMap<String, FuzzyVariable>();
				output.put(firePower.getName(), firePower);
	
	}
	
	
	/*// passo al metodo la distanza corrente del nemico
	public void setEnemyDistance(double dist) {
		
		((InputVariable)distance).setValue(dist);
		
	}
	
	// passo al metodo la velocità corrente del nemico
	public void setEnemySpeed(double sp) {
		
		((InputVariable)speed).setValue(sp);
		
	}*/
	
	// resituisco il valore crisp di firePower
	/*public double getFirepower() {
		
		
		for (Rule r : rules) {
			den += r.applyRule(input, output);
		}
	    	
    	// calcolo valore crisp 
    	return crisp = firePower.getOutput(den);
	}*/
	
	
	// unico metodo che riceve distanza e velocità corrente del nemico
	// e restituisce la potenza di firePower
	
	public double setFire(double dist, double sp) {
		
		((InputVariable)distance).setValue(dist);
		((InputVariable)speed).setValue(Math.abs(sp));
		
		for (Rule r : rules) {
			den += r.applyRule(input, output);
		}
		
		crisp = firePower.getOutput(den);
		System.out.println("distanza del nemico: "+ dist);
		System.out.println("velocità del nemico: " + sp);
		System.out.println("valore di sparo: " + crisp);
		return crisp;
	}
	
	
	
	public void setHypotenuse(double h, double w) {
		
		this.hypotenuse = Math.sqrt(w*w + h*h);
	}
	
	public double getHypotenuse() {
		
		return this.hypotenuse;
	}

}
