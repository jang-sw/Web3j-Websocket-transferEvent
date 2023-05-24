package web3;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.WebSocketService;

public class TransferSocket {
	

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in);){
//			Set Infura Endpoint & Contract Address
        		System.out.println("Enter Your Infura WSS Endpoint (  wss://{} ) : ");
			String infuraEndpoint = scanner.nextLine();
			System.out.println("Enter Your Smart Contract Address : ");
			String contractAddress = scanner.nextLine();
    		
//			Try Socket Connect 
			WebSocketService webSocketService = new WebSocketService(infuraEndpoint, true);
			System.out.println("== Socket Connecting Started ==");
			int failCnt = 5;
			while (failCnt >= 0) {
				try {
					webSocketService.connect();
					break;
				} catch (ConnectException e1) {
					if(failCnt == 0) e1.printStackTrace();
					failCnt--;
				}
            		}
            		System.out.println("== Socket Connected ==");
			
//			The schedule for checking the Web3 Client version has been set to maintain the socket connection.
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
    				@Override
    				public void run() {
    					Request<?, Web3ClientVersion> request = new Request<>("web3_clientVersion", Arrays.asList(), webSocketService, Web3ClientVersion.class);
    					webSocketService.sendAsync(request, Web3ClientVersion.class).thenAccept(v -> {
    						LocalDateTime now = LocalDateTime.now();
    						System.out.println(String.format("[ %s ] [Check Version] - %s", now.toString() ,v.getResult()));
    					});
    				}
    			};
    			timer.scheduleAtFixedRate(timerTask, 0, 3000000);
            
//			Event and filter setup for subscribing to Transfer.
// 			To examine the transfer logs of all blocks from the initial execution, set the filter's block range from EARLIEST to LATEST.

			Web3j web3 = Web3j.build(webSocketService);  
			EthFilter filter = new EthFilter(
				DefaultBlockParameterName.LATEST,
				DefaultBlockParameterName.LATEST,
				contractAddress
			);
            		Event TRANSFER_EVENT = new Event("Transfer", 
            	    		Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
            
//			Add Transfer Event Topic To Filter
            		filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));

//			Event Subscribe
			web3.ethLogFlowable(filter).subscribe(eventLog -> {
//				Event Log analysis

//            			String smartContractAddress = eventLog.getTopics().get(0);
//            			String from = eventLog.getTopics().get(1);
				String to = eventLog.getTopics().get(2);
				String tokenId = eventLog.getTopics().get(3);
				LocalDateTime now = LocalDateTime.now();
            			System.out.println(String.format("[ %s ] [TRANSFER OCCUR] - New Owner :: 0x%s  &  TokenId :: %s " ,now.toString(), Keys.toChecksumAddress(to).substring(26), Long.decode(tokenId) ));          
            		}, e -> {
//				TODO
				if (e instanceof ArrayIndexOutOfBoundsException) {
				    e.printStackTrace();
				} else if (e instanceof NumberFormatException ) {
				    e.printStackTrace();
				} else {
				    e.printStackTrace();
				}
			});
		} catch (Exception e2) {
//			TODO
		}
    }
}
