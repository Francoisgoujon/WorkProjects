package definition;

import java.util.ArrayList;
import java.util.List;

public class ContrainteMin implements Constraint {
	
	List<Variable> vars;  // m = min(x,y) avec m = vars.get(0), x = vars.get(1), y = vars.get(2)

	public ContrainteMin(Variable m, Variable x, Variable y) {
		this.vars = new ArrayList<Variable>();
		this.getVars().add(m);
		this.getVars().add(x);
		this.getVars().add(y);
	}
	
	@Override
	public List<Variable> getVars() {
		return vars;
	}
	
	public int min(int x, int y) {
		if (x<y) {
			return x;
		} else {
			return y;
		}
	}
	@Override
	public boolean isSatisfied() {
		return (this.allInstantiated() && (this.getVars().get(0).getValue() == min(this.getVars().get(1).getValue(), this.getVars().get(2).getValue())));
	}

	@Override
	public boolean isNecessary() {
		boolean b = false;
		Domain dom0 = this.getVars().get(0).getDomain().clone();
		Domain dom1 = this.getVars().get(1).getDomain().clone();
		Domain dom2 = this.getVars().get(2).getDomain().clone();
		for (int m : dom0) {
			for (int x : dom1) {
				for (int y : dom2) {
					if (m == min(x, y)) {
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
		int min0 = dom0.firstValue();
		int min1 = dom1.firstValue();
		int min2 = dom2.firstValue();
		for (int m : dom0) {
			if (m < min(min1, min2)) {
				dom0.remove(m);
				b = true;
			}
		}
		for (int x : dom1) {
			if (x < min0) {
				dom1.remove(x);
				b = true;
			}
		}
		for (int y : dom2) {
			if (y < min0) {
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
		return this.getVars().get(0).toString() + "= min(" + this.getVars().get(1).toString() + "," + this.getVars().get(2).toString() +")";
	}

}
