package server ;
public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			for(int i =0 ; i < 20  ; i++) {
				try {
				if(i == 10) throw new RuntimeException();
				
			} 
			finally	{System.out.println(i);}
				}
			}

}
