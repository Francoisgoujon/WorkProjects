package definition;

import java.util.ArrayList;
import java.util.List;

public class ContrainteDifferent implements Constraint {
	
	List<Variable> vars;
	
	public ContrainteDifferent(Variable v1, Variable v2) {
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
		return (this.allInstantiated() && !(this.getVars().get(0).getValue() == this.getVars().get(1).getValue()));
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
		return this.getVars().get(0).isInstantiated() && this.getVars().get(1).isInstantiated();
	}
	public String toString() {
		return this.getVars().get(0).toString() + "!=" + this.getVars().get(1).toString();
	}

	

}
