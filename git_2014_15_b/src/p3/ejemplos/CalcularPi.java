package p3.ejemplos;

import java.text.DecimalFormat;


public class CalcularPi {
	
	
	public static void main(String[] args) {
		
		DecimalFormat form = new DecimalFormat();
		form.setMaximumFractionDigits(8);
		
		Thread misHilos[]   = new Thread[5];
		Worker misWorkers[] = new Worker[5];

		int iters = 10;
		
		System.out.println("Creating and starting Workers.");
		for (int i = 0; i < misHilos.length; i++){
			misWorkers[i] = new Worker(iters);
			misHilos[i] = new Thread(misWorkers[i]);
			misHilos[i].start();
			iters *= 10;
		}
		System.out.println("Joining to Workers.");
		for (int i = 0; i < misHilos.length; i++){
			try{
				misHilos[i].join();
			}
			catch (InterruptedException e){ 
				e.printStackTrace();
			}
		}
		System.out.println("Workers have finished.");
		for (int i = 0; i < misWorkers.length; i++){
			System.out.println("Worker[" + i + "].pi = " +  form.format(misWorkers[i].getPi()));
		}
		System.out.println("Main is finished.");
	}
}


class Worker implements Runnable {
	
	private double pi;  
	private int iters;

	
	public Worker(int iters){
		this.iters = iters;
	}

	public void run (){
		pi = calc_pi(iters);
	}	
	
	public double getPi(){
		return pi;
	}
	
	private static double calc_pi(long iters){
		if (iters < 0){
			return 0;
		}
		double pi = 0;
		double signo = 1;
		for (int k = 0; k <= iters; k++){
			pi += (signo/(2*k + 1));
			signo = -signo;
		}
		return 4 * pi;
	}
}