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

   

    
### Zookeeper 의 역할
- Zookeeper : cluster 의 하위요소에 대한 전반적인 메타정보, Controller 정보, 선출, Broker 정보 등
- Broker : 실제 data 를 받아 저장하고 있음
- Controller : broker 대장 = 리더 선정, topic 생성, partition 생성, 복제본 관리 