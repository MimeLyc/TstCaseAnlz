package strategies;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genetic {
	int GenesNum = 0; //How Many Test Cases.
	final int PopulationSize = 100; //The size of the Population.
	final float CrossoverProbability = 0.8f, MutationProbability = 0.1f; //The Probabilities to Crossover and Mutation.
	static String Directory, matrixFile;
//	String CoverageFile; //The statement/method/branch coverage file.
	int[][] CoverageMatrix; //Store the test case coverage information(Matrix).
	int[][] Population; //Store all the Chromosomes in the Population.
	ArrayList<Integer> testCaseCovered = new ArrayList<Integer>(); //Store the Statement that are covered in the Coverage file.
	
	int[] resultSequence = new int[this.GenesNum]; //Store the Result Test Case Sequence.
	
	static int Nth = 0; //The Nth Generation.
	
	public Genetic(int[][] cvdMatrix,String Directory,String matrixFile){
		this.CoverageMatrix = cvdMatrix;
		this.Directory = Directory; //get the directory to Create a output file for Statistic Data.
		this.matrixFile = matrixFile; //Create a new file use the same file prefix for Statistic Data.

//        System.out.println(cvdMatrix.length);
        this.GenesNum = cvdMatrix.length; //Initialize the GenesNum, which is the Test Case Number.
        this.Population = new int[this.PopulationSize][this.GenesNum];

        for(int i=0; i<CoverageMatrix[0].length; i++){
            boolean covered = false;
            for(int j=0; j<CoverageMatrix.length; j++){
                if(CoverageMatrix[j][i] == 1){
                    covered = true;
                    break;
                }
            }
            if(covered){
                testCaseCovered.add(i);
            }
        }
//		this.CoverageFile = Directory+matrixFile;
	}

	
	//Compute the Average Percentage Block/Statement/Branch Coverage
	public float getAveragePercentageCoverage(int[] Chromosome){
		float AveragePercentageCoverage = 0;
		int firstCoveredSum = 0; //Sum of the first test case in the order that covers the Block/Statement/Branch i.
			//System.out.println("testCaseCovered Size: "+this.testCaseCovered.size());
		for(int k=0; k<this.testCaseCovered.size(); k++){
			for(int i=0; i<Chromosome.length; i++){
				if(this.CoverageMatrix[Chromosome[i]][this.testCaseCovered.get(k)] == 1){
					firstCoveredSum += i;
					break;
				}
			}
		}
		//System.out.println("firstCoveredSum: "+firstCoveredSum);
		AveragePercentageCoverage = 1-(float)((float)firstCoveredSum / (float)(this.GenesNum * this.testCaseCovered.size()))+(1.0F / (float)(2 * this.GenesNum));
		return AveragePercentageCoverage;
	}
	
	//Get all the Average Percentage Coverage Metric Value in the Population and Compute the Fitness for each Chromosome.
	public float[] getAllAveragePercentageCoverageMetricAndFitness(){
		float Metrics[] = new float[this.PopulationSize];
		float Fitness[] = new float[this.PopulationSize];
		
		for(int i=0; i<this.PopulationSize; i++){
			Metrics[i] = this.getAveragePercentageCoverage(this.Population[i]);
		}
		float[] tempMetrics = Arrays.copyOf(Metrics, Metrics.length); // Copy the original Array to sort it.
		Arrays.sort(tempMetrics); //Sort the temp Metric Value Array.
		//this.Print(tempMetrics);
		int[] Positions = new int[Metrics.length];
		//Get the Position value for each Chromosome.
		for(int i=0; i<Metrics.length; i++){
			for(int j=0; j<tempMetrics.length; j++){
				if(tempMetrics[j] == Metrics[i]){
					Positions[i] = j+1; //Larger APC will get larger Position Value.
					tempMetrics[j] = -1;
					break;
				}
			}
		}
		//Arrays.sort(Positions);
		//System.out.println("Positions len: "+Positions.length);
		//this.Print(Positions); // Print the Metrics Value.
		//Compute the Fitness for each Chromosome.
		for(int i=0; i<Positions.length; i++){
			Fitness[i] = 2 * ((Positions[i] -1) / (float)this.PopulationSize);
		}
		//this.Print(Fitness);
		return Fitness;
	}
	//Use Stochastic Universal Sampling(SUS) for Selection N individuals.
	public int[] Selection(int N){
		float[] Fitness = this.getAllAveragePercentageCoverageMetricAndFitness(); //Get the Fitness value for each Chromosome.
		/*float totalFitness = 0;
		
		for(int i=0; i<Fitness.length; i++){
			totalFitness += Fitness[i];
		}

		System.out.println("TotalFitness: "+totalFitness);*/
		//
		//this.Print(Fitness);
		
		float[] MaxAndMin = this.getMaxAndMin(Fitness); //get the Max and Min Fitness value.
		//System.out.println(this.Nth+" Max: "+MaxAndMin[0]+", Min: "+MaxAndMin[1]);
		
		int[] Choose = this.SUS(Fitness, N);
		//this.Print(Choose);
		//System.out.println(Choose[0]+":"+Fitness[Choose[0]]+", "+Choose[1]+":"+Fitness[Choose[1]]);
		
		return Choose;
		
	}
	//Stochastic Universal Sampling
	public int[] SUS(float[] Fitness, int N){
		double totalFitness = 0; //Sum of fitness.
		double P = 0; //distance between the pointers.
		
		for(int i=0; i<Fitness.length; i++){
			totalFitness += Fitness[i];
		}
		P = totalFitness / N;
		//System.out.println("P: "+P);
		//Pick random number between 0 and P
		double start = Math.random() * P;
		//Pick n individuals
		int[] individuals = new int[N];
		int index = 0;
		double sum = Fitness[index];
		for(int i=0; i<N; i++){
			// Determine pointer to a segment in the population
            double pointer = start + i * P;
            // Find segment, which corresponds to the pointer
          	  if (sum >= pointer) {
              		  individuals[i] = index;
          	  } else {
                	for (++index; index < Fitness.length; index++) {
                  	  sum += Fitness[index];
                  	  if (sum >= pointer) {
                      	 	 individuals[i] = index;
                       	 	 break;
                   	 }
               		}
           	   }
		}
		// Return the set of indexes, pointing to the chosen individuals
        return individuals;
	}
	public int[] RWS(float[] Fitness, float[] points, int N){
		int[] keep = new int[N];
		int k=0;
		for(int i=0; i<points.length; i++){
			for(int p=0; p<Fitness.length; p++){
				float sum = 0;
				for(int q=0; q<=p; q++){
					sum += Fitness[q];
				}
				if(sum >= points[i]){
					keep[k] = p;
					k++;
					break;
				}
			}
		}
		return keep;
	}
	
	public int uniform(int i, int m){ // Returns a random integer i <= uniform(i,m) <= m
		return i+(int)(Math.random() * (m-i));
	}
	
	public int[] permute(int permutation[], int n)
	{
	    int i;
	    for (i = 0; i < n; i++) {
	        int j = uniform(i, n -1);
	        int swap = permutation[i];
	        permutation[i] = permutation[j];
	        permutation[j] = swap;
	    }
	    return permutation;
	}
	//Generate the Random Population
	public void GenerateRandomPopulation(){
		int[] Sample = new int[this.GenesNum];
		for(int i=0; i<Sample.length; i++){
			Sample[i] = i;
		}
		
		for(int j=0; j< this.PopulationSize; j++){
			int[] temp = Arrays.copyOf(Sample, Sample.length);
			this.Population[j] = this.permute(temp, temp.length);
			//this.Print(this.Population[j]); //Print the Population.
		}
	}
	
	public float[] getMaxAndMin(float[] a){
		float[] results = new float[2];
		results[0] = a[0];
		results[1] = a[0];
		for(int i=0; i<a.length; i++){
			if(a[i] > results[0]){
				results[0] = a[i]; //get the Max.
			}else if(a[i] < results[1]){
				results[1] = a[i]; //get the Min.
			}
		}
		return results;
	}
	public void Print(ArrayList<Integer> a){
		for(int i=0; i<a.size(); i++){
			System.out.print(a.get(i)+" ");
		}
		System.out.println();
	}
	public void Print(int[] a){
		for(int i=0; i<a.length; i++){
			System.out.print(a[i]+" ");
		}
		System.out.println();
	}
	public void Print(float[] a){
		for(int i=0; i<a.length; i++){
			System.out.print(a[i]+" ");
		}
		System.out.println();
	}
	//CrossOver the 2 input Chromosome
	public Object[] CrossOver(int[] a, int[] b){
		if(a.length != b.length){
			System.out.println("Two Array Size is NOT Equal: "+a.length+", "+b.length);
			System.exit(0);
		}
		int len = a.length;
		int[] p1 = Arrays.copyOf(a,  len);
		int[] p2 = Arrays.copyOf(b,  len);
	
		int random = (int)(Math.random() * len);//2;
		int[] o1 = new int[len], o2 = new int[len];
		ArrayList<Integer> a1 = new ArrayList<Integer>();
		ArrayList<Integer> a2 = new ArrayList<Integer>();
		
		for(int i=0; i<random; i++){
			o1[i] = p1[i];
			a1.add(o1[i]);
			o2[i] = p2[i];
			a2.add(o2[i]);
		}
		for(int i=0; i<len; i++){
			if(a1.contains(p2[i])){
				p2[i] = -1;
			}
		}
		for(int i=0, j= random; i<len  && j < len; i++){
			if(p2[i] != -1){
				o1[j] = p2[i];
				j++;
			}
		}
		for(int i=0, j = 0; i<len && j<random; i++){
			if(a2.contains(p1[i])){
				p1[i] = -1;
			}
		}
		for(int i=0, j=random; i<len && j < len; i++){
			if(p1[i] != -1){
				o2[j] = p1[i];
				j++;
			}
		}
		//Print(o1);
		//Print(o2);
		
		return new Object[]{o1, o2};
		//p1 = o1;
		//p2 = o2;
	}
	
	public void Mutation(int[] Chromosome){ //
		//Randomly select two Genes and exchange their value;
		int pos0=0, pos1=0;
		
		while(pos0 == pos1){
			pos0 = this.uniform(0, this.GenesNum);
			pos1 = this.uniform(0, this.GenesNum);
			/*System.out.println("ERROR: Mutation() pos0 == pos1.");
			System.exit(1);*/
		}
		//Exchange the two Genes.
		int temp = Chromosome[pos0];
		Chromosome[pos0] = Chromosome[pos1];
		Chromosome[pos1] = temp;
		//System.out.println("Mutation:");
		//this.Print(Chromosome);
		//return Chromosome;
	}
	//Return the Selected Test Case Sequence.
	public int[] getSelectedTestSequence(){
		int Generations = 300; //The total number of Generations.
		
//		this.getCoverageMatrix(this.CoverageFile); //Read the Coverage Matrix.
		this.GenerateRandomPopulation(); // Generate the Initial Population.
		
		//Start the Whole Process for the number of Generations.
		for(int g=0; g<=Generations; g++){
			this.Nth = g; //Store the Nth Generation.
			//System.out.println(g+" Generation Start-----------");
			this.AveragePercentageCoverageCheck(g);//TODO 暂时不知到这个方法干啥的
			//this.PopulationCheck("1");
		int[] Individuals = this.Selection(this.PopulationSize); //Select the next generation.
		//this.PopulationCheck("2");
		
		int[][] tempPopulation = new int[this.PopulationSize][this.GenesNum];
		//System.out.println("Here1.");
		//System.out.print("check");
		//this.Print(Individuals);
		for(int i=0; i<Individuals.length; i++){
			tempPopulation[i] = this.Population[Individuals[i]];
		}
		//this.Print(this.Population[0]);
		this.Population = tempPopulation; // reassign the Selected Population to the Current Population.
		//float[] Fitness = this.getAllAveragePercentageCoverageMetricAndFitness();//Check
		//System.out.print("check");
		//this.Print(Fitness);//Check
		//this.Print(this.Population[0]);
		//this.PopulationCheck("3");
		
		//CrossOver 80 Chromosomes and Mutate 10 Chromosomes.
		this.CrossOverAndMutate2();
		
		}//End of the Whole Process.
		
		//float[] Fitness = this.getAllAveragePercentageCoverageMetricAndFitness(); //Get the Result Fitness.
		//System.out.println("Fitness Result: ");
		//this.Print(Fitness);
		return this.resultSequence;
	}
	
	public void CrossOverAndMutate2(){//CrossOver 80 Chromosomes and Mutate 10 Chromosomes.
		int[] originalArray = new int[this.PopulationSize];
		for(int i=0; i<this.PopulationSize; i++){ //Initialize the Original Array.
			originalArray[i] = i;
		}
		int[] CrossOverArray = KnuthShuffle(originalArray);
	    //this.Print(originalArray);
	    //After Randomly select 80 Chromosomes, CrossOver them.
	    int CrossOverNum = 80;
	    
	    for(int i=0; i<CrossOverNum; i = i+2){
	    	int parent0 = CrossOverArray[i], parent1 = CrossOverArray[i+1];
	    	Object[] Offsprings =  this.CrossOver(this.Population[parent0], this.Population[parent1]); //Crossover the 2 Parents.
	    	//System.out.println("Here3.");
	    	if(Offsprings.length != 2){
	    		System.out.println("ERROR: Offersprings len is not 2!");
	    		System.exit(1);
	    	}
	    	this.Population[parent0] = (int[]) Offsprings[0];
	    	this.Population[parent1] = (int[]) Offsprings[1];
	    }
	    //Random Select 10 Chromosomes and Mutate.
	    int[] MutationArray = KnuthShuffle(originalArray);
	    int MutationNum = 10; //Random Select 10 Chromosomes to Mutate.
	    
	    for(int i=0; i<MutationNum; i++){
	    	int select = MutationArray[i];
	    	this.Mutation(this.Population[select]);
	    }
	}
	
	public int[] KnuthShuffle(int[] a ){
			//Use  Fisher–Yates shuffle(Knuth Shuffle).
			int[] originalArray = Arrays.copyOf(a, a.length);
			Random rnd = new Random();
		    for (int i = originalArray.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      int tmp = originalArray[index];
		      originalArray[index] = originalArray[i];
		      originalArray[i] = tmp;
		    }
		    return originalArray;
	}
	public void PopulationCheck(String Message){//check that each Chromosome is Valid.
		//System.out.println(this.GenesNum+">>");
		for(int i=0; i<this.PopulationSize; i++){
			boolean valid = true;
			int validValue = 2211;
			int currentValue = 0;
			for(int j=0; j<this.GenesNum; j++){
				currentValue += this.Population[i][j];
			}
			if(currentValue != validValue){
				System.out.println("ERROR: PopulationCheck()--"+Message);
				this.Print(this.Population[i]);
				System.out.println("-----------------------------");
			}
		}
	}
	
	public void ChromosomeCheck(int[] c, String message){//check the Chromosome is valid.
		int sum = 0;
		for(int i=0; i<c.length; i++){
			sum += c[i];
		}
		/*if(sum != 2211){
			System.out.println("ERROR: Chromosome is Wrong.--"+message);
			this.Print(c);
		}*/
	}
	public void AveragePercentageCoverageCheck(int g){
		//Print the Max Average Percentage Coverage.
		float Metrics[] = new float[this.PopulationSize];
		float Max = 0, Sum = 0;
		ArrayList<Integer> MaxAPC = new ArrayList<Integer>(); //Store the Chromosome that has the Largest APC.

		for(int i=0; i<this.PopulationSize; i++){
			Metrics[i] = this.getAveragePercentageCoverage(this.Population[i]);
			if(Metrics[i] > Max){
				Max = Metrics[i];
			}
			Sum += Metrics[i];
		}
		for(int i=0; i<Metrics.length; i++){
			if(Metrics[i] == Max){
				MaxAPC.add(i);
			}
		}
		//System.out.println("Max APC: "+Max+", Average: "+(Sum / this.PopulationSize)+", Max APC Num: "+MaxAPC.size());

		if(g == 300){
			try{
			String outputDataFile = this.Directory+File.separator+this.matrixFile+"_output";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputDataFile));
			//for(int k=0; k<MaxAPC.size(); k++){//start
				//System.out.println("Sequence "+k);
			//get the Coverage Increase Trend.
			int[] selectedSequence = this.Population[MaxAPC.get(0)];
			this.resultSequence = selectedSequence; //return the Selected Test Case Sequence.
			int Statements = this.CoverageMatrix[0].length;
			int[] accumulatedCoverageArray = new int[Statements];

			//Output the selected Sequence.
			bw.write("Selected Sequence: \n");
			for(int i=0; i<selectedSequence.length; i++){
				bw.write(selectedSequence[i]+" ");
			}
			//System.out.println("Coverages:");
			bw.write("\nCoverages Increasement:\n");
			for(int i=0; i<selectedSequence.length; i++){
				this.MergeTwoCoverageArray(this.CoverageMatrix[selectedSequence[i]], accumulatedCoverageArray);
				float ratio = this.getNumberOfOneInArray(accumulatedCoverageArray) / (float)Statements;
				//System.out.println(ratio+",");
				bw.write(ratio+",");
			}
			//}//end\
			bw.flush();
			bw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//Return the Number of 1 in the Test Case Coverage Array
	public int getNumberOfOneInArray(int[] a){
		int num = 0;
		for(int i=0; i<a.length; i++){
			if(a[i] == 1){
				num++;
			}
		}
		return num;
	}
	//Merge the New Coverage Array to the Old Array.
	public void MergeTwoCoverageArray(int[] newArray, int[] oldArray){
		if(newArray.length != oldArray.length){
			System.out.println("ERROR: MergeTwoCoverageArray(): 2 Arrays length are not equal.");
		}
		for(int i=0; i<oldArray.length; i++){
			if(newArray[i] == 1){
				oldArray[i] = newArray[i];
			}
		}
	}
	/*For Unit Test.
	public static void main(String[] args){
		String directory = "/home/mimelyc/PGWork/30Programs/";
		String outputFile = "CoveredMethodMatrix.txt";
		Genetic tc = new Genetic(directory, outputFile);
		tc.getSelectedTestSequence();


	}
	*/
}
