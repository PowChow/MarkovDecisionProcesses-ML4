package burlap.assignment4.easy;

import burlap.assignment4.easy.BasicGridWorld;
import burlap.assignment4.util.BasicRewardFunction;
import burlap.assignment4.util.BasicTerminalFunction;
import burlap.assignment4.util.MapPrinter;
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

public class EasyGridPlotTest {
	
	private static boolean visualizeInitialGridWorld = true;
	private static boolean runValueIteration = true;
	private static boolean runPolicyIteration = true;
	private static boolean runQLearning = true;
	
	private static Integer MAX_ITERATIONS = 100;
	private static Integer NUM_INTERVALS = 100;

	protected static int[][] userMap = new int[][] { 
		{ 0, 0, 0, 0, 0},
		{ 0, 1, 1, 1, 0},
		{ 0, 1, 1, 1, 0},
		{ 1, 0, 1, 1, 0},
		{ 0, 0, 0, 0, 0}, };

	public static void main(String [] args){

		// convert to BURLAP indexing
		int[][] map = MapPrinter.mapToMatrix(userMap);
		int maxX = map.length-1;
		int maxY = map[0].length-1;
		// 

		BasicGridWorld gen = new BasicGridWorld(map,maxX,maxY); //0 index map is 5X5
		Domain domain = gen.generateDomain();

		//setup initial state
		State s = BasicGridWorld.getExampleState(domain);
		//EasyGridWorld.setAgent(s, 0, 0);
		//EasyGridWorld.setLocation(s, 0, 10, 10);

		//ends when the agent reaches a location
//		final TerminalFunction tf = new SinglePFTF(domain.
//				getPropFunction(EasyGridWorld.PFATLOCATION));
//		
//		//reward function definition
//		final RewardFunction rf = new GoalBasedRF(new TFGoalCondition(tf), 5., -0.1);
		
		//replaced with EasyGridWorld Reward and Terminal Function Calls
		final RewardFunction rf = new BasicRewardFunction(maxX,maxY); //Goal is at the top right grid
		final TerminalFunction tf = new BasicTerminalFunction(maxX,maxY); //Goal is at the top right grid	


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
				return "Q-learning, gamma .2";
				//return "Value Iteration"; 
			}

			@Override
			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.2, hashingFactory, 0.3, 0.1);
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
				