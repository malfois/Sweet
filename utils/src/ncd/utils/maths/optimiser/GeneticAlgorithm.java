/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package ncd.utils.maths.optimiser;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.eclipse.january.dataset.Dataset;
import org.eclipse.january.dataset.DatasetUtils;
import org.eclipse.january.dataset.DoubleDataset;
import org.eclipse.january.dataset.IDataset;

import ncd.parameter.DoubleParameter;
import ncd.utils.maths.fitting.Function;

public class GeneticAlgorithm {

	protected final RandomGenerator		generator					= new MersenneTwister();				// Well19937a();
	protected final RandomDataGenerator	prng						= new RandomDataGenerator(generator);

	private final int					MaxNumberOfStaticBestValue	= 50;
	private final int					maxIterations				= 10000;
	private double						qualityFactor				= 0.0;

	protected Function					function;
	protected DoubleDataset				coord;
	protected DoubleDataset				data;
	protected double					residual;

	public GeneticAlgorithm(IDataset coordinates, IDataset data, Function function) {
		this.coord = (DoubleDataset) DatasetUtils.cast(coordinates, Dataset.FLOAT64);
		this.data = (DoubleDataset) DatasetUtils.cast(data, Dataset.FLOAT64);
		this.function = function;
		this.residual = function.residual(data, coordinates);
	}

	/**
	 * Constructor which takes quality of fit and seed for random number generator
	 * 
	 * @param quality
	 * @param seed
	 */
	public GeneticAlgorithm(double quality, Long seed) {
		qualityFactor = quality;
		if (seed != null) generator.setSeed(seed);
	}

	public Function optimize(IDataset coordinates, IDataset data, Function function) {

		this.coord = (DoubleDataset) DatasetUtils.cast(coordinates, Dataset.FLOAT64);
		this.data = (DoubleDataset) DatasetUtils.cast(data, Dataset.FLOAT64);
		this.function = function;
		this.residual = function.residual(data, coordinates);

		Population population = new Population(this.function, this.data, this.coord, true);
		// now do the epochs
		double minval = Double.MAX_VALUE;
		double previousMinval = minval;
		int numberOfTimesMinvalTheSame = 0;

		int iterationCount = 0;
		while (minval > qualityFactor) {
			double mean = 0;

			population = population.evolve(iterationCount);
			mean = population.getMean();

			// at the end find the best solution, and evaluate it, to fix the values into the model
			minval = population.getMinVal();
			// Exit if the number of iterations has been exceeded
			iterationCount++;
			if (iterationCount > maxIterations) {

				System.out.println("Too many iterations have been performed, best available soulution has been provided");
				break;
			}

			// Exit if the minval has not changed in a number of iterations
			if (previousMinval == minval) {
				numberOfTimesMinvalTheSame += 1;
				if (numberOfTimesMinvalTheSame > MaxNumberOfStaticBestValue) {
					// the solution is probably good enough,
					break;
				}
			} else {
				previousMinval = minval;
				numberOfTimesMinvalTheSame = 0;
			}
			if (minval == 0.0) break;
			minval = Math.abs(mean - minval) / minval;

		}
		// at the end find the best solution, and evaluate it, to fix the values
		// into the model
		Individual best = population.getIndividual(0);
		int nParameters = best.getNumberOfParameters();
		for (int i = 0; i < nParameters; i++) {
			double param = best.getParameter(i);
			DoubleParameter params = this.function.getParameter(i);
			params.setValue(param);
			this.function.setParameter(i, params);
		}

		this.residual = this.function.residual(this.data, coord);
		return this.function;
	}

	public double getMinVal() {
		return this.residual;
	}

	private class Population {

		private int				numberOfIndividuals;
		private Individual[]	individuals;
		private Function		function;
		private DoubleDataset	data;
		private DoubleDataset	coordinates;

		public Population(Function function, DoubleDataset data, DoubleDataset coordinates, boolean generate) {
			this.function = function;
			this.data = data;
			this.coordinates = coordinates;
			this.evaluateNumberOfIndividuals();
			if (generate) this.generate(function);
		}

		public void evaluateNumberOfIndividuals() {
			int numberOfParameters = this.function.getNoOfParameters();
			// top epoch is normally the number of dimensions multiplied by 20 minus 1.
			this.numberOfIndividuals = numberOfParameters * 20 - 1;
			if (this.numberOfIndividuals < 100) this.numberOfIndividuals = 99;
			this.individuals = new Individual[this.numberOfIndividuals + 1];
		}

		public double getMean() {
			double mean = 0.0;
			for (int i = 0; i <= this.numberOfIndividuals; i++) {
				double delta = this.individuals[i].getResidual() - mean;
				mean = mean + delta / (i + 1);
			}
			return mean;
		}

		public double getMinVal() {
			double minval = this.individuals[0].getResidual();
			for (int i = 1; i <= this.numberOfIndividuals; i++) {
				if (individuals[i].getResidual() < minval) {
					minval = individuals[i].getResidual();
				}
			}
			return minval;
		}

		public void generate(Function function) {
			int numberOfParameters = this.function.getNoOfParameters();

			// first one should be the original, just in case its a good solution
			this.individuals[0] = new Individual(numberOfParameters);
			for (int j = 0; j < numberOfParameters; j++) {
				this.individuals[0].setParameter(j, this.function.getParameterValue(j));
			}

			// the others explore space in the bounded regions
			for (int i = 1; i <= this.numberOfIndividuals; i++) {
				this.individuals[i] = new Individual(numberOfParameters);
				for (int j = 0; j < numberOfParameters; j++) {
					double upper = function.getParameter(j).getMaximum();
					double lower = function.getParameter(j).getMinimum();
					double value = prng.nextUniform(lower, upper);
					this.individuals[i].setParameter(j, value);
				}
			}

			this.calculateResiduals();
		}

		public void calculateResiduals() {
			this.individuals[0].calculateResidual(this.function, this.data, this.coordinates);
			for (int i = 1; i <= this.numberOfIndividuals; i++) {
				this.individuals[i].calculateResidual(this.function, this.data, this.coordinates);
			}
		}

		public Individual updateBestIndividual() {
			// the first member of the new epoch, should be the best member of the last
			double minvalue = this.individuals[0].getResidual();
			int minposition = 0;
			for (int i = 1; i <= this.numberOfIndividuals; i++) {
				double res = this.individuals[i].getResidual();
				if (res < minvalue) {
					minvalue = res;
					minposition = i;
				}
			}
			return this.individuals[minposition];
		}

		public Population evolve(int iterationCount) {
			final double mutantProportion = 0.5;
			Individual individu0 = this.updateBestIndividual();

			Population nextPopulation = new Population(this.function, this.data, this.coordinates, false);
			nextPopulation.setIndividual(0, individu0);
			for (int i = 1; i <= this.numberOfIndividuals; i++) {
				Individual[] parents = this.getParents();
				Individual individu = this.crossbreed(parents);
				if (generator.nextDouble() > mutantProportion) {
					this.getMutatedGene(individu);
				}
				nextPopulation.setIndividual(i, individu);
			}
			nextPopulation.clip();
			nextPopulation.calculateResiduals();
			return nextPopulation;
		}

		public void clip() {
			for (int i = 0; i <= this.numberOfIndividuals; i++) {
				this.individuals[i].clip(this.function);
			}
		}

		public Individual[] getParents() {
			// get mum and dad
			int c1 = prng.nextInt(0, this.numberOfIndividuals);
			int c2 = prng.nextInt(0, this.numberOfIndividuals);
			int c3 = prng.nextInt(0, this.numberOfIndividuals);
			int c4 = prng.nextInt(0, this.numberOfIndividuals);

			while (Double.isNaN(this.individuals[c1].getResidual())) {
				c1 = prng.nextInt(0, this.numberOfIndividuals);
			}
			while (Double.isNaN(this.individuals[c2].getResidual())) {
				c2 = prng.nextInt(0, this.numberOfIndividuals);
			}
			while (Double.isNaN(this.individuals[c3].getResidual())) {
				c3 = prng.nextInt(0, this.numberOfIndividuals);
			}
			while (Double.isNaN(this.individuals[c4].getResidual())) {
				c4 = prng.nextInt(0, this.numberOfIndividuals);
			}

			int mum = this.individuals[c1].getResidual() < this.individuals[c2].getResidual() ? c1 : c2;
			int dad = this.individuals[c3].getResidual() < this.individuals[c4].getResidual() ? c3 : c4;
			Individual[] parents = new Individual[2];
			parents[0] = this.individuals[mum];
			parents[1] = this.individuals[dad];

			return parents;
		}

		public Individual crossbreed(Individual[] parents) {
			int numberOfParameters = parents[0].getNumberOfParameters();
			Individual individual = new Individual(numberOfParameters);

			// cross-breed at a point, between 2 different functions.
			int point = prng.nextInt(0, numberOfParameters - 1);
			Individual parentepoch = parents[0];

			if (1 >= point) {
				parentepoch = parents[1];
			}
			for (int l = 0; l < numberOfParameters; l++) {
				individual.setParameter(l, parentepoch.getParameter(l));
			}
			return individual;
		}

		public void getMutatedGene(Individual individu) {
			// set some factors
			final double mutantScaling = 0.5;

			// add in random mutation
			int c1 = prng.nextInt(0, this.numberOfIndividuals);
			int c2 = prng.nextInt(0, this.numberOfIndividuals);

			Individual individu1 = this.individuals[c1];
			Individual individu2 = this.individuals[c2];
			int numberOfParameters = individu1.getNumberOfParameters();
			double[] param = individu.getParameters();
			double[] values = new double[numberOfParameters];
			for (int i = 0; i < numberOfParameters; i++) {
				values[i] = param[i];
			}
			for (int i = 0; i < numberOfParameters; i++) {
				values[i] += (individu1.getParameter(i) - individu2.getParameter(i)) * mutantScaling;
			}
			for (int i = 0; i < numberOfParameters; i++) {
				individu.setParameter(i, values[i]);
			}
		}

		public Individual getIndividual(int index) {
			return this.individuals[index];
		}

		public void setIndividual(int index, Individual individual) {
			this.individuals[index] = individual;
		}

		@Override
		public String toString() {
			String text = "Population [nIndividuals= " + this.numberOfIndividuals + " ";
			int nparam = this.individuals.length;
			for (int i = 0; i < nparam; i++) {
				text = text + String.valueOf(i) + " " + this.individuals[i].toString() + " ";
			}
			return text;
		}

	}

	private class Individual {

		private double[]	parameters;
		private double		residual;
		private int			numberOfParameters;

		public Individual(int numberOfparameters) {
			this.numberOfParameters = numberOfparameters;
			parameters = new double[this.numberOfParameters];
		}

		public int getNumberOfParameters() {
			return this.numberOfParameters;
		}

		public Double getParameter(int index) {
			return parameters[index];
		}

		public void setParameter(int index, double parameter) {
			this.parameters[index] = parameter;
		}

		public double[] getParameters() {
			double[] parametersArray = new double[this.numberOfParameters];
			int i = 0;
			for (double params : this.parameters) {
				parametersArray[i] = params;
				i++;
			}
			return parametersArray;
		}

		public boolean clip(Function f) {
			boolean isclip = false;
			// at the end of the epoch, flush the next epoch to the epoch
			for (int j = 0; j < this.numberOfParameters; j++) {
				// then clip it
				final DoubleParameter p = f.getParameter(j);

				if (this.parameters[j] > p.getMaximum()) {
					this.parameters[j] = 2. * p.getMaximum() - this.parameters[j];
					isclip = true;
				}
				if (this.parameters[j] < p.getMinimum()) {
					this.parameters[j] = 2. * p.getMinimum() - this.parameters[j];
					isclip = true;
				}
			}
			return isclip;
		}

		public void calculateResidual(Function function, DoubleDataset data, DoubleDataset coordinates) {
			for (int j = 0; j < this.numberOfParameters; j++) {
				function.getParameter(j).setValue(this.parameters[j]);
			}
			double res = function.residual(data, coordinates);
			this.residual = Double.isNaN(res) ? Double.MAX_VALUE : res;
		}

		public double getResidual() {
			return this.residual;
		}

		@Override
		public String toString() {
			String text = "Individual [residual=" + residual + "[parameters= ";
			for (int i = 0; i < this.numberOfParameters; i++) {
				text = text + String.format("%8.3e ", this.parameters[i]);
			}
			text = text + "]";
			return text;
		}

	}

}
