package web3;

import java.net.ConnectException;
import java.util.Arrays;
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
        String infuraEndpoint = "wss:// {Infura WSS Endpoint} ";
        WebSocketService webSocketService = new WebSocketService(infuraEndpoint, true);
        System.out.println("== Sokcet Connecting Started ==");
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
        System.out.println("== Sokcet Connected ==");
        
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//			@Override
//			public void run() {
//				Request<?, Web3ClientVersion> request = new Request<>("web3_clientVersion", Arrays.asList(), webSocketService, Web3ClientVersion.class);
//				webSocketService.sendAsync(request, Web3ClientVersion.class);
//			}
//		};
//		timer.scheduleAtFixedRate(timerTask, 0, 3000000);
		
        
        Web3j web3 = Web3j.build(webSocketService);
        
        String contractAddress = " { Smart Contract Address } ";

        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                contractAddress
        );
        Event transferEvent = new Event("Transfer", 
        	    Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));
        
        filter.addSingleTopic(EventEncoder.encode(transferEvent));

        web3.ethLogFlowable(filter).subscribe(eventLog -> {
//        	String smartContractAddress = eventLog.getTopics().get(0);
//        	String from = eventLog.getTopics().get(1);
        	String to = eventLog.getTopics().get(2);
        	String tokenId = eventLog.getTopics().get(3);
        	System.out.println("New Owner :: 0x" + Keys.toChecksumAddress(to).substring(26) + "  &  TokenId :: " + Long.decode(tokenId));
        	
        }, e -> {
        	if (e instanceof ArrayIndexOutOfBoundsException) {
				// TODO catch 
                e.printStackTrace();
            } else if (e instanceof NumberFormatException ) {
				// TODO catch 
                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        });
    }


}
