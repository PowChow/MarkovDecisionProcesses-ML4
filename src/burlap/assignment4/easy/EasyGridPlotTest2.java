package burlap.assignment4.easy;

import burlap.assignment4.easy.EasyGridWorld;
import burlap.assignment4.util.BasicRewardFunction;
import burlap.assignment4.util.BasicTerminalFunction;
import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.oomdp.auxiliary.common.ConstantStateGenerator;
import burlap.oomdp.auxiliary.common.SinglePFTF;
import burlap.oomdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.GoalBasedRF;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class EasyGridPlotTest2 {

	public static void main(String [] args){

		EasyGridWorld gw = new EasyGridWorld(); 
		final Domain domain = gw.generateDomain(); //generate the grid world domain

		//setup initial state
		State s = EasyGridWorld.getExampleState(domain);
		//EasyGridWorld.setAgent(s, 0, 0);
		//EasyGridWorld.setLocation(s, 0, 10, 10);

		//ends when the agent reaches a location
//		final TerminalFunction tf = new SinglePFTF(domain.
//				getPropFunction(EasyGridWorld.PFATLOCATION));
//		
//		//reward function definition
//		final RewardFunction rf = new GoalBasedRF(new TFGoalCondition(tf), 5., -0.1);
		
		//replaced with EasyGridWorld Reward and Terminal Function Calls
		final RewardFunction rf = new BasicRewardFunction(10, 10);
		final TerminalFunction tf = new BasicTerminalFunction(10, 10);		


		//initial state generator
		final ConstantStateGenerator sg = new ConstantStateGenerator(s);


		//set up the state hashing system for looking up states
		final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();


		/**
		 * Create factory for Q-learning agent
		 */
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {

			@Override
			public String getAgentName() {
				return "Q-learning, gamma .99";
				//return "Value Iteration"; 
			}

			@Override
			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
				//return new ValueIteration(domain, rf, tf, 0.99, hashingFactory, 0.001, 100);
			}
		};

		//define learning environment
		SimulatedEnvironment env = new SimulatedEnvironment(domain, rf, tf, sg);

		//define experiment
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env,
				10, 100, qLearningFactory);

		exp.setUpPlottingConfiguration(500, 250, 2, 1000, TrialMode.MOSTRECENTANDAVERAGE,
				PerformanceMetric.CUMULATIVESTEPSPEREPISODE, 
				PerformanceMetric.AVERAGEEPISODEREWARD);


		//start experiment
		exp.startExperiment();


	}

}
				