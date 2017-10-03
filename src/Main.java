import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		State stateObj;
		System.out.println("Starting Block World..\n");
		System.out.println("Enter number of stacks and blocks");
		Scanner s = new Scanner(System.in);
		int numStacks = s.nextInt();
		int numBlocks = s.nextInt();
		AStar obj = new AStar(numStacks, numBlocks);
		//obj.initialize();
		obj.ComputeResult();
		
		s.close();
		System.out.println("Exiting BW..\n");
	}
}
