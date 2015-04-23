package burlap.oomdp.stochasticgames;

import java.util.Map;

import burlap.oomdp.core.states.State;

public interface WorldObserver {
	public void observe(State s, JointAction ja, Map<String, Double> reward, State sp);
}
