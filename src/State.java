import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class State {
	Vector<Vector<Character>> stacks;
	int numStacks, numBlocks;
	
	State(int numStacks, int numBlocks) {
		stacks = new Vector<Vector<Character>>();
		for(int i = 0; i<numStacks; i++) {
			if(!stacks.add(new Stack<Character>()))
				System.out.println("Cannot initialize the stacks");
		}
		this.numStacks = numStacks;
		this.numBlocks = numBlocks;
	}
	
	State(State obj) {
		this.stacks = new Vector<Vector<Character>>();
		this.numStacks = obj.numStacks;
		this.numBlocks = obj.numBlocks;
		for(int i = 0; i<obj.getStackNum(); i++) {
			Vector<Character> vect = new Vector<Character>();
			for(Character c : obj.stacks.get(i)) {
				vect.add(c);
			}
			this.stacks.add(vect);
		}
	}
	
	void makeGoalState() {
		Vector<Character> first = stacks.get(0);
		int i = 0;
		for(char c = 'A'; i < numBlocks && c <= 'Z'; i++, c++) {
			first.add(c);
		}
	}
	
	void problemGenerator() {
		this.makeGoalState();
		int numberOfMoves = ThreadLocalRandom.current().nextInt(0, 1001);
		for(int i = 0; i< numberOfMoves; i++) {
			int oldStackIndex = ThreadLocalRandom.current().nextInt(0, numStacks);
			int newStackIndex = ThreadLocalRandom.current().nextInt(0, numStacks);
			if(this.isValidMove(oldStackIndex, newStackIndex)) 
				this.move(oldStackIndex, newStackIndex);
		}
	}
	
	boolean isValidMove(int oldStackIndex, int newStackIndex) {
		if(oldStackIndex < 0 || oldStackIndex >= numStacks)
			return false;
		if(newStackIndex < 0 || newStackIndex >= numStacks)
			return false;
		if(oldStackIndex == newStackIndex)
			return false;
		if(stacks.get(oldStackIndex).isEmpty())
			return false;
		return true;
	}
	
	
	int getStackNum() {
		return numStacks;
	}
	
	
	public boolean equals(Object obj) {
		int i = 0;
		if (obj instanceof State) {
			State s = (State) obj;
			for(Vector<Character> stack : this.stacks) {
				if(stack.size() != s.stacks.get(i).size())
					return false;
				
				int j = 0;
				for(char c : stack) {
					if(c != s.stacks.get(i).get(j++)) 
						return false;
				}
				i++;
			}
		} else {
			return false;
		}
		return true;
	}
	
	public int hashCode(){
		String s = "";
		for(Vector<Character> stack : this.stacks) {
			for(char c : stack) {
				s.concat(c + "");
			}
			s.concat(".");
		}
		return s.hashCode();
	}
	
	Boolean goalTest(int numBlocks) {
		int i;
		for(i = 1; i<stacks.size(); i++) {
			if(!stacks.get(i).isEmpty())
				return false;
		}
		char c = 'A';
		for(i =  0; i<numBlocks; i++) {
			if(stacks.get(0).get(i) != c++)
				return false;
		}
		return true;
	}
	
	//the operator
	void move(int oldStackIndex, int newStackIndex) {
		//System.out.println("moving from " + oldStackIndex + "to " + newStackIndex);
		Vector<Character> temp = stacks.get(oldStackIndex);
		stacks.get(newStackIndex).add(temp.remove(temp.size() - 1));
	}
	
	Vector<State> successor() {
		Vector<State> res = new Vector<State>();
		for(int i =0; i < this.getStackNum(); i++) {
			Vector<Character> oldStack = stacks.get(i);
			if(oldStack.isEmpty())
				continue;
			
			for(int j =0; j < this.getStackNum(); j++) {
				Vector<Character> newStack = stacks.get(j);
				if(i != j) {
						State newState = new State(this);
						//System.out.println("Called for: " + i + " " + j);
						newState.move(i, j);
						res.add(newState);
				}
			}
		}
		return res;
	}
	
	int getHeuristicCost() {
		int cost = 0;
		//this.display();
		
		//using no of blocks out of place
		/*for(int i = 1; i < numStacks; i++) {
			cost += stacks.get(i).size();
		}
		char c;
		int i;
		Vector<Character> first = stacks.get(0);
		for(i = 0, c = 'A'; i < first.size(); i++, c++) {
				if(first.get(i) != c) {
					cost += first.size() - i;
					break;
				}
		}*/
		
		//finding length of sorted and length after sorted
		cost = numBlocks * 5;
		int los = 0, lan = 0, emptyStacks = 0;; char nextCh = 'A';
		Vector<Character> first = stacks.get(0);
		for(char c = 'A'; los < first.size() && first.get(los) == c; los++, c++);
		int las = first.size() - los;
		if(los!=0) {
			nextCh = (char)first.get(los-1);
			nextCh++;
		}
		
		for(int i = 1; i<stacks.size(); i++) {
			if(stacks.get(i).contains(nextCh)) {
				Vector<Character> next = stacks.get(i);
				lan = next.size() - next.indexOf(nextCh) - 1;
			}
			if(stacks.get(i).isEmpty())
				emptyStacks++;
		}
		if((numBlocks - los) >= (stacks.size() - 1))
			cost += (-5*los) + (2*las) + (2*lan) + emptyStacks;
		else
			cost += (-5*los) + (2*las) + (2*lan);
		//System.out.println("los: " + los + "las: " + las + "lan: " + lan);
		
		
		
		//System.out.println(cost);
		return cost;
	}
	
	void display() {
		Iterator<Vector<Character>> it = stacks.iterator();
		Vector<Character> temp;
		Iterator<Character> its;
		int idx = 1;
		while(it.hasNext()) {
			System.out.print(idx++ + "\t|\t");
			temp = it.next();
			for(its = temp.iterator(); its.hasNext(); System.out.print(its.next()));
			System.out.println('\n');
		}
	}
}
