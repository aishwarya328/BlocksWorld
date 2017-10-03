import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class AStar {
	Vector<State> explored;
	HashMap<State, Integer> cost;
	HashMap<State, Integer> depth;
	HashMap<State, State> parent;
	
	PriorityQueue<State> frontier;
	int numStacks, numBlocks;
	State initial;
	int iter, maxQueueSize;
	
	AStar(int numStacks, int numBlocks) {
		frontier = new PriorityQueue<State>(10, new Comparator<State>() {
			@Override
			public int compare(State arg0, State arg1) {
				int cost0 = cost.get(arg0);
				int cost1 = cost.get(arg1);
				
				if(cost0 > cost1)
					return 1;
				if(cost0 < cost1)
					return -1;	
				return 0;
			}
		});
		
		this.numStacks = numStacks;
		this.numBlocks = numBlocks;
		iter = 0;
		maxQueueSize = 0;
		explored = new Vector<State>();
		cost = new HashMap<State, Integer>();
		depth = new HashMap<State, Integer>();
		parent = new HashMap<State, State>();
		initial = new State(numStacks, numBlocks);
	}
	
	void initialize() {
		//initialize from file
		File file = new File("C:\\Users\\aishwarya328\\eclipse-workspace\\BlocksWorld\\src\\input");
		Scanner s;
		try {
			s = new Scanner(file);
			for(int i = 0; i<numStacks; i++) {
				//System.out.println("Enter no of blocks in stack " + i);
				int n = s.nextInt();
				//System.out.println("Enter blocks in stack " + i + " from bottom to top\n");
				for(int j = 0; j<n; j++) {
					char c = s.next().charAt(0);
					initial.stacks.get(i).add(c);
				}
			}
			
			file.delete();
			s.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//test successor
		/*Vector<State> childStates = initial.successor();
		for(State child : childStates)
			child.display();*/
		//test equals
		/*State test = new State(numStacks, numBlocks);	
		if(!test.equals(initial))
			System.out.println("equals works fine!");*/
	}
	
	State AStarSearch() {
		//initial.problemGenerator();
		this.initialize();
		cost.put(initial, 0);
		depth.put(initial, 0);
		parent.put(initial,  initial);//its own parent
		frontier.add(initial);
		System.out.println("initial state:");
		initial.display();
		
		do {
			if(frontier.isEmpty()) 
				return null;
			
			State s = frontier.remove();
			if(s.goalTest(numBlocks) == true)
				return s;
			explored.add(s);
			
			/*System.out.println("Cost values");
			Object[] al = frontier.toArray();
			for(Object obj : al) {
				if(obj instanceof State)
					System.out.print(cost.get(obj) + " ");
			}*/
			
			
			
			System.out.println("iter=" + (iter++) + ", queue=" + frontier.size() + ", f=g+h=" + cost.get(s) + ", depth=" + depth.get(s));
			if(frontier.size() > maxQueueSize)
				maxQueueSize = frontier.size();
			
			//get all possible children states
			Vector<State> childStates = s.successor();
			for(State child : childStates) {
				//child.display();
				if(!explored.contains(child) && !frontier.contains(child)) {
					depth.put(child, depth.get(s) + 1);
					cost.put(child, (depth.get(child) + child.getHeuristicCost()));
					//cost.put(child, cost.get(s) + 1);
					parent.put(child, s);
					frontier.add(child);
				} else if(frontier.contains(child) && cost.get(child) > (depth.get(child) + child.getHeuristicCost())) {
				//} else if(frontier.contains(child) && cost.get(child) > cost.get(s) + 1) {
					frontier.remove(child);
					parent.replace(child, s);
					depth.replace(child, depth.get(s) + 1);
					cost.replace(child, (depth.get(child) + child.getHeuristicCost()));
					//cost.replace(child, cost.get(s) + 1);
					frontier.add(child);
				}
			}
			/*if(iter>50)
				return null;*/
		}while(true);
	}
	
	void ComputeResult() {
		State result = AStarSearch();
		
		if(result == null)
			System.out.println("Not possible to reach goal state");
		else {
			System.out.println("success! depth=" + depth.get(result) + ", total_goal_tests=" + iter + ", max_queue_size=" + maxQueueSize);
			System.out.println("solution path:");
			while(parent.get(result)!=result) {
			//	result.display();
				result = parent.get(result);
			}
		}
	}
}
