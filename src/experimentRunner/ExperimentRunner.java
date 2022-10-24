package experimentRunner;

import java.io.IOException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import evolutionSimulation.EvolutionSimulation;
import evolutionSimulation.GenerationInheritance;
import evolutionSimulation.MutationInheritance;
import evolutionSimulation.NonDynamicEvolutionSimulation;
import evolutionSimulation.UnchangingInheritance;
import evolutionSimulation.learningStrategy.StrategyGeneration;

public class ExperimentRunner {
	Random rand;
	int simulations;
	public ExperimentRunner(JSONObject obj) {
		try{
			simulations = obj.getInt("simulations");
		}catch(JSONException e) {
			simulations = 1;
		}
		try{
			rand = new Random(obj.getInt("seed"));
		}catch(JSONException e) {
			rand = new Random();
		}
		
	}
	
	public void run(JSONObject obj) throws IOException{
		for(int i = 0; i<simulations; i++) {
			EvolutionSimulation simulation = getSimulation(obj.getJSONObject("simulation"));
			simulation.run();
		}
	}

	private EvolutionSimulation getSimulation(JSONObject obj) {
		switch (obj.getString("type")){
		case"DynamicEvolutionSimulation":{
			
		}
		case"NonDynamicEvolutionSimulation":{
			GenerationInheritance inheritance = getInheritance(obj.getJSONObject("inheritance"));
			Landscape landscape = 
			StrategyGeneration gen0 = getGeneration(obj.getJSONObject("gen0"));
			GenerationInheritance comparisonInheritance = getInheritance(obj.getJSONObject("comparisonInheritance"));
			StrategyGeneration[] comp0;
			JSONArray comp0JSON = obj.getJSONArray("comp0");
			comp0 = new StrategyGeneration[comp0JSON.length()];
			for(int i = 0; i<comp0.length; i++) {
				comp0[i] = getGeneration(comp0JSON.getJSONObject(i));
			}
			int numGens = obj.getInt("numGens");
			int strategyRuns = obj.getInt("strategyRuns");
			return new NonDynamicEvolutionSimulation(inheritance, gen0, comparisonInheritance, comp0, numGens, strategyRuns);
		}
		}
		return null;
	}
	private StrategyGeneration getGeneration(JSONObject jsonObject) {
		
	}

	private GenerationInheritance getInheritance(JSONObject obj) {
		switch(obj.getString("type")) {
		case "MutationInheritance":{
			float mutationRate = obj.getFloat("mutationRate");
			float percentChildren = obj.getFloat("percentChildren");
			return new MutationInheritance(mutationRate, percentChildren, rand);
		}
		case "UnchangingInheritance":{
			return new UnchangingInheritance();
		}
		}
		return null;
	}
}
