package main;

import java.util.LinkedList;
import java.util.Queue;

public class QueueTest {
	public static void main(String[] args) {
		Queue<Integer> queue = new LinkedList<>();
		queue.offer(1);
		queue.offer(2);
		queue.offer(3);
		System.out.println(queue.peek());
		String string = "";
		for (Integer integer : queue) {
			if(string.length()==0)
				string+=integer;
			else 
				string+=","+integer;
		}
		
		String s[] = string.split(",");
		for (String string2 : s) {
			queue.offer(Integer.parseInt(string2));
		}
		System.out.println(string);
		System.out.println(queue.poll());
	}
}
