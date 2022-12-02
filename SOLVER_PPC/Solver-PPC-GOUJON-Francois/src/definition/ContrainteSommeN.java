package definition;

import java.util.ArrayList;
import java.util.List;

public class ContrainteSommeN implements Constraint {
	
	List<Variable> vars;  // s = x1 + x2 + .. + xn avec s = vars.get(0), x1 = vars.get(1), x2 =vars.get(2), ...

	public ContrainteSommeN(Variable s, Variable[] vars) {
		this.vars = new ArrayList<Variable>();
		this.getVars().add(s);
		int n = vars.length;
		for (int i=0;i<n;i++) {
			this.getVars().add(vars[i]);
		}
	}
	
	@Override
	public List<Variable> getVars() {
		return vars;
	}

	@Override
	public boolean isSatisfied() {
		int somme = 0;
		int n = this.getVars().size();
		for (int i=1;i<n;i++) {
			somme = somme + this.getVars().get(i).getValue();
		}
		return (this.allInstantiated() && (this.getVars().get(0).getValue() == somme));
	}

	@Override
	public boolean isNecessary() {
		return true;
	}

	@Override
	public boolean filter() {
		return false;
	}

	@Override
	public boolean allInstantiated() {
		boolean b = true;
		int n = this.getVars().size();
		for (int i=0;i<n;i++) {
			if (!this.getVars().get(i).isInstantiated()) {
				b = false;
				break;
			}
		}
		return b;
	}
	public String toString() {
		List<Variable> varsSansS = new ArrayList<Variable>();
		int n = this.getVars().size();
		for (int i=1; i<n;i++) {
			varsSansS.add(this.getVars().get(i));
		}
		return this.getVars().get(0).toString() + "=" + varsSansS.toString();
	}
}
