package p3.impl;

import java.awt.Point;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import p3.basic.IMethodRequest;
import p3.basic.IScheduler;
import utilidades.Utilidades;

public class Server implements IScheduler, Runnable {
	
	boolean stop = false;
	
	static Connection conn;
 	static final String connURL = "jdbc:mysql://localhost/reparaciones?user=alumno&password=lsi";

	boolean started = false;
	boolean debug = true;
	Thread dispatcher;
	
	ConcurrentLinkedQueue<IMethodRequest> requests = new ConcurrentLinkedQueue<IMethodRequest>();

	
	public Server(){
		conn = Utilidades.connect(connURL);
	}

	@Override
	public void putRequest(IMethodRequest rq) {
		requests.add(rq);
	}

	@Override
	public void dispatch() {
		if (started){
			return;
		}
		System.out.println("Dispatcher.start dispatching");
		dispatcher = new Thread(this);
		stop = false;
		started = true;
		dispatcher.start();
	}
	
	public void stop(){
		stop = true;
		started = false;
	}
	
	public void run(){
		while (!stop){
			IMethodRequest request = null;
			
			for(Iterator<IMethodRequest> it = requests.iterator(); it.hasNext();){
				request = it.next();
				it.remove();
				handleRequest(request);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Dispatcher.dispatch ---> Stop dispatching ");
		dispatcher = null;
	}

	@Override
	public boolean isActive() {
		return !stop;
	}
	
	private void handleRequest(IMethodRequest rq){
		rq.execute();
	}
	
	public static String getData(String query){
		String res = new String();
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			rs = Utilidades.getTableFromQuery(conn, query);
			Vector<String> filas = Utilidades.getResultStrings(rs, "\t");
			for(String s : filas){
				sb.append(s + "\n");
			}
			sb.append("Query done  ---> " + query);
		} catch (Exception e) {
			sb.append(e.getMessage());
		}
		res = sb.toString();
		
		// Añadido para ralentizar el proceso ...
		// y visualizarlo.
		TraceService ts = new TraceService(query);
		String lineas [] = res.split("\n");
		for(String s: lineas){
			ts.appendLine(s);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	public static String modifyData(String query){
		String res = new String();
		StringBuffer sb = new StringBuffer();
		
		try {
			Utilidades.updateData(conn, query);
			sb.append("Query done  ---> " + query);
			
		} catch (Exception e) {
			sb.append(e.getMessage());
		}
		res = sb.toString();
		
		return res;
	}
	
}


class TraceService extends JFrame {
	
    private JTextArea tA;  
    private JScrollPane scroll;
    static int x, y;
    
    public TraceService(String s){
    	super(s);
    	
    	tA  = new JTextArea("Expected Results", 4, 30);
	    scroll = new JScrollPane(tA);
	    getContentPane().add(scroll);
	    
	    setSize (390,300);
	    this.setLocation(new Point(x,y));
	    x += 400;
	 	setVisible(true);  
	 	validate();
	 	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    }
    
    public void appendLine(String s){
    	tA.append(s + "\n");
    }
    
   
}
