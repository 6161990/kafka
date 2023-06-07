# kafka
: Distributed Message Platform

#### 목적 : Event/Message 전송을 위해 사용
#### 장점 : 고가용성, 빠른 처리
#### 단점 : 순서보장 어려움, 미니멀한 사용이 어려움

- Distributed = 나눠서 작업할 수 있어서 빠르다.
- pub/sub = 전달하는 쪽은 전달받는 쪽을 관여할 필요가 없다.
- Producer, Consumer 가 존재하며, Consumer 는 ConsumerGroup 의 하위에 있다. 
- many : many (다대다 관계)로 동작한다.
- Server : Broker.Zookeeper 는 Broker 간의 분산처리 정보가 관리된다.(Meta, Controller, Topic, Partition 정보)
- 3개 이상의 Broker 로 구성
- KSQL, Connector, Kstream 등의 추가 라이브러리 모듈을 붙일 수 있다.
- KRaft 통해 zookeeper 제거할 수 있다. (Not stable)   
- cmak : kafka 무료 GUI

### 개념
- topic : 폴더, 특정 목적으로 생성된 data 집합(구분)
- partition : 하위 폴더. Topic의 하위 개념으로 분산을 위해 나누어 처리되는 단위, leader/follower 존재하여 실제 read/write는 리더에서 발생. follower 는 leader를 복사함.
- replica : leader 의 장애를 대응하기 위해 만들어놓는 복사본 follower. pull 방식으로 leader에서 복제함. 
- offset : 책갈피. consumer 가 어디까지 가져갔는지 저장하는 값. consumer group 별로 상이

    
### Zookeeper 의 역할
- Zookeeper : cluster 의 하위요소에 대한 전반적인 메타정보, Controller 정보, 선출, Broker 정보 등
- Broker : 실제 data 를 받아 저장하고 있음
- Controller : broker 대장 = 리더 선정, topic 생성, partition 생성, 복제본 관리 


### Stream Join 
| 참고 | https://www.confluent.io/blog/crossing-streams-joins-apache-kafka/   


![input-streams-1.jpeg](..%2F..%2F..%2FDesktop%2Finput-streams-1.jpeg)   
![inner_stream-stream_join.jpeg](..%2F..%2F..%2FDesktop%2Finner_stream-stream_join.jpeg)   
![left-stream-stream-join.jpeg](..%2F..%2F..%2FDesktop%2Fleft-stream-stream-join.jpeg)   
![outer-stream-stream-join.jpeg](..%2F..%2F..%2FDesktop%2Fouter-stream-stream-join.jpeg)


### KStream - KTable
- KStream  : 스트림이 오면 빨대 꽂고 모든 데이터를 계속 빨아들인다. 
- KTable : 해당 파티션 키의 최신의 상태에만 관심이 있다. 
  1. kim:hayoon
  2. kim:jayoon ✅ 


| 참고 |
https://www.popit.kr/kafka-%EC%9A%B4%EC%98%81%EC%9E%90%EA%B0%80-%EB%A7%90%ED%95%98%EB%8A%94-producer-acks/