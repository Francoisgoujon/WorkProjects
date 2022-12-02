package definition;

import java.util.ArrayList;
import java.util.List;

public class ContrainteInferieurEgal implements Constraint {
	
	List<Variable> vars;
	
	public ContrainteInferieurEgal(Variable v1, Variable v2) {
		this.vars = new ArrayList<Variable>();
		this.getVars().add(v1);
		this.getVars().add(v2);
	}
	
	@Override
	public List<Variable> getVars() {
		return this.vars;
	}

	@Override
	public boolean isSatisfied() {
		return (this.allInstantiated() && (this.getVars().get(0).getValue() <= this.getVars().get(1).getValue()));
	}

	@Override
	public boolean isNecessary() {
		boolean b = false;
		Domain dom1 = this.getVars().get(0).getDomain().clone();
		Domain dom2 = this.getVars().get(1).getDomain().clone();
		for (int v1 : dom1) {
			for (int v2 : dom2) {
				if (v1 <= v2) {
					b = true;
					break;
				}
			}
		}
		return b;
	}

	@Override
	public boolean filter() {
		boolean b = false;
		Domain dom1 = this.getVars().get(0).getDomain();
		Domain dom2 = this.getVars().get(1).getDomain();
		int max2 = dom2.lastValue();
		for (int v : dom1) {
			if (v > max2) {
				dom1.remove(v);
				b = true;
			}
		}
		int min1 = dom1.firstValue();
		for (int v : dom2) {
			if (v < min1) {
				dom2.remove(v);
				b = true;
			}
		}
		return b;
	}

	@Override
	public boolean allInstantiated() {
		return this.getVars().get(0).isInstantiated() && this.getVars().get(1).isInstantiated();
	}
	public String toString() {
		return this.getVars().get(0).toString() + "<=" + this.getVars().get(1).toString();
	}


}
