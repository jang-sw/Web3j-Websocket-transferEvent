# Web3j-Websocket-transferEvent
java web3j (websocket) example


Subscribe to the Transfer event in a smart contract via WebSocket using the Web3j library, and print the token ID and the new owner whenever a new minting or owner change occurs.
It seems that Web3j.build has a default value of 15 seconds (Ethereum block creation time) for polling to confirm network node responses.

Web3j 라이브러리를 사용하여 WebSocket을 통해 스마트 컨트렉트의 Transfer 이벤트를 구독하고, 새로 민팅되거나 또는 소유자 변경이 발생할 때마다 token id와 owner를 출력
Web3j.build가 15초(이더리움 블록 생성 시간)의 default 값을 가지고 polling으로 네트워크 노드의 응답을 확인하는 것으로 보임

Web3j ライブラリを使用して、WebSocket を介してスマートコントラクトの Transfer イベントをサブスクライブし、新しくミンティングされたり所有者が変更されるたびにtokenidとownerを出力します。
Web3j.buildが15秒(Ethereumブロック生成時間)のdefault値を持ち、pollingでネットワークノードの応答を確認しているように見えます。
