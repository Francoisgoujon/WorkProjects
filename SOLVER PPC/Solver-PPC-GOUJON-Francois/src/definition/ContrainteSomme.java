package definition;

import java.util.ArrayList;
import java.util.List;

public class ContrainteSomme implements Constraint {
	
	List<Variable> vars;  // s = x + y avec s = vars.get(0), x = vars.get(1), y =vars.get(2)

	public ContrainteSomme(Variable s, Variable x, Variable y) {
		this.vars = new ArrayList<Variable>();
		this.getVars().add(s);
		this.getVars().add(x);
		this.getVars().add(y);
	}
	
	@Override
	public List<Variable> getVars() {
		return vars;
	}

	@Override
	public boolean isSatisfied() {
		return (this.allInstantiated() && (this.getVars().get(0).getValue() == this.getVars().get(1).getValue() + this.getVars().get(2).getValue()));
	}

	@Override
	public boolean isNecessary() {
		boolean b = false;
		Domain dom0 = this.getVars().get(0).getDomain().clone();
		Domain dom1 = this.getVars().get(1).getDomain().clone();
		Domain dom2 = this.getVars().get(2).getDomain().clone();
		for (int s : dom0) {
			for (int x : dom1) {
				for (int y : dom2) {
					if (s == x + y) {
						b = true;
						break;
					}
				
				}
			}
		}
		return b;
	}

	@Override
	public boolean filter() {
		boolean b = false;
		Domain dom0 = this.getVars().get(0).getDomain();
		Domain dom1 = this.getVars().get(1).getDomain();
		Domain dom2 = this.getVars().get(2).getDomain();
		int max0 = dom0.lastValue();
		int min1 = dom1.firstValue();
		int min2 = dom2.firstValue();
		int max1 = dom1.lastValue();
		int max2 = dom2.lastValue();
		for (int s : dom0) {
			if (s < min1 + min2) {
				dom0.remove(s);
				b = true;
			}
			if (s > max1 + max2) {
				dom0.remove(s);
				b = true;
			}
		}
		for (int x : dom1) {
			if (x > max0) {
				dom1.remove(x);
				b = true;
			}
		}
		for (int y : dom2) {
			if (y > max0) {
				dom1.remove(y);
				b = true;
			}
		}
		return b;
	}

	@Override
	public boolean allInstantiated() {
		return this.getVars().get(0).isInstantiated() && this.getVars().get(1).isInstantiated() && this.getVars().get(2).isInstantiated();
	}
	public String toString() {
		return this.getVars().get(0).toString() + "=" + this.getVars().get(1).toString() + "+" + this.getVars().get(2).toString();
	}
}
